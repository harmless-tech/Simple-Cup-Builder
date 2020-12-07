package tech.harmless.simplecupbuilder.cmd;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.harmless.simplecupbuilder.utils.Log;
import tech.harmless.simplecupbuilder.utils.OS;
import tech.harmless.simplecupbuilder.utils.enums.EnumExitCodes;
import tech.harmless.simplecupbuilder.utils.tuples.FinalTuple;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class ProcessCommand {

    private static final FinalTuple<Integer, String> errTuple =
            new FinalTuple<>(Integer.MIN_VALUE, "FATAL - COMMAND FAILED");

    //TODO Better way for cmdLine?
    //TODO Refactor!
    @NotNull
    public static FinalTuple<Integer, String> run(@NotNull String cmdLine, @NotNull String command,
                                                  @NotNull String wrkDir, @NotNull String[] addPath,
                                                  @Nullable Map<String, String> addEnv) {
        File workDir = new File(wrkDir);
        if(!workDir.mkdirs()) //TODO Check exist has well.
            Log.info(wrkDir + " could not be created. Assuming that it already exists.");

        List<String> cmd = new ArrayList<>();
        if(OS.getOs() == OS.EnumOS.WINDOWS) //TODO Add support for other OS shells.
            cmd.addAll(Arrays.asList(cmdLine.split(" ")));
        cmd.addAll(Arrays.asList(command.split(" ")));

        ProcessBuilder builder = new ProcessBuilder(cmd);
        builder.redirectErrorStream(true);
        builder.directory(workDir);
        if(addEnv != null)
            builder.environment().putAll(addEnv);

        String pathName = OS.getPathName();

        StringBuilder pathBuilder = new StringBuilder(builder.environment().get(pathName));
        for(String add : addPath)
            pathBuilder.insert(0, add + File.pathSeparator);
        builder.environment().put(pathName, pathBuilder.toString());

        try {
            Process p = builder.start();
            p.onExit().join();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder strBuilder = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                strBuilder.append(line);
                strBuilder.append(System.getProperty("line.separator"));
            }
            reader.close();

            return new FinalTuple<>(p.exitValue(), strBuilder.toString());
        }
        catch(IOException e) {
            Log.exception(e);
            Log.fatal(EnumExitCodes.PROCESS_START_FAILURE, "Failure when starting a process!");
        }

        return errTuple;
    }
}
