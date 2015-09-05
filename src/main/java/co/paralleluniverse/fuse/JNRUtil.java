package co.paralleluniverse.fuse;

import com.kenai.jffi.MemoryIO;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import jnr.ffi.Pointer;
import jnr.ffi.StructLayout;

final class JNRUtil {
    public static ByteBuffer toByteBuffer(Pointer pointer, long capacity) {
        if (capacity > Integer.MAX_VALUE)
            throw new IllegalArgumentException("Capacity too big: " + capacity);
        return MemoryIO.getInstance().newDirectByteBuffer(pointer.address(), (int) capacity);
    }

    public static String toString(StructLayout layout, Pointer p) {
        StringBuilder sb = new StringBuilder();
        Field[] fields = layout.getClass().getDeclaredFields();
        sb.append(layout.getClass().getSimpleName()).append(" { \n");
        final String fieldPrefix = "    ";
        for (Field field : fields) {
            try {
                sb.append(fieldPrefix)
                        .append(field.getType().getSimpleName()).append(' ').append(field.getName()).append(": ")
                        .append(field.getType().getMethod("toString", Pointer.class).invoke(p))
                        .append('\n');
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
        }
        sb.append("}\n");

        return sb.toString();
    }

    private JNRUtil() {
    }
}
