package tech.harmless.simplecupbuilder;

import tech.harmless.simplecupbuilder.build.BuildManager;
import tech.harmless.simplecupbuilder.cmd.GitCommand;
import tech.harmless.simplecupbuilder.utils.Log;

import java.io.File;
import java.util.Date;

/* TODO
 * Inject build number and other environment args.
 * Delete tmp directory on exit?
 * Add a way to check for new releases.
 *
 * Thread for build manager, builder, wait timer.
 * File metadata for cache file. (Better yet just have a cache file with a string printed to it to keep track of file "metadata")
 * Multiple cache files.
 * Custom file format and parser (for build files).
 * user interface (console and gui)
 * (op=true) for build command options.
 * Have a web api using micronaut. (Maybe a frontend too)
 * Add null safety checks.
 */
public class SimpleCupBuilder {

    // Top level directories.
    //TODO Switch these to File(PATH)?
    public static final String DATA_DIR = "scb/";
    public static final String INTERNAL_DIR = DATA_DIR + ".scb/";
    public static final String CACHE_DIR = INTERNAL_DIR + "cache/";
    public static final String BUILD_DIR = INTERNAL_DIR + "builds/";
    public static final String ARCHIVE_DIR = INTERNAL_DIR + "archives/";
    public static final String TMP_DIR = INTERNAL_DIR + "tmp/";

    // File extensions.
    public static final String CONFIG_FILE_EXT = ".toml";
    public static final String CACHE_FILE_EXT = ".cache";
    public static final String LOG_FILE_EXT = ".log";

    // File prefixes.

    // Files.
    public static final String LOG_FILE = INTERNAL_DIR + "scb" + LOG_FILE_EXT;
    public static final String LOG_ERR_FILE = INTERNAL_DIR + "scb-err" + LOG_FILE_EXT;
    public static final String CUP_FILE = DATA_DIR + "cup" + CONFIG_FILE_EXT;

    // Dynamic options.
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

        // Setup Processes
        BuildManager buildManager = new BuildManager();
        // CONSOLE

        ThreadGroup threadGroup = new ThreadGroup("Main Processes");

        Thread buildManagerThread = new Thread(threadGroup, buildManager, "Build Manager Thread");
        Thread consoleThread = new Thread(threadGroup, null, "Console Thread");

        buildManagerThread.start();
        consoleThread.start();

        //TODO Testing.
        try {
            //FinalTuple<Integer, String> cReturn =
                   //ProcessCommand.run("pwsh /c", "rustc --version", CACHE_DIR, new String[0], new HashMap<>());

            //Log.process("\n" + cReturn.getY());
            //Log.process("Exit Code " + cReturn.getX());

            //cReturn = ProcessCommand.run("pwsh /c", "git status", CACHE_DIR, new String[0], new HashMap<>());

            //Log.process("\n" + cReturn.getY());
            //Log.process("Exit Code " + cReturn.getX());

            /*File f = new File(CUP_FILE);
            BufferedReader is = new BufferedReader(new FileReader(f));
            TomlTable table = Toml.from(is);
            Log.debug(table);*/

            GitCommand.commitHash("l");
            GitCommand.commitHash("dddp");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        //

        //TODO Start BuildManager Thread.
        //TODO Start Console input Thread.

        // End
        try {
            buildManagerThread.join();
            consoleThread.join();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }

        Log.info(BuildConfig.NAME + " closing...");
    }

    private static void createDirs() {
        new File(DATA_DIR).mkdirs();
        new File(INTERNAL_DIR).mkdirs();
        new File(CACHE_DIR).mkdirs();
        new File(BUILD_DIR).mkdirs();
        new File(ARCHIVE_DIR).mkdirs();
        new File(TMP_DIR).mkdirs();
    }
}
