package tech.harmless.simplecupbuilder.utils;

import tech.harmless.simplecupbuilder.SimpleCupBuilder;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Log {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSSS", Locale.ENGLISH);
    private static PrintStream outStream;
    private static PrintStream errStream;

    static {
        try {
            String dir = SimpleCupBuilder.CACHE_DIR;
            new File(dir).mkdirs();

            outStream = new PrintStream(new File(dir + "scb.log"));
            outStream.println("Out logging init at " + new Date() + ".");
            outStream.flush();

            errStream = new PrintStream(new File(dir + "scb-err.log"));
            errStream.println("Err logging init at " + new Date() + ".");
            errStream.flush();
        }
        catch(IOException e) {
            System.err.println("Could not setup loggers!");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void process(Object message) {
        out("PROCESS", message);
    }

    public static void info(Object message) {
        out("INFO", message);
    }

    public static void warn(Object message) {
        out("WARN", message);
    }

    public static void debug(Object message) {
        if(!SimpleCupBuilder.DEBUG)
            return;

        out("DEBUG", message);
    }

    public static void trace(Object caller, Object message) {
        if(!SimpleCupBuilder.DEBUG)
            return;

        out("TRACE", "Message from " + caller + ": " + message);
    }

    public static void error(Object message) {
        err("ERROR", message);
    }

    public static void exception(Exception e) {
        e.printStackTrace();
        e.printStackTrace(errStream);

        errStream.flush();
    }

    public static void fatal(int code, Object message) {
        err("FATAL", message);

        outStream.close();
        errStream.close();

        System.exit(code);
    }

    public static void print(Object message) {
        System.out.println(message);

        outStream.println(message);
        outStream.flush();
    }

    public static void printErr(Object message) {
        System.err.println(message);

        errStream.println(message);
        errStream.flush();
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
