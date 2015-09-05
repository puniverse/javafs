package co.paralleluniverse.fuse;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import jnr.ffi.Pointer;
import jnr.ffi.types.gid_t;
import jnr.ffi.types.pid_t;
import jnr.ffi.types.uid_t;

/**
 * Fuse file system.
 * All documentation from "fuse.h"
 *
 * @see http://fuse.sourceforge.net/doxygen/index.html
 * @see http://fuse.sourceforge.net/wiki/
 */
public abstract class FuseFilesystem {
    private static final String defaultFilesystemName = "userfs-";
    private static final Pattern regexNormalizeFilesystemName = Pattern.compile("[^a-zA-Z]");

    /**
     * Perform destroy-time cleanup. Takes two {@link FuseFilesystem}s arguments which should be equal in most cases, but may
     * not in the case of a wrapped filesystem object for logging ({@link LoggedFuseFilesystem}).
     *
     * @param mountedFilesystem The {@link FuseFilesystem} object that is actually mounted (the one receiving the destroy call)
     * @param userFilesystem    The {@link FuseFilesystem} that the user believes is mounted (the one that the user called .mount on)
     */
    final static void _destroy(FuseFilesystem mountedFilesystem, FuseFilesystem userFilesystem) {
        final Path oldMountPoint;
        mountedFilesystem.mountLock.lock();
        userFilesystem.mountLock.lock();
        try {
            if (!mountedFilesystem.isMounted())
                throw new IllegalStateException("destroy called on a non-mounted filesystem");

            oldMountPoint = mountedFilesystem.mountPoint;
            Fuse.destroyed(mountedFilesystem);
            userFilesystem.mountPoint = null;
            mountedFilesystem.mountPoint = null;
        } finally {
            userFilesystem.mountLock.unlock();
            mountedFilesystem.mountLock.unlock();
        }
        mountedFilesystem.afterUnmount(oldMountPoint);
    }

    private final ReentrantLock mountLock = new ReentrantLock();
    private final AutoUnmountHook unmountHook = new AutoUnmountHook();
    private volatile Path mountPoint = null;
    private Logger logger = null;

    protected abstract void afterUnmount(Path mountPoint);

    protected abstract void beforeMount(Path mountPoint);

    public final boolean isMounted() {
        return getMountPoint() != null;
    }

    void _destroy() {
        _destroy(this, this);
    }

    /**
     * Returns the raw fuse_context structure. Only valid when called while a filesystem operation is taking place.
     *
     * @return The fuse_context structure by reference.
     */
    protected final StructFuseContext getFuseContext() {
        if (!isMounted())
            throw new IllegalStateException("Cannot get FUSE context if the filesystem is not mounted.");

        return Fuse.getFuseContext();
    }

    /**
     * Returns the gid field of the fuse context. Only valid when called while a filesystem operation is taking place.
     *
     * @return The group ID of the process executing an operation on this filesystem.
     */
    protected @gid_t long getFuseContextGid() {
        return getFuseContext().gid.get();
    }

    /**
     * Returns the pid field of the fuse context. Only valid when called while a filesystem operation is taking place.
     *
     * @return The process ID of the process executing an operation on this filesystem.
     */
    protected @pid_t long getFuseContextPid() {
        return getFuseContext().pid.get();
    }

    /**
     * Returns the uid field of the fuse context. Only valid when called while a filesystem operation is taking place.
     *
     * @return The user ID of the user running the process executing an operation of this filesystem.
     */
    protected @uid_t long getFuseContextUid() {
        return getFuseContext().uid.get();
    }

    final String getFuseName() {
        String name = getName();
        if (name == null)
            return defaultFilesystemName;

        name = regexNormalizeFilesystemName.matcher(name).replaceAll("");
        if (name.isEmpty())
            return defaultFilesystemName;

        return name.toLowerCase();
    }

    protected final Logger getLogger() {
        return logger;
    }

    public final Path getMountPoint() {
        return this.mountPoint;
    }

    protected abstract String getName();

    protected abstract String[] getOptions();

    final AutoUnmountHook getUnmountHook() {
        return unmountHook;
    }

    /**
     * Populates a {@link StatWrapper} with somewhat-sane, usually-better-than-zero values. Subclasses may override this to
     * customize the default parameters applied to the stat structure, or to prevent such behavior in the first place (by
     * overriding this method with an empty one).
     *
     * @param stat The StructStat object to write to.
     * @param uid  The UID under which the JVM is running.
     * @param gid  The GID under which the JVM is running.
     */
    protected StructStat defaultStat(StructStat stat) {
        // Set some sensible defaults
        stat.mode(0).setAllTimesMillis(Fuse.getInitTime()).nlink(1).uid(Fuse.getUid()).gid(Fuse.getGid());
        return stat;
    }

    public final FuseFilesystem log(boolean logging) {
        return log(logging ? Logger.getLogger(getClass().getCanonicalName()) : null);
    }

    public final FuseFilesystem log(Logger logger) {
        mountLock.lock();
        try {
            if (mountPoint != null)
                throw new IllegalStateException("Cannot turn logging on/orr when filesystem is already mounted.");
            this.logger = logger;
        } finally {
            mountLock.unlock();
        }
        return this;
    }

    final void mount(Path mountPoint, boolean blocking) throws IOException {
        mountLock.lock();
        try {
            if (isMounted()) {
                mountLock.unlock();
                throw new IllegalStateException(getFuseName() + " is already mounted at " + this.mountPoint);
            }
            this.mountPoint = mountPoint;
        } finally {
            mountLock.unlock();
        }
        beforeMount(mountPoint);
    }

    final void unmount() throws IOException {
        if (!isMounted())
            throw new IllegalStateException("Tried to unmount a filesystem which is not mounted");
    }

    final class AutoUnmountHook extends Thread {
        @Override
        public final void run() {
            try {
                Fuse.unmount(FuseFilesystem.this);
            } catch (final Exception e) {
                // Can't do much here in a shutdown hook. Silently ignore.
            }
        }
    }

    ////////////////////////////////
    /**
     * Get file attributes.
     *
     * Similar to stat(). The 'st_dev' and 'st_blksize' fields are ignored.
     * The 'st_ino' field is ignored except if the 'use_ino' mount option is given.
     *
     * @param path
     * @param stat
     * @return
     */
    protected abstract int getattr(String path, StructStat stat);

    /**
     * Read the target of a symbolic link.
     *
     * The buffer should be filled with a null terminated string.
     * The buffer size argument includes the space for the terminating null character.
     * If the linkname is too long to fit in the buffer, it should be truncated. The return value should be 0 for success.
     *
     * @param path
     * @param buffer
     * @param size
     * @return
     */
    protected abstract int readlink(String path, ByteBuffer buffer, long size);

    /**
     * Create a file node.
     *
     * This is called for creation of all non-directory, non-symlink nodes.
     * If the filesystem defines a create() method, then for regular files that will be called instead.
     *
     * @param path
     * @param mode
     * @param dev
     * @return
     */
    protected abstract int mknod(String path, long mode, long dev);

    /**
     * Create a directory.
     *
     * Note that the mode argument may not have the type specification bits set, i.e. S_ISDIR(mode) can be false.
     * To obtain the correct directory type bits use mode|S_IFDIR
     *
     * @param path
     * @param mode
     * @return
     */
    protected abstract int mkdir(String path, long mode);

    /**
     * Remove a file.
     *
     * @param path
     * @return
     */
    protected abstract int unlink(String path);

    /**
     * Remove a directory.
     *
     * @param path
     * @return
     */
    protected abstract int rmdir(String path);

    /**
     * Create a symbolic link.
     *
     * @param path
     * @param target
     * @return
     */
    protected abstract int symlink(String path, String target);

    /**
     * Rename a file.
     *
     * @param path
     * @param newName
     * @return
     */
    protected abstract int rename(String path, String newName);

    /**
     * Create a hard link to a file.
     *
     * @param path
     * @param target
     * @return
     */
    protected abstract int link(String path, String target);

    /**
     * Change the permission bits of a file.
     *
     * @param path
     * @param mode
     * @return
     */
    protected abstract int chmod(String path, long mode);

    /**
     * Change the owner and group of a file.
     *
     * @param path
     * @param uid
     * @param gid
     * @return
     */
    protected abstract int chown(String path, long uid, long gid);

    /**
     * Change the size of a file.
     *
     * @param path
     * @param offset
     * @return
     */
    protected abstract int truncate(String path, long offset);

    /**
     * File open operation.
     * <p>
     * No creation (O_CREAT, O_EXCL) and by default also no truncation (O_TRUNC) flags will be passed to open().
     * If an application specifies O_TRUNC, fuse first calls truncate() and then open().
     * Only if 'atomic_o_trunc' has been specified and kernel version is 2.6.24 or later, O_TRUNC is passed on to open.
     * <p>
     * Unless the 'default_permissions' mount option is given, open should check if the operation is permitted for the given flags.
     * Optionally open may also return an arbitrary filehandle in the fuse_file_info structure, which will be passed to all file operations.
     * <p>
     * Changed in version 2.2
     *
     * @param path
     * @param info
     * @return
     */
    protected abstract int open(String path, StructFuseFileInfo info);

    /**
     * Read data from an open file.
     * <p>
     * Read should return exactly the number of bytes requested except on EOF or error,
     * otherwise the rest of the data will be substituted with zeroes.
     * An exception to this is when the 'direct_io' mount option is specified,
     * in which case the return value of the read system call will reflect the return value of this operation.
     * <p>
     * Changed in version 2.2
     *
     * @param path
     * @param buffer
     * @param size
     * @param offset
     * @param info
     * @return
     */
    protected abstract int read(String path, ByteBuffer buffer, long size, long offset, StructFuseFileInfo info);

    /**
     * Write data to an open file.
     * <p>
     * Write should return exactly the number of bytes requested except on error.
     * An exception to this is when the 'direct_io' mount option is specified (see read operation).
     * <p>
     * Changed in version 2.2
     *
     * @param path
     * @param buf
     * @param bufSize
     * @param writeOffset
     * @param info
     * @return
     */
    protected abstract int write(String path, ByteBuffer buf, long bufSize, long writeOffset, StructFuseFileInfo info);

    /**
     * Get file system statistics.
     * <p>
     * The 'f_frsize', 'f_favail', 'f_fsid' and 'f_flag' fields are ignored
     * <p>
     * Replaced 'struct statfs' parameter with 'struct statvfs' in version 2.5
     *
     * @param path
     * @param statvfs
     * @return
     */
    protected abstract int statfs(String path, StructStatvfs statvfs);

    /**
     * Possibly flush cached data.
     * <p>
     * BIG NOTE: This is not equivalent to fsync(). It's not a request to sync dirty data.
     * <p>
     * Flush is called on each close() of a file descriptor.
     * So if a filesystem wants to return write errors in close() and the file has cached dirty data,
     * this is a good place to write back data and return any errors. Since many applications ignore close() errors this is not always useful.
     * <p>
     * NOTE: The flush() method may be called more than once for each open(). This happens if more than one file descriptor refers to an opened file due to dup(), dup2() or fork() calls. It is not possible to determine if a flush is final, so each flush should be treated equally. Multiple write-flush sequences are relatively rare, so this shouldn't be a problem.
     * <p>
     * Filesystems shouldn't assume that flush will always be called after some writes, or that if will be called at all.
     * <p>
     * Changed in version 2.2
     *
     * @param path
     * @param info
     * @return
     */
    protected abstract int flush(String path, StructFuseFileInfo info);

    /**
     * Release an open file.
     * <p>
     * Release is called when there are no more references to an open file: all file descriptors are closed and all memory mappings are unmapped.
     * <p>
     * For every open() call there will be exactly one release() call with the same flags and file descriptor.
     * It is possible to have a file opened more than once, in which case only the last release will mean, that no more reads/writes will happen on the file.
     * The return value of release is ignored.
     * <p>
     * Changed in version 2.2
     *
     * @param path
     * @param info
     * @return
     */
    protected abstract int release(String path, StructFuseFileInfo info);

    /**
     * Synchronize file contents.
     * <p>
     * If the datasync parameter is non-zero, then only the user data should be flushed, not the meta data.
     * <p>
     * Changed in version 2.2
     *
     * @param path
     * @param datasync
     * @param info
     * @return
     */
    protected abstract int fsync(String path, int datasync, StructFuseFileInfo info);

    /**
     * Set extended attribute.
     *
     * Set the attribute NAME of the file pointed to by PATH to VALUE (which
     * is SIZE bytes long).
     *
     * @param flags see {@link XAttrConstants}
     * @return Return 0 on success, -1 for errors.
     */
    protected abstract int setxattr(String path, String xattr, ByteBuffer value, long size, int flags, int position);

    /**
     * Get extended attribute.
     *
     * Get the attribute NAME of the file pointed to by PATH to VALUE (which is
     * SIZE bytes long).
     *
     * @return Return 0 on success, -1 for errors.
     */
    protected abstract int getxattr(String path, String xattr, XattrFiller filler, long size, long position);

    /**
     * List extended attributes.
     * <p>
     * The retrieved list is placed
     * in list, a caller-allocated buffer whose size (in bytes) is specified
     * in the argument size. The list is the set of (null-terminated)
     * names, one after the other. Names of extended attributes to which
     * the calling process does not have access may be omitted from the
     * list. The length of the attribute name list is returned
     *
     * @param path
     * @param filler
     * @return
     */
    protected abstract int listxattr(String path, XattrListFiller filler);

    /**
     * Remove extended attributes.
     * Remove the attribute NAME from the file pointed to by PATH.
     *
     * @param path
     * @param xattr
     * @return 0 on success, -1 for errors.
     */
    protected abstract int removexattr(String path, String xattr);

    /**
     * Open directory.
     * <p>
     * Unless the 'default_permissions' mount option is given, this method should check if opendir is permitted for this directory.
     * Optionally opendir may also return an arbitrary filehandle in the fuse_file_info structure,
     * which will be passed to readdir, closedir and fsyncdir.
     * <p>
     * Introduced in version 2.3
     *
     * @param path
     * @param info
     * @return
     */
    protected abstract int opendir(String path, StructFuseFileInfo info);

    /**
     * Read directory.
     *
     * The filesystem may choose between two modes of operation:
     * <p>
     * <ol>
     * <li>The readdir implementation ignores the offset parameter, and passes zero to the filler function's offset.
     * The filler function will not return '1' (unless an error happens), so the whole directory is read in a single readdir operation.</li>
     *
     * <li>The readdir implementation keeps track of the offsets of the directory entries.
     * It uses the offset parameter and always passes non-zero offset to the filler function.
     * When the buffer is full (or an error happens) the filler function will return '1'.</li>
     * </ol>
     * Introduced in version 2.3
     *
     * @param path
     * @param filler
     * @return
     */
    protected abstract int readdir(String path, StructFuseFileInfo info, DirectoryFiller filler);

    /**
     * Release directory.
     * <p>
     * Introduced in version 2.3
     *
     * @param path
     * @param info
     * @return
     */
    protected abstract int releasedir(String path, StructFuseFileInfo info);

    /**
     * Synchronize directory contents.
     * <p>
     * If the datasync parameter is non-zero, then only the user data should be flushed, not the meta data
     * <p>
     * Introduced in version 2.3
     *
     * @param path
     * @param datasync
     * @param info
     * @return
     */
    protected abstract int fsyncdir(String path, int datasync, StructFuseFileInfo info);

    /**
     * Initialize filesystem.
     * <p>
     * The return value will passed in the private_data field of fuse_context to all file operations and as a parameter to the destroy() method.
     * <p>
     * Introduced in version 2.3 Changed in version 2.6
     */
    protected abstract void init();

    /**
     * Clean up filesystem
     * <p>
     * Called on filesystem exit.
     * <p>
     * Introduced in version 2.3
     */
    protected abstract void destroy();

    /**
     * Check file access permissions.
     * <p>
     * This will be called for the access() system call.
     * If the 'default_permissions' mount option is given, this method is not called.
     * <p>
     * This method is not called under Linux kernel versions 2.4.x
     * <p>
     * Introduced in version 2.5
     *
     * @param mask see {@link AccessConstants}
     * @return -ENOENT if the path doesn't exist, -EACCESS if the requested permission isn't available, or 0 for success
     * @return
     */
    protected abstract int access(String path, int access);

    /**
     * Create and open a file.
     * <p>
     * If the file does not exist, first create it with the specified mode, and then open it.
     * <p>
     * If this method is not implemented or under Linux kernel versions earlier than 2.6.15,
     * the mknod() and open() methods will be called instead.
     * <p>
     * Introduced in version 2.5
     *
     * @param path
     * @param mode
     * @param info
     * @return
     */
    protected abstract int create(String path, long mode, StructFuseFileInfo info);

    /**
     * Change the size of an open file.
     * <p>
     * This method is called instead of the truncate() method if the truncation was invoked from an ftruncate() system call.
     * <p>
     * If this method is not implemented or under Linux kernel versions earlier than 2.6.15, the truncate() method will be called instead.
     * <p>
     * Introduced in version 2.5
     *
     * @param path
     * @param offset
     * @param info
     * @return
     */
    protected abstract int ftruncate(String path, long offset, StructFuseFileInfo info);

    /**
     * Get attributes from an open file.
     * <p>
     * This method is called instead of the getattr() method if the file information is available.
     * <p>
     * Currently this is only called after the create() method if that is implemented (see above).
     * Later it may be called for invocations of fstat() too.
     * <p>
     * Introduced in version 2.5
     *
     * @param path
     * @param stat
     * @param info
     * @return
     */
    protected abstract int fgetattr(String path, StructStat stat, StructFuseFileInfo info);

    /**
     * Perform POSIX file locking operation.
     * <p>
     * The cmd argument will be either F_GETLK, F_SETLK or F_SETLKW.
     * <p>
     * For the meaning of fields in 'struct flock' see the man page for fcntl(2). The l_whence field will always be set to SEEK_SET.
     * <p>
     * For checking lock ownership, the 'fuse_file_info->owner' argument must be used.
     * <p>
     * For F_GETLK operation, the library will first check currently held locks,
     * and if a conflicting lock is found it will return information without calling this method.
     * This ensures, that for local locks the l_pid field is correctly filled in.
     * The results may not be accurate in case of race conditions and in the presence of hard links,
     * but it's unlikely that an application would rely on accurate GETLK results in these cases.
     * If a conflicting lock is not found, this method will be called, and the filesystem may fill out l_pid by a meaningful value,
     * or it may leave this field zero.
     * <p>
     * For F_SETLK and F_SETLKW the l_pid field will be set to the pid of the process performing the locking operation.
     * <p>
     * Note: if this method is not implemented, the kernel will still allow file locking to work locally.
     * Hence it is only interesting for network filesystems and similar.
     * <p>
     * Introduced in version 2.6
     *
     * @param path
     * @param info
     * @param command
     * @param flock
     * @return
     */
    protected abstract int lock(String path, StructFuseFileInfo info, int command, StructFlock flock);

    /**
     * Change the access and modification times of a file with nanosecond resolution.
     * <p>
     * This supersedes the old utime() interface. New applications should use this.
     * <p>
     * See the utimensat(2) man page for details.
     * <p>
     * Introduced in version 2.6
     *
     * @param path
     * @param timeBuffer
     * @return
     */
    protected abstract int utimens(String path, StructTimeBuffer timeBuffer);

    /**
     * Map block index within file to block index within device/
     * <p>
     * Note: This makes sense only for block device backed filesystems mounted with the 'blkdev' option
     * <p>
     * Introduced in version 2.6
     *
     * @param path
     * @param info
     * @return
     */
    protected abstract int bmap(String path, StructFuseFileInfo info);

    /**
     * Ioctl.
     * <p>
     * flags will have FUSE_IOCTL_COMPAT set for 32bit ioctls in 64bit environment.
     * The size and direction of data is determined by _IOC_*() decoding of cmd.
     * For _IOC_NONE, data will be NULL, for _IOC_WRITE data is out area, for _IOC_READ in area and if both are set in/out area.
     * In all non-NULL cases, the area is of _IOC_SIZE(cmd) bytes.
     *
     * @param flags See {
     * @IoctlFlags}
     */
    protected abstract int ioctl(String path, int cmd, Pointer arg, StructFuseFileInfo fi, long flags, Pointer data);

    /**
     * Poll for IO readiness events.
     * <p>
     * Note: If ph is non-NULL, the client should notify when IO readiness events occur by calling
     * fuse_notify_poll() with the specified ph.
     * <p>
     * Regardless of the number of times poll with a non-NULL ph is received, single notification is enough to clear all.
     * Notifying more times incurs overhead but doesn't harm correctness.
     * <p>
     * The callee is responsible for destroying ph with fuse_pollhandle_destroy() when no longer in use.
     *
     * @param reventsp A pointer to a bitmask of the returned events satisfied.
     */
    protected abstract int poll(String path, StructFuseFileInfo fi, StructFusePollHandle ph, Pointer reventsp);

    /**
     * Write contents of buffer to an open file.
     * <p>
     * Similar to the write() method, but data is supplied in a generic buffer.
     * Use fuse_buf_copy() to transfer data to the destination.
     */
    protected abstract int write_buf(String path, StructFuseBufvec buf, long off, StructFuseFileInfo fi);

    /**
     * Store data from an open file in a buffer.
     * <p>
     * Similar to the read() method, but data is stored and returned in a generic buffer.
     * <p>
     * No actual copying of data has to take place, the source file descriptor may simply be stored in the buffer for later data transfer.
     * <p>
     * The buffer must be allocated dynamically and stored at the location pointed to by bufp.
     * If the buffer contains memory regions, they too must be allocated using malloc().
     * The allocated memory will be freed by the caller.
     */
    protected abstract int read_buf(String path, Pointer bufp, long size, long off, StructFuseFileInfo fi);

    /**
     * Perform BSD file locking operation
     * <p>
     * The op argument will be either LOCK_SH, LOCK_EX or LOCK_UN
     * <p>
     * Nonblocking requests will be indicated by ORing LOCK_NB to
     * the above operations
     * <p>
     * For more information see the flock(2) manual page.
     * <p>
     * Additionally fi->owner will be set to a value unique to this open file.
     * This same value will be supplied to ->release() when the file is released.
     * <p>
     * Note: if this method is not implemented, the kernel will still allow file locking to work locally.
     * Hence it is only interesting for network filesystems and similar.
     *
     * @param op see {@link ru.serce.jnrfuse.struct.Flock}
     */
    protected abstract int flock(String path, StructFuseFileInfo fi, int op);

    /**
     * Allocates space for an open file.
     * <p>
     * This function ensures that required space is allocated for specified file.
     * If this function returns success then any subsequent write request to specified range is guaranteed not to fail because of lack
     * of space on the file system media.
     */
    protected abstract int fallocate(String path, int mode, long off, long length, StructFuseFileInfo fi);
}
