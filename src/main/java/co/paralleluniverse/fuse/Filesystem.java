package co.paralleluniverse.fuse;

import jnr.ffi.provider.jffi.ClosureHelper;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import jnr.ffi.Pointer;
import jnr.ffi.annotations.In;
import jnr.ffi.annotations.Out;
import jnr.ffi.types.dev_t;
import jnr.ffi.types.gid_t;
import jnr.ffi.types.mode_t;
import jnr.ffi.types.off_t;
import jnr.ffi.types.size_t;
import jnr.ffi.types.u_int32_t;
import jnr.ffi.types.uid_t;
import static co.paralleluniverse.fuse.JNRUtil.toByteBuffer;
import co.paralleluniverse.fuse.StructFuseOperationsIfaces.*;

class Filesystem implements
        _readlink, _mknod, _mkdir, _unlink, _rmdir, _symlink, _rename, _link, _chmod, _chown, _truncate,
        _open, _read, _write, _flush, _release, _fsync, _statfs, _lock, _getattr, _fgetattr,
        _setxattr_MAC, _setxattr_NOT_MAC,
        _getxattr_MAC, _getxattr_NOT_MAC,
        _listxattr, _removexattr,
        _opendir, _readdir, _releasedir, _fsyncdir,
        _init, _destroy, _access, _create, _ftruncate, _utimens, _bmap,
        _ioctl, _poll, _write_buf, _read_buf, _flock, _fallocate {
    private final FuseFilesystem fs;

    public Filesystem(FuseFilesystem fs) {
        this.fs = fs;
    }

    @Override
    public final int _getattr(String path, Pointer stat) {
        return fs.getattr(path, fs.defaultStat(new StructStat(stat, path)));
    }

    @Override
    public final int _readlink(String path, Pointer buffer, @size_t long size) {
        final ByteBuffer buf = toByteBuffer(buffer, size);
        final int result = fs.readlink(path, buf, size);
        if (result == 0) {
            try {
                buf.put((byte) 0);
            } catch (final BufferOverflowException e) {
                ((ByteBuffer) buf.position(buf.limit() - 1)).put((byte) 0);
            }
        }
        return result;
    }

    @Override
    public final int _mknod(String path, @mode_t long mode, @dev_t long dev) {
        return fs.mknod(path, mode, dev);
    }

    @Override
    public final int _mkdir(String path, @mode_t long mode) {
        return fs.mkdir(path, mode);
    }

    @Override
    public final int _unlink(String path) {
        return fs.unlink(path);
    }

    @Override
    public final int _rmdir(String path) {
        return fs.rmdir(path);
    }

    @Override
    public final int _symlink(String path, String target) {
        return fs.symlink(path, target);
    }

    @Override
    public final int _rename(String path, String newName) {
        return fs.rename(path, newName);
    }

    @Override
    public final int _link(String path, String target) {
        return fs.link(path, target);
    }

    @Override
    public final int _chmod(String path, @mode_t long mode) {
        return fs.chmod(path, mode);
    }

    @Override
    public final int _chown(String path, @uid_t long uid, @gid_t long gid) {
        return fs.chown(path, uid, gid);
    }

    @Override
    public final int _truncate(String path, @off_t long offset) {
        return fs.truncate(path, offset);
    }

    @Override
    public final int _open(String path, Pointer info) {
        return fs.open(path, new StructFuseFileInfo(info, path));
    }

    @Override
    public final int _read(String path, @Out Pointer buffer, @size_t long size, @off_t long offset, Pointer info) {
        final ByteBuffer buf = toByteBuffer(buffer, size);
        return fs.read(path, buf, size, offset, new StructFuseFileInfo(info, path));
    }

    @Override
    public final int _write(String path, @In Pointer buffer, @size_t long size, @off_t long offset, Pointer info) {
        final ByteBuffer buf = toByteBuffer(buffer, size);
        return fs.write(path, buf, size, offset, new StructFuseFileInfo(info, path));
    }

    @Override
    public final int _statfs(String path, Pointer statsvfs) {
        return fs.statfs(path, new StructStatvfs(statsvfs, path));
    }

    @Override
    public final int _flush(String path, Pointer info) {
        return fs.flush(path, new StructFuseFileInfo(info, path));
    }

    @Override
    public final int _release(String path, Pointer info) {
        return fs.release(path, new StructFuseFileInfo(info, path));
    }

    @Override
    public final int _fsync(String path, int datasync, @In Pointer info) {
        return fs.fsync(path, datasync, new StructFuseFileInfo(info, path));
    }

    @Override
    public final int _setxattr(String path, String xattr, Pointer value, @size_t long size, int flags) {
        return _setxattr(path, xattr, value, size, flags, 0);
    }

    @Override
    public final int _setxattr(String path, String xattr, Pointer value, @size_t long size, int flags, int position) {
        final ByteBuffer val = toByteBuffer(value, size);
        return fs.setxattr(path, xattr, val, size, flags, position);
    }

    @Override
    public final int _getxattr(String path, String xattr, Pointer buffer, @size_t long size) {
        return _getxattr(path, xattr, buffer, size, 0);
    }

    @Override
    public final int _getxattr(String path, String xattr, Pointer buffer, @size_t long size, @u_int32_t long position) {
        final XattrFiller filler = new XattrFiller(buffer == null ? null : toByteBuffer(buffer, size), size, (int) position);
        final int result = fs.getxattr(path, xattr, filler, size, position);
        return result < 0 ? result : (int) filler.getSize();
    }

    @Override
    public final int _listxattr(String path, Pointer buffer, @size_t long size) {
        final XattrListFiller filler = new XattrListFiller(buffer == null ? null : toByteBuffer(buffer, size), size);
        final int result = fs.listxattr(path, filler);
        return result < 0 ? result : (int) filler.requiredSize();
    }

    @Override
    public final int _removexattr(String path, String xattr) {
        return fs.removexattr(path, xattr);
    }

    @Override
    public final int _opendir(String path, Pointer info) {
        return fs.opendir(path, new StructFuseFileInfo(info, path));
    }

    @Override
    public final int _readdir(String path, Pointer buf, Pointer fillFunction, @off_t long offset, @In Pointer info) {
        return fs.readdir(path,
                new StructFuseFileInfo(info, path),
                new DirectoryFillerImpl(buf, ClosureHelper.getInstance().fromNative(fillFunction, DirectoryFillerImpl.fuse_fill_dir_t.class)));
    }

    @Override
    public final int _releasedir(String path, Pointer info) {
        return fs.releasedir(path, new StructFuseFileInfo(info, path));
    }

    @Override
    public final int _fsyncdir(String path, int datasync, @In Pointer info) {
        return fs.fsyncdir(path, datasync, new StructFuseFileInfo(info, path));
    }

    @Override
    public final void _init(Pointer conn) {
        fs.init();
    }

    @Override
    public final void _destroy() {
        fs.destroy();
        fs._destroy();
    }

    @Override
    public final int _access(String path, int access) {
        return fs.access(path, access);
    }

    @Override
    public final int _create(String path, @mode_t long mode, Pointer info) {
        return fs.create(path, mode, new StructFuseFileInfo(info, path));
    }

    @Override
    public final int _ftruncate(String path, @off_t long offset, @In Pointer info) {
        return fs.ftruncate(path, offset, new StructFuseFileInfo(info, path));
    }

    @Override
    public final int _fgetattr(String path, Pointer stat, Pointer info) {
        return fs.fgetattr(path,
                fs.defaultStat(new StructStat(stat, path)),
                new StructFuseFileInfo(info, path));
    }

    @Override
    public final int _lock(String path, Pointer info, int cmd, Pointer flock) {
        final StructFuseFileInfo fileWrapper = new StructFuseFileInfo(info, path);
        final StructFlock flockWrapper = new StructFlock(flock, path);
        final int result = fs.lock(path, fileWrapper, cmd, flockWrapper);
        return result;
    }

    @Override
    public final int _utimens(String path, Pointer timebuffer) {
        return fs.utimens(path, new StructTimeBuffer(timebuffer));
    }

    @Override
    public final int _bmap(String path, Pointer info) {
        return fs.bmap(path, new StructFuseFileInfo(info, path));
    }

    @Override
    public void _ioctl(String path, int cmd, Pointer arg, Pointer fi, long flags, Pointer data) {
        fs.ioctl(path, cmd, arg, new StructFuseFileInfo(fi, path), flags, data);
    }

    @Override
    public void _poll(String path, Pointer fi, Pointer ph, Pointer reventsp) {
        fs.poll(path, new StructFuseFileInfo(fi, path), new StructFusePollHandle(ph), reventsp);
    }

    @Override
    public void _write_buf(String path, Pointer buf, long off, Pointer fi) {
        fs.write_buf(path, new StructFuseBufvec(buf), off, new StructFuseFileInfo(fi, path));
    }

    @Override
    public void _read_buf(String path, Pointer bufp, long size, long off, Pointer fi) {
        fs.read_buf(path, bufp, size, off, new StructFuseFileInfo(fi, path));
    }

    @Override
    public void _flock(String path, Pointer fi, int op) {
        fs.flock(path, new StructFuseFileInfo(fi, path), op);
    }

    @Override
    public void _fallocate(String path, int mode, long off, long length, Pointer fi) {
        fs.fallocate(path, mode, off, length, new StructFuseFileInfo(fi, path));
    }
}
