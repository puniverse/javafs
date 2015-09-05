package co.paralleluniverse.fuse;

import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.StructLayout;
import jnr.ffi.Platform.CPU;

public class StructStatvfs {
    /* Definitions for the flag in `f_flag'.*/
    public static final int ST_RDONLY = 1;       // Mount read-only.
    public static final int ST_NOSUID = 2;       // Ignore suid and sgid bits.
    public static final int ST_NODEV = 4;        // Disallow access to device special files.
    public static final int ST_NOEXEC = 8;	 // Disallow program execution.
    public static final int ST_SYNCHRONOUS = 16; // Writes are synced at once.
    public static final int ST_MANDLOCK = 64;	 // Allow mandatory locks on an FS.
    public static final int ST_WRITE = 128;      // Write on file/directory/symlink.
    public static final int ST_APPEND = 256;	 // Append-only file.
    public static final int ST_IMMUTABLE = 512;	 // Immutable file.
    public static final int ST_NOATIME = 1024;	 // Do not update access times.
    public static final int ST_NODIRATIME = 2048;// Do not update directory access times.
    public static final int ST_RELATIME = 4096;	 // Update atime relative to mtime/ctime.

    private static final class Layout extends StructLayout {
        public final SignedLong f_bsize;  // file system block size
        public final SignedLong f_frsize; // fragment size
        public final blkcnt_t f_blocks;   // size of fs in f_frsize units
        public final blkcnt_t f_bfree;    // # free blocks
        public final blkcnt_t f_bavail;   // # free blocks for non-root
        public final fsfilcnt_t f_files;  // # inodes
        public final fsfilcnt_t f_ffree;  // # free inodes
        public final fsfilcnt_t f_favail; // # free inodes for non-root
        public final Signed32 f_unused;
        public final UnsignedLong f_flag;   // mount flags
        public final UnsignedLong f_namemax;// maximum filename length
        public final Signed32[] __f_spare;

        private Layout(Runtime runtime) {
            super(runtime);

            switch (Platform.platform()) {
                case FREEBSD: {
                    this.f_bavail = new blkcnt_t();
                    this.f_bfree = new blkcnt_t();
                    this.f_blocks = new blkcnt_t();
                    this.f_ffree = new fsfilcnt_t();
                    this.f_favail = new fsfilcnt_t();
                    this.f_files = new fsfilcnt_t();
                    this.f_bsize = new SignedLong();
                    SignedLong __pad0 = new SignedLong();
                    this.f_frsize = new SignedLong();
                    break;
                }
                default: {
                    this.f_bsize = new SignedLong();
                    this.f_frsize = new SignedLong();
                    this.f_blocks = new blkcnt_t();
                    this.f_bfree = new blkcnt_t();
                    this.f_bavail = new blkcnt_t();
                    this.f_files = new fsfilcnt_t();
                    this.f_ffree = new fsfilcnt_t();
                    this.f_favail = new fsfilcnt_t();
                }

            }
            final boolean is32bit = (jnr.ffi.Platform.getNativePlatform().getCPU() == CPU.I386 || jnr.ffi.Platform.getNativePlatform().getCPU() == CPU.ARM);
            this.f_unused = is32bit ? new Signed32() : null;
            this.f_flag = new UnsignedLong();
            this.f_namemax = new UnsignedLong();
            this.__f_spare = array(new Signed32[6]);
        }
    }

    private static final Layout layout = new Layout(Runtime.getSystemRuntime());
    private final Pointer p;
    private final String path;

    public StructStatvfs(Pointer p, String path) {
        this.p = p;
        this.path = path;
    }

    public final long bavail() {
        return layout.f_bavail.get(p);
    }

    public final StructStatvfs bavail(long f_bavail) {
        layout.f_bavail.set(p, f_bavail);
        return this;
    }

    public final long bfree() {
        return layout.f_bfree.get(p);
    }

    public final StructStatvfs bfree(long f_bfree) {
        layout.f_bfree.set(p, f_bfree);
        return this;
    }

    public final long blocks() {
        return layout.f_blocks.get(p);
    }

    public final StructStatvfs blocks(long f_blocks) {
        layout.f_blocks.set(p, f_blocks);
        return this;
    }

    public final long bsize() {
        return layout.f_bsize.get(p);
    }

    public final StructStatvfs bsize(long f_bsize) {
        layout.f_bsize.set(p, f_bsize);
        return this;
    }

    public final long favail() {
        return layout.f_favail.get(p);
    }

    public final StructStatvfs favail(long f_favail) {
        layout.f_favail.set(p, f_favail);
        return this;
    }

    public final long ffree() {
        return layout.f_ffree.get(p);
    }

    public final StructStatvfs ffree(long f_ffree) {
        layout.f_ffree.set(p, f_ffree);
        return this;
    }

    public final long files() {
        return layout.f_files.get(p);
    }

    public final StructStatvfs files(long f_files) {
        layout.f_files.set(p, f_files);
        return this;
    }

    public final long frsize() {
        return layout.f_frsize.get(p);
    }

    public final StructStatvfs frsize(long f_frsize) {
        layout.f_frsize.set(p, f_frsize);
        return this;
    }

    public final long flags() {
        return layout.f_flag.get(p);
    }

    public final StructStatvfs flags(long f_flags) {
        layout.f_flag.set(p, f_flags);
        return this;
    }

    public final StructStatvfs set(long blockSize, long fragmentSize, long freeBlocks, long availBlocks, long totalBlocks,
                                   long freeFiles, long availFiles, long totalFiles) {
        return setSizes(blockSize, fragmentSize).setBlockInfo(freeBlocks, availBlocks, totalBlocks).setFileInfo(freeFiles,
                availFiles, totalFiles);
    }

    public final StructStatvfs setBlockInfo(long freeBlocks, long availBlocks, long totalBlocks) {
        return bfree(freeBlocks).bavail(availBlocks).blocks(totalBlocks);
    }

    public final StructStatvfs setFileInfo(long freeFiles, long availFiles, long totalFiles) {
        return ffree(freeFiles).favail(availFiles).files(totalFiles);
    }

    public final StructStatvfs setSizes(long blockSize, long fragmentSize) {
        return bsize(blockSize).frsize(fragmentSize);
    }

    @Override
    public final java.lang.String toString() {
        if (path != null)
            return path + "\n" + JNRUtil.toString(layout, p);
        return JNRUtil.toString(layout, p);
    }
}
