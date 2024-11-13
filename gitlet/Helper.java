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
        String headName = getCurrentHeadName();
        Branch headObj = readObject(join(Repository.BRANCH_DIR, headName), Branch.class);
        return readObject(join(Repository.COMMIT_DIR, headObj.getRef()), Commit.class);
    }

    /** This method gets commit from the input commit id in the gitlet repo
     * @param id: the commitId;
     * @return a commit object
     */

    static Commit getCommitFromCommitID(String id) {
        if (id.compareTo("") == 0) {
            return null;
        }
        File targetFile = join(Repository.COMMIT_DIR, id);
        if (!targetFile.exists()) {
            message("No commit with that id exists.");
            System.exit(0);
            return null;
        }
        return readObject(targetFile, Commit.class);
    }

    static Commit getCommitFromBranch(String branchName) {
        File targetFile = join(Repository.BRANCH_DIR, branchName);
        if (!targetFile.exists()) {
            message("No commit with that id exists.");
            System.exit(0);
            return null;
        }
        Branch targetBranch = readObject(targetFile, Branch.class);
        return getCommitFromCommitID(targetBranch.getRef());
    }

    static Blob getBlobFromBlobID(String id) {
        if (id.compareTo("") == 0) {
            return null;
        }
        return readObject(join(Repository.BLOB_DIR, id), Blob.class);
    }

    /** Branch Methods collections */
    static String getCurrentHeadName() {
        return readContentsAsString(Repository.HEAD_STATE);
    }

    static Branch getCurrentHEADBranch() {
        String headName = getCurrentHeadName();
        return getBranchFromBranchName(headName);
    }

    /** This method gets branch from branchName.
     * @param branchName
     * @return a branch instance
     */
    static Branch getBranchFromBranchName(String branchName) {
        return readObject(join(Repository.BRANCH_DIR, branchName), Branch.class);
    }

    /** This method checks if the file in the CWD denoted by filename
     * is all the same(both in filename and contents) as that in current commit.
     */
    static boolean sameAsCurrentCommit(String filename) {
        Commit currentCommit = getCommitFromHEAD();
        File compareFile = join(Repository.CWD, filename);
        Blob compareBlob = Blob.blobifyFile(filename, compareFile);
        HashMap<String, String> currentBlobs = currentCommit.getBlobs();
        if (currentBlobs == null) {
            return false;
        }
        if (currentBlobs.containsKey(filename)) {
            if (currentBlobs.get(filename).compareTo(compareBlob.getBlobId()) == 0) {
                return true;
            }
        }
        return false;
    }

    /** This method checks any file in the CWD is untracked. A file that is untracked
     *  in a gitlet project is not in the current commit snapshot and currently in CWD.
     * @param filename
     * @return a boolean value
     */

    static boolean isUntrackedFile(String filename) {
        Commit curCommit = getCommitFromHEAD();
        boolean inWorkingDir = join(Repository.CWD, filename).exists();
        boolean stagedForAddition = join(Repository.STAGE_ADD, filename).exists();
        boolean stagedForRemoval = join(Repository.STAGE_REMOVE, filename).exists();
        boolean inCurrentCommit = curCommit.getBlobs().containsKey(filename);
        boolean res = (inWorkingDir) && (!stagedForAddition)
                && ((!inCurrentCommit) || stagedForRemoval);
        return res;
    }

    /** This method adds the prepared commit to the .gitlet repo */
    static void submitCommit(Commit commit) {
        File commitFile = join(Repository.COMMIT_DIR, commit.getCommitId());
        writeObject(commitFile, commit);
    }

    /** This method is the subprocess of checkout and reset. It restores the snapshot
     * of a given commit to the CWD.
     */
    static void restoreCommitSnapshot(Commit tgtCommit) {
        // delete files in CWD
        List<String> filenamesCWD = plainFilenamesIn(Repository.CWD);
        for (String filename : filenamesCWD) {
            join(Repository.CWD, filename).delete();
        }
        // add snapshot back to the CWD
        HashMap<String, String> blobs = tgtCommit.getBlobs();
        for (HashMap.Entry<String, String> entryset : blobs.entrySet()) {
            writeContents(
                    join(Repository.CWD, entryset.getKey()),
                    getBlobFromBlobID(entryset.getValue()).getFileContent());
        }
    }

    /** This method move a given branch object to certain commit by changing the ref of the branch.
     *
     * @param branch: the branch object object
     * @param commit: the target commit object
     */
    static void moveBranchtoCommit(Branch branch, Commit commit) {
        branch.setRef(commit.getCommitId());
        writeObject(join(Repository.BRANCH_DIR, branch.getName()), branch);
    }

    /** This method move a given branch object to certain commit by changing the ref of the branch.
     *
     * @param branchName: the branchId
     * @param commit: the target commit object
     */
    static void moveBranchtoCommit(String branchName, Commit commit) {
        File targetFile = join(Repository.BRANCH_DIR, branchName);
        if (!targetFile.exists()) {
            message("No such branch exists.");
            System.exit(0);
        }
        Branch targetBranch = readObject(targetFile, Branch.class);
        moveBranchtoCommit(targetBranch, commit);
    }

    /** merge Method Collections */
    static Commit commonAncestor(Branch thisBranch, Branch thatBranch) {
        return commonAncestor(getCommitFromCommitID(thisBranch.getRef()),
                getCommitFromCommitID(thatBranch.getRef()));
    }

    static Commit commonAncestor(Commit currentCommit, Commit branchCommit) {
        List<String> currentAncestors = new ArrayList<>();
        List<String> branchAncestors = new ArrayList<>();
        while (currentCommit != null) {
            currentAncestors.add(currentCommit.getCommitId());
            currentCommit = getCommitFromCommitID(currentCommit.getFirstParent());
        }
        while (branchCommit != null) {
            branchAncestors.add(branchCommit.getCommitId());
            branchCommit = getCommitFromCommitID(branchCommit.getFirstParent());
        }
        Collections.reverse(currentAncestors);
        Collections.reverse(branchAncestors);
        String tgtId = null;
        int i = 0;
        int minSize = Math.min(currentAncestors.size(), branchAncestors.size());
        while (i < minSize) {
            if (currentAncestors.get(i).equals(branchAncestors.get(i))) {
                i++;
            } else {
                break;
            }
        }
        tgtId = currentAncestors.get(i - 1);
        // break previous while due to find dif commits, but not out of bound
        Commit tgtCommit = getCommitFromCommitID(tgtId);
        return tgtCommit;
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
