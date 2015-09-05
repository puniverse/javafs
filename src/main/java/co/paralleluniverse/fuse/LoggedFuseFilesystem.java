package co.paralleluniverse.fuse;

import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import jnr.ffi.Pointer;

final class LoggedFuseFilesystem extends FuseFilesystem {
    private static interface LoggedMethod<T> {
        public T invoke();
    }

    private static interface LoggedVoidMethod {
        public void invoke();
    }

    private static final String methodSuccess = "Method succeeded.";
    private static final String methodFailure = "Exception thrown: ";
    private static final String methodResult = " Result: ";
    private final String className;
    private final Logger logger;
    private final FuseFilesystem filesystem;

    LoggedFuseFilesystem(FuseFilesystem filesystem, Logger logger) {
        this.filesystem = filesystem;
        this.logger = logger;
        className = filesystem.getClass().getName();
    }

    private void log(final String methodName, final LoggedVoidMethod method) {
        log(methodName, method, null, (Object[]) null);
    }

    private void log(final String methodName, final LoggedVoidMethod method, final String path, final Object... args) {
        try {
            logger.entering(className, methodName, args);
            method.invoke();
            logger.logp(Level.INFO, className, methodName, (path == null ? "" : "[" + path + "] ") + methodSuccess, args);
            logger.exiting(className, methodName, args);
        } catch (Throwable e) {
            logException(e, methodName, null, args);
        }
    }

    private <T> T log(String methodName, T defaultValue, LoggedMethod<T> method) {
        return log(methodName, defaultValue, method, null, (Object[]) null);
    }

    private <T> T log(String methodName, T defaultValue, LoggedMethod<T> method, String path, Object... args) {
        try {
            logger.entering(className, methodName, args);
            final T result = method.invoke();
            logger.logp(Level.INFO, className, methodName, (path == null ? "" : "[" + path + "] ") + methodSuccess + methodResult + result, args);
            logger.exiting(className, methodName, args);
            return result;
        } catch (Throwable e) {
            return logException(e, methodName, defaultValue, args);
        }
    }

    private <T> T logException(Throwable e, String methodName, T defaultValue, Object... args) {
        final StackTraceElement[] stack = e.getStackTrace();
        final StringBuilder builder = new StringBuilder();
        for (StackTraceElement element : stack)
            builder.append('\n').append(element);

        logger.logp(Level.SEVERE, className, methodName, methodFailure + e + builder.toString(), args);
        return defaultValue;
    }

    @Override
    public final void _destroy() {
        destroy();
        _destroy(this, filesystem);
    }

    @Override
    public int access(final String path, final int access) {
        return log("access", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.access(path, access);
            }
        }, path, access);
    }

    @Override
    public void afterUnmount(final Path mountPoint) {
        log("afterUnmount", new LoggedVoidMethod() {
            @Override
            public void invoke() {
                filesystem.afterUnmount(mountPoint);
            }
        }, mountPoint.toString());
    }

    @Override
    public void beforeMount(final Path mountPoint) {
        log("beforeMount", new LoggedVoidMethod() {
            @Override
            public void invoke() {
                filesystem.beforeMount(mountPoint);
            }
        }, mountPoint.toString());
    }

    @Override
    public int bmap(final String path, final StructFuseFileInfo info) {
        return log("bmap", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.bmap(path, info);
            }
        }, path, info);
    }

    @Override
    public int chmod(final String path, final long mode) {
        return log("chmod", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.chmod(path, mode);
            }
        }, path, mode);
    }

    @Override
    public int chown(final String path, final long uid, final long gid) {
        return log("chown", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.chown(path, uid, gid);
            }
        }, path, uid, gid);
    }

    @Override
    public int create(final String path, final long mode, final StructFuseFileInfo info) {
        return log("create", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.create(path, mode, info);
            }
        }, path, mode, info);
    }

    @Override
    public void destroy() {
        log("destroy", new LoggedVoidMethod() {
            @Override
            public void invoke() {
                filesystem.destroy();
            }
        });
    }

    @Override
    public int fgetattr(final String path, final StructStat stat, final StructFuseFileInfo info) {
        return log("fgetattr", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.fgetattr(path, stat, info);
            }
        }, path, stat);
    }

    @Override
    public int flush(final String path, final StructFuseFileInfo info) {
        return log("flush", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.flush(path, info);
            }
        }, path, info);
    }

    @Override
    public int fsync(final String path, final int datasync, final StructFuseFileInfo info) {
        return log("fsync", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.fsync(path, datasync, info);
            }
        }, path, info);
    }

    @Override
    public int fsyncdir(final String path, final int datasync, final StructFuseFileInfo info) {
        return log("fsyncdir", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.fsyncdir(path, datasync, info);
            }
        }, path, info);
    }

    @Override
    public int ftruncate(final String path, final long offset, final StructFuseFileInfo info) {
        return log("ftruncate", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.ftruncate(path, offset, info);
            }
        }, path, offset, info);
    }

    @Override
    public int getattr(final String path, final StructStat stat) {
        return log("getattr", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.getattr(path, stat);
            }
        }, path, stat);
    }

    @Override
    protected String getName() {
        return log("getName", null, new LoggedMethod<String>() {
            @Override
            public String invoke() {
                return filesystem.getName();
            }
        });
    }

    @Override
    protected String[] getOptions() {
        return log("getOptions", null, new LoggedMethod<String[]>() {
            @Override
            public String[] invoke() {
                return filesystem.getOptions();
            }
        });
    }

    @Override
    public int getxattr(final String path, final String xattr, final XattrFiller filler, final long size, final long position) {
        return log("getxattr", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.getxattr(path, xattr, filler, size, position);
            }
        }, path, xattr, filler, size, position);
    }

    @Override
    public void init() {
        log("init", new LoggedVoidMethod() {
            @Override
            public void invoke() {
                filesystem.init();
            }
        });
    }

    @Override
    public int link(final String path, final String target) {
        return log("link", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.link(path, target);
            }
        }, path, target);
    }

    @Override
    public int listxattr(final String path, final XattrListFiller filler) {
        return log("listxattr", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.listxattr(path, filler);
            }
        }, path, filler);
    }

    @Override
    public int lock(final String path, final StructFuseFileInfo info, final int command, final StructFlock flock) {
        return log("lock", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.lock(path, info, command, flock);
            }
        }, path, info, command, flock);
    }

    @Override
    public int mkdir(final String path, final long mode) {
        return log("mkdir", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.mkdir(path, mode);
            }
        }, path, mode);
    }

    @Override
    public int mknod(final String path, final long mode, final long dev) {
        return log("mknod", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.mknod(path, mode, dev);
            }
        }, path, mode, dev);
    }

    @Override
    public int open(final String path, final StructFuseFileInfo info) {
        return log("open", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.open(path, info);
            }
        }, path, info);
    }

    @Override
    public int opendir(final String path, final StructFuseFileInfo info) {
        return log("opendir", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.opendir(path, info);
            }
        }, path, info);
    }

    @Override
    public int read(final String path, final ByteBuffer buffer, final long size, final long offset, final StructFuseFileInfo info) {
        return log("read", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.read(path, buffer, size, offset, info);
            }
        }, path, buffer, size, offset, info);
    }

    @Override
    public int readdir(final String path, final StructFuseFileInfo info, final DirectoryFiller filler) {
        return log("readdir", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.readdir(path, info, filler);
            }
        }, path, filler);
    }

    @Override
    public int readlink(final String path, final ByteBuffer buffer, final long size) {
        return log("readlink", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.readlink(path, buffer, size);
            }
        }, path, buffer, size);
    }

    @Override
    public int release(final String path, final StructFuseFileInfo info) {
        return log("release", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.release(path, info);
            }
        }, path, info);
    }

    @Override
    public int releasedir(final String path, final StructFuseFileInfo info) {
        return log("releasedir", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.releasedir(path, info);
            }
        }, path, info);
    }

    @Override
    public int removexattr(final String path, final String xattr) {
        return log("removexattr", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.removexattr(path, xattr);
            }
        }, path, xattr);
    }

    @Override
    public int rename(final String path, final String newName) {
        return log("rename", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.rename(path, newName);
            }
        });
    }

    @Override
    public int rmdir(final String path) {
        return log("rmdir", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.rmdir(path);
            }
        }, path);
    }

    @Override
    public int setxattr(final String path, final String name, final ByteBuffer buf, final long size, final int flags, final int position) {
        return log("setxattr", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.setxattr(path, name, buf, size, flags, position);
            }
        }, path, name, buf, size, flags, position);
    }

    @Override
    public int statfs(final String path, final StructStatvfs statvfs) {
        return log("statfs", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.statfs(path, statvfs);
            }
        }, path, statvfs);
    }

    @Override
    public int symlink(final String path, final String target) {
        return log("symlink", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.symlink(path, target);
            }
        }, path, target);
    }

    @Override
    public int truncate(final String path, final long offset) {
        return log("truncate", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.truncate(path, offset);
            }
        }, path, offset);
    }

    @Override
    public int unlink(final String path) {
        return log("unlink", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.unlink(path);
            }
        }, path);
    }

    @Override
    public int utimens(final String path, final StructTimeBuffer timeBuffer) {
        return log("utimens", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.utimens(path, timeBuffer);
            }
        }, path, timeBuffer);
    }

    @Override
    public int write(final String path, final ByteBuffer buf, final long bufSize, final long writeOffset, final StructFuseFileInfo wrapper) {
        return log("write", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.write(path, buf, bufSize, writeOffset, wrapper);
            }
        }, path, buf, bufSize, writeOffset, wrapper);
    }

    @Override
    protected int ioctl(final String path, final int cmd, final Pointer arg, final StructFuseFileInfo fi, final long flags, final Pointer data) {
        return log("ioctl", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.ioctl(path, cmd, arg, fi, flags, data);
            }
        }, path, cmd, arg, fi, flags, data);
    }

    @Override
    protected int poll(final String path, final StructFuseFileInfo fi, final StructFusePollHandle ph, final Pointer reventsp) {
        return log("poll", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.poll(path, fi, ph, reventsp);
            }
        }, path, fi, ph, reventsp);
    }

    @Override
    protected int write_buf(final String path, final StructFuseBufvec buf, final long off, final StructFuseFileInfo fi) {
        return log("write_buf", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.write_buf(path, buf, off, fi);
            }
        }, path, buf, off, fi);
    }

    @Override
    protected int read_buf(final String path, final Pointer bufp, final long size, final long off, final StructFuseFileInfo fi) {
        return log("read_buf", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.read_buf(path, bufp, size, off, fi);
            }
        }, path, bufp, size, off, fi);
    }

    @Override
    protected int flock(final String path, final StructFuseFileInfo fi, final int op) {
        return log("flock", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.flock(path, fi, op);
            }
        }, path, fi, op);
    }

    @Override
    protected int fallocate(final String path, final int mode, final long off, final long length, final StructFuseFileInfo fi) {
        return log("fallocate", 0, new LoggedMethod<Integer>() {
            @Override
            public Integer invoke() {
                return filesystem.fallocate(path, mode, off, length, fi);
            }
        }, path, mode, off, length, fi);
    }

}
