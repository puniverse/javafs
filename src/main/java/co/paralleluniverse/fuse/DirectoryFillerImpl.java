package co.paralleluniverse.fuse;

import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import jnr.ffi.Pointer;
import jnr.ffi.annotations.Delegate;
import jnr.ffi.types.off_t;

/**
 * A class which provides functionality to pass filenames back to FUSE as part of a readdir() call.
 */
final class DirectoryFillerImpl implements DirectoryFiller {
    private static final String currentDirectory = ".";
    private static final String parentDirectory = "..";
    private final Pointer buf;
    private final fuse_fill_dir_t nativeFunction;
    private final Set<String> addedFiles = new HashSet<String>();

    DirectoryFillerImpl(Pointer buf, fuse_fill_dir_t nativeFunction) {
        this.buf = buf;
        this.nativeFunction = nativeFunction;

        add(Arrays.asList(currentDirectory, parentDirectory));
    }

    public static interface fuse_fill_dir_t {
        @Delegate
        int invoke(Pointer buf, ByteBuffer name, Pointer stat, @off_t long off);
    }

    @Override
    public final boolean add(Iterable<String> files) {
        int result;
        for (String file : files) {
            if (file == null)
                continue;

            file = Paths.get(file).getFileName().toString(); // Keep only the name component

            if (addedFiles.add(file)) {
                result = nativeFunction.invoke(buf, ByteBuffer.wrap(file.getBytes()), null, 0);
                if (result != 0)
                    return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder output = new StringBuilder();
        int count = 0;
        for (final String file : addedFiles) {
            output.append(file);
            if (count < addedFiles.size() - 1)
                output.append(", ");
            count++;
        }
        return output.toString();
    }
}
