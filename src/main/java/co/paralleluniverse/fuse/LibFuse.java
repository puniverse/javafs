package co.paralleluniverse.fuse;

import jnr.ffi.Pointer;
import jnr.ffi.types.size_t;
import jnr.ffi.types.ssize_t;

public interface LibFuse {
    static interface LibFuseProbe {
    }

    static interface LibMacFuseProbe extends LibFuseProbe {
        String macfuse_version();
    }

    StructFuseContext fuse_get_context();

    /**
     * Main function of FUSE.
     * <p>
     * This function does the following:
     * - parses command line options (-d -s and -h)
     * - passes relevant mount options to the fuse_mount()
     * - installs signal handlers for INT, HUP, TERM and PIPE
     * - registers an exit handler to unmount the filesystem on program exit
     * - creates a fuse handle
     * - registers the operations
     * - calls either the single-threaded or the multi-threaded event loop
     *
     * @param argc      the argument counter passed to the main() function
     * @param argv      the argument vector passed to the main() function
     * @param op        the file system operation
     * @param user_data user data supplied in the context during the init() method
     * @return 0 on success, nonzero on failure
     */
    int fuse_main_real(int argc, String[] argv, StructFuseOperations op, @size_t int size, Pointer user_data);

    @size_t long fuse_buf_size(Pointer bufv); // StructFuseBufvec

    @ssize_t long fuse_buf_copy(Pointer dstv, Pointer srcv, int flags); // StructFuseBufvec

    void fuse_pollhandle_destroy(Pointer ph); // StructFusePollhandle

    int fuse_notify_poll(Pointer ph); // StructFusePollhandle
}
