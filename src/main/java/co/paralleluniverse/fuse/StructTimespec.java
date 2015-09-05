package co.paralleluniverse.fuse;

import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;
import jnr.ffi.StructLayout;

final class StructTimespec {
    static final class Layout extends StructLayout {
        Layout(Runtime runtime) {
            super(runtime);
        }
        public final SignedLong tv_sec = new SignedLong();
        public final SignedLong tv_nsec = new SignedLong();
    }
    static final Layout layout = new Layout(Runtime.getSystemRuntime());

    private final Pointer p;

    public StructTimespec(Pointer p) {
        this.p = p;
    }

    public long nsec() {
        return layout.tv_nsec.longValue(p);
    }

    public long sec() {
        return layout.tv_sec.longValue(p);
    }

    public void set(double time) {
        set(layout, p, time);
    }

    public void set(long sec, long nsec) {
        set(layout, p, sec, nsec);
    }

    public void setMillis(long millis) {
        set(millis / 1000L, (millis % 1000L) * 1000000L);
    }

    public void setSeconds(long seconds) {
        set(seconds);
    }

    static void set(StructTimespec.Layout layout, Pointer p, double time) {
        set(layout, p, (long) time, (long) (time * 1000000000d));
    }

    static void set(StructTimespec.Layout layout, Pointer p, long sec, long nsec) {
        layout.tv_sec.set(p, sec);
        layout.tv_nsec.set(p, nsec);
    }

    static void setMillis(StructTimespec.Layout layout, Pointer p, long millis) {
        set(layout, p, millis / 1000L, (millis % 1000L) * 1000000L);
    }

    static void setSeconds(StructTimespec.Layout layout, Pointer p, long seconds) {
        set(layout, p, seconds);
    }
}
