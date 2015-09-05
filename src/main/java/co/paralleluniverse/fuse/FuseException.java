package co.paralleluniverse.fuse;

import java.io.IOException;

final class FuseException extends IOException {
    private static long serialVersionUID = -3323428017667312747L;

    FuseException(int returnCode) {
        super("FUSE returned error code " + returnCode);
    }
}
