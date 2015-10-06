package co.paralleluniverse.javafs;

import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.FileLock;
import java.nio.file.AccessDeniedException;
import java.nio.file.AccessMode;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.attribute.FileTime;
import java.nio.file.NotDirectoryException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.ReadOnlyFileSystemException;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import jnr.constants.platform.Errno;
import jnr.ffi.Pointer;
import co.paralleluniverse.fuse.AccessConstants;
import co.paralleluniverse.fuse.DirectoryFiller;
import co.paralleluniverse.fuse.StructFuseFileInfo;
import co.paralleluniverse.fuse.FuseFilesystem;
import co.paralleluniverse.fuse.StructFlock;
import co.paralleluniverse.fuse.StructFuseBufvec;
import co.paralleluniverse.fuse.StructFusePollHandle;
import co.paralleluniverse.fuse.StructStat;
import co.paralleluniverse.fuse.StructStatvfs;
import co.paralleluniverse.fuse.StructTimeBuffer;
import co.paralleluniverse.fuse.TypeMode;
import co.paralleluniverse.fuse.XattrFiller;
import co.paralleluniverse.fuse.XattrListFiller;
import java.util.Arrays;
import java.util.logging.Level;

class FuseFileSystemProvider extends FuseFilesystem {
    private final FileSystemProvider fsp;
    private final FileSystem fs;
    private final ConcurrentMap<Long, Object> openFiles = new ConcurrentHashMap<>();
    private final AtomicLong fileHandle = new AtomicLong(0);
    private final boolean debug;
    private static final long BLOCK_SIZE = 4096;

    public FuseFileSystemProvider(FileSystemProvider fsp, URI uri, boolean debug) {
        this.fsp = fsp;
        this.fs = fsp.getFileSystem(uri);
        this.debug = debug;
    }

    public FuseFileSystemProvider(FileSystem fs, boolean debug) {
        this.fsp = fs.provider();
        this.fs = fs;
        this.debug = debug;
    }

    private Path path(String p) {
        return fs.getPath(p);
    }

    @Override
    protected void afterUnmount(Path mountPoint) {
    }

    @Override
    protected void beforeMount(Path mountPoint) {
    }

    @Override
    protected String getName() {
        return fs.toString();
    }

    @Override
    protected String[] getOptions() {
        return null;
    }

    @Override
    protected int getattr(String path, StructStat stat) {
        try {
            Path p = path(path);
            BasicFileAttributes attributes = fsp.readAttributes(p, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);

            // time
            stat.atime(sec(attributes.lastAccessTime()), nsec(attributes.lastAccessTime()));
            stat.mtime(sec(attributes.lastModifiedTime()), nsec(attributes.lastModifiedTime()));
            stat.ctime(sec(attributes.creationTime()), nsec(attributes.creationTime()));

            stat.size(attributes.size());

            // mode
            long mode = 0L;
            if (attributes.isRegularFile())
                mode = TypeMode.S_IFREG;
            else if (attributes.isDirectory())
                mode = TypeMode.S_IFDIR;
            else if (attributes.isSymbolicLink())
                mode = TypeMode.S_IFLNK;

            PosixFileAttributes pas = attributes instanceof PosixFileAttributes ? (PosixFileAttributes) attributes : null;
            if (pas == null) {
                try {
                    pas = fsp.readAttributes(p, PosixFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
                } catch (UnsupportedOperationException e) {
                }
            }
            if (pas != null)
                mode |= permissionsToMode(pas.permissions());
            stat.mode(mode);

            try {
                final Map<String, Object> uattrs = fsp.readAttributes(p, "unix:*", LinkOption.NOFOLLOW_LINKS);
                int uid = (int) uattrs.get("uid");
                stat.uid(uid);

                int gid = (int) uattrs.get("gid");
                stat.gid(gid);
            } catch (Exception e) {
            }
            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int readlink(String path, ByteBuffer buffer, long size) {
        try {
            fillBufferWithString(fsp.readSymbolicLink(path(path)).toString(), buffer, size);
            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int mknod(String path, long mode, long dev) {
        return create(path, mode, null);
    }

    @Override
    protected int mkdir(String path, long mode) {
        try {
            fsp.createDirectory(path(path), PosixFilePermissions.asFileAttribute(modeToPermissions(mode)));
            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int unlink(String path) {
        try {
            Path p = path(path);
            if (Files.isDirectory(p))
                throw new IOException(p + " is a directory");
            fsp.delete(path(path));
            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int rmdir(String path) {
        try {
            Path p = path(path);
            if (!Files.isDirectory(p))
                throw new IOException(p + " is not a directory");
            fsp.delete(p);
            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int symlink(String path, String target) {
        try {
            fsp.createSymbolicLink(path(path), path(target));
            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int rename(String path, String newName) {
        try {
            fsp.move(path(path), path(newName));
            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int link(String path, String target) {
        try {
            fsp.createLink(path(target), path(path));
            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int chmod(String path, long mode) {
        try {
            final PosixFileAttributeView attrs = fsp.getFileAttributeView(path(path), PosixFileAttributeView.class);
            attrs.setPermissions(modeToPermissions(mode));
            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int chown(String path, long uid, long gid) {
        try {
            final PosixFileAttributeView attrs = fsp.getFileAttributeView(path(path), PosixFileAttributeView.class);
            attrs.setOwner(fs.getUserPrincipalLookupService().lookupPrincipalByName(Long.toString(uid)));
            attrs.setGroup(fs.getUserPrincipalLookupService().lookupPrincipalByGroupName(Long.toString(gid)));
            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int truncate(String path, long offset) {
        try {
            final SeekableByteChannel ch = fsp.newByteChannel(path(path), EnumSet.of(StandardOpenOption.WRITE));
            ch.truncate(offset);
            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int open(String path, StructFuseFileInfo info) {
        try {
            final SeekableByteChannel channel = fsp.newByteChannel(path(path), fileInfoToOpenOptions(info));
            final long fh = fileHandle.incrementAndGet();
            openFiles.put(fh, channel);
            info.fh(fh);
            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int read(String path, ByteBuffer buffer, long size, long offset, StructFuseFileInfo info) {
        try {
            final Channel channel = toChannel(info);
            if (channel instanceof SeekableByteChannel) {
                final SeekableByteChannel ch = ((SeekableByteChannel) channel);
                if (info.nonseekable())
                    assert offset == ch.position();
                else
                    ch.position(offset);
                int n = ch.read(buffer);
                if (n > 0) {
                    if (!info.noblock())
                        assert n <= 0 || n == size;
                    else {
                        int c;
                        while (n < size) {
                            if ((c = ch.read(buffer)) <= 0)
                                break;
                            n += c;
                        }
                    }
                }
                return n;
            } else if (channel instanceof AsynchronousFileChannel) {
                final AsynchronousFileChannel ch = ((AsynchronousFileChannel) channel);
                int n = ch.read(buffer, offset).get();
                assert n == size;
                return n;
            } else
                throw new UnsupportedOperationException();
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int write(String path, ByteBuffer buffer, long size, long offset, StructFuseFileInfo info) {
        try {
            final Channel channel = toChannel(info);
            if (channel instanceof SeekableByteChannel) {
                final SeekableByteChannel ch = ((SeekableByteChannel) channel);
                if (!info.append() && !info.nonseekable())
                    ch.position(offset);
                int n = ch.write(buffer);
                if (n > 0) {
                    if (!info.noblock())
                        assert n <= 0 || n == size;
                    else {
                        int c;
                        while (n < size) {
                            if ((c = ch.write(buffer)) <= 0)
                                break;
                            n += c;
                        }
                    }
                }
                return n;
            } else if (channel instanceof AsynchronousFileChannel) {
                final AsynchronousFileChannel ch = ((AsynchronousFileChannel) channel);
                int n = ch.write(buffer, offset).get();
                return n;
            } else
                throw new UnsupportedOperationException();
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int statfs(String path, StructStatvfs statvfs) {

        try {
            boolean hasStore = false; // only one store allowed
            for (FileStore store : fs.getFileStores()) {
                if (hasStore)
                    throw new IOException("Multiple FileStores not supported");
                hasStore = true;

                final long blockSize = BLOCK_SIZE;
                statvfs.bsize(blockSize);
                statvfs.blocks(store.getTotalSpace() / blockSize);
                statvfs.bfree(store.getUnallocatedSpace() / blockSize);
                statvfs.bavail(store.getUsableSpace() / blockSize);

                long mountFlags = 0;
                if (fs.isReadOnly())
                    mountFlags |= StructStatvfs.ST_RDONLY;
                statvfs.flags(mountFlags);
            }
            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int flush(String path, StructFuseFileInfo info) {
        return 0;
    }

    @Override
    public int release(String path, StructFuseFileInfo info) {
        try {
            final Channel ch = toChannel(info);
            ch.close();
            openFiles.remove(info.fh());
            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int fsync(String path, int datasync, StructFuseFileInfo info) {
        try {
            final Channel channel = toChannel(info);
            if (channel instanceof FileChannel) {
                final FileChannel ch = ((FileChannel) channel);
                ch.force(datasync == 0);
            } else if (channel instanceof AsynchronousFileChannel) {
                final AsynchronousFileChannel ch = ((AsynchronousFileChannel) channel);
                ch.force(true);
                ch.force(datasync == 0);
            } else
                throw new UnsupportedOperationException();
            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int setxattr(String path, String xattr, ByteBuffer value, long size, int flags, int position) {
        return -Errno.ENOSYS.intValue();
    }

    @Override
    protected int getxattr(String path, String xattr, XattrFiller filler, long size, long position) {
        return -Errno.ENOSYS.intValue();
    }

    @Override
    protected int listxattr(String path, XattrListFiller filler) {
        return -Errno.ENOSYS.intValue();
    }

    @Override
    protected int removexattr(String path, String xattr) {
        return -Errno.ENOSYS.intValue();
    }

    @Override
    protected int opendir(String path, StructFuseFileInfo info) {
        try {
            final DirectoryStream<Path> ds = fsp.newDirectoryStream(path(path), new DirectoryStream.Filter<Path>() {
                @Override
                public boolean accept(Path entry) throws IOException {
                    return true;
                }
            });
            final long fh = fileHandle.incrementAndGet();
            openFiles.put(fh, ds);
            info.fh(fh);
            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int readdir(String path, StructFuseFileInfo info, DirectoryFiller filler) {
        final DirectoryStream<Path> ds = (DirectoryStream<Path>) openFiles.get(info.fh());
        filler.add(toStringIterable(ds));
        return 0;
    }

    @Override
    protected int releasedir(String path, StructFuseFileInfo info) {
        try {
            final DirectoryStream<Path> ds = (DirectoryStream<Path>) openFiles.get(info.fh());
            ds.close();
            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int fsyncdir(String path, int datasync, StructFuseFileInfo info) {
        return 0;
    }

    @Override
    protected void init() {
    }

    @Override
    protected void destroy() {
        try {
            fs.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected int access(String path, int access) {
        try {
            final Path p = path(path);
            fsp.checkAccess(p, toAccessMode(access, Files.isDirectory(p)));
            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int create(String path, long mode, StructFuseFileInfo info) {
        try {
            final Set<OpenOption> options = fileInfoToOpenOptions(info);
            options.add(StandardOpenOption.CREATE);
            final SeekableByteChannel channel = fsp.newByteChannel(path(path), options);
            final long fh = fileHandle.incrementAndGet();
            openFiles.put(fh, channel);
            info.fh(fh);
            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int ftruncate(String path, long offset, StructFuseFileInfo info) {
        try {
            final Channel channel = toChannel(info);
            if (channel instanceof SeekableByteChannel)
                ((SeekableByteChannel) channel).truncate(offset);
            else if (channel instanceof AsynchronousFileChannel)
                ((AsynchronousFileChannel) channel).truncate(offset);
            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int fgetattr(String path, StructStat stat, StructFuseFileInfo info) {
        return getattr(path, stat);
    }

    @Override
    protected int lock(String path, StructFuseFileInfo info, int command, StructFlock flock) {
        try {
            throw new UnsupportedOperationException();
//            if (command == StructFlock.CMD_GETLK)
//                throw new UnsupportedOperationException();
//
//            final Channel channel = toChannel(info);
//            if (channel instanceof FileChannel) {
//                FileChannel ch = (FileChannel) channel;
//                switch (command) {
//                    case StructFlock.CMD_SETLK:
//                        FileLock lock = ch.lock(flock.start(), flock.len(), false);
//                        lock.
//                }
//            } else if (channel instanceof AsynchronousFileChannel) {
//                AsynchronousFileChannel ch = (AsynchronousFileChannel) channel;
//            }
//            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int utimens(String path, StructTimeBuffer timeBuffer) {
        try {
            fsp.getFileAttributeView(path(path), BasicFileAttributeView.class).setTimes(
                    FileTime.from(toNanos(timeBuffer.mod_sec(), timeBuffer.mod_nsec()), TimeUnit.NANOSECONDS),
                    FileTime.from(toNanos(timeBuffer.ac_sec(), timeBuffer.ac_nsec()), TimeUnit.NANOSECONDS),
                    null);
            return 0;
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    protected int bmap(String path, StructFuseFileInfo info) {
        try {
            throw new UnsupportedOperationException();
        } catch (Exception e) {
            return -errno(e);
        }
    }

    @Override
    public int ioctl(String path, int cmd, Pointer arg, StructFuseFileInfo fi, long flags, Pointer data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int poll(String path, StructFuseFileInfo fi, StructFusePollHandle ph, Pointer reventsp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected int write_buf(String path, StructFuseBufvec buf, long off, StructFuseFileInfo fi) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: implement
    }

    @Override
    protected int read_buf(String path, Pointer bufp, long size, long off, StructFuseFileInfo fi) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: implement
    }

    @Override
    public int flock(String path, StructFuseFileInfo fi, int op) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int fallocate(String path, int mode, long off, long length, StructFuseFileInfo fi) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    ////////////

    private Channel toChannel(StructFuseFileInfo info) {
        return (Channel) openFiles.get(info.fh());
    }

    private static Set<OpenOption> fileInfoToOpenOptions(StructFuseFileInfo info) {
        final Set<OpenOption> options = new HashSet<>();
        if (info != null) {
            if (info.create())
                options.add(StandardOpenOption.CREATE);
            if (info.append())
                options.add(StandardOpenOption.APPEND);
            if (info.truncate())
                options.add(StandardOpenOption.TRUNCATE_EXISTING);
            switch (info.openMode()) {
                case StructFuseFileInfo.O_RDONLY:
                    options.add(StandardOpenOption.READ);
                    break;
                case StructFuseFileInfo.O_WRONLY:
                    options.add(StandardOpenOption.WRITE);
                    break;
                case StructFuseFileInfo.O_RDWR:
                    options.add(StandardOpenOption.READ);
                    options.add(StandardOpenOption.WRITE);
                    break;
            }
        }
        return options;
    }

    private static Set<PosixFilePermission> modeToPermissions(long mode) {
        final EnumSet<PosixFilePermission> permissions = EnumSet.noneOf(PosixFilePermission.class);
        if ((mode & TypeMode.S_IRUSR) != 0)
            permissions.add(PosixFilePermission.OWNER_READ);
        if ((mode & TypeMode.S_IWUSR) != 0)
            permissions.add(PosixFilePermission.OWNER_WRITE);
        if ((mode & TypeMode.S_IXUSR) != 0)
            permissions.add(PosixFilePermission.OWNER_EXECUTE);
        if ((mode & TypeMode.S_IRGRP) != 0)
            permissions.add(PosixFilePermission.GROUP_READ);
        if ((mode & TypeMode.S_IWGRP) != 0)
            permissions.add(PosixFilePermission.GROUP_WRITE);
        if ((mode & TypeMode.S_IXGRP) != 0)
            permissions.add(PosixFilePermission.GROUP_EXECUTE);
        if ((mode & TypeMode.S_IROTH) != 0)
            permissions.add(PosixFilePermission.OTHERS_READ);
        if ((mode & TypeMode.S_IWOTH) != 0)
            permissions.add(PosixFilePermission.OTHERS_WRITE);
        if ((mode & TypeMode.S_IXOTH) != 0)
            permissions.add(PosixFilePermission.OTHERS_EXECUTE);
        return permissions;
    }

    private static long permissionsToMode(Set<PosixFilePermission> permissions) {
        long mode = 0;
        for (PosixFilePermission px : permissions) {
            switch (px) {
                case OWNER_READ:
                    mode |= TypeMode.S_IRUSR;
                    break;
                case OWNER_WRITE:
                    mode |= TypeMode.S_IWUSR;
                    break;
                case OWNER_EXECUTE:
                    mode |= TypeMode.S_IXUSR;
                    break;
                case GROUP_READ:
                    mode |= TypeMode.S_IRGRP;
                    break;
                case GROUP_WRITE:
                    mode |= TypeMode.S_IWGRP;
                    break;
                case GROUP_EXECUTE:
                    mode |= TypeMode.S_IXGRP;
                    break;
                case OTHERS_READ:
                    mode |= TypeMode.S_IROTH;
                    break;
                case OTHERS_WRITE:
                    mode |= TypeMode.S_IWOTH;
                    break;
                case OTHERS_EXECUTE:
                    mode |= TypeMode.S_IXOTH;
                    break;
            }
        }
        return mode;
    }

    private static AccessMode[] toAccessMode(int access, boolean dir) {
        List<AccessMode> modes = new ArrayList<>(3);
        if ((access & AccessConstants.R_OK) != 0 || (dir && (access & AccessConstants.X_OK) != 0))
            modes.add(AccessMode.READ);
        if ((access & AccessConstants.W_OK) != 0)
            modes.add(AccessMode.WRITE);
        if (!dir && (access & AccessConstants.X_OK) != 0)
            modes.add(AccessMode.EXECUTE);
        return modes.toArray(new AccessMode[modes.size()]);
    }

    private static Iterable<String> toStringIterable(final Iterable<Path> iterable) {
        return new Iterable<String>() {

            @Override
            public Iterator<String> iterator() {
                final Iterator<Path> it = iterable.iterator();
                return new Iterator<String>() {

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public String next() {
                        return it.next().toString();
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    private int errno(Throwable e) {
        final Errno en = errno0(e);
        if (en == null)
            return 0;
        if (debug)
            getLogger().log(Level.WARNING, "Exception", e);
        return en.intValue();
    }

    private static Errno errno0(Throwable t) {
        if (t == null)
            return null;

        if (t instanceof IllegalArgumentException)
            return Errno.EINVAL;
        if (t instanceof UnsupportedOperationException)
            return Errno.ENOSYS; // Errno.EOPNOTSUPP
        if (t instanceof InterruptedException)
            return Errno.EINTR;
        if (t instanceof NullPointerException || t instanceof ClassCastException)
            return Errno.EFAULT;

        if (t instanceof SecurityException)
            return Errno.EPERM;

        if (t instanceof TimeoutException)
            return Errno.ETIMEDOUT;

        if (t instanceof AccessDeniedException)
            return Errno.EACCES;
        if (t instanceof FileAlreadyExistsException)
            return Errno.EEXIST;
        if (t instanceof FileNotFoundException || t instanceof NoSuchFileException)
            return Errno.ENOENT;
        if (t instanceof NotDirectoryException)
            return Errno.ENOTDIR;
        if (t instanceof DirectoryNotEmptyException)
            return Errno.ENOTEMPTY;
        if (t instanceof ReadOnlyFileSystemException)
            return Errno.EROFS; // Errno.EACCES
        if (t instanceof IOException || t instanceof IOError)
            return Errno.EIO;
        return Errno.EFAULT;
    }

    private static void fillBufferWithString(String str, ByteBuffer buffer, long size) {
        final byte[] bytes = str.getBytes();
        final int s = (int) Math.min((long) Integer.MAX_VALUE, size - 1);
        buffer.put(bytes, 0, Math.min(bytes.length, s));
        buffer.put((byte) 0);
        buffer.flip();
    }

    private static long toNanos(long sec, long nanos) {
        return sec * 1_000_000_000 + nanos;
    }

    private static long sec(FileTime ft) {
        return ft != null ? sec(ft.to(TimeUnit.NANOSECONDS)) : 0;
    }

    private static long nsec(FileTime ft) {
        return ft != null ? nsec(ft.to(TimeUnit.NANOSECONDS)) : 0;
    }

    private static long sec(long nanos) {
        return nanos / 1_000_000_000;
    }

    private static long nsec(long nanos) {
        return nanos % 1_000_000_000;
    }
}
