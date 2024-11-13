package gitlet;

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
}
