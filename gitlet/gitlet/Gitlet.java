package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;

/** The Gitlet class that stores all the information of the version
 *  control system. Any operations are done through a Gitlet object.
 *  @author Alex Liang
 */
public class Gitlet implements Serializable {
    /** Path to CWD. */
    private static final String CWD = System.getProperty("user.dir") + "/";

    /** Path to the `.gitlet` folder. */
    private static final String GITLET_PATH = CWD + ".gitlet/";

    /** Path to the folder that stores all the commits. */
    private static final String COMMITS_PATH = CWD + ".gitlet/commits/";

    /** Path to the folder that stores all staged files. */
    private static final String STAGE_PATH = CWD + ".gitlet/stage/";

    /** Path to the folder that stores all the blobs with ID as filename. */
    private static final String BLOBS_PATH = CWD + ".gitlet/blobs/";

    /** Max length of hash id. */
    private static final int ID_LENGTH = 40;

    /** Pointer to the commit that is currently being tracked.
     * This should be the latest commit. */
    private Commit _head;

    /** The branch that is currently in. */
    private String _currBranch;

    /** The HashMap that stores all the branches created. It is a
     * mapping from the branch name to the branch's head's hash ID. */
    private HashMap<String, String> _branchList;

    /** The HashMap tha maps abbreviated commit ID's to their
     * full ID's. HashMap(abbrev., fullID)*/
    private HashMap<String, String> _commits;

    /** Staging area of the system. */
    private Stage _stageArea;

    /** Constructor. */
    public Gitlet() throws IOException {
        Commit firstCommit = new Commit();
        storeCommit(firstCommit);
        _head = firstCommit;
        _currBranch = firstCommit.getBranch();
        _branchList = new HashMap<String, String>();
        _branchList.put(_currBranch, _head.getID());
        _commits = new HashMap<String, String>();
        _commits.put(firstCommit.getID().substring(0, 5), firstCommit.getID());
        _stageArea = new Stage();
    }

    /** Store the gitlet object. */
    public void initialize() throws IOException {
        File gitlet = new File(GITLET_PATH + "gitlet");
        gitlet.createNewFile();
        Utils.writeObject(gitlet, this);
    }

    /** Store a commit object in the folder.
     * @param cm
     * */
    public void storeCommit(Commit cm) throws IOException {
        File newCommit = new File(COMMITS_PATH + cm.getID());
        newCommit.createNewFile();
        Utils.writeObject(newCommit, cm);
    }

    /** Load a commit object from the folder.
     * @param commitID
     * @return a Commit object.
     * */
    public Commit loadCommit(String commitID) throws GitletException {
        File filename = new File(COMMITS_PATH + commitID);
        if (!filename.exists()) {
            throw new GitletException("Commit " + commitID + " doesn't exist");
        }
        Commit cm = Utils.readObject(filename, Commit.class);
        return cm;
    }

    /** Run the `add` command.
     * @param filename
     * */
    public void add(String filename) {
        if (_stageArea.getStageRemoval().containsKey(filename)) {
            _stageArea.unremove(filename);
        } else {
            _stageArea.add(filename, _head);
        }

        update();
    }

    /** Run the `commit` command.
     * @param message
     * */
    public void commit(String message) throws IOException {
        if (_stageArea.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        } else if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }

        HashMap<String, String> parentBlobs = _head.getBlobs();

        HashMap<String, Blob> stageAdd = _stageArea.getStageAdd();
        HashMap<String, Blob> stageRemoval = _stageArea.getStageRemoval();

        for (Map.Entry<String, Blob> set : stageAdd.entrySet()) {
            String filename = set.getKey();
            Blob blob = set.getValue();

            parentBlobs.put(filename, blob.getID());

            File blobFile = new File(BLOBS_PATH  + blob.getID());
            blobFile.createNewFile();
            Utils.writeObject(blobFile, blob);
        }

        for (Map.Entry<String, Blob> set : stageRemoval.entrySet()) {
            String filename = set.getKey();
            parentBlobs.remove(filename);
        }

        Commit newCommit = new Commit(message, parentBlobs,
                _head.getID(), _currBranch);
        File newCommitFile = new File(COMMITS_PATH + newCommit.getID());
        newCommitFile.createNewFile();
        Utils.writeObject(newCommitFile, newCommit);

        _head = newCommit;
        _branchList.put(_currBranch, newCommit.getID());
        _commits.put(newCommit.getID().substring(0, 5), newCommit.getID());

        _stageArea.clear();
        update();
    }

    /** Method for checkout file command(with or without
     * commit id specified).
     * @param commitID
     * @param filename
     * */
    public void checkout(String commitID, String filename) throws IOException {
        if (commitID.length() < ID_LENGTH) {
            boolean found = false;
            for (String id : _commits.values()) {
                if (id.contains(commitID)) {
                    commitID = id;
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("No commit with that id exists.");
                System.exit(0);
            }
        } else {
            if (!_commits.containsValue(commitID)) {
                System.out.println("No commit with that id exists.");
                System.exit(0);
            }
        }

        Commit commit = getCommit(commitID);

        Blob blob = commit.getBlob(filename);
        if (blob == null) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }

        File file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }

        String contents = blob.getContent();
        Utils.writeContents(file, contents);
    }

    /** Run the `checkout` command with branch as argument.
     * @param branchName
     * */
    public void checkoutBranch(String branchName) throws IOException {
        if (!_branchList.containsKey(branchName)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        } else if (branchName.equals(_currBranch)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        checkUntracked();

        String branchHeadID = _branchList.get(branchName);
        Commit branchHead = getCommit(branchHeadID);
        HashMap<String, String> blobs = branchHead.getBlobs();
        for (Map.Entry<String, String> set : blobs.entrySet()) {
            String filename = set.getKey();
            File file = new File(filename);
            String blobID = set.getValue();
            Blob blob = getBlob(blobID);

            file.createNewFile();
            Utils.writeContents(file, blob.getContent());
        }

        HashMap<String, String> oldBlobs = _head.getBlobs();
        for (Map.Entry<String, String> set : oldBlobs.entrySet()) {
            String filename = set.getKey();
            if (!blobs.containsKey(filename)) {
                Utils.restrictedDelete(filename);
            }
        }

        _currBranch = branchName;
        _head = getCommit(_branchList.get(_currBranch));
        _stageArea.clear();

        update();
    }

    /** Run the `log` command. */
    public void log() {
        String curr = _head.getID();

        while (curr != null) {
            Commit cm = getCommit(curr);
            String date = cm.getTimeAsString();
            String message = cm.getMessage();

            System.out.println("===");
            System.out.println("commit " + curr);
            System.out.println("Date: " + date);
            System.out.println(message);
            System.out.println();

            curr = cm.getParent();
        }
    }

    /** Run the `global-log` command. */
    public void globalLog() {
        for (String filename : Utils.plainFilenamesIn(COMMITS_PATH)) {
            File commitFile = new File(COMMITS_PATH + filename);
            Commit cm = Utils.readObject(commitFile, Commit.class);
            String date = cm.getTimeAsString();
            String message = cm.getMessage();

            System.out.println("===");
            System.out.println("commit " + filename);
            System.out.println("Date: " + date);
            System.out.println(message);
            System.out.println();
        }
    }

    /** Run the `rm` command.
     * @param filename
     * */
    public void remove(String filename) {
        if (!_stageArea.contains(filename) && !isTracked(filename)) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }

        if (_stageArea.contains(filename)) {
            _stageArea.unstage(filename);
        }

        if (isTracked(filename)) {
            String id = _head.getBlobs().get(filename);
            Blob b = getBlob(id);
            _stageArea.stageRemove(b);
            Utils.restrictedDelete(filename);
        }

        update();
    }

    /** Run the `find` command.
     * @param message
     * */
    public void find(String message) {
        boolean found = false;

        for (String filename : Utils.plainFilenamesIn(COMMITS_PATH)) {
            File commitFile = new File(COMMITS_PATH + filename);
            Commit cm = Utils.readObject(commitFile, Commit.class);
            if (cm.getMessage().equals(message)) {
                System.out.println(cm.getID());
                found = true;
            }
        }

        if (!found) {
            System.out.println("Found no commit with that message");
            System.exit(0);
        }
    }

    /** Run the `find` command. */
    public void status() {
        System.out.println("=== Branches ===");
        ArrayList<String> branches =
                new ArrayList<String>(_branchList.keySet());
        Collections.sort(branches);
        for (String branch : branches) {
            if (branch.equals(_currBranch)) {
                System.out.print("*");
            }
            System.out.println(branch);
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        ArrayList<String> stagedFiles =
                new ArrayList<String>(_stageArea.getStageAdd().keySet());
        Collections.sort(stagedFiles);
        for (String file : stagedFiles) {
            System.out.println(file);
        }
        System.out.println();

        ArrayList<String> removedFiles =
                new ArrayList<String>(_stageArea.getStageRemoval().keySet());
        Collections.sort(removedFiles);
        System.out.println("=== Removed Files ===");
        for (String file : removedFiles) {
            System.out.println(file);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();

        System.out.println("=== Untracked Files ===");
    }

    /** Run `branch`.
     * @param branchname
     * */
    public void branch(String branchname) {
        if (_branchList.containsKey(branchname)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }

        _branchList.put(branchname, _head.getID());

        update();
    }

    /** Run `rm-branch`.
     * @param branchname
     * */
    public void removeBranch(String branchname) {
        if (!_branchList.containsKey(branchname)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        } else if (_currBranch.equals(branchname)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }

        _branchList.remove(branchname);

        update();
    }

    /** Run `reset`.
     * @param commitID
     * */
    public void reset(String commitID) throws IOException {
        if (commitID.length() < ID_LENGTH) {
            boolean found = false;
            for (String id : _commits.values()) {
                if (id.contains(commitID)) {
                    commitID = id;
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("No commit with that id exists.");
                System.exit(0);
            }
        } else {
            if (!_commits.containsValue(commitID)) {
                System.out.println("No commit with that id exists.");
                System.exit(0);
            }
        }
        checkUntracked();

        Commit cm = loadCommit(commitID);
        for (String file : Utils.plainFilenamesIn(CWD)) {
            if (!cm.contains(file)) {
                Utils.restrictedDelete(file);
            } else {
                checkout(commitID, file);
            }
        }

        _stageArea.clear();
        _branchList.put(_currBranch, cm.getID());
        _head = cm;

        update();
    }

    /** Run `merge`.
     * @param branchname
     * */
    public void merge(String branchname) throws IOException {
        checkMergeError(branchname);
        checkUntracked();
        Commit givenHead = loadCommit(_branchList.get(branchname));
        ArrayList<Commit> known = traverse(givenHead);
        Commit split = findSplit(known, _head);
        if (split.equals(_head)) {
            checkoutBranch(branchname);
            System.out.println("Current branch fast-forwarded.");
        }
        HashMap<String, String> splitBlobs = split.getBlobs();
        HashMap<String, String> givenBlobs = givenHead.getBlobs();
        HashMap<String, String> currentBlobs = _head.getBlobs();
        boolean conflict = false;
        for (String file : givenBlobs.keySet()) {
            if (split.contains(file)) {
                if (!givenBlobs.get(file).equals(splitBlobs.get(file))) {
                    checkout(givenHead.getID(), file);
                    add(file);
                }
            } else {
                checkout(givenHead.getID(), file);
                add(file);
            }
            conflict = checkConflict1(file, split, givenHead,
                    currentBlobs, givenBlobs, splitBlobs);
        }
        for (String file : splitBlobs.keySet()) {
            if (_head.contains(file)
                    && currentBlobs.get(file).equals(splitBlobs.get(file))
                    && !givenHead.contains(file)) {
                remove(file);
            }
            if (_head.contains(file)
                    && !currentBlobs.get(file).equals(splitBlobs.get(file))
                    && !givenHead.contains(file)) {
                doConflict(file, _head, givenHead);
                conflict = true;
            } else if (givenHead.contains(file)
                    && !givenBlobs.get(file).equals(splitBlobs.get(file))
                    && !_head.contains(file)) {
                doConflict(file, _head, givenHead);
                conflict = true;
            }
            if (_head.contains(file) && !givenHead.contains(file)
                    && !currentBlobs.get(file).equals(splitBlobs.get(file))) {
                doConflict(file, _head, givenHead);
                conflict = true;
            }
        }
        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }
        if (split.equals(givenHead)) {
            System.out.println("Given branch is an ancestor "
                    + "of the current branch.");
        }
        mergeCommit(_currBranch, branchname);
        update();
    }

    public void mergeCommit(String curr, String given) throws IOException {
        HashMap<String, String> parentBlobs = _head.getBlobs();
        HashMap<String, Blob> stageAdd = _stageArea.getStageAdd();

        for (Map.Entry<String, Blob> set : stageAdd.entrySet()) {
            String filename = set.getKey();
            Blob blob = set.getValue();

            parentBlobs.put(filename, blob.getID());

            File blobFile = new File(BLOBS_PATH  + blob.getID());
            blobFile.createNewFile();
            Utils.writeObject(blobFile, blob);
        }

        String message = "Merged " + given + " into " + curr + ".";
        Commit newCommit = new Commit(message, parentBlobs,
                _head.getID(), _currBranch);
        newCommit.setMergedParent(_branchList.get(given));
        File newCommitFile = new File(COMMITS_PATH + newCommit.getID());
        newCommitFile.createNewFile();
        Utils.writeObject(newCommitFile, newCommit);

        _head = newCommit;
        _branchList.put(_currBranch, newCommit.getID());
        _commits.put(newCommit.getID().substring(0, 5), newCommit.getID());

        _stageArea.clear();
        update();
    }

    public boolean checkConflict1(String file, Commit split, Commit givenHead,
                                  HashMap<String, String> currentBlobs,
                                  HashMap<String, String> givenBlobs,
                                  HashMap<String, String> splitBlobs) {
        boolean conflict = false;
        if (_head.contains(file) && split.contains(file)
                && !currentBlobs.get(file).equals(givenBlobs.get(file))
                && !currentBlobs.get(file).equals(splitBlobs.get(file))
                && !splitBlobs.get(file).equals(givenBlobs.get(file))) {
            doConflict(file, _head, givenHead);
            conflict = true;
        }

        if (_head.contains(file) && !split.contains(file)
                && !currentBlobs.get(file).equals(givenBlobs.get(file))) {
            doConflict(file, _head, givenHead);
            conflict = true;
        } else if (!_head.contains(file) && split.contains(file)
                && !splitBlobs.get(file).equals(givenBlobs.get(file))) {
            doConflict(file, _head, givenHead);
            conflict = true;
        }
        return conflict;
    }

    public void checkMergeError(String branchname) {
        if (!_stageArea.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        if (!_branchList.containsKey(branchname)) {
            System.out.println("A branch with that name does not exist");
            System.exit(0);
        }
        if (branchname.equals(_currBranch)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
    }

    public void doConflict(String filename,
                           Commit curr, Commit given) {
        Blob currBlob = curr.getBlob(filename);
        Blob givenBlob = given.getBlob(filename);

        String currContent = "";
        String givenContent = "";

        if (currBlob != null) {
            currContent = currBlob.getContent();
        }

        if (givenBlob != null) {
            givenContent = givenBlob.getContent();
        }

        String newContent = "<<<<<<< HEAD\n";
        newContent += currContent;
        newContent += "=======\n";
        newContent += givenContent;
        newContent += ">>>>>>>\n";

        File f = new File(filename);
        Utils.writeContents(f, newContent);
        add(filename);

    }

    /** Traverse the commit tree.
     * @param head
     * @return A ArrayList containing all parent commits
     * */
    public ArrayList<Commit> traverse(Commit head) {
        ArrayList<Commit> visited = new ArrayList<Commit>();
        Queue<Commit> q = new LinkedList<Commit>();

        q.add(head);
        while (!q.isEmpty()) {
            Commit curr = q.poll();
            visited.add(curr);

            if (curr.getParent() != null) {
                Commit parent = loadCommit(curr.getParent());
                if (!visited.contains(parent)) {
                    q.add(parent);
                }
            }

            if (curr.getMergedParent() != null) {
                Commit mergedParent = loadCommit(curr.getMergedParent());
                if (!visited.contains(mergedParent)) {
                    q.add(mergedParent);
                }
            }

        }

        return visited;
    }

    public Commit findSplit(ArrayList<Commit> known, Commit head) {
        ArrayList<Commit> visited = new ArrayList<Commit>();
        Queue<Commit> q = new LinkedList<Commit>();

        q.add(head);
        while (!q.isEmpty()) {
            Commit curr = q.poll();
            visited.add(curr);

            if (known.contains(curr)) {
                return curr;
            }

            if (curr.getParent() != null) {
                Commit parent = loadCommit(curr.getParent());
                if (!visited.contains(parent)) {
                    q.add(parent);
                }
            }

            if (curr.getMergedParent() != null) {
                Commit mergedParent = loadCommit(curr.getMergedParent());
                if (!visited.contains(mergedParent)) {
                    q.add(mergedParent);
                }
            }

        }

        return null;
    }

    /** Given the id, retrieve the commit.
     * @param commitID
     * @return  a Commit object
     * */
    public Commit getCommit(String commitID) throws GitletException {
        File commitFile = new File(COMMITS_PATH + commitID);
        if (!commitFile.exists()) {
            return null;
        }
        Commit commit = Utils.readObject(commitFile, Commit.class);
        return commit;
    }

    /** Given the id, retrieve the blob.
     * @param blobID
     * @return a Blob object
     * */
    public Blob getBlob(String blobID) {
        File blobFile = new File(BLOBS_PATH + blobID);
        if (!blobFile.exists()) {
            return null;
        }
        Blob blob = Utils.readObject(blobFile, Blob.class);
        return blob;
    }

    /** Check if a file is tracked by the head commit.
     * @param filename
     * @return true or false
     * */
    public boolean isTracked(String filename) {
        return _head.contains(filename) || _stageArea.contains(filename);
    }

    /** Check if any files in CWD are untracked. */
    public void checkUntracked() {

        for (String filename : Utils.plainFilenamesIn(CWD)) {
            if (!isTracked(filename)) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                System.exit(0);
            }
        }
    }

    /** Get commit head.
     * @return the latest commit.
     * */
    public Commit getHead() {
        return _head;
    }

    /** Get current branch.
     * @return current branch name.
     * */
    public String getCurrBranch() {
        return _currBranch;
    }

    /** Get all the branches.
     * @return a branch list as a hashmap.
     * */
    public HashMap<String, String> getBranchList() {
        return _branchList;
    }

    /** Get the staging area.
     * @return a Stage object.
     * */
    public Stage getStageArea() {
        return _stageArea;
    }

    /** Overwrite the gitlet file to update all the changes
     * to the Gitlet object. */
    public void update() {
        File gitlet = new File(GITLET_PATH + "gitlet");
        Utils.writeObject(gitlet, this);
    }
}
