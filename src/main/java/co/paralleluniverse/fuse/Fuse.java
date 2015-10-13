package co.paralleluniverse.fuse;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import jnr.ffi.Struct;

public final class Fuse {
    private static final class MountThread extends Thread {
        private Integer result = null;
        private final String[] args;
        private final StructFuseOperations operations;
        private final LibFuse fuse;
        private final Path mountPoint;

        private MountThread(String filesystemName, LibFuse fuse, String[] args, Path mountPoint, StructFuseOperations operations) {
            super(filesystemName + "-fuse");
            this.fuse = fuse;
            this.args = args;
            this.mountPoint = mountPoint;
            this.operations = operations;
        }

        private Integer getResult() {
            return result;
        }

        @Override
        public void run() {
            result = fuse.fuse_main_real(args.length, args, operations, Struct.size(operations), null);
        }
    }

    private static LibFuse libFuse = null;
    private static long initTime;
    private static final Lock initLock = new ReentrantLock();
    private static final Random defaultFilesystemRandom = new Random();
    private static final long errorSleepDuration = 750;
    private static String fusermount = "fusermount";
    private static String umount = "umount";
    private static int currentUid = 0;
    private static int currentGid = 0;
    private static final ConcurrentMap<Path, FuseFilesystem> mountedFs = new ConcurrentHashMap<>();

    static void destroyed(FuseFilesystem fuseFilesystem) {
        if (handleShutdownHooks()) {
            try {
                Runtime.getRuntime().removeShutdownHook(fuseFilesystem.getUnmountHook());
            } catch (IllegalStateException e) {
                // Already shutting down; this is fine and expected, ignore the exception.
            }
        }
    }

    static StructFuseContext getFuseContext() {
        return init().fuse_get_context();
    }

    static int getGid() {
        return currentGid;
    }

    static long getInitTime() {
        init();
        return initTime;
    }

    static int getUid() {
        return currentUid;
    }

    private static boolean handleShutdownHooks() {
        final SecurityManager security = System.getSecurityManager();
        if (security == null) {
            return true;
        }
        try {
            security.checkPermission(new RuntimePermission("shutdownHooks"));
            return true;
        } catch (SecurityException e) {
            return false;
        }
    }

    static LibFuse init() throws UnsatisfiedLinkError {
        if (libFuse != null)
            return libFuse; // No need to lock if everything is fine already

        initLock.lock();
        try {
            if (libFuse == null) {
                libFuse = Platform.fuse();
                initTime = System.currentTimeMillis();
            }
            try {
                currentUid = Integer.parseInt(new ProcessGobbler("id", "-u").getStdout());
                currentGid = Integer.parseInt(new ProcessGobbler("id", "-g").getStdout());
            } catch (Exception e) {
                // Oh well, keep default values
            }
            return libFuse;
        } finally {
            initLock.unlock();
        }
    }

    public static void mount(FuseFilesystem filesystem, Path mountPoint, boolean blocking, boolean debug, Map<String, String> mountOptions) throws IOException {
        mountPoint = mountPoint.toAbsolutePath().normalize().toRealPath();
        if (!Files.isDirectory(mountPoint))
            throw new NotDirectoryException(mountPoint.toString());

        if (!Files.isReadable(mountPoint) || !Files.isWritable(mountPoint) || !Files.isExecutable(mountPoint))
            throw new AccessDeniedException(mountPoint.toString());

        final Logger logger = filesystem.getLogger();
        if (logger != null)
            filesystem = new LoggedFuseFilesystem(filesystem, logger);

        filesystem.mount(mountPoint, blocking);

        final String filesystemName = filesystem.getFuseName();
        final String[] options = toOptionsArray(mountOptions);
        final String[] argv;
        if (options == null)
            argv = new String[debug ? 4 : 3];
        else {
            argv = new String[(debug ? 4 : 3) + options.length];
            System.arraycopy(options, 0, argv, (debug ? 3 : 2), options.length);
        }
        
        argv[0] = filesystemName;
        argv[1] = "-f";
        if (debug)
            argv[2] = "-d";
        argv[argv.length - 1] = mountPoint.toString();
        
        final LibFuse fuse = init();
        final StructFuseOperations operations = new StructFuseOperations(jnr.ffi.Runtime.getRuntime(fuse), filesystem);

        if (handleShutdownHooks())
            Runtime.getRuntime().addShutdownHook(filesystem.getUnmountHook());

        mountedFs.put(mountPoint, filesystem);

        final Integer result;
        if (blocking)
            result = fuse.fuse_main_real(argv.length, argv, operations, Struct.size(operations), null);
        else {
            final MountThread mountThread = new MountThread(filesystemName, fuse, argv, mountPoint, operations);
            mountThread.start();
            try {
                mountThread.join(errorSleepDuration);
            } catch (final InterruptedException e) {
            }
            result = mountThread.getResult();
        }
        if (result != null && result != 0)
            throw new FuseException(result);
    }

    static void unmount(FuseFilesystem fuseFilesystem) throws IOException {
        fuseFilesystem.unmount();
        final Path mountPoint = fuseFilesystem.getMountPoint();

        final FuseFilesystem fs = mountedFs.remove(mountPoint);
        assert fs == null || fs == fuseFilesystem;

        unmount(mountPoint);
    }

    public static void setFusermount(String fusermount) {
        Fuse.fusermount = fusermount;
    }

    public static void setUmount(String umount) {
        Fuse.umount = umount;
    }

    /**
     * Try to unmount an existing FUSE mountpoint. NOTE: You should use {@link FuseFilesystem#unmount FuseFilesystem.unmount()}
     * for unmounting the FuseFilesystem (or let the shutdown hook take care unmounting during shutdown of the application).
     * This method is available for special cases, e.g. where mountpoints were left over from previous invocations and need to
     * be unmounted before the filesystem can be mounted again.
     *
     * @param mountPoint The location where the filesystem is mounted.
     * @throws IOException thrown if an error occurs while starting the external process.
     */
    public static void unmount(Path mountPoint) throws IOException {
        final FuseFilesystem fs = mountedFs.get(mountPoint);
        if (fs != null) {
            unmount(fs);
            return;
        }
        ProcessGobbler process;
        try {
            process = new ProcessGobbler(Fuse.fusermount, "-z", "-u", mountPoint.toString());
        } catch (IOException e) {
            process = new ProcessGobbler(Fuse.umount, mountPoint.toString());
        }
        final int res = process.getReturnCode();
        if (res != 0)
            throw new FuseException(res);
    }
    
    private static String[] toOptionsArray(Map<String, String> options) {
        if (options == null) {
            return null;
        }

        int i = 0;
        String[] values = new String[options.size() * 2];
        for (Entry<String, String> entry : options.entrySet()) {
            values[i++] = "-o";
            values[i++] = entry.getKey()
                    + (entry.getValue() == null ? "" : "=" + entry.getValue());
        }

        return values;
    }
}
