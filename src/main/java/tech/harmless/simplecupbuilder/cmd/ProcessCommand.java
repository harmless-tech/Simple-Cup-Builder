package tech.harmless.simplecupbuilder.cmd;

import tech.harmless.simplecupbuilder.utils.Log;
import tech.harmless.simplecupbuilder.utils.OS;
import tech.harmless.simplecupbuilder.utils.enums.EnumExitCodes;
import tech.harmless.simplecupbuilder.utils.tuples.FinalTuple;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class ProcessCommand {

    private static final FinalTuple<Integer, String> errTuple =
            new FinalTuple<>(Integer.MIN_VALUE, "FATAL - COMMAND FAILED");

    //TODO Better way for cmdLine?
    //TODO Refactor!
    public static FinalTuple<Integer, String> run(String cmdLine, String command, String wrkDir, String[] addPath,
                                                  Map<String, String> addEnv) {
        if(cmdLine == null || command == null || wrkDir == null)
            return errTuple;

        File workDir = new File(wrkDir);
        workDir.mkdirs();

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
        String pathSep = OS.getPathSep();

        StringBuilder pathBuilder = new StringBuilder(builder.environment().get(pathName));
        for(String add : addPath)
            pathBuilder.insert(0, add + pathSep);
        builder.environment().put(pathName, pathBuilder.toString());

        try {
            Process p = builder.start();
            p.onExit().join();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder strBuilder = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                strBuilder.append(line);
                strBuilder.append(System.getProperty("line.separator"));
            }

            return new FinalTuple<>(p.exitValue(), strBuilder.toString());
        }
        catch(IOException e) {
            Log.exception(e);
            Log.fatal(EnumExitCodes.PROCESS_START_FAILURE, "Failure when starting a process!");
        }

        return errTuple;
    }
}
