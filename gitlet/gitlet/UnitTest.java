package gitlet;

import ucb.junit.textui;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/** The suite of all JUnit tests for the gitlet package.
 *  @author Alex Liang
 */
public class UnitTest {

    /** Run the JUnit tests in the loa package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(UnitTest.class));
    }

    private static final String CWD = System.getProperty("user.dir") + "/";

    /** Path to the `.gitlet` folder */
    private static final String GITLET_PATH = CWD + ".gitlet/";

    /** Path to the Gitlet object in `.gitlet` folder */
    private static final String GITLET_OBJ_PATH = CWD + ".gitlet/gitlet";

    /** Path to the folder that stores all the commits*/
    private static final String COMMITS_PATH = CWD + ".gitlet/commits/";

    /** Path to the folder that stores all staged files. */
    private static final String STAGE_PATH = CWD + ".gitlet/stage/";

    /** Path to the folder that stores all the blobs with ID as filename. */
    private static final String BLOBS_PATH = CWD + ".gitlet/blobs/";

    /** Path to the folder that stores all the test files. */
    private static final String TEST_FILE_PATH = "../testing/test_files";

    private static final String INITIAL_COMMIT = "5b6d34fd772b5e3b663c90"
            + "db766a54d42f9583a7";

    private static final String TEST_COMMIT = "b28528bf639a6474f5f859b576"
            + "d42935badeb88c";

    /** A dummy test to avoid complaint. */
    @Test
    public void placeholderTest() {
    }

    /** Test the initialization of Gitlet. */
    @Test
    public void mainInitTest() throws IOException {
        clean();

        Gitlet gitlet = Main.initGitlet();

        File gl = new File(GITLET_PATH);
        File stage = new File(STAGE_PATH);
        File commits = new File(COMMITS_PATH);

        assertTrue(gl.exists());
        assertTrue(stage.exists());
        assertTrue(commits.exists());

        Commit first = gitlet.loadCommit(INITIAL_COMMIT);
        assertEquals("initial commit", first.getMessage());
        assertEquals(INITIAL_COMMIT, first.getID());
        assertEquals("master", first.getBranch());
    }

    /** Test the `add` command of Gitlet. */
    @Test
    public void addTest() throws IOException {
        clean();

        Gitlet gitlet = Main.initGitlet();

        File gl = new File(GITLET_OBJ_PATH);
        File test = new File("test.txt");
        test.createNewFile();
        Utils.writeContents(test, "This is a test.");

        gitlet.add("test.txt");

        gitlet = Utils.readObject(gl, Gitlet.class);
        Stage stage = gitlet.getStageArea();
        Blob b = stage.getBlob("test.txt");
        assertEquals("This is a test.", b.getContent());

        test.delete();
    }

    /** Test the `commit` command of Gitlet. */
    @Test
    public void commitTest() throws IOException {
        clean();

        Gitlet gitlet = Main.initGitlet();

        Stage stage = gitlet.getStageArea();
        File testFiles = new File(TEST_FILE_PATH);
        File[] allTestFiles = testFiles.listFiles();
        ArrayList<String> fileNames = new ArrayList<String>();

        for (File f : allTestFiles) {
            String filename = f.getPath();
            fileNames.add(filename);
            gitlet.add(filename);
        }

        gitlet.commit("Testing `commit` command");
        File gl = new File(GITLET_OBJ_PATH);
        gitlet = Utils.readObject(gl, Gitlet.class);
        assertEquals(TEST_COMMIT, gitlet.getHead().getID());

        for (String filename : fileNames) {
            Blob currBlob = new Blob(filename);
            File currFile = new File(BLOBS_PATH + currBlob.getID());
            assertTrue(currFile.exists());
        }

        File commitFile = new File(COMMITS_PATH + TEST_COMMIT);
        Commit newCommit = Utils.readObject(commitFile, Commit.class);

        System.out.println("The commit time is:" + newCommit.getTimeAsString());
        assertEquals("Testing `commit` command", newCommit.getMessage());
        assertEquals(TEST_COMMIT, newCommit.getID());
        assertEquals(INITIAL_COMMIT, newCommit.getParent());
        assertEquals("master", newCommit.getBranch());

        assertTrue(stage.getStageAdd().isEmpty());
        assertTrue(stage.getStageRemoval().isEmpty());

        gitlet.globalLog();
    }

    /** Util method to clean up all the folders and files created via `init`*/
    public void clean() {
        File gl = new File(GITLET_PATH);
        deleteFolder(gl);
    }

    boolean deleteFolder(File folder) {
        File[] allContents = folder.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteFolder(file);
            }
        }
        return folder.delete();
    }
}


