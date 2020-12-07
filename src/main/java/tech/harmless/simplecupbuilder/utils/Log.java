package tech.harmless.simplecupbuilder.utils;

import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.harmless.simplecupbuilder.SimpleCupBuilder;
import tech.harmless.simplecupbuilder.utils.enums.EnumExitCodes;
import tech.harmless.simplecupbuilder.utils.tuples.FinalTuple;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Log {

    private static final Object syncObj = new Object();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSSS", Locale.ENGLISH);
    private static PrintStream outStream;
    private static PrintStream errStream;

    static {
        try {
            outStream = new PrintStream(SimpleCupBuilder.LOG_FILE, StandardCharsets.UTF_8);
            outStream.println("Out logging init at " + new Date() + ".");
            outStream.flush();

            errStream = new PrintStream(SimpleCupBuilder.LOG_ERR_FILE, StandardCharsets.UTF_8);
            errStream.println("Err logging init at " + new Date() + ".");
            errStream.flush();
        }
        catch(IOException e) {
            System.err.println("Could not setup loggers!");
            e.printStackTrace();
            System.exit(EnumExitCodes.LOG_SETUP_FAILURE);
        }
    }

    public static void process(@Nullable String processName, @Nullable FinalTuple<Integer, String> cmd) {
        if(processName == null || cmd == null)
            return;

        String str = "Process " + processName + " exited with exit code " + cmd.getX() + ".\n" + cmd.getY();
        out("PROCESS", str);
    }

    public static void info(@Nullable Object message) {
        if(message == null)
            return;

        out("INFO", message);
    }

    public static void warn(@Nullable Object message) {
        if(message == null)
            return;

        out("WARN", message);
    }

    public static void debug(@Nullable Object message) {
        if(message == null || !SimpleCupBuilder.DEBUG)
            return;

        out("DEBUG", message);
    }

    public static void trace(@Nullable Object caller, @Nullable Object message) {
        if(message == null || !SimpleCupBuilder.DEBUG)
            return;

        out("TRACE", "Message from " + caller + ": " + message);
    }

    public static void error(@Nullable Object message) {
        if(message == null)
            return;

        err("ERROR", message);
    }

    public static void exception(@NotNull Exception e) {
        synchronized(syncObj) {
            e.printStackTrace();
            e.printStackTrace(errStream);

            errStream.flush();
        }
    }

    public static void fatal(@MagicConstant(flagsFromClass = EnumExitCodes.class) int code, @NotNull Object message) {
        err("FATAL", message);

        outStream.close();
        errStream.close();

        System.exit(code);
    }

    public static void print(@Nullable Object message) {
        if(message == null)
            return;

        synchronized(syncObj) {
            System.out.println(message);

            outStream.println(message);
            outStream.flush();
        }
    }

    public static void printErr(@Nullable Object message) {
        if(message == null)
            return;

        synchronized(syncObj) {
            System.err.println(message);

            errStream.println(message);
            errStream.flush();
        }
    }

    private static void out(@NotNull String header, @NotNull Object message) {
        print(addHeader(header) + message);
    }

    private static void err(@NotNull String header, @NotNull Object message) {
        printErr(addHeader(header) + message);
    }

    @NotNull
    private static String addHeader(@NotNull String name) {
        synchronized(dateFormat) {
            return "[" + dateFormat.format(new Date()) + " / " + name + "]: ";
        }
    }
}
