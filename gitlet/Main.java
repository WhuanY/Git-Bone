package gitlet;

import static gitlet.Utils.exitWithErrorMsg;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author WalterWu
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            Utils.message("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                Repository.init();
                break;
            case "add":
                /** in gitlet, only one file may be added at a time */
                // TODO
                break;
            case "commit":
                // TODO
                break;
            case "rm":
                // TODO
                break;
            case "log":
                // TODO
                break;
            case "global-log":
                // TODO
                break;
            case "find":
                // TODO
                break;
            case "status":
                // TODO
                break;
            case "checkout":
                if (!Repository.alreadyInitialized()) {
                    System.exit(0);
                }
                // Handle the `checkout` command
                if ((args.length < 2) || (args.length > 4)) {
                    // TODO
                } else if (args.length == 2) {
                    // TODO
                } else if (args.length == 3) {
                    if (!(args[1].equals("--"))) {
                        // TODO
                    }
                    Repository.checkoutFilename(args[2]);
                } else {
                    if (!(args[2].equals("--"))) {
                       // TODO
                    }
                    // TODO
                }
                break;
            case "branch":
                // TODO
                break;
            case "rm-branch":
                // TODO
                break;
            case "reset":
                // TODO
                Repository.reset(args[1]);
                break;
            case "merge":
                // TODO
                break;
            default:
                exitWithErrorMsg("No command with that name exists.");
        }
    }
}
