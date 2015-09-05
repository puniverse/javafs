package co.paralleluniverse.fuse;

import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.StructLayout;

public class StructFuseFileInfo {
    private static final class Layout extends StructLayout {

        private Layout(Runtime runtime) {
            super(runtime);
        }

        public final Signed32 flags = new Signed32();
        public final SignedLong fh_old = new SignedLong();
        public final Signed32 writepage = new Signed32();
        public final Signed32 flags_bitfield = new Signed32();
        public final Unsigned64 fh = new Unsigned64();
        public final Unsigned64 lock_owner = new Unsigned64();
    }
    private static final Layout layout = new Layout(Runtime.getSystemRuntime());

    private final Pointer p;
    private final String path;

    StructFuseFileInfo(Pointer p, String path) {
        this.p = p;
        this.path = path;
    }

    @Override
    public final java.lang.String toString() {
        if (path != null)
            return path + "\n" + JNRUtil.toString(layout, p);
        return JNRUtil.toString(layout, p);
    }

//    FuseFileInfo(final FuseFileInfo fileinfo) {
//        this(null, fileinfo);
//    }
    public final boolean append() {
        return (layout.flags.intValue(p) & O_APPEND) != 0;
    }

    public final StructFuseFileInfo append(boolean append) {
        layout.flags.set(p, (layout.flags.get(p) & ~O_APPEND) | (append ? O_APPEND : 0));
        return this;
    }

    public final boolean create() {
        return (layout.flags.get(p) & O_CREAT) != 0;
    }

    public final StructFuseFileInfo create(boolean create) {
        layout.flags.set(p, (layout.flags.get(p) & ~O_CREAT) | (create ? O_CREAT : 0));
        return this;
    }

    public final boolean noblock() {
        return (layout.flags.get(p) & O_NONBLOCK) != 0;
    }

    public final StructFuseFileInfo noblock(boolean value) {
        layout.flags.set(p, (layout.flags.get(p) & ~O_NONBLOCK) | (value ? O_NONBLOCK : 0));
        return this;
    }

    public final boolean direct_io() {
        return (layout.flags_bitfield.get(p) & BIT_DIRECT_IO) != 0;
    }

    public final StructFuseFileInfo direct_io(boolean direct_io) {
        if (direct_io)
            layout.flags_bitfield.set(p, layout.flags_bitfield.get(p) | BIT_DIRECT_IO);
        else
            layout.flags_bitfield.set(p, layout.flags_bitfield.get(p) & ~BIT_DIRECT_IO);
        return this;
    }

    public final long fh() {
        return layout.fh.get(p);
    }

    public final StructFuseFileInfo fh(long fh) {
        layout.fh.set(p, fh);
        return this;
    }

    public final long fh_old() {
        return layout.fh_old.get(p);
    }

    public final StructFuseFileInfo fh_old(long fh_old) {
        layout.fh_old.set(p, fh_old);
        return this;
    }

    public final int flags() {
        return layout.flags.intValue(p);
    }

    public final StructFuseFileInfo flags(int flags) {
        layout.flags.set(p, flags);
        return this;
    }

    public final boolean flockrelease() {
        return (layout.flags_bitfield.get(p) & BIT_FLOCKRELEASE) != 0;
    }

    public final StructFuseFileInfo flockrelease(boolean flockrelease) {
        if (flockrelease)
            layout.flags_bitfield.set(p, layout.flags_bitfield.get(p) | BIT_FLOCKRELEASE);
        else
            layout.flags_bitfield.set(p, layout.flags_bitfield.get(p) & ~BIT_FLOCKRELEASE);
        return this;
    }

    public final boolean flush() {
        return (layout.flags_bitfield.get(p) & BIT_FLUSH) != 0;
    }

    public final StructFuseFileInfo flush(boolean flush) {
        if (flush)
            layout.flags_bitfield.set(p, layout.flags_bitfield.get(p) | BIT_FLUSH);
        else
            layout.flags_bitfield.set(p, layout.flags_bitfield.get(p) & ~BIT_FLUSH);

        return this;
    }

    public final boolean keep_cache() {
        return (layout.flags_bitfield.get(p) & BIT_KEEP_CACHE) != 0;
    }

    public final StructFuseFileInfo keep_cache(boolean keep_cache) {
        if (keep_cache)
            layout.flags_bitfield.set(p, layout.flags_bitfield.get(p) | BIT_KEEP_CACHE);
        else
            layout.flags_bitfield.set(p, layout.flags_bitfield.get(p) & ~BIT_KEEP_CACHE);

        return this;
    }

    public final long lock_owner() {
        return layout.lock_owner.longValue(p);
    }

    public final StructFuseFileInfo lock_owner(long lock_owner) {
        layout.lock_owner.set(p, lock_owner);
        return this;
    }

    public final boolean nonseekable() {
        return (layout.flags_bitfield.get(p) & BIT_NONSEEKABLE) != 0;
    }

    public final StructFuseFileInfo nonseekable(boolean nonseekable) {
        if (nonseekable)
            layout.flags_bitfield.set(p, layout.flags_bitfield.get(p) | BIT_NONSEEKABLE);
        else
            layout.flags_bitfield.set(p, layout.flags_bitfield.get(p) & ~BIT_NONSEEKABLE);

        return this;
    }

    public final int openMode() {
        return layout.flags.intValue(p) & openMask;
    }

    public final StructFuseFileInfo openMode(int openMode) {
        layout.flags.set(p, (layout.flags.get(p) & ~openMask) | openMode);
        return this;
    }

    public final boolean truncate() {
        return (layout.flags.get(p) & O_TRUNC) != 0;
    }

    public final StructFuseFileInfo truncate(boolean truncate) {
        layout.flags.set(p, (layout.flags.get(p) & ~O_TRUNC) | (truncate ? O_TRUNC : 0));
        return this;
    }

    public final boolean writepage() {
        return layout.writepage.get(p) != 0;
    }

    public final StructFuseFileInfo writepage(boolean writepage) {
        layout.writepage.set(p, writepage ? 1 : 0);
        return this;
    }

    public static final int openMask = 03;
    public static final int O_RDONLY = 00;
    public static final int O_WRONLY = 01;
    public static final int O_RDWR = 02;
    public static final int O_CREAT = 0100;
    public static final int O_EXCL = 0200;
    public static final int O_NOCTTY = 0400;
    public static final int O_TRUNC = 01000;
    public static final int O_APPEND = 02000;
    public static final int O_NONBLOCK = 04000;
    public static final int O_NDELAY = O_NONBLOCK;
    public static final int O_SYNC = 010000;
    public static final int O_ASYNC = 020000;
    public static final int O_DIRECT = 040000;
    public static final int O_DIRECTORY = 0200000;
    public static final int O_NOFOLLOW = 0400000;
    public static final int O_NOATIME = 01000000;
    public static final int O_CLOEXEC = 02000000;

    private static final int BIT_DIRECT_IO = 1 << 0;
    private static final int BIT_KEEP_CACHE = 1 << 1;
    private static final int BIT_FLUSH = 1 << 2;
    private static final int BIT_NONSEEKABLE = 1 << 3;
    private static final int BIT_FLOCKRELEASE = 1 << 4;
}
