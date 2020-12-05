package tech.harmless.simplecupbuilder.cmd;

import tech.harmless.simplecupbuilder.SimpleCupBuilder;
import tech.harmless.simplecupbuilder.git.EnumGit;
import tech.harmless.simplecupbuilder.utils.tuples.FinalTuple;

//TODO Add support for cmd line options.
//TODO Better error reporting.
public final class GitCommand {

    public static EnumGit clone(String id, String url) {
        return EnumGit.NULL;
    }

    public static EnumGit pull(String id) {
        return EnumGit.NULL;
    }

    public static EnumGit status(String id) {
        //TODO Run git status and check for the 'nothing to commit, working tree clean' message.
        return EnumGit.NULL;
    }

    public static String commitHash(String id) { //TODO Null check.
        if(id != null) {
            FinalTuple<Integer, String> cReturn = ProcessCommand.run("pwsh /c",
                    "git log -1 --pretty=format:\"%H\"", SimpleCupBuilder.BUILD_DIR + id,
                    new String[0], null);

            //TODO Output check is kinda redundant.
            if(cReturn.getX() == 0 && !(cReturn.getY().length() > 8 && cReturn.getY().startsWith("fatal:")))
                return cReturn.getY();
        }

        return null;
    }
}
