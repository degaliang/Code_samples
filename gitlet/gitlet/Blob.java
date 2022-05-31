package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/** The Blob class that stores all the information of the file
 * that is being tracked by Gitlet.
 *  @author Alex Liang
 */
public class Blob implements Serializable {
    /** Name of the file. */
    private String _filename;

    /** Hash ID of the file. */
    private String _id;

    /** Contents stored in this file as a string. */
    private String _content;

    /** Construct a blob.
     * @param filename
     * */
    public Blob(String filename) {
        _filename = filename;
        File file = new File(filename);
        _content = Utils.readContentsAsString(file);
        _id = hash();
    }

    /** Hash the file content to get the SHA-1 id of this file.
     * @return hash ID
     * */
    public String hash() {
        ArrayList<Object> vals = new ArrayList<Object>();
        vals.add(Utils.serialize(_filename));
        vals.add(getContentAsByte());

        return Utils.sha1(Utils.serialize(this));
    }

    /** Get the filename.
     * @return filename
     * */
    public String getFilename() {
        return _filename;
    }

    /** Get the id.
     * @return id
     * */
    public String getID() {
        return _id;
    }

    /** Get file content as a string.
     * @return file content
     * */
    public String getContent() {
        return _content;
    }

    /** Get file content as bytes.
     * @return content as bytes
     * */
    public byte[] getContentAsByte() {
        return Utils.serialize(_content);
    }

    /** Overriding equals to compare two Blob objects. */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Blob)) {
            return false;
        }

        Blob blob = (Blob) o;

        return this._id.equals(blob._id);
    }

    /** Overriding. */
    @Override
    public int hashCode() {
        return _id.hashCode();
    }
}
