package co.paralleluniverse.fuse;

import jnr.ffi.Pointer;
import jnr.ffi.StructLayout;
import jnr.ffi.Runtime;

/**
 * @see "fuse_lowlevel.c"
 *
 * <pre>
 * struct fuse_pollhandle {
 *   uint64_t kh;
 *   struct fuse_chan *ch;
 *   struct fuse_ll *f;
 * };
 * </pre>
 *
 * @author Sergey Tselovalnikov
 * @since 02.06.15
 */
public class StructFusePollHandle {
    private static final class Layout extends StructLayout {
        public final Unsigned64 kh = new Unsigned64();
        // TODO struct fuse_chan *ch;
        public final Pointer ch = new Pointer();
        // TODO struct fuse_ll *f;
        public final Pointer f = new Pointer();

        protected Layout(Runtime runtime) {
            super(runtime);
        }
    }
    
    private static final Layout layout = new Layout(Runtime.getSystemRuntime());
    private final Pointer p;

    public StructFusePollHandle(Pointer p) {
        this.p = p;
    }
    
    public long kh() {
        return layout.kh.get(p);
    }
}
