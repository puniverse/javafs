package co.paralleluniverse.fuse;

public interface LibDl {
    public static final int RTLD_LAZY = 0x1;
    public static final int RTLD_NOW = 0x2;
    public static final int RTLD_GLOBAL = 0x100;

    public void dlopen(String file, int mode);
}
