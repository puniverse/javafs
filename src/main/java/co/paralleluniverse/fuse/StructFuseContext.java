package co.paralleluniverse.fuse;

import jnr.ffi.Runtime;
import jnr.ffi.Struct;

abstract class StructFuseContext extends Struct {
    public final Pointer fuse = new Pointer();
    public final uid_t uid = new uid_t();
    public final gid_t gid = new gid_t();
    public final pid_t pid = new pid_t();
    public final Pointer private_data = new Pointer();

    public StructFuseContext(Runtime runtime) {
        super(runtime);
    }
}
