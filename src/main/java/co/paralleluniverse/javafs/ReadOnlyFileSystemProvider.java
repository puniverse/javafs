package co.paralleluniverse.javafs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessDeniedException;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.ReadOnlyFileSystemException;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author pron
 */
class ReadOnlyFileSystemProvider extends FileSystemProvider {
    private final FileSystemProvider fsp;

    public ReadOnlyFileSystemProvider(FileSystemProvider fsp) {
        this.fsp = fsp;
    }

    @Override
    public FileSystem newFileSystem(Path path, Map<String, ?> env) throws IOException {
        return new ReadOnlyFileSystem(fsp.newFileSystem(path, env));
    }

    @Override
    public OutputStream newOutputStream(Path path, OpenOption... options) throws IOException {
        throw new ReadOnlyFileSystemException();
    }

    @Override
    public FileChannel newFileChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        checkOpenOptions(options);
        return fsp.newFileChannel(path, options, attrs);
    }

    @Override
    public AsynchronousFileChannel newAsynchronousFileChannel(Path path, Set<? extends OpenOption> options, ExecutorService executor, FileAttribute<?>... attrs) throws IOException {
        checkOpenOptions(options);
        return fsp.newAsynchronousFileChannel(path, options, executor, attrs);
    }

    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        checkOpenOptions(options);
        return fsp.newByteChannel(path, options, attrs);
    }

    @Override
    public void checkAccess(Path path, AccessMode... modes) throws IOException {
        for(AccessMode mode : modes) {
            if(mode == AccessMode.WRITE)
                throw new AccessDeniedException(path.toString());
        }
        fsp.checkAccess(path, modes);
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

    @Override
    public String toString() {
        return fsp.toString();
    }

    @Override
    public String getScheme() {
        return fsp.getScheme();
    }

    @Override
    public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
        return fsp.newFileSystem(uri, env);
    }

    @Override
    public FileSystem getFileSystem(URI uri) {
        return fsp.getFileSystem(uri);
    }

    @Override
    public Path getPath(URI uri) {
        return fsp.getPath(uri);
    }

    @Override
    public InputStream newInputStream(Path path, OpenOption... options) throws IOException {
        return fsp.newInputStream(path, options);
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException {
        return fsp.newDirectoryStream(dir, filter);
    }

    @Override
    public Path readSymbolicLink(Path link) throws IOException {
        return fsp.readSymbolicLink(link);
    }

    @Override
    public boolean isSameFile(Path path, Path path2) throws IOException {
        return fsp.isSameFile(path, path2);
    }

    @Override
    public boolean isHidden(Path path) throws IOException {
        return fsp.isHidden(path);
    }

    @Override
    public FileStore getFileStore(Path path) throws IOException {
        return fsp.getFileStore(path);
    }

    @Override
    public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
        return fsp.getFileAttributeView(path, type, options);
    }

    @Override
    public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
        return fsp.readAttributes(path, type, options);
    }

    @Override
    public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
        return fsp.readAttributes(path, attributes, options);
    }
}
