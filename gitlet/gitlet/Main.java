package gitlet;

import java.io.File;
import java.io.IOException;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Alex Liang
 */
public class Main {
    /** CWD. */
    private static final String CWD = System.getProperty("user.dir") + "/";

    /** Path to the `.gitlet` folder. */
    private static final String GITLET_PATH = CWD + ".gitlet/";

    /** Path to the Gitlet object stored `.gitlet` folder. */
    private static final String GITLET_OBJ_PATH = CWD + ".gitlet/gitlet";

    /** Path to the folder that stores all the commits. */
    private static final String COMMITS_PATH = CWD + ".gitlet/commits/";

    /** Path to the folder that stores all staged files. */
    private static final String STAGE_PATH = CWD + ".gitlet/stage/";

    /** Path to the folder that stores all the blobs with ID as filename. */
    private static final String BLOBS_PATH = CWD + ".gitlet/blobs/";

    /** Number of arguments. */
    private static final int CHECKOUT_FILE = 3;

    /** Number of arguments. */
    private static final int CHECKOUT_BRANCH = 2;

    /** Number of arguments. */
    private static final int CHECKOUT_COMMIT_FILE = 4;

    /** Usage: java gitlet.Main ARGS, where ARGS contains.
     *
     *  init
     *
     *  add [file name]
     *
     *  commit [message]
     *
     *  log
     *
     *  checkout -- [file name]
     *
     *  checkout [commit id] -- [file name]
     *
     *  checkout [branch name]
     *  */
    public static void main(String... args) throws IOException {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String cmd = args[0];
        if (!cmd.equals("init")) {
            File gitletFile = new File(GITLET_OBJ_PATH);
            if (!gitletFile.exists()) {
                System.out.println("Not in an initialized Gitlet directory.");
                System.exit(0);
            }
        }
        switch (cmd) {
        case "init":
            initGitlet();
            break;
        case "add":
            runAdd(args);
            break;
        case "commit":
            runCommit(args);
            break;
        case "rm":
            runRemove(args);
            break;
        case "checkout":
            runCheckout(args);
            break;
        case "log":
            runLog();
            break;
        case "global-log":
            runGlobalLog();
            break;
        case "find":
            runFind(args);
            break;
        case "status":
            runStatus();
            break;
        case "branch":
            runBranch(args);
            break;
        case "rm-branch":
            runRemoveBranch(args);
            break;
        case "reset":
            runReset(args);
            break;
        case "merge":
            runMerge(args);
            break;
        default:
            System.out.println("No command with that name exists.");
            System.exit(0);
        }
        return;
    }

    /** Initialize Gitlet in the current directory. This method returns
     * a Gitlet object for testing.
     * */
    public static Gitlet initGitlet() throws IOException {
        File gitletFile = new File(GITLET_OBJ_PATH);
        if (gitletFile.exists()) {
            System.out.println("A Gitlet version-control system "
                    + "already exists in the current directory");
            System.exit(0);
        }

        File gitlet = new File(GITLET_PATH);
        gitlet.mkdir();
        File stage = new File(STAGE_PATH);
        stage.mkdir();
        File commits = new File(COMMITS_PATH);
        commits.mkdir();
        File blobs = new File(BLOBS_PATH);
        blobs.mkdir();

        Gitlet gl = new Gitlet();
        gl.initialize();

        return gl;
    }

    /** Run `add` command.
     * @param args
     * */
    public static void runAdd(String[] args) {
        File gitletFile = new File(GITLET_OBJ_PATH);
        Gitlet gitlet = Utils.readObject(gitletFile, Gitlet.class);
        gitlet.add(args[1]);
    }

    /** Run `commit` command.
     * @param args
     * */
    public static void runCommit(String[] args)
            throws IOException {
        File gitletFile = new File(GITLET_OBJ_PATH);
        Gitlet gitlet = Utils.readObject(gitletFile, Gitlet.class);
        gitlet.commit(args[1]);
    }

    /** Run `commit` command.
     * @param args
     * */
    public static void runCheckout(String[] args)
            throws IOException {
        File gitletFile = new File(GITLET_OBJ_PATH);
        Gitlet gitlet = Utils.readObject(gitletFile, Gitlet.class);

        switch (args.length) {
        case CHECKOUT_FILE:
            gitlet.checkout(gitlet.getHead().getID(), args[2]);
            break;
        case CHECKOUT_BRANCH:
            gitlet.checkoutBranch(args[1]);
            break;
        case CHECKOUT_COMMIT_FILE:
            if (!args[2].equals("--")) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            gitlet.checkout(args[1], args[3]);
            break;

        default:
        }
    }

    /** Run `log` command.
     * */
    public static void runLog() throws IOException {
        File gitletFile = new File(GITLET_OBJ_PATH);
        Gitlet gitlet = Utils.readObject(gitletFile, Gitlet.class);
        gitlet.log();
    }

    public static void runGlobalLog() {
        File gitletFile = new File(GITLET_OBJ_PATH);
        Gitlet gitlet = Utils.readObject(gitletFile, Gitlet.class);
        gitlet.globalLog();
    }

    public static void runRemove(String[] args) {
        File gitletFile = new File(GITLET_OBJ_PATH);
        Gitlet gitlet = Utils.readObject(gitletFile, Gitlet.class);
        gitlet.remove(args[1]);
    }

    public static void runFind(String[] args) {
        File gitletFile = new File(GITLET_OBJ_PATH);
        Gitlet gitlet = Utils.readObject(gitletFile, Gitlet.class);
        gitlet.find(args[1]);
    }

    public static void runStatus() {
        File gitletFile = new File(GITLET_OBJ_PATH);
        Gitlet gitlet = Utils.readObject(gitletFile, Gitlet.class);
        gitlet.status();
    }

    public static void runBranch(String[] args) {
        File gitletFile = new File(GITLET_OBJ_PATH);
        Gitlet gitlet = Utils.readObject(gitletFile, Gitlet.class);
        gitlet.branch(args[1]);
    }

    public static void runRemoveBranch(String[] args) {
        File gitletFile = new File(GITLET_OBJ_PATH);
        Gitlet gitlet = Utils.readObject(gitletFile, Gitlet.class);
        gitlet.removeBranch(args[1]);
    }

    public static void runReset(String[] args)
            throws IOException {
        File gitletFile = new File(GITLET_OBJ_PATH);
        Gitlet gitlet = Utils.readObject(gitletFile, Gitlet.class);
        gitlet.reset(args[1]);
    }

    public static void runMerge(String[] args)
            throws IOException {
        File gitletFile = new File(GITLET_OBJ_PATH);
        Gitlet gitlet = Utils.readObject(gitletFile, Gitlet.class);
        gitlet.merge(args[1]);
    }
}
