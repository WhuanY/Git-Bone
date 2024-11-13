package gitlet;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import static gitlet.Helper.*;
import static gitlet.Utils.*;

/**
 * Represents a gitlet repository.
 * This repo has only of the following function:
 * 1. Setting up persistance for gitlet.
 * 2. Representing Main Logic for any commands.
 * 3. Functions that only related to this repository.
 *
 * @author WalterWu
 */
public class Repository {
    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File COMMIT_DIR = Utils.join(GITLET_DIR, "commits");
    public static final File BLOB_DIR = Utils.join(GITLET_DIR, "blobs");
    public static final File STAGE_ADD = Utils.join(GITLET_DIR, "stage_add");
    public static final File STAGE_REMOVE = Utils.join(GITLET_DIR, "stage_remove");
    public static final File BRANCH_DIR = Utils.join(GITLET_DIR, "branch");
    public static final File HEAD_STATE = Utils.join(BRANCH_DIR, "HEAD");

    public static void setupPersistance() {
        GITLET_DIR.mkdir();
        BLOB_DIR.mkdir();
        COMMIT_DIR.mkdir();
        STAGE_ADD.mkdir();
        STAGE_REMOVE.mkdir();
        BRANCH_DIR.mkdir();
    }

    /**
     * As name implies, this function is called before any operation besides init
     * Any gitlet operations would be invalid with .gitlet repo haven't set up yet.
     */

    public static boolean alreadyInitialized() {
        // TODO
    }

    public static void init() {
        // TODO
    }

    public static void add(String filename) {
        // TODO
    }

    public static void commit(String msg) {
        // TODO
    }

    public static void rm(String filename) {
        // TODO
    }

    public static void log() {
        // TODO
    }

    public static void globalLog() {
       // TODO
    }

    public static void find(String commitMessage) {
        // TODO
    }

    public static void status() {
       // TODO
    }

    public static void checkoutFilename(String filename) {
       // TODO
    }

    public static void checkoutCommitIdFile(String commitId, String filename) {
       // TODO
    }

    public static void checkoutBranch(String branchName) {
        // TODO
    }

    public static void branch(String branchName) {
        // TODO
    }

    public static void rmBranch(String branchName) {
        // TODO
    }

    public static void reset(String commitId) {
        // TODO
    }

    public static void merge(String branchName) {
        // TODO: deal with merge error cases first

        // TODO: get the split point, currentCommit and branch Commit
        Commit currentCommit = getCommitFromHEAD();
        Commit branchCommit = getCommitFromBranch(branchName);
        Commit nearestSplitCommit = commonAncestor(currentCommit, branchCommit);

        // TODO: deal with two special cases: fast-forwarded or another.

        // TODO: the main logic of merging! You should implement `mergeByFile` below.
        Boolean hasMergeConflict = mergeByFile(
                nearestSplitCommit, currentCommit, branchCommit);  // main logic inside
        if (hasMergeConflict) {
            message("Encountered a merge conflict.");
        }

        String commitMsg = "Merge " + branchName + " into " + getCurrentHeadName() + ".";
        // TODO: commit files processed after the main logic. Remember to use `commit()`
    }

    /** main merge opeartions based on conditions of:
     *
     * @param s: nearest split point commit
     * @param c: current Commit
     * @param b: branch Commit
     * @return true if there is at least one merge conflict, false otherwise
     */
    private static boolean mergeByFile(Commit s, Commit c, Commit b) {
        HashMap<String, String> branchCommitBlobs = b.getBlobs();
        HashMap<String, String> curCommitBlobs = c.getBlobs();
        HashMap<String, String> nearestSplitCommitBlobs = s.getBlobs();
        Boolean hasMergeConflict = false;
        // TODO: Finish the main logic or merging every single file.
        // TODO: scan the splitpoint file first!
        for (HashMap.Entry<String, String> mapEntry : nearestSplitCommitBlobs.entrySet()) {
            String scanFileName = mapEntry.getKey();
            String sBID = nearestSplitCommitBlobs.get(scanFileName);
            String cBID = curCommitBlobs.get(scanFileName);
            String bBID = branchCommitBlobs.get(scanFileName);
            if (cBID != null && bBID != null) {
                // TODO: My notation when implementing the logic with code. Wish it helps!
                // F: file in splitPoint. F1 and F2 means modified but not removed
                // 0: file not found or tracked
                // (s, c, b) represents the file state

                // (F, F, F) -> Nothing Changed
                // (F, F1, F1) -> Nothing Changed
                // (F, F, F1) -> F1 in WD, in Stage Area

                // (F, F1, F2) -> F1 + F2 with merge

            } else if (cBID != null && bBID == null) {
                // TODO: My notation when implementing the logic with code. Wish it helps!
                // (F, F, 0) -> F
            } else if (cBID == null && bBID != null) {
                // TODO: See a possible example of how helper function can be utilized.
                // TODO: You can modified here if you want.
                // (F, 0, F) -> 0
                // (F, 0, F1) -> 0 + F1 with merge conflict
                if (!sBID.equals(bBID)) {
                    mergeConflictFiles(scanFileName, null, getBlobFromBlobID(bBID));
                    hasMergeConflict = true;
                }
                // My practice is to remove the scanned file once processed.
                branchCommitBlobs.remove(scanFileName);
            }
        //  else {(F, 0, 0) -> 0}
        }
        // TODO: following the scan split approach, you can easily finish this!
        for (HashMap.Entry<String, String> mapEntry : curCommitBlobs.entrySet()) {
            String scanFileName = mapEntry.getKey();
            // TODO
            branchCommitBlobs.remove(scanFileName);
            }
//          else {(0, F1, 0) -> F1 in WD. Not change;}
        // TODO: as above. now you can focus on those only present in the branchCommit!
        for (HashMap.Entry<String, String> mapEntry : branchCommitBlobs.entrySet()) {
            // (0, 0, F1) -> F1 in WD and staged
            // TODO
        }
        return hasMergeConflict;
    }
}
