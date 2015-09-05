package co.paralleluniverse.fuse;

import jnr.ffi.*;
import jnr.ffi.Runtime;

/**
 * Data buffer vector
 * <p>
 * An array of data buffers, each containing a memory pointer or a
 * file descriptor.
 * <p>
 * Allocate dynamically to add more than one buffer.
 *
 * @author Sergey Tselovalnikov
 * @since 02.06.15
 */
public class StructFuseBufvec {
    private static final class Layout extends StructLayout {
        public final size_t count = new size_t(); // Number of buffers in the array
        public final size_t idx = new size_t(); // Index of current buffer within the array
        public final size_t off = new size_t(); // Current offset within the current buffer
        public final StructFuseBuf.Layout buf = inner(StructFuseBuf.layout); // Array of buffers
        
        private Layout(Runtime runtime) {
            super(runtime);
        }
    }

    static final Layout layout = new Layout(Runtime.getSystemRuntime());

    private final Pointer p;

    public StructFuseBufvec(Pointer p) {
        this.p = p;
    }
    
    public final long cout() {
        return layout.count.get(p);
    }
    
    public final long idx() {
        return layout.idx.get(p);
    }
    
    public final long off() {
        return layout.off.get(p);
    }
    
    public final StructFuseBuf buf() {
        return new StructFuseBuf(p.getPointer(layout.buf.offset()));
    }

    /**
     * Similar to FUSE_BUFVEC_INIT macros
     */
    public static void init(StructFuseBufvec buf, long size) {
        layout.count.set(buf.p, 1);
        layout.idx.set(buf.p, 0);
        layout.off.set(buf.p, 0);        
        layout.buf.size.set(buf.p, size);
        layout.buf.flags.set(buf.p, 0);
        layout.buf.mem.set(buf.p, 0);
        layout.buf.fd.set(buf.p, -1);
        layout.buf.pos.set(buf.p, 0);
    }
}
