package co.paralleluniverse.fuse;

import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.StructLayout;

public class StructStat {
    private static final class Layout extends StructLayout {
        public final dev_t st_dev;     // Device.
        public final ino_t st_ino;     // File serial number.
        public final mode_t st_mode;   // File mode.
        public final nlink_t st_nlink; // Link count.
        public final uid_t st_uid;     // User ID of the file's owner.
        public final gid_t st_gid;     // Group ID of the file's group.
        public final dev_t st_rdev;    // 
        public final StructTimespec.Layout st_atime; // Time of last access.
        public final StructTimespec.Layout st_mtime; // Time of last modification.
        public final StructTimespec.Layout st_ctime; // Time of last status change.
        public final StructTimespec.Layout st_birthtime;
        public final off_t st_size;        // Size of file, in bytes.
        public final blkcnt_t st_blocks;   // Number 512-byte blocks allocated.
        public final blksize_t st_blksize; // Optimal block size for I/O.
        public final int32_t st_gen;
        public final int32_t st_lspare;
        public final int64_t st_qspare;

        private Layout(Runtime runtime) {
            super(runtime);

            switch (Platform.platform()) {
                case LINUX_X86_64: {
                    this.st_dev = new dev_t();
                    this.st_ino = new ino_t();
                    this.st_nlink = new nlink_t();
                    this.st_mode = new mode_t();
                    this.st_uid = new uid_t();
                    this.st_gid = new gid_t();
                    int32_t __pad0 = new int32_t();
                    this.st_rdev = new dev_t();
                    this.st_size = new off_t();
                    this.st_blksize = new blksize_t();
                    this.st_blocks = new blkcnt_t();
                    this.st_atime = inner(new StructTimespec.Layout(getRuntime()));
                    this.st_mtime = inner(new StructTimespec.Layout(getRuntime()));
                    this.st_ctime = inner(new StructTimespec.Layout(getRuntime()));
                    this.st_birthtime = null;
                    this.st_gen = null;
                    this.st_lspare = null;
                    this.st_qspare = null;
                    break;
                }
                case LINUX_ARM:
                case LINUX_I686: {
                    this.st_dev = new dev_t();
                    int16_t __pad1 = new int16_t();
                    u_int32_t __st_ino = new u_int32_t();
                    this.st_mode = new mode_t();
                    this.st_nlink = new nlink_t();
                    this.st_uid = new uid_t();
                    this.st_gid = new gid_t();
                    this.st_rdev = new dev_t();
                    int16_t __pad2 = new int16_t();
                    this.st_size = new off_t();
                    this.st_blksize = new blksize_t();
                    this.st_blocks = new blkcnt_t();
                    this.st_atime = inner(new StructTimespec.Layout(getRuntime()));
                    this.st_mtime = inner(new StructTimespec.Layout(getRuntime()));
                    this.st_ctime = inner(new StructTimespec.Layout(getRuntime()));
                    this.st_ino = new ino_t();
                    this.st_birthtime = null;
                    this.st_gen = null;
                    this.st_lspare = null;
                    this.st_qspare = null;
                    break;
                }
                case LINUX_PPC: {
                    this.st_dev = new dev_t();
                    this.st_ino = new ino_t();
                    this.st_mode = new mode_t();
                    this.st_nlink = new nlink_t();
                    this.st_uid = new uid_t();
                    this.st_gid = new gid_t();
                    this.st_rdev = new dev_t();
                    int16_t __pad0 = new int16_t();
                    this.st_size = new off_t();
                    this.st_blksize = new blksize_t();
                    this.st_blocks = new blkcnt_t();
                    this.st_atime = inner(new StructTimespec.Layout(getRuntime()));
                    this.st_mtime = inner(new StructTimespec.Layout(getRuntime()));
                    this.st_ctime = inner(new StructTimespec.Layout(getRuntime()));
                    this.st_birthtime = null;
                    this.st_gen = null;
                    this.st_lspare = null;
                    this.st_qspare = null;
                    break;
                }
                case MAC: {
                    this.st_dev = new dev_t();
                    this.st_mode = new mode_t();
                    this.st_nlink = new nlink_t();
                    this.st_ino = new ino_t();
                    this.st_uid = new uid_t();
                    this.st_gid = new gid_t();
                    this.st_rdev = new dev_t();
                    this.st_atime = inner(new StructTimespec.Layout(getRuntime()));
                    this.st_mtime = inner(new StructTimespec.Layout(getRuntime()));
                    this.st_ctime = inner(new StructTimespec.Layout(getRuntime()));
                    this.st_birthtime = inner(new StructTimespec.Layout(getRuntime()));
                    this.st_size = new off_t();
                    this.st_blocks = new blkcnt_t();
                    this.st_blksize = new blksize_t();
                    this.st_gen = new int32_t();
                    this.st_lspare = new int32_t();
                    this.st_qspare = new int64_t();
                    break;
                }
                case FREEBSD:
                case MAC_MACFUSE: {
                    this.st_dev = new dev_t();
                    this.st_ino = new ino_t();
                    this.st_mode = new mode_t();
                    this.st_nlink = new nlink_t();
                    this.st_uid = new uid_t();
                    this.st_gid = new gid_t();
                    this.st_rdev = new dev_t();
                    this.st_atime = inner(new StructTimespec.Layout(getRuntime()));
                    this.st_mtime = inner(new StructTimespec.Layout(getRuntime()));
                    this.st_ctime = inner(new StructTimespec.Layout(getRuntime()));
                    this.st_size = new off_t();
                    this.st_blocks = new blkcnt_t();
                    this.st_blksize = new blksize_t();
                    this.st_birthtime = null;
                    this.st_gen = null;
                    this.st_lspare = null;
                    this.st_qspare = null;
                    break;
                }
                default:
                    throw new AssertionError();
            }
        }

    }

    private static final Layout layout = new Layout(Runtime.getSystemRuntime());

    private final Pointer p;
    final String path;

    public StructStat(Pointer p, String path) {
        this.p = p;
        this.path = path;
    }

    private void setTime(StructTimespec.Layout layout, long sec, long nsec) {
        StructTimespec.set(layout, p, sec, nsec);
    }

    public StructStat atime(long sec) {
        return atime(sec, 0);
    }

    public StructStat atime(long sec, long nsec) {
        setTime(layout.st_atime, sec, nsec);
        return this;
    }

    public StructStat blksize(long blksize) {
        layout.st_blksize.set(p, blksize);
        return this;
    }

    public StructStat blocks(long blocks) {
        layout.st_blocks.set(p, blocks);
        return this;
    }

    public StructStat ctime(long sec) {
        return ctime(sec, 0);
    }

    public StructStat ctime(long sec, long nsec) {
        setTime(layout.st_ctime, sec, nsec);
        return this;
    }

    public StructStat dev(long dev) {
        layout.st_dev.set(p, dev);
        return this;
    }

    public StructStat gen(long gen) {
        if (layout.st_gen != null)
            layout.st_gen.set(p, gen);
        return this;
    }

    public StructStat gid(long gid) {
        layout.st_gid.set(p, gid);
        return this;
    }

    public StructStat ino(long ino) {
        layout.st_ino.set(p, ino);
        return this;
    }

    public StructStat lspare(long lspare) {
        if (layout.st_lspare != null)
            layout.st_lspare.set(p, lspare);
        return this;
    }

    public long mode() {
        return layout.st_mode.get(p);
    }

    public StructStat mode(long bits) {
        layout.st_mode.set(p, bits);
        return this;
    }

    public StructStat mtime(long sec) {
        return mtime(sec, 0);
    }

    public StructStat mtime(long sec, long nsec) {
        setTime(layout.st_mtime, sec, nsec);
        return this;
    }

    public StructStat nlink(long nlink) {
        layout.st_nlink.set(p, nlink);
        return this;
    }

    public StructStat qspare(long qspare) {
        if (layout.st_qspare != null)
            layout.st_qspare.set(p, qspare);
        return this;
    }

    public StructStat rdev(long rdev) {
        layout.st_rdev.set(p, rdev);
        return this;
    }

    public StructStat setAllTimes(long sec, long nsec) {
        return setTimes(sec, nsec, sec, nsec, sec, nsec);
    }

    public StructStat setAllTimesMillis(long millis) {
        final long sec = millis / 1000L;
        final long nsec = (millis % 1000L) * 1000000L;
        return setAllTimes(sec, nsec);
    }

    public StructStat setAllTimesSec(long sec) {
        return setAllTimesSec(sec, sec, sec);
    }

    public StructStat setAllTimesSec(long atime, long mtime, long ctime) {
        return setAllTimesSec(atime, mtime, ctime, ctime);
    }

    public StructStat setAllTimesSec(long atime, long mtime, long ctime, long birthtime) {
        return setTimes(atime, 0, mtime, 0, ctime, 0);
    }

    public StructStat setTimes(long atime_sec, long atime_nsec, long mtime_sec,
                               long mtime_nsec, long ctime_sec, long ctime_nsec) {
        return setTimes(atime_sec, atime_nsec, mtime_sec, mtime_nsec, ctime_sec, ctime_nsec, ctime_sec, ctime_nsec);
    }

    public StructStat setTimes(long atime_sec, long atime_nsec, long mtime_sec, long mtime_nsec,
                               long ctime_sec, long ctime_nsec, long birthtime_sec, long birthtime_nsec) {
        setTime(layout.st_atime, atime_sec, atime_nsec);
        setTime(layout.st_mtime, mtime_sec, mtime_nsec);
        setTime(layout.st_ctime, ctime_sec, ctime_nsec);
        if (layout.st_birthtime != null)
            setTime(layout.st_birthtime, birthtime_sec, birthtime_nsec);
        return this;
    }

    public StructStat size(long size) {
        layout.st_size.set(p, size);
        return this;
    }

    public StructStat uid(long uid) {
        layout.st_uid.set(p, uid);
        return this;
    }

    @Override
    public java.lang.String toString() {
        if (path != null)
            return path + "\n" + JNRUtil.toString(layout, p);
        return JNRUtil.toString(layout, p);
    }
}
