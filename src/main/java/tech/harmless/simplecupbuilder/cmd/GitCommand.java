package tech.harmless.simplecupbuilder.cmd;

import tech.harmless.simplecupbuilder.SimpleCupBuilder;
import tech.harmless.simplecupbuilder.utils.Log;
import tech.harmless.simplecupbuilder.utils.tuples.FinalTuple;

//TODO Add support for cmd line options.
//TODO Better error reporting. (EnumStatus)
public final class GitCommand {

    public static boolean clone(String id, String url) {
        throw new RuntimeException("Not Implemented!");
    }

    public static boolean fetch(String id) {
        throw new RuntimeException("Not Implemented!");
    }

    public static boolean checkout(String id, String branch) {
        throw new RuntimeException("Not Implemented!");
    }

    public static boolean pull(String id, String branch) {
        throw new RuntimeException("Not Implemented!");
    }

    public static boolean needsPull(String id) {
        //TODO This should check for the words "Your branch is behind".

        if(id != null) {
            boolean f = fetch(id); // Do we really care about the return value?

            throw new RuntimeException("Not Implemented!");
        }

        return false;
    }

    //TODO More verbose reason for failing? Or is logging it enough.
    public static String commitHash(String id) {
        if(id != null) {
            FinalTuple<Integer, String> cReturn = ProcessCommand.run("pwsh /c",
                    "git log -1 --pretty=format:\"%H\"", SimpleCupBuilder.BUILD_DIR + id,
                    new String[0], null);

            Log.process("Git Log", cReturn);

            if(cReturn.getX() == 0)
                return cReturn.getY();
        }

        return "NO COMMITS";
    }
}
