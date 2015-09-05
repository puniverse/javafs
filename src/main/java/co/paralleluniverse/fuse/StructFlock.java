package co.paralleluniverse.fuse;

import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.StructLayout;

public class StructFlock {
    public static final int CMD_GETLK = 5;
    public static final int CMD_SETLK = 6;
    public static final int CMD_SETLKW = 7; // set lock write

    public static final int LOCK_SH = 1;  // Shared lock.
    public static final int LOCK_EX = 2;  // Exclusive lock.
    public static final int LOCK_UN = 8;  // Unlock.

    // lock types
    public static final int F_RDLCK = 0;  // Read lock.
    public static final int F_WRLCK = 1;  // Write lock
    public static final int F_UNLCK = 2;  // Remove lock

    public static final int SEEK_SET = 0; // Offset is calculated from the start of the file.
    public static final int SEEK_CUR = 1; // Offset is calculated from the current position in the file.
    public static final int SEEK_END = 2; // Offset is calculated from the end of the file.

    private static final class Layout extends StructLayout {
        public final int16_t l_type;   // Type of lock: F_RDLCK, F_WRLCK, or F_UNLCK.
        public final int16_t l_whence; // Where `l_start' is relative to (like `lseek') -- SEEK_SET/CUR/END
        public final off_t l_start;    // Offset where the lock begins.
        public final off_t l_len;      // Size of the locked area; zero means until EOF.
        public final pid_t l_pid;      // Process holding the lock
        public final int32_t l_sysid;

        private Layout(Runtime runtime) {
            super(runtime);

            switch (Platform.platform()) {
                case FREEBSD: {
                    this.l_start = new off_t();
                    this.l_len = new off_t();
                    this.l_pid = new pid_t();
                    this.l_type = new int16_t();
                    this.l_whence = new int16_t();
                    this.l_sysid = new int32_t();
                    break;
                }
                default: {
                    this.l_type = new int16_t();
                    this.l_whence = new int16_t();
                    this.l_start = new off_t();
                    this.l_len = new off_t();
                    this.l_pid = new pid_t();
                    this.l_sysid = null;
                }
            }
        }
    }

    private static final Layout layout = new Layout(Runtime.getSystemRuntime());

    private final Pointer p;
    private final String path;

    StructFlock(Pointer p, String path) {
        this.p = p;
        this.path = path;
    }

    public final int type() {
        return (int) layout.l_type.get(p);
    }

    public final StructFlock type(int l_type) {
        layout.l_type.set(p, l_type);
        return this;
    }

    public final long len() {
        return layout.l_len.get(p);
    }

    public final StructFlock len(long l_len) {
        layout.l_len.set(p, l_len);
        return this;
    }

    public final long pid() {
        return layout.l_pid.get(p);
    }

    public final StructFlock pid(long l_pid) {
        layout.l_pid.set(p, l_pid);
        return this;
    }

    public final long start() {
        return layout.l_start.get(p);
    }

    public final StructFlock start(long l_start) {
        layout.l_start.set(p, l_start);
        return this;
    }

    public final long sysid() {
        return layout.l_sysid != null ? layout.l_sysid.get(p) : -1L;
    }

    public final StructFlock sysid(long l_sysid) {
        layout.l_sysid.set(p, l_sysid);
        return this;
    }

    public final long whence() {
        return layout.l_whence.get(p);
    }

    public final StructFlock whence(long l_whence) {
        layout.l_whence.set(p, l_whence);
        return this;
    }

    @Override
    public final java.lang.String toString() {
        if (path != null)
            return path + "\n" + JNRUtil.toString(layout, p);
        return JNRUtil.toString(layout, p);
    }
}
