package tech.harmless.simplecupbuilder.cmd;

import org.jetbrains.annotations.NotNull;
import tech.harmless.simplecupbuilder.SimpleCupBuilder;
import tech.harmless.simplecupbuilder.utils.EmptyTypes;
import tech.harmless.simplecupbuilder.utils.Log;
import tech.harmless.simplecupbuilder.utils.tuples.FinalTuple;

//TODO Better error reporting? (EnumStatus)
//TODO Make things less repetitive.
public final class GitCommand {

    public static boolean clone(@NotNull String id, @NotNull String url) {
        FinalTuple<Integer, String> cReturn = ProcessCommand.run("pwsh /c",
                "git clone --recursive " + url + " " + id, SimpleCupBuilder.BUILD_DIR,
                EmptyTypes.STRING_ARRAY, null);

        Log.process("Git Clone", cReturn);

        return cReturn.getX() == 0;

    }

    public static boolean checkout(@NotNull String id, @NotNull String branch) {
        FinalTuple<Integer, String> cReturn = ProcessCommand.run("pwsh /c",
                "git checkout " + branch, SimpleCupBuilder.BUILD_DIR + id,
                EmptyTypes.STRING_ARRAY, null);

        Log.process("Git Checkout", cReturn);

        return cReturn.getX() == 0;

    }

    public static boolean submoduleInit(@NotNull String id) {
        FinalTuple<Integer, String> cReturn = ProcessCommand.run("pwsh /c",
                "git submodule update --init --recursive", SimpleCupBuilder.BUILD_DIR + id,
                EmptyTypes.STRING_ARRAY, null);

        Log.process("Git Submodule", cReturn);

        return cReturn.getX() == 0;

    }

    public static boolean fetch(@NotNull String id) {
        FinalTuple<Integer, String> cReturn = ProcessCommand.run("pwsh /c",
                "git fetch", SimpleCupBuilder.BUILD_DIR + id,
                EmptyTypes.STRING_ARRAY, null);

        Log.process("Git Fetch", cReturn);

        return cReturn.getX() == 0;

    }

    public static boolean pull(@NotNull String id, @NotNull String branch) {
        FinalTuple<Integer, String> cReturn = ProcessCommand.run("pwsh /c",
                "git pull origin " + branch, SimpleCupBuilder.BUILD_DIR + id,
                EmptyTypes.STRING_ARRAY, null);

        Log.process("Git Pull", cReturn);

        return cReturn.getX() == 0;

    }

    // If status fails then there is no git repo.
    public static boolean status(@NotNull String id) {
        FinalTuple<Integer, String> cReturn = ProcessCommand.run("pwsh /c",
                "git status", SimpleCupBuilder.BUILD_DIR + id,
                EmptyTypes.STRING_ARRAY, null);

        Log.process("Git Status", cReturn);

        return cReturn.getX() == 0;

    }

    @NotNull
    public static String commitHash(@NotNull String id) {
        FinalTuple<Integer, String> cReturn = ProcessCommand.run("pwsh /c",
                "git log -1 --pretty=format:\"%H\"", SimpleCupBuilder.BUILD_DIR + id,
                EmptyTypes.STRING_ARRAY, null);

        Log.process("Git Log", cReturn);

        if(cReturn.getX() == 0)
            return cReturn.getY();

        return EmptyTypes.STRING;
    }
}
