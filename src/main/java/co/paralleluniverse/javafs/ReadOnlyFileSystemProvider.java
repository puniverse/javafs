package co.paralleluniverse.javafs;

import co.paralleluniverse.filesystem.FileSystemProviderAdapter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessDeniedException;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.ReadOnlyFileSystemException;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author pron
 */
class ReadOnlyFileSystemProvider extends FileSystemProviderAdapter {
    public ReadOnlyFileSystemProvider(FileSystemProvider fsp) {
        super(fsp);
    }

    @Override
    protected FileSystem wrapFileSystem(FileSystem fs) {
        return new ReadOnlyFileSystem(fs, this);
    }

    @Override
    public OutputStream newOutputStream(Path path, OpenOption... options) throws IOException {
        throw new ReadOnlyFileSystemException();
    }

    @Override
    public FileChannel newFileChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        checkOpenOptions(options);
        return super.newFileChannel(path, options, attrs);
    }

    @Override
    public AsynchronousFileChannel newAsynchronousFileChannel(Path path, Set<? extends OpenOption> options, ExecutorService executor, FileAttribute<?>... attrs) throws IOException {
        checkOpenOptions(options);
        return super.newAsynchronousFileChannel(path, options, executor, attrs);
    }

    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        checkOpenOptions(options);
        return super.newByteChannel(path, options, attrs);
    }

    @Override
    public void checkAccess(Path path, AccessMode... modes) throws IOException {
        for (AccessMode mode : modes) {
            if (mode == AccessMode.WRITE)
                throw new AccessDeniedException(path.toString());
        }
        super.checkAccess(path, modes);
    }

    private void checkOpenOptions(Set<? extends OpenOption> options) {
        if (options.contains(StandardOpenOption.CREATE)
                || options.contains(StandardOpenOption.CREATE_NEW)
                || options.contains(StandardOpenOption.APPEND)
                || options.contains(StandardOpenOption.WRITE)
                || options.contains(StandardOpenOption.DELETE_ON_CLOSE))
            throw new ReadOnlyFileSystemException();
    }

    @Override
    public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
        throw new ReadOnlyFileSystemException();
    }

    @Override
    public void createSymbolicLink(Path link, Path target, FileAttribute<?>... attrs) throws IOException {
        throw new ReadOnlyFileSystemException();
    }

    @Override
    public void createLink(Path link, Path existing) throws IOException {
        throw new ReadOnlyFileSystemException();
    }

    @Override
    public void delete(Path path) throws IOException {
        throw new ReadOnlyFileSystemException();
    }

    @Override
    public boolean deleteIfExists(Path path) throws IOException {
        throw new ReadOnlyFileSystemException();
    }

    @Override
    public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
        throw new ReadOnlyFileSystemException();
    }

    @Override
    public void copy(Path source, Path target, CopyOption... options) throws IOException {
        throw new ReadOnlyFileSystemException();
    }

    @Override
    public void move(Path source, Path target, CopyOption... options) throws IOException {
        throw new ReadOnlyFileSystemException();
    }
}
