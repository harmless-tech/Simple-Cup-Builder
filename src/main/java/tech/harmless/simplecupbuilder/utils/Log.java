package tech.harmless.simplecupbuilder.utils;

import tech.harmless.simplecupbuilder.SimpleCupBuilder;
import tech.harmless.simplecupbuilder.utils.tuples.FinalTuple;

import java.io.IOException;
import java.io.PrintStream;
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
            outStream = new PrintStream(SimpleCupBuilder.LOG_FILE);
            outStream.println("Out logging init at " + new Date() + ".");
            outStream.flush();

            errStream = new PrintStream(SimpleCupBuilder.LOG_ERR_FILE);
            errStream.println("Err logging init at " + new Date() + ".");
            errStream.flush();
        }
        catch(IOException e) {
            System.err.println("Could not setup loggers!");
            e.printStackTrace();
            System.exit(EnumExitCodes.LOG_SETUP_FAILURE);
        }
    }

    public static void process(String processName, FinalTuple<Integer, String> cmd) {
        if(processName == null || cmd == null)
            return;

        String str = "\nProcess " + processName + " exited with exit code " + cmd.getX() + ".\n" + cmd.getY();
        out("PROCESS", str);
    }

    public static void info(Object message) {
        if(message == null)
            return;

        out("INFO", message);
    }

    public static void warn(Object message) {
        if(message == null)
            return;

        out("WARN", message);
    }

    public static void debug(Object message) {
        if(message == null || !SimpleCupBuilder.DEBUG)
            return;

        out("DEBUG", message);
    }

    public static void trace(Object caller, Object message) {
        if(message == null || !SimpleCupBuilder.DEBUG)
            return;

        out("TRACE", "Message from " + caller + ": " + message);
    }

    public static void error(Object message) {
        if(message == null)
            return;

        err("ERROR", message);
    }

    public static void exception(Exception e) {
        synchronized(syncObj) {
            e.printStackTrace();
            e.printStackTrace(errStream);

            errStream.flush();
        }
    }

    public static void fatal(int code, Object message) {
        err("FATAL", message);

        outStream.close();
        errStream.close();

        System.exit(code);
    }

    public static void print(Object message) {
        if(message == null)
            return;

        synchronized(syncObj) {
            System.out.println(message);

            outStream.println(message);
            outStream.flush();
        }
    }

    public static void printErr(Object message) {
        if(message == null)
            return;

        synchronized(syncObj) {
            System.err.println(message);

            errStream.println(message);
            errStream.flush();
        }
    }

    private static void out(String header, Object message) {
        print(addHeader(header) + message);
    }

    private static void err(String header, Object message) {
        printErr(addHeader(header) + message);
    }

    private static String addHeader(String name) {
        return "[" + dateFormat.format(new Date()) + " / " + name + "]: ";
    }
}
