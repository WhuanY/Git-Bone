package gitlet;

import java.io.Serializable;
import java.util.HashMap;

import static gitlet.Utils.humanReadableTime;
import static gitlet.Utils.message;

/**
 * Represents a gitlet commit object
 * A commit object contains metadata for any single commmit.
 *
 * @author Walter Wu
 */
public class Commit implements Serializable {
    /** The commit object itself contains all metadata related to the commit */

    /**
     * The unique identifier of a single Commit.
     */
    private String commitId;
    /**
     * The time when the commit is created.
     */
    private long timeStamp;
    /**
     * The message of this Commit.
     */
    private String message;
    /**
     * Maps containing all snapshots of current version blobs
     * relates to current commits.
     * key: file names of the blob; val: blobId
     */
    private HashMap<String, String> blobs;
    private String firstParent;
    private String secondParent;

    public Commit(String message) {
        this.message = message;
        this.blobs = new HashMap<String, String>();
        this.firstParent = "";
        this.secondParent = "";
    }

    /**
     * give a commitId based on all current metadata of the currentCommit
     */
    public void resetCommitId() {
        this.commitId = Utils.sha1(String.valueOf(timeStamp), message);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public String getCommitId() {
        return this.commitId;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public HashMap<String, String> getBlobs() {
        return this.blobs;
    }

    public void addBlob(Blob blob) {
        blobs.put(blob.getFileName(), blob.getBlobId());
    }

    public String getFirstParent() {
        return this.firstParent;
    }

    public String getSecondParent() {
        return this.secondParent;
    }

    public void setFirstParent(Commit firstParent) {
        this.firstParent = firstParent.getCommitId();
    }

    public void setSecondParent(Commit secondParent) {
        this.secondParent = secondParent.getCommitId();
    }

    public void addNewPrecommit(Commit preCommit) {
        if (this.getFirstParent().equals("")) {
            firstParent = preCommit.getCommitId();
        } else {
            secondParent = preCommit.getCommitId();
        }
    }

    public void printLog() {
        message("===");
        message("commit %s", commitId);
        message("Date: %s", humanReadableTime(timeStamp));
        message(message);
        message("");
    }
}
