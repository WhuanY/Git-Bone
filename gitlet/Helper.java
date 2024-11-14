package gitlet;

/**
 * Helper Methods requires `multi-class` logic operation
 * Function unrelated to the project persistance is stored in gitlet.Utils.
 * Function related to the project persistance is stored in gitlet.Helper
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static gitlet.Utils.*;

public class Helper {

    /** This method related to take a branch object as input and a commit as Output.
     * Thus is defined in the helper class. It returns the commit pointed by the
     * current pointer.
     * @param
     * @return current Commit object
     */

    static Commit getCommitFromHEAD() {
        // TODO: Finish this by using helpers in Utils. This method is frequently used!
        return null;
    }

    /** This method gets commit from the input commit id in the gitlet repo
     * @param id: the commitId;
     * @return a commit object
     */

    static Commit getCommitFromCommitID(String id) {
        // TODO
        return null;
    }

    static Commit getCommitFromBranch(String branchName) {
       // TODO
        return null;
    }

    static Blob getBlobFromBlobID(String id) {
        // TODO
        return null;
    }

    /** Branch Methods collections */
    static String getCurrentHeadName() {
        return readContentsAsString(Repository.HEAD_STATE);
    }

    static Branch getCurrentHEADBranch() {
        // TODO
        return null;
    }

    /** This method gets branch from branchName.
     * @param branchName
     * @return a branch instance
     */
    static Branch getBranchFromBranchName(String branchName) {
        // TODO
        return null;
    }

    /** This method checks if the file in the CWD denoted by filename
     * is all the same(both in filename and contents) as that in current commit.
     */
    static boolean sameAsCurrentCommit(String filename) {
        // TODO
        return false;
    }

    /** This method checks any file in the CWD is untracked. A file that is untracked
     *  in a gitlet project is not in the current commit snapshot and currently in CWD.
     * @param filename
     * @return a boolean value
     */

    static boolean isUntrackedFile(String filename) {
        // TODO
        return false;
    }

    /** This method adds the prepared commit to the .gitlet repo */
    static void submitCommit(Commit commit) {
        // TODO
    }

    /** This method is the subprocess of checkout and reset. It restores the snapshot
     * of a given commit to the CWD.
     */
    static void restoreCommitSnapshot(Commit tgtCommit) {
        // TODO
    }

    /** This method move a given branch object to certain commit by changing the ref of the branch.
     *
     * @param branch: the branch object object
     * @param commit: the target commit object
     */
    static void moveBranchtoCommit(Branch branch, Commit commit) {
        // TODO
    }

    /** This method move a given branch object to certain commit by changing the ref of the branch.
     *
     * @param branchName: the branchId
     * @param commit: the target commit object
     */
    static void moveBranchtoCommit(String branchName, Commit commit) {
       // TODO
    }

    /** merge Method Collections */
    static Commit commonAncestor(Branch thisBranch, Branch thatBranch) {
        // TODO
        return null;
    }

    static Commit commonAncestor(Commit currentCommit, Commit branchCommit) {
        // TODO
        return null;
    }

    /** Deal with two blobs that will encounter a confilct when merging. Finish 3 steps
     *  1. Generate the newContent 2. Write the content to a file to the WD. 3. stage the new file
     * @param headBlob: the Blob in the head;
     * @param branchBlob: the Blob in the merging branch */
    static void mergeConflictFiles(String fileName, Blob headBlob, Blob branchBlob) {
        String headContent = "";
        String branchContent = "";
        if (headBlob != null) {
            headContent = headBlob.getFileContent();
        }
        if (branchBlob != null) {
            branchContent = branchBlob.getFileContent();
        }
        String newContent = "<<<<<<< HEAD\n".concat(headContent).concat("=======\n").concat(
                branchContent).concat(">>>>>>>\n");
        File newWDFile = join(Repository.CWD, headBlob.getFileName());
        File newStageFile = join(Repository.STAGE_ADD, headBlob.getFileName());
        writeContents(newWDFile, newContent);
        writeContents(newStageFile, newContent);
    }
}
