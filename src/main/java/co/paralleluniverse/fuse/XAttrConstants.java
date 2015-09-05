package co.paralleluniverse.fuse;

/**
 * The following constants should be used for the fifth parameter of
 * `*setxattr'.
 *
 * @author Sergey Tselovalnikov
 * @since 05.06.15
 */
final class XAttrConstants {
    public static final int XATTR_CREATE = 1;	/* set value, fail if attr already exists.  */
    public static final int XATTR_REPLACE = 2;	/* set value, fail if attr does not exist.  */

    private XAttrConstants() {
    }
}
