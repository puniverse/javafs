package co.paralleluniverse.fuse;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import jnr.ffi.LibraryLoader;

import co.paralleluniverse.fuse.LibFuse.LibFuseProbe;
import co.paralleluniverse.fuse.LibFuse.LibMacFuseProbe;

import jnr.ffi.Platform.CPU;

final class Platform {
    public static enum PlatformEnum {
        LINUX_X86_64, LINUX_I686, LINUX_PPC, MAC, MAC_MACFUSE, FREEBSD, LINUX_ARM
    }

    private static final String[] osxFuseLibraries = {"fuse4x", "osxfuse", "macfuse", "fuse"};
    private static PlatformEnum platform = null;
    private static LibFuse libFuse = null;
    private static final Lock initLock = new ReentrantLock();

    static final LibFuse fuse() {
        if (libFuse == null)
            init();
        return libFuse;
    }

    private static void init() {
        if (libFuse != null)
            return;
        initLock.lock();
        try {
            final jnr.ffi.Platform p = jnr.ffi.Platform.getNativePlatform();
            // Need to recheck
            if (libFuse == null) {
                switch (p.getOS()) {
                    case FREEBSD:
                        platform = PlatformEnum.FREEBSD;
                        libFuse = LibraryLoader.create(LibFuse.class).failImmediately().load("fuse");
                        break;
                    case DARWIN:
                        // First, need to load iconv
                        final LibDl dl = LibraryLoader.create(LibDl.class).failImmediately().load("iconv");
                        dl.dlopen("iconv", LibDl.RTLD_LAZY | LibDl.RTLD_GLOBAL);
                        libFuse = null;
                        LibFuseProbe probe;
                        for (String library : osxFuseLibraries) {
                            try {
                                // Regular FUSE-compatible fuse library
                                platform = PlatformEnum.MAC;
                                libFuse = LibraryLoader.create(LibFuse.class).failImmediately().load(library);
                                break;
                            } catch (Throwable e) {
                                // Carry on
                            }
                            try {
                                probe = LibraryLoader.create(LibMacFuseProbe.class).failImmediately().load(library);
                                ((LibMacFuseProbe) probe).macfuse_version();
                                // MacFUSE-compatible fuse library
                                platform = PlatformEnum.MAC_MACFUSE;
                                libFuse = LibraryLoader.create(LibFuse.class).failImmediately().load("fuse");
                                break;
                            } catch (Throwable e) {
                                // Carry on
                            }
                        }
                        if (libFuse == null) {
                            // Everything failed. Do a last-ditch attempt.
                            // Worst-case scenario, this causes an exception
                            // which will be more meaningful to the user than a NullPointerException on libFuse.
                            libFuse = LibraryLoader.create(LibFuse.class).failImmediately().load("fuse");
                        }
                        break;

                    case LINUX:
                        LibraryLoader<LibFuse> libraryLoader = LibraryLoader.create(LibFuse.class);
                        if (p.getCPU() == CPU.X86_64) {
                            libraryLoader.search("/lib/x86_64-linux-gnu");
                            platform = PlatformEnum.LINUX_X86_64;
                        } else if(p.getCPU() == CPU.I386) {
                            libraryLoader.search("/lib/i386-linux-gnu");
                            platform = PlatformEnum.LINUX_I686;
                        } else if(p.getCPU() == CPU.ARM) {
                            platform = PlatformEnum.LINUX_ARM;
                        } else {
                            platform = PlatformEnum.LINUX_PPC;
                        }

                        libFuse = libraryLoader.failImmediately().load("fuse");
                        break;

                    default:
                        throw new RuntimeException("Unsupported Platform");
                }
            }
        } finally {
            initLock.unlock();
        }
    }

    public static final PlatformEnum platform() {
        if (platform == null)
            init();

        return platform;
    }
}
