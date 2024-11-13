package gitlet;

import java.io.IOException;
import java.io.Serializable;

public class Branch implements Serializable {
    private String name;
    private String ref;

    public Branch(String branchName, String branchRef) {
        name = branchName;
        ref = branchRef;
    }

    public String getName() {
        return name;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String newref) {
        ref = newref;
    }

    public static void setInitHead(String newHeadName) {
        try {
            if (!Repository.HEAD_STATE.exists()) {
                Repository.HEAD_STATE.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeContents(Repository.HEAD_STATE, newHeadName);
    }
}
