package co.paralleluniverse.javafs;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Map;

import co.paralleluniverse.fuse.Fuse;

/**
 * Mounts Java {@link FileSystem}s as a FUSE filesystems.
 * 
 * @author pron
 */
public final class JavaFS {
	
    /**
     * Mounts a filesystem.
     *
     * @param fs           the filesystem
     * @param mountPoint   the path of the mount point
     * @param readonly     if {@code true}, mounts the filesystem as read-only
     * @param log          if {@code true}, all filesystem calls will be logged with juc logging.
     * @param mountOptions the platform specific mount options (e.g. {@code ro}, {@code rw}, etc.). {@code null} for value-less options.
     */
    public static void mount(FileSystem fs, Path mountPoint, boolean readonly, boolean log, Map<String, String> mountOptions) throws IOException {
        if (readonly)
            fs = new ReadOnlyFileSystem(fs);
        Fuse.mount(new FuseFileSystemProvider(fs, log).log(log), mountPoint, false, log, mountOptions);
    }
    
    /**
     * Mounts a filesystem.
     *
     * @param fs         the filesystem
     * @param mountPoint the path of the mount point
     * @param readonly   if {@code true}, mounts the filesystem as read-only
     * @param log        if {@code true}, all filesystem calls will be logged with juc logging.
     */
    public static void mount(FileSystem fs, Path mountPoint, boolean readonly, boolean log) throws IOException {
        mount(fs,mountPoint, readonly, log, null);
    }

    /**
     * Mounts a filesystem.
     *
     * @param fs           the filesystem
     * @param mountPoint   the path of the mount point
     * @param mountOptions the platform specific mount options (e.g. {@code ro}, {@code rw}, etc.).  {@code null} for value-less options.
     */
    public static void mount(FileSystem fs, Path mountPoint, Map<String, String> mountOptions) throws IOException {
        mount(fs, mountPoint, false, false, mountOptions);
    }
    
    /**
     * Mounts a filesystem.
     *
     * @param fs         the filesystem
     * @param mountPoint the path of the mount point
     */
    public static void mount(FileSystem fs, Path mountPoint) throws IOException {
        mount(fs, mountPoint, false, false, null);
    }

    /**
     * Try to unmount an existing mountpoint.
     *
     * @param mountPoint The location where the filesystem is mounted.
     * @throws IOException thrown if an error occurs while starting the external process.
     */
    public static void unmount(Path mountPoint) throws IOException {
        Fuse.unmount(mountPoint);
    }

    private JavaFS() {
    }
}
