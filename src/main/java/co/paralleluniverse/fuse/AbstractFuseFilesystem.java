package co.paralleluniverse.fuse;

import java.nio.ByteBuffer;
import java.nio.file.Path;
import jnr.constants.platform.Errno;
import jnr.ffi.Pointer;

/**
 * An adapter that tries to put sane defaults for default return values. Will silently pretend that most non-critical operations
 * have succeeded, but return ENOSYS on non-implemented important operations.
 */
public abstract class AbstractFuseFilesystem extends FuseFilesystem {
    @Override
    protected String getName() {
        return null;
    }

    @Override
    protected String[] getOptions() {
        return null;
    }

    @Override
    protected void afterUnmount(Path mountPoint) {
    }

    @Override
    protected void beforeMount(Path mountPoint) {
    }

    @Override
    protected int getattr(String path, StructStat stat) {
        return -Errno.ENOSYS.intValue();
    }

    @Override
    protected int readlink(String path, ByteBuffer buffer, long size) {
        return 0;
    }

    @Override
    protected int mknod(String path, long mode, long dev) {
        return create(path, mode, null);
    }

    @Override
    protected int mkdir(String path, long mode) {
        return 0;
    }

    @Override
    protected int unlink(String path) {
        return 0;
    }

    @Override
    protected int rmdir(String path) {
        return 0;
    }

    @Override
    protected int symlink(String path, String target) {
        return 0;
    }

    @Override
    protected int rename(String path, String newName) {
        return 0;
    }

    @Override
    protected int link(String path, String target) {
        return 0;
    }

    @Override
    protected int chmod(String path, long mode) {
        return 0;
    }

    @Override
    protected int chown(String path, long uid, long gid) {
        return 0;
    }

    @Override
    protected int truncate(String path, long offset) {
        return 0;
    }

    @Override
    protected int open(String path, StructFuseFileInfo info) {
        return 0;
    }

    @Override
    protected int read(String path, ByteBuffer buffer, long size, long offset, StructFuseFileInfo info) {
        return 0;
    }

    @Override
    protected int write(String path, ByteBuffer buf, long bufSize, long writeOffset, StructFuseFileInfo wrapper) {
        return 0;
    }

    @Override
    protected int statfs(String path, StructStatvfs stratvfs) {
        return 0;
    }

    @Override
    protected int flush(String path, StructFuseFileInfo info) {
        return 0;
    }

    @Override
    protected int release(String path, StructFuseFileInfo info) {
        return 0;
    }

    @Override
    protected int fsync(String path, int datasync, StructFuseFileInfo info) {
        return 0;
    }

    @Override
    protected int setxattr(String path, String xattr, ByteBuffer buf, long size, int flags, int position) {
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
        return 0;
    }

    @Override
    protected int readdir(String path, StructFuseFileInfo info, DirectoryFiller filler) {
        return 0;
    }

    @Override
    protected int releasedir(String path, StructFuseFileInfo info) {
        return 0;
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
    }

    @Override
    protected int access(String path, int access) {
        return -Errno.ENOSYS.intValue();
    }

    @Override
    protected int create(String path, long mode, StructFuseFileInfo info) {
        return -Errno.ENOSYS.intValue();
    }

    @Override
    public int ftruncate(String path, long offset, StructFuseFileInfo info) {
        return truncate(path, offset);
    }

    @Override
    protected int fgetattr(String path, StructStat stat, StructFuseFileInfo info) {
        return getattr(path, stat);
    }

    @Override
    protected int lock(String path, StructFuseFileInfo info, int command, StructFlock flock) {
        return -Errno.ENOSYS.intValue();
    }

    @Override
    protected int utimens(String path, StructTimeBuffer timeBuffer) {
        return -Errno.ENOSYS.intValue();
    }

    @Override
    protected int bmap(String path, StructFuseFileInfo info) {
        return 0;
    }

    @Override
    public int ioctl(String path, int cmd, Pointer arg, StructFuseFileInfo fi, long flags, Pointer data) {
        return -ErrorCodes.ENOSYS();
    }

    @Override
    public int poll(String path, StructFuseFileInfo fi, StructFusePollHandle ph, Pointer reventsp) {
        return -ErrorCodes.ENOSYS();
    }

    @Override
    protected int write_buf(String path, StructFuseBufvec buf, long off, StructFuseFileInfo fi) {
        return -ErrorCodes.ENOSYS();
    }

    @Override
    protected int read_buf(String path, Pointer bufp, long size, long off, StructFuseFileInfo fi) {
        return -ErrorCodes.ENOSYS();
    }

    @Override
    public int flock(String path, StructFuseFileInfo fi, int op) {
        return -ErrorCodes.ENOSYS();
    }

    @Override
    public int fallocate(String path, int mode, long off, long length, StructFuseFileInfo fi) {
        return -ErrorCodes.ENOSYS();
    }

}
