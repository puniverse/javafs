package co.paralleluniverse.filesystem;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;

public abstract class FileSystemAdapter extends FileSystem {
    private final FileSystem fs;
    private final FileSystemProvider fsp;

    protected FileSystemAdapter(FileSystem fs, FileSystemProvider fsp) {
        this.fs = fs;
        this.fsp = fsp;
    }

//    protected FileSystemAdapter(FileSystem fs) {
//        this.fs = fs;
//        this.fsp = wrapFileSystemProvider(fs.provider());
//    }
//    protected FileSystemProvider wrapFileSystemProvider(FileSystemProvider fsp) {
//        return fsp;
//    }
    @Override
    public String toString() {
        return fs.toString();
    }

    @Override
    public FileSystemProvider provider() {
        return fsp;
    }

    @Override
    public void close() throws IOException {
        fs.close();
    }

    @Override
    public boolean isOpen() {
        return fs.isOpen();
    }

    @Override
    public boolean isReadOnly() {
        return fs.isReadOnly();
    }

    @Override
    public String getSeparator() {
        return fs.getSeparator();
    }

    @Override
    public Iterable<Path> getRootDirectories() {
        return fs.getRootDirectories();
    }

    @Override
    public Iterable<FileStore> getFileStores() {
        return fs.getFileStores();
    }

    @Override
    public Set<String> supportedFileAttributeViews() {
        return fs.supportedFileAttributeViews();
    }

    @Override
    public Path getPath(String first, String... more) {
        return fs.getPath(first, more);
    }

    @Override
    public PathMatcher getPathMatcher(String syntaxAndPattern) {
        return fs.getPathMatcher(syntaxAndPattern);
    }

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        return fs.getUserPrincipalLookupService();
    }

    @Override
    public WatchService newWatchService() throws IOException {
        return fs.newWatchService();
    }
}
