package tech.harmless.simplecupbuilder;

import tech.harmless.simplecupbuilder.cmd.Command;
import tech.harmless.simplecupbuilder.git.GitCommands;
import tech.harmless.simplecupbuilder.utils.Log;
import tech.harmless.simplecupbuilder.utils.tuples.FinalTuple;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

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

        //TODO Testing.
        try {
            FinalTuple<Integer, String> cReturn =
                    Command.run("pwsh /c", "rustc --version", CACHE_DIR, new String[0], new HashMap<>());

            Log.process("\n" + cReturn.getY());
            Log.process("Exit Code " + cReturn.getX());

            cReturn = Command.run("pwsh /c", "git status", CACHE_DIR, new String[0], new HashMap<>());

            Log.process("\n" + cReturn.getY());
            Log.process("Exit Code " + cReturn.getX());

            /*File f = new File(CUP_FILE);
            BufferedReader is = new BufferedReader(new FileReader(f));
            TomlTable table = Toml.from(is);
            Log.debug(table);*/

            GitCommands.commitHash("l");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        //
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
