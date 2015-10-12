package co.paralleluniverse.fuse;

import jnr.ffi.NativeType;
import jnr.ffi.Struct;
import co.paralleluniverse.fuse.StructFuseOperationsIfaces.*;
import static jnr.ffi.provider.jffi.ClosureHelper.toNative;

class StructFuseOperations extends Struct {
    private final Function<_getattr> getattr = function(_getattr.class);
    private final Function<_readlink> readlink = function(_readlink.class);
    private final Pointer getdir = new Pointer();
    private final Function<_mknod> mknod = function(_mknod.class);
    private final Function<_mkdir> mkdir = function(_mkdir.class);
    private final Function<_unlink> unlink = function(_unlink.class);
    private final Function<_rmdir> rmdir = function(_rmdir.class);
    private final Function<_symlink> symlink = function(_symlink.class);
    private final Function<_rename> rename = function(_rename.class);
    private final Function<_link> link = function(_link.class);
    private final Function<_chmod> chmod = function(_chmod.class);
    private final Function<_chown> chown = function(_chown.class);
    private final Function<_truncate> truncate = function(_truncate.class);
    private final Pointer utime = new Pointer();
    private final Function<_open> open = function(_open.class);
    private final Function<_read> read = function(_read.class);
    private final Function<_write> write = function(_write.class);
    private final Function<_statfs> statfs = function(_statfs.class);
    private final Function<_flush> flush = function(_flush.class);
    private final Function<_release> release = function(_release.class);
    private final Function<_fsync> fsync = function(_fsync.class);
    private final Pointer setxattr = new Pointer();
    private final Pointer getxattr = new Pointer();
    private final Function<_listxattr> listxattr = function(_listxattr.class);
    private final Function<_removexattr> removexattr = function(_removexattr.class);
    private final Function<_opendir> opendir = function(_opendir.class);
    private final Function<_readdir> readdir = function(_readdir.class);
    private final Function<_releasedir> releasedir = function(_releasedir.class);
    private final Function<_fsyncdir> fsyncdir = function(_fsyncdir.class);
    private final Function<_init> init = function(_init.class);
    private final Function<_destroy> destroy = function(_destroy.class);
    private final Function<_access> access = function(_access.class);
    private final Function<_create> create = function(_create.class);
    private final Function<_ftruncate> ftruncate = function(_ftruncate.class);
    private final Function<_fgetattr> fgetattr = function(_fgetattr.class);
    private final Function<_lock> lock = function(_lock.class);
    private final Function<_utimens> utimens = function(_utimens.class);
    private final Function<_bmap> bmap = function(_bmap.class);
    /**
     * Flag indicating that the filesystem can accept a NULL path
     * as the first argument for the following operations:
     *
     * read, write, flush, release, fsync, readdir, releasedir,
     * fsyncdir, ftruncate, fgetattr, lock, ioctl and poll
     *
     * If this flag is set these operations continue to work on
     * unlinked files even if "-ohard_remove" option was specified.
     */
    private final Padding flag_nullpath_ok = new Padding(NativeType.UCHAR, 1);
    /**
     * Flag indicating that the path need not be calculated for
     * the following operations:
     *
     * read, write, flush, release, fsync, readdir, releasedir,
     * fsyncdir, ftruncate, fgetattr, lock, ioctl and poll
     *
     * Closely related to flag_nullpath_ok, but if this flag is
     * set then the path will not be calculaged even if the file
     * wasn't unlinked.  However the path can still be non-NULL if
     * it needs to be calculated for some other reason.
     */
    private final Padding flag_nopath = new Padding(NativeType.UCHAR, 1);
    /**
     * Flag indicating that the filesystem accepts special
     * UTIME_NOW and UTIME_OMIT values in its utimens operation.
     */
    private final Padding flag_utime_omit_ok = new Padding(NativeType.UCHAR, 1);
    /**
     * Reserved flags, don't set
     */
    private final Padding flag_reserved = new Padding(NativeType.UCHAR, 1);
    private final Function<_ioctl> ioctl = function(_ioctl.class);
    private final Function<_poll> poll = function(_poll.class);
    private final Pointer write_buf = new Pointer();
    private final Pointer read_buf = new Pointer();
    private final Function<_flock> flock = function(_flock.class);
    private final Function<_fallocate> fallocate = function(_fallocate.class);
    
    @SuppressWarnings("unused")
    public StructFuseOperations(jnr.ffi.Runtime runtime, FuseFilesystem fs) {
        super(runtime);
        final Filesystem filesystem = new Filesystem(fs);
        getattr.set(filesystem);
        readlink.set(filesystem);
        mknod.set(filesystem);
        mkdir.set(filesystem);
        unlink.set(filesystem);
        rmdir.set(filesystem);
        symlink.set(filesystem);
        rename.set(filesystem);
        link.set(filesystem);
        chmod.set(filesystem);
        chown.set(filesystem);
        truncate.set(filesystem);
        utime.set((jnr.ffi.Pointer) null);
        open.set(filesystem);
        read.set(filesystem);
        write.set(filesystem);
        statfs.set(filesystem);
        flush.set(filesystem);
        release.set(filesystem);
        fsync.set(filesystem);
        switch (Platform.platform()) {
            case MAC:
            case MAC_MACFUSE:
                setxattr.set(toNative(_setxattr_MAC.class, filesystem));
                getxattr.set(toNative(_getxattr_MAC.class, filesystem));
                break;
            default:
                setxattr.set(toNative(_setxattr_NOT_MAC.class, filesystem));
                getxattr.set(toNative(_getxattr_NOT_MAC.class, filesystem));
        }
        listxattr.set(filesystem);
        removexattr.set(filesystem);
        opendir.set(filesystem);
        readdir.set(filesystem);
        releasedir.set(filesystem);
        fsyncdir.set(filesystem);
        init.set(filesystem);
        destroy.set(filesystem);
        access.set(filesystem);
        create.set(filesystem);
        ftruncate.set(filesystem);
        fgetattr.set(filesystem);
        lock.set(filesystem);
        utimens.set(filesystem);
        bmap.set(filesystem);
        ioctl.set(filesystem);
        poll.set(filesystem);
        flock.set(filesystem);
        fallocate.set(filesystem);
    }
}
