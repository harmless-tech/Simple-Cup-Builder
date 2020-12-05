package tech.harmless.simplecupbuilder.cmd;

import tech.harmless.simplecupbuilder.SimpleCupBuilder;
import tech.harmless.simplecupbuilder.utils.Log;
import tech.harmless.simplecupbuilder.utils.tuples.FinalTuple;

//TODO Add support for cmd line options.
//TODO Better error reporting. (EnumStatus)
public final class GitCommand {

    public static void clone(String id, String url) {
    }

    public static void pull(String id) {
    }

    public static void status(String id) {
        //TODO Run git status and check for the 'nothing to commit, working tree clean' message.
    }

    //TODO More verbose reason for failing.
    public static String commitHash(String id) {
        if(id != null) {
            FinalTuple<Integer, String> cReturn = ProcessCommand.run("pwsh /c",
                    "git log -1 --pretty=format:\"%H\"", SimpleCupBuilder.BUILD_DIR + id,
                    new String[0], null);

            Log.process("Git Log", cReturn);

            if(cReturn.getX() == 0)
                return cReturn.getY();
        }

        return null;
    }
}
