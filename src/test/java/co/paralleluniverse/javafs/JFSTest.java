/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.paralleluniverse.javafs;

import co.paralleluniverse.javafs.JavaFS;
import com.google.common.jimfs.Jimfs;
import static com.google.common.truth.Truth.assert_;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pron
 */
public class JFSTest {

    public JFSTest() {
    }

    @Test
    public void test() throws Exception {
        FileSystem fs = Jimfs.newFileSystem();
        try (DataOutputStream os = new DataOutputStream(Files.newOutputStream(fs.getPath("/jimfs.txt")))) {
            os.writeUTF("JIMFS");
        }

        final Path mnt = Files.createTempDirectory("jfsmnt");
        try {
            JavaFS.mount(fs, mnt, false, false);

            // From this point on we use the old file IO
            File root = mnt.toFile();

            // verify that we are, in fact, in Jimfs
            try (DataInputStream is = new DataInputStream(new FileInputStream(new File(root, "jimfs.txt")))) {
                assertEquals("JIMFS", is.readUTF());
            }

            try (DataOutputStream os = new DataOutputStream(new FileOutputStream(new File(root, "a.txt")))) {
                os.writeUTF("hello!");
            }
            try (DataOutputStream os = new DataOutputStream(new FileOutputStream(new File(root, "b.txt")))) {
                os.writeUTF("wha?");
            }
            try (DataOutputStream os = new DataOutputStream(new FileOutputStream(new File(root, "c.txt")))) {
                os.writeUTF("goodbye!");
            }

            assert_().that(root.list()).asList().has().allOf("a.txt", "b.txt", "c.txt", "jimfs.txt");

            try (DataInputStream is = new DataInputStream(new FileInputStream(new File(root, "a.txt")))) {
                assertEquals("hello!", is.readUTF());
            }
            try (DataInputStream is = new DataInputStream(new FileInputStream(new File(root, "b.txt")))) {
                assertEquals("wha?", is.readUTF());
            }
            try (DataInputStream is = new DataInputStream(new FileInputStream(new File(root, "c.txt")))) {
                assertEquals("goodbye!", is.readUTF());
            }
        } finally {
            JavaFS.unmount(mnt);
            Files.delete(mnt);
        }

    }
}
