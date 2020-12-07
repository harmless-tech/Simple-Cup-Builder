package tech.harmless.simplecupbuilder;

import edu.umd.cs.findbugs.annotations.CheckReturnValue;
import tech.harmless.simplecupbuilder.build.BuildManager;
import tech.harmless.simplecupbuilder.utils.Log;
import tech.harmless.simplecupbuilder.utils.enums.EnumExitCodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Date;

/* TODO
 * Inject build number and other environment args.
 * Delete tmp directory on exit?
 * Add a way to check for new releases.
 *
 * jPackage support. (Would be nice)
 * File metadata for cache file. (Better yet just have a cache file with a string printed to it to keep track of file "metadata")
 * Multiple cache files.
 * Custom file format and parser (for build files). (Maybe switch to ini instead.)
 * user interface (console and gui)
 * (op=true) for build command options.
 * Have a web api using micronaut. (Maybe a frontend too)
 * Add null safety checks.
 * Javadocs! and comments!
 * Use mongodb to store data and build logs.
 * Custom jar api to allow for jars to be dynamically loaded for special build processes.
 */
public class SimpleCupBuilder {

    // Top level directories.
    public static final String DATA_DIR = "scb/";
    public static final String INTERNAL_DIR = DATA_DIR + ".scb/";
    public static final String CACHE_DIR = INTERNAL_DIR + "cache/";
    public static final String BUILD_DIR = INTERNAL_DIR + "builds/";
    public static final String ARCHIVE_DIR = INTERNAL_DIR + "archives/";
    public static final String TMP_DIR = INTERNAL_DIR + "tmp/";

    // File extensions.
    public static final String CONFIG_FILE_EXT = ".toml";
    public static final String LOG_FILE_EXT = ".log";
    public static final String CACHE_FILE_EXT = ".cache";
    public static final String META_FILE_EXT = ".metadata"; // Used to keep track of cache info. (Cache version)
    public static final String HASH_FILE_EXT = ".hash";
    public static final String COMPRESSED_FILE_EXT = ".zip";
    public static final String INSTANCE_LOCK_FILE_EXT = ".instance_lock";

    // File prefixes.
    public static final String ARCHIVE_FILE_PREFIX = "archive-";

    // Files.
    public static final String INSTANCE_LOCK_FILE = TMP_DIR + INSTANCE_LOCK_FILE_EXT;
    public static final String LOG_FILE = INTERNAL_DIR + "scb" + LOG_FILE_EXT;
    public static final String LOG_ERR_FILE = INTERNAL_DIR + "scb-err" + LOG_FILE_EXT;
    public static final String CUP_FILE = DATA_DIR + "cup" + CONFIG_FILE_EXT;

    // Dynamic options.
    public static boolean DEBUG = false;

    //TODO Move some things to their own methods.
    public static void main(String[] args) {
        // Before
        System.out.println("Program launch at " + new Date() + ".");

        createDirs();

        // Lock Check
        FileOutputStream instanceLock = null;
        FileLock lock = null;
        try {
            instanceLock = new FileOutputStream(INSTANCE_LOCK_FILE);
            FileChannel fc = instanceLock.getChannel();
            lock = fc.tryLock();

            if(lock == null) {
                System.err.println("Could not create instance lock! Currently held by another program!");
                System.exit(EnumExitCodes.LOCK_SETUP_FAILURE);
            }
        }
        catch(IOException e) {
            System.err.println("Could not create instance lock!");
            e.printStackTrace();
            System.exit(EnumExitCodes.LOCK_SETUP_FAILURE);
        }

        // After
        Log.info("Name: " + BuildConfig.NAME + ", Version: " + BuildConfig.VERSION +
                ", Author: " + BuildConfig.AUTHOR_NAME + ", Build Time: " + BuildConfig.BUILD_TIME);

        //TODO Move this to own method?
        for(String arg : args) {
            switch(arg) {
                case "--debug" -> {
                    DEBUG = true;
                    Log.info("Enabled debug mode.");
                }
                case "--gui" -> Log.info("GUI not supported yet."); //TODO GUI later.
                case "--nothing" -> Log.info("Nothing happened...");
                default -> Log.info("No args.");
            }
        }

        // Setup Processes
        BuildManager buildManager = new BuildManager();
        // CONSOLE

        Thread buildManagerThread = new Thread(null, buildManager, "Build Manager Thread");
        Thread consoleThread = new Thread(null, null, "Console Thread");

        buildManagerThread.start();
        consoleThread.start(); //TODO Allow user input.

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

            //GitCommand.commitHash("l");
            //GitCommand.commitHash("dddp");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        //

        // End
        try {
            buildManagerThread.join();
            consoleThread.join(); //TODO This thread should end when the buildManager one does.
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }

        Log.info(BuildConfig.NAME + " closing...");

        try {
            lock.close();
            instanceLock.close();
        }
        catch(IOException e) {
            System.err.println("Could not release instance lock!");
            e.printStackTrace();
            System.exit(EnumExitCodes.LOCK_RELEASE_FAILURE);
        }
    }

    private static void createDirs() {
        boolean ignored = new File(DATA_DIR).mkdirs();
        ignored = new File(INTERNAL_DIR).mkdirs();
        ignored = new File(CACHE_DIR).mkdirs();
        ignored = new File(BUILD_DIR).mkdirs();
        ignored = new File(ARCHIVE_DIR).mkdirs();
        ignored = new File(TMP_DIR).mkdirs();
    }
}
