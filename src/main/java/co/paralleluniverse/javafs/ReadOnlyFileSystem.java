package co.paralleluniverse.javafs;

import co.paralleluniverse.filesystem.FileSystemAdapter;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;

/**
 *
 * @author pron
 */
class ReadOnlyFileSystem extends FileSystemAdapter {
    public ReadOnlyFileSystem(FileSystem fs) {
        super(fs, new ReadOnlyFileSystemProvider(fs.provider()));
    }

    ReadOnlyFileSystem(FileSystem fs, FileSystemProvider fsp) {
        super(fs, fsp);
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }
}
