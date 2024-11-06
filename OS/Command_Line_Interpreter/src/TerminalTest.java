import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

public class TerminalTest {

    private Terminal terminal;

    @BeforeEach
    public void setup() {
        terminal = new Terminal();  // initialize the Terminal object before each test
    }

    @Test
    public void testPwd() {
        String result = terminal.pwd();
        assertEquals(System.getProperty("user.dir"), result, "PWD should return the current working directory.");
    }

    @Test
    public void testCdValidDirectory() {
        String newDirectory = "src";
        terminal.cd(new String[]{newDirectory});
        assertEquals(new File(System.getProperty("user.dir"), newDirectory).getAbsolutePath(), terminal.pwd(), "CD should change the current directory.");
    }

    @Test
    public void testCdInvalidDirectory() {
        terminal.cd(new String[]{"invalidDirectory"});
        assertNotEquals(new File(System.getProperty("user.dir"), "invalidDirectory").getAbsolutePath(), terminal.pwd(), "CD should fail for non-existent directory.");
    }

    @Test
    public void testLs() {
        String[] args = {};
        String result = terminal.ls(args);
        assertNotNull(result, "LS should list contents of the current directory.");
    }

    @Test
    public void testMkdir() {
        String dirName = "testDir";
        terminal.mkdir(new String[]{dirName});
        File dir = new File(System.getProperty("user.dir"), dirName);
        assertTrue(dir.exists(), "MKDIR should create the directory.");
        dir.delete();  // clean up after the test
    }

    @Test
    public void testRmdir() {
        String dirName = "testDir";
        terminal.mkdir(new String[]{dirName});  // first create the directory
        terminal.rmdir(new String[]{dirName});
        File dir = new File(System.getProperty("user.dir"), dirName);
        assertFalse(dir.exists(), "RMDIR should remove the empty directory.");
    }

    @Test
    public void testTouch() {
        String fileName = "testFile.txt";
        terminal.touch(new String[]{fileName});
        File file = new File(System.getProperty("user.dir"), fileName);
        assertTrue(file.exists(), "TOUCH should create the file.");
        file.delete();  // clean up after the test
    }

    @Test
    public void testRm() throws IOException {
        String fileName = "testFileToDelete.txt";
        terminal.touch(new String[]{fileName});
        terminal.rm(fileName);
        File file = new File(System.getProperty("user.dir"), fileName);
        assertFalse(file.exists(), "RM should delete the file.");
    }

    @Test
    public void testMv() {
        String sourceFile = "source.txt";
        String destFile = "destination.txt";
        terminal.touch(new String[]{sourceFile});
        terminal.mv(new String[]{sourceFile, destFile});
        File source = new File(System.getProperty("user.dir"), sourceFile);
        File destination = new File(System.getProperty("user.dir"), destFile);
        assertFalse(source.exists(), "MV should delete the source file.");
        assertTrue(destination.exists(), "MV should create the destination file.");
        destination.delete();  // clean up after the test
    }

    @Test
    public void testCat() {
        String fileName = "testFile.txt";
        terminal.touch(new String[]{fileName});
        terminal.cat(new String[]{fileName});
        File file = new File(System.getProperty("user.dir"), fileName);
        assertTrue(file.exists(), "CAT should print the contents of the file.");
        file.delete();  // clean up after the test
    }

    @Test
    public void testHelp() {
        terminal.help();
    }
}
