package tech.harmless.simplecupbuilder;

import com.github.jezza.Toml;
import com.github.jezza.TomlTable;
import tech.harmless.simplecupbuilder.cmd.CommandReturn;
import tech.harmless.simplecupbuilder.cmd.PCommand;
import tech.harmless.simplecupbuilder.utils.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;

/* TODO
 * Inject build number and other environment args.
 * Delete tmp directory on exit?
 * Add a way to check for new releases.
 *
 * Thread for build manager, builder, wait timer.
 * File metadata for cache file.
 * Multiple cache files.
 * Custom file format and parser (for build files).
 * user interface (console and gui)
 * (op=true) for build command options.
 */
public class SimpleCupBuilder {

    // Top level directories.
    public static final String DATA_DIR = "scb/";
    public static final String INTERNAL_DIR = DATA_DIR + ".scb/";
    public static final String CACHE_DIR = INTERNAL_DIR + "cache/";
    public static final String BUILD_DIR = INTERNAL_DIR + "build/";
    public static final String ARCHIVES_DIR = INTERNAL_DIR + "archives/";
    public static final String TMP_DIR = INTERNAL_DIR + "tmp/";

    // File extensions.
    public static final String CONFIG_FILE_EXT = ".toml";
    public static final String CACHE_FILE_EXT = ".cache";

    // Files.
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
            CommandReturn cReturn =
                    PCommand.run("pwsh /c", "rustc --version", CACHE_DIR, new String[0], new HashMap<>());

            Log.process("\n" + cReturn.getOutput());
            Log.process("Exit Code " + cReturn.getExitCode());

            File f = new File("scb/cup.toml");
            BufferedReader is = new BufferedReader(new FileReader(f));
            TomlTable table = Toml.from(is);
            Log.debug(table);
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
