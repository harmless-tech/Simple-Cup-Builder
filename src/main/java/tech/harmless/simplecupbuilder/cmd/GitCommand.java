package tech.harmless.simplecupbuilder.cmd;

import tech.harmless.simplecupbuilder.SimpleCupBuilder;
import tech.harmless.simplecupbuilder.utils.EmptyTypes;
import tech.harmless.simplecupbuilder.utils.Log;
import tech.harmless.simplecupbuilder.utils.tuples.FinalTuple;

//TODO Better error reporting? (EnumStatus)
//TODO Make things less repetitive.
public final class GitCommand {

    public static boolean clone(String id, String url) {
        if(id != null && url != null) {
            FinalTuple<Integer, String> cReturn = ProcessCommand.run("pwsh /c",
                    "git clone --recursive " + url + " " + id, SimpleCupBuilder.BUILD_DIR,
                    EmptyTypes.STRING_ARRAY, null);

            Log.process("Git Clone", cReturn);

            return cReturn.getX() == 0;
        }

        return false;
    }

    public static boolean fetch(String id) {
        if(id != null) {
            FinalTuple<Integer, String> cReturn = ProcessCommand.run("pwsh /c",
                    "git fetch", SimpleCupBuilder.BUILD_DIR + id,
                    EmptyTypes.STRING_ARRAY, null);

            Log.process("Git Clone", cReturn);

            return cReturn.getX() == 0;
        }

        return false;
    }

    public static boolean checkout(String id, String branch) {
        if(id != null && branch != null) {
            FinalTuple<Integer, String> cReturn = ProcessCommand.run("pwsh /c",
                    "git checkout " + branch,  SimpleCupBuilder.BUILD_DIR + id,
                    EmptyTypes.STRING_ARRAY, null);

            Log.process("Git Checkout", cReturn);

            return cReturn.getX() == 0;
        }

        return false;
    }

    public static boolean pull(String id, String branch) {
        if(id != null && branch != null) {
            FinalTuple<Integer, String> cReturn = ProcessCommand.run("pwsh /c",
                    "git pull origin " + branch,  SimpleCupBuilder.BUILD_DIR + id,
                    EmptyTypes.STRING_ARRAY, null);

            Log.process("Git Pull", cReturn);

            return cReturn.getX() == 0;
        }

        return false;
    }

    // If status fails then there is no git repo.
    public static boolean status(String id) {
        if(id != null) {
            FinalTuple<Integer, String> cReturn = ProcessCommand.run("pwsh /c",
                    "git status", SimpleCupBuilder.BUILD_DIR + id,
                    EmptyTypes.STRING_ARRAY, null);

            Log.process("Git Status", cReturn);

            return cReturn.getX() == 0;
        }

        return false;
    }
    
    public static String commitHash(String id) {
        if(id != null) {
            FinalTuple<Integer, String> cReturn = ProcessCommand.run("pwsh /c",
                    "git log -1 --pretty=format:\"%H\"", SimpleCupBuilder.BUILD_DIR + id,
                    EmptyTypes.STRING_ARRAY, null);

            Log.process("Git Log", cReturn);

            if(cReturn.getX() == 0)
                return cReturn.getY();
        }

        return EmptyTypes.STRING;
    }
}
