package co.paralleluniverse.fuse;

import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.StructLayout;
import jnr.ffi.TypeAlias;

class StructFuseConnInfo {
    static final class Layout extends StructLayout {
        Layout(Runtime runtime) {
            super(runtime);
        }
        public final int32_t proto_major = new int32_t();
        public final int32_t proto_minor = new int32_t();
        public final int32_t async_read = new int32_t();
        public final int32_t max_write = new int32_t();
        public final int32_t max_readahead = new int32_t();
        public final int32_t enable = new int32_t();
        public final int32_t want = new int32_t();
        private final Padding reserved = new Padding(getRuntime().findType(TypeAlias.int32_t), 25);
    }

    static final Layout layout = new Layout(Runtime.getSystemRuntime());
    private final Pointer p;

    public StructFuseConnInfo(Pointer p) {
        this.p = p;
    }

    public final void setOptions(final boolean setVolumeName, final boolean caseInsensitive) {
        layout.want.set(p, (setVolumeName ? 0x2 : 0x0) | (caseInsensitive ? 0x1 : 0x0));
    }
}
