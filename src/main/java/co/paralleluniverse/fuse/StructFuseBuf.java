package co.paralleluniverse.fuse;

import jnr.ffi.Pointer;
import jnr.ffi.StructLayout;
import jnr.ffi.Runtime;

/**
 * Single data buffer
 * <p>
 * Generic data buffer for I/O, extended attributes, etc... Data may
 * be supplied as a memory pointer or as a file descriptor
 */
class StructFuseBuf {
    static final class Layout extends StructLayout {
        /**
         * Size of data in bytes
         */
        public final size_t size = new size_t();

        /**
         * Buffer flags
         */
        public final Enum<FuseBufFlags> flags = new Enum<>(FuseBufFlags.class);

        /**
         * Memory pointer
         * <p>
         * Used unless FUSE_BUF_IS_FD flag is set.
         */
        public final Pointer mem = new Pointer();

        /**
         * File descriptor
         * <p>
         * Used if FUSE_BUF_IS_FD flag is set.
         */
        public final Signed32 fd = new Signed32();

        /**
         * File position
         * <p>
         * Used if FUSE_BUF_FD_SEEK flag is set.
         */
        public final off_t pos = new off_t();

        private Layout(Runtime runtime) {
            super(runtime);
        }
    }
    static final Layout layout = new Layout(Runtime.getSystemRuntime());

    private final Pointer p;

    public StructFuseBuf(Pointer p) {
        this.p = p;
    }

    public final long size() {
        return layout.size.get(p);
    }
    
    public final FuseBufFlags flags() {
        return layout.flags.get(p);
    }
    
    public final int fd() {
        return layout.fd.get(p);
    }
    
    public final long pos() {
        return layout.pos.get(p);
    }
}
