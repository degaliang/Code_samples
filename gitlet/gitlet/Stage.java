package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

/** The Stage class that keeps track of all the files
 * staged for addition or removal.
 *  @author Alex Liang
 */
public class Stage implements Serializable {
    /** All staged files for addition. stageArea<Filename, FileID>. */
    private HashMap<String, Blob> _stageAdd;

    /** All staged files for removal. stageArea<Filename, FileID>. */
    private HashMap<String, Blob> _stageRemoval;

    /** Construct a new stage area. There should be only one
     * stage object stored in the `stage` folder.
     * */
    public Stage() {
        _stageAdd = new HashMap<String, Blob>();
        _stageRemoval = new HashMap<String, Blob>();
    }

    /** Add a new file to the stage area if it was not added
     * Staging an already-staged file overwrites the previous
     * entry in the staging area with the new contents. If the
     * current working version of the file is identical to the
     * version in the current commit, do not stage it to be added,
     * and remove it from the staging area if it is already there
     * (as can happen when a file is changed, added, and then changed back).
     * @param filename
     * @param head
     */
    public void add(String filename, Commit head) {
        File file = new File(filename);

        if (!file.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }

        Blob newBlob = new Blob(filename);
        HashMap<String, String> existBlobs = head.getBlobs();
        if (!existBlobs.containsKey(filename)) {
            _stageAdd.put(filename, newBlob);
        } else {
            if (newBlob.getID().equals(existBlobs.get(filename))) {
                _stageAdd.remove(filename);
            } else {
                _stageAdd.put(filename, newBlob);
            }
        }
    }

    public void stageRemove(String filename) {
        Blob blob = new Blob(filename);
        _stageRemoval.put(filename, blob);
    }

    public void stageRemove(Blob b) {
        _stageRemoval.put(b.getFilename(), b);
    }

    public void unstage(String filename) {
        _stageAdd.remove(filename);
    }

    public void unremove(String filename) {
        _stageRemoval.remove(filename);
    }

    public Blob getBlob(String filename) {
        if (_stageAdd.containsKey(filename)) {
            return _stageAdd.get(filename);
        } else if (_stageRemoval.containsKey(filename)) {
            return _stageRemoval.get(filename);
        }

        return null;
    }

    public HashMap<String, Blob> getStageAdd() {
        return _stageAdd;
    }

    public HashMap<String, Blob> getStageRemoval() {
        return _stageRemoval;
    }

    public boolean isEmpty() {
        return _stageAdd.isEmpty() && _stageRemoval.isEmpty();
    }

    public boolean contains(String filename) {
        return _stageAdd.containsKey(filename);
    }

    public void clear() {
        _stageAdd.clear();
        _stageRemoval.clear();
    }
}
