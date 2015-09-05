package co.paralleluniverse.fuse;

import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.StructLayout;

public final class StructTimeBuffer {
    static final class Layout extends StructLayout {
        Layout(Runtime runtime) {
            super(runtime);
        }
        public final StructTimespec.Layout actime = inner(new StructTimespec.Layout(getRuntime()));
        public final StructTimespec.Layout modtime = inner(new StructTimespec.Layout(getRuntime()));
    }
    static final Layout layout = new Layout(Runtime.getSystemRuntime());

    private final Pointer p;

    StructTimeBuffer(Pointer p) {
        this.p = p;
    }

    public long ac_nsec() {
        return layout.actime.tv_nsec.get(p);
    }

    public long ac_sec() {
        return layout.actime.tv_sec.get(p);
    }

    public StructTimeBuffer ac_set(double time) {
        StructTimespec.set(layout.actime, p, time);
        return this;
    }

    public StructTimeBuffer ac_set(long sec, long nsec) {
        StructTimespec.set(layout.actime, p, sec, nsec);
        return this;
    }

    public StructTimeBuffer ac_setMillis(long millis) {
        StructTimespec.setMillis(layout.actime, p, millis);
        return this;
    }

    public StructTimeBuffer ac_setSeconds(long seconds) {
        StructTimespec.setSeconds(layout.actime, p, seconds);
        return this;
    }

    public long mod_nsec() {
        return layout.modtime.tv_nsec.get(p);
    }

    public long mod_sec() {
        return layout.modtime.tv_sec.get(p);
    }

    public final StructTimeBuffer mod_set(double time) {
        StructTimespec.set(layout.modtime, p, time);
        return this;
    }

    public StructTimeBuffer mod_set(long sec, final long nsec) {
        StructTimespec.set(layout.modtime, p, sec, nsec);
        return this;
    }

    public StructTimeBuffer mod_setMillis(long millis) {
        StructTimespec.setMillis(layout.modtime, p, millis);
        return this;
    }

    public StructTimeBuffer mod_setSeconds(long seconds) {
        StructTimespec.setSeconds(layout.modtime, p, seconds);
        return this;
    }

    public StructTimeBuffer both_set(double time) {
        ac_set(time);
        mod_set(time);
        return this;
    }

    public StructTimeBuffer both_set(long sec, long nsec) {
        ac_set(sec, nsec);
        mod_set(sec, nsec);
        return this;
    }

    public StructTimeBuffer both_setMillis(long millis) {
        ac_setMillis(millis);
        mod_setMillis(millis);
        return this;
    }

    public StructTimeBuffer both_setSeconds(long seconds) {
        ac_setSeconds(seconds);
        mod_setSeconds(seconds);
        return this;
    }

    @Override
    public java.lang.String toString() {
        return layout.toString();
    }
}
