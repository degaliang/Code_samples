package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.TimeZone;

/** The Commit class that stores all the information of the commit
 * that is made in Gitlet.
 *  @author Alex Liang
 */
public class Commit implements Serializable {
    /** CWD. */
    private static final String CWD = System.getProperty("user.dir") + "/";

    /** Path to the folder that stores all the blobs with ID as filename. */
    private static final String BLOBS_PATH = CWD + ".gitlet/blobs/";

    /** Initial year. */
    private static final int YEAR = 1970;

    /** Commit message. */
    private String _message;

    /** Commit time. */
    private Date _time;

    /** Blobs included in the commit. Stored as HashMap<Filename, Blob>. */
    private HashMap<String, String> _blobs;

    /** ID of the parent of this commit. The initial commit will
     *  have a parent NULL. */
    private String _parent;

    /** ID of the merged-in parent. */
    private String _mergedParent;

    /** Hash ID of this commit. */
    private String _id;

    /** Branch of the coomit. */
    private String _branch;

    /** Initial commit constructor. */
    public Commit() {
        _message = "initial commit";
        _time = setTime(YEAR, 1, 4, 0, 0, 0);
        _blobs = new HashMap<String, String>();
        _parent = null;
        _mergedParent = null;
        _branch = "master";
        _id = hash();
    }

    /** Constructor.
     * @param message
     * @param blobs
     * @param branch
     * @param parent
     * */
    public Commit(String message, HashMap<String, String> blobs,
                  String parent, String branch) {
        _message = message;
        _time = new Date();
        _blobs = blobs;
        _parent = parent;
        _mergedParent = null;
        _branch = branch;
        _id = hash();
    }

    /** Hash the Commit content to get the SHA-1 id of this commit.
     * @return hash ID of the commit
     * */
    public String hash() {
        ArrayList<Object> vals = new ArrayList<Object>();
        vals.add(Utils.serialize(_message));
        vals.add(Utils.serialize(_parent));
        vals.add(Utils.serialize(_id));
        vals.add(Utils.serialize(_branch));

        return Utils.sha1(Utils.serialize(vals));
    }

    public void setMergedParent(String mergedParent) {
        this._mergedParent = mergedParent;
    }

    /** Get the blob from the current commit. Throw an exception
     * if such a file does not exist.
     * @param filename
     * @return a Blob
     * */
    public Blob getBlob(String filename) {
        if (!_blobs.containsKey(filename)) {
            return null;
        }

        String blobID = _blobs.get(filename);
        File blobFile = new File(BLOBS_PATH + blobID);
        Blob blob = Utils.readObject(blobFile, Blob.class);

        return blob;
    }

    /** Get the commit message.
     * @return commit message
     * */
    public String getMessage() {
        return _message;
    }

    /** Get the commit time.
     * @return a Date object
     * */
    public Date getTime() {
        return _time;
    }

    /** Get the commit time as a string.
     * @return time as a string
     * */
    public String getTimeAsString() {
        String pattern = "EE MMM dd HH:mm:ss yyyy Z";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        return simpleDateFormat.format(_time);
    }

    /** Get the blobs(filetracked) in this commit.
     * @return all the blobcs in this commit
     * */
    public HashMap<String, String> getBlobs() {
        return _blobs;
    }

    /** Get the parent of this commit.
     * @return ID of the parent
     * */
    public String getParent() {
        return _parent;
    }

    /** Get the merged-in parent of this commit.
     * @return ID of the merged_in parent
     * */
    public String getMergedParent() {
        return _mergedParent;
    }

    /** Get the commit hash ID.
     * @return commit ID
     * */
    public String getID() {
        return _id;
    }

    /** Get the branch this commit belongs to.
     * @return branch name
     * */
    public String getBranch() {
        return _branch;
    }

    /** Check if the commit contains a file.
     * @param filename
     * @return true or false
     * */
    public boolean contains(String filename) {
        return _blobs.containsKey(filename);
    }

    /** Time setting helper function.
     * @param date
     * @param hrs
     * @param min
     * @param month
     * @param sec
     * @param year
     * @return  a Date object
     * */
    public Date setTime(int year, int month, int date,
                        int hrs, int min, int sec) {
        Date d = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, sec);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.HOUR, hrs);
        calendar.set(Calendar.DATE, date);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);

        return calendar.getTime();
    }

    /** Overriding equals to compare two Commit objects. */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Commit)) {
            return false;
        }

        Commit cm = (Commit) o;

        return this._id.equals(cm.getID());
    }

    /** Overriding. */
    @Override
    public int hashCode() {
        return _id.hashCode();
    }
}
