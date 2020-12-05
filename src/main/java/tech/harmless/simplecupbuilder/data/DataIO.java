package tech.harmless.simplecupbuilder.data;

import com.github.jezza.Toml;
import com.github.jezza.TomlArray;
import com.github.jezza.TomlTable;
import tech.harmless.simplecupbuilder.SimpleCupBuilder;
import tech.harmless.simplecupbuilder.utils.Log;
import tech.harmless.simplecupbuilder.utils.tuples.FinalTuple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public final class DataIO {

    private static final String[] emptyStrArray = new String[0];

    //TODO Cup data does not need to be hashed? But should it?
    public static CupData processCup() {
        try {
            CupData data = new CupData();

            File f = new File(SimpleCupBuilder.CUP_FILE);
            if(f.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(f));
                TomlTable root = Toml.from(br);

                data.options_gitUpdateTimer = (long) root.getOrDefault("options.git_update_timer", 15);
                data.options_parallelBuilds = (boolean) root.getOrDefault("options.parallel_builds", false);
                data.options_drinks =
                        ((TomlArray) root.getOrDefault("options.drinks", emptyStrArray)).toArray(emptyStrArray);
                data.process_windows = (String) root.getOrDefault("process.windows", "pwsh /c");
                data.addPath_windows =
                        ((TomlArray) root.getOrDefault("add_path.windows", emptyStrArray)).toArray(emptyStrArray);
                data.addPath_linux =
                        ((TomlArray) root.getOrDefault("add_path.linux", emptyStrArray)).toArray(emptyStrArray);
                data.addPath_macos =
                        ((TomlArray) root.getOrDefault("add_path.macos", emptyStrArray)).toArray(emptyStrArray);

                //TODO Environment.
                data.envMap = null;
                //TODO Alias.
                data.aliasMap = null;

                return data;
            }
            else
                Log.error("Cup file does not exist.");
        }
        catch(IOException | ClassCastException e) {
            Log.error("Failed to import cup.toml and process it.");
            Log.exception(e);
        }

        return null;
    }

    // Returns drink data and the hash of the file.
    public static FinalTuple<DrinkData, String> processDrink(String path) {
        throw new RuntimeException("Not implemented!");
    }

    //TODO This should be called on the builder thread? Sync.
    public static FinalTuple<DrinkData, String> processInternalDrink(DrinkData drink) {
        //TODO Allow for internal build file processing.
        throw new RuntimeException("Not implemented!");
    }
}
