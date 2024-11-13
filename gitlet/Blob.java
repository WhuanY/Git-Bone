package gitlet;

import java.io.File;
import java.io.Serializable;

/**
 * Represents any historical state of a single file.
 * when a file in a working directory is changed and
 * staged for commit, gitlet saves a copy of the file
 * with a new version. The new version can be referenced
 * the blob.
 */
public class Blob implements Serializable {
    /**
     * blob id
     */
    private String blobId;
    /**
     * the fileName the blob refers to. Used when checkout.
     */
    private String fileName;
    /**
     * the fileContent of the file. Used when checkout required
     */
    private String fileContent;

    public Blob(String id, String fileName, String content) {
        this.blobId = id;
        this.fileName = fileName;
        this.fileContent = content;
    }

    public String getBlobId() {
        return this.blobId;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getFileContent() {
        return this.fileContent;
    }

    public boolean equals(Blob b) {
        return b.getBlobId().equals(this.blobId);
    }

    /**
     * Any file staged for commit should be `blobified` first
     */
    public static Blob blobifyFile(String filename, File f) {
        // TODO: choose your blobifyFile method.;
        return null;
    }

}
