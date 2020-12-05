package tech.harmless.simplecupbuilder.git;

import tech.harmless.simplecupbuilder.SimpleCupBuilder;
import tech.harmless.simplecupbuilder.cmd.CommandReturn;
import tech.harmless.simplecupbuilder.cmd.PCommand;

//TODO Add support for cmd line options.
//TODO Better error reporting.
public final class GitCommands {

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
        CommandReturn cReturn = PCommand.run("pwsh /c", "git log -1 --pretty=format:\"%H\"",
                SimpleCupBuilder.BUILD_DIR + id, new String[0], null);

        //TODO Output check is kinda redundant.
        if(cReturn.getExitCode() != 0 || (cReturn.getOutput().length() > 8 && cReturn.getOutput().startsWith("fatal:")))
            return null;

        return cReturn.getOutput();
    }
}
