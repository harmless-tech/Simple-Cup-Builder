package tech.harmless.simplecupbuilder;

import tech.harmless.simplecupbuilder.utils.Log;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Date;

/*
 * Inject build number.
 */

public class SimpleCupBuilder {

    public static final String DATA_DIR = "scb/";
    public static final String CACHE_DIR = "scb_cache/";
    public static final String TMP_DIR = CACHE_DIR + "tmp/";

    public static boolean DEBUG = false;

    public static void main(String[] args) {
        // Before
        System.out.println("Program launch at " + new Date() + ".");

        createDirs();

        // After
        Log.info("Name: " + BuildConfig.NAME + ", Version: " + BuildConfig.VERSION +
                ", Author: " + BuildConfig.AUTHOR_NAME + ", Build Time: " + BuildConfig.BUILD_TIME);

        for(String arg : args) {
            switch(arg) {
                case "--debug" -> {
                    DEBUG = true;
                    Log.info("Enabled debug mode.");
                }
                case "--gui" -> Log.info("GUI not supported yet."); //TODO GUI later.
                case "--nothing" -> Log.info("Nothing happened...");
            }
        }

        //TODO Create required dirs?
        //TODO Search scb directory and search for build config files.

        //TODO Testing.
        try {
            /*Process p1 = Runtime.getRuntime().exec("pwsh /c java", new String[]{}, new File(TMP_DIR));
            Process p2 = p1.onExit().join();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p2.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            String result = builder.toString();

            BufferedReader readerErr = new BufferedReader(new InputStreamReader(p2.getErrorStream()));
            StringBuilder builderErr = new StringBuilder();
            String lineErr = null;
            while((lineErr = reader.readLine()) != null) {
                builder.append(lineErr);
                builder.append(System.getProperty("line.separator"));
            }
            String resultErr = builder.toString();

            System.out.println(result);
            System.out.println(resultErr);
            System.out.println(p2.exitValue());*/

            ProcessBuilder pBuilder = new ProcessBuilder("pwsh", "/c", "rustc", "--version");
            /*pBuilder.environment().forEach((key, value) -> {
                System.out.println(key + "        " + value);
            });*/
            pBuilder.redirectErrorStream(true);
            Process p1 = pBuilder.start();
            p1.onExit().join();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p1.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            String result = builder.toString();

            System.out.println(result);
            System.out.println(p1.exitValue());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        //
    }

    private static void createDirs() {
        new File(DATA_DIR).mkdirs();
        new File(CACHE_DIR).mkdirs();
        new File(TMP_DIR).mkdirs();
    }
}
