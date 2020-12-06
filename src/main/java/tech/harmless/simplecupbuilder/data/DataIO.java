package tech.harmless.simplecupbuilder.data;

import com.github.jezza.Toml;
import com.github.jezza.TomlArray;
import com.github.jezza.TomlTable;
import tech.harmless.simplecupbuilder.SimpleCupBuilder;
import tech.harmless.simplecupbuilder.utils.Log;
import tech.harmless.simplecupbuilder.utils.Security;
import tech.harmless.simplecupbuilder.utils.tuples.FinalTuple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public final class DataIO {

    private static final String emptyStr = "";
    private static final String[] emptyStrArray = new String[0];

    //TODO Cup data does not need to be hashed? But should it?
    @SuppressWarnings("Duplicates")
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
            Log.error("Failed to import " + SimpleCupBuilder.CUP_FILE + " and process it.");
            Log.exception(e);
        }

        return null;
    }

    // Returns drink data and the hash of the file.
    @SuppressWarnings("Duplicates")
    public static FinalTuple<DrinkData, String> processDrink(String namePath) {
        try {
            DrinkData data = new DrinkData();

            File f = new File(SimpleCupBuilder.DATA_DIR + namePath + SimpleCupBuilder.CONFIG_FILE_EXT);
            if(f.exists()) {
                // Drink Data
                BufferedReader br = new BufferedReader(new FileReader(f));
                TomlTable root = Toml.from(br);

                data.drinkInfo_name = (String) root.getOrDefault("drink_info.name", emptyStr);
                data.drinkInfo_id = (String) root.getOrDefault("drink_info.id", emptyStr);

                data.git_url = (String) root.getOrDefault("git.url", emptyStr);
                data.git_branch = (String) root.getOrDefault("git.branch", "master");
                data.git_sshKeyPassword = (String) root.getOrDefault("git.ssh_key_password", emptyStr);
                data.git_internal_build_file = (String) root.getOrDefault("git.internal_build_file", emptyStr);

                data.archive_format = (String) root.getOrDefault("archive.format", "zip");
                data.archive_limit = (long) root.getOrDefault("archive.limit", 5);
                data.archive_files =
                        ((TomlArray) root.getOrDefault("archive.files", emptyStrArray)).toArray(emptyStrArray);

                data.buildOps_wrkDir = (String) root.getOrDefault("build_ops.wrk_dir", emptyStr);
                data.buildOps_fullCleanBuild = (boolean) root.getOrDefault("build_ops.full_clean_build", false);
                data.buildOps_removeBeforeBuild =
                        ((TomlArray) root.getOrDefault("build_ops.remove_before_build", emptyStrArray))
                                .toArray(emptyStrArray);
                data.buildOps_splitBuild = (boolean) root.getOrDefault("build_ops.split_build", true);

                data.build_preCheck =
                        ((TomlArray) root.getOrDefault("build.pre_check", emptyStrArray)).toArray(emptyStrArray);
                data.build_commands =
                        ((TomlArray) root.getOrDefault("build.cmds", emptyStrArray)).toArray(emptyStrArray);
                data.build_testCommands =
                        ((TomlArray) root.getOrDefault("build.test_cmds", emptyStrArray)).toArray(emptyStrArray);

                data.buildWindows_preCheck = ((TomlArray) root.getOrDefault("build_windows.pre_check", emptyStrArray))
                        .toArray(emptyStrArray);
                data.buildWindows_commands =
                        ((TomlArray) root.getOrDefault("build_windows.cmds", emptyStrArray)).toArray(emptyStrArray);
                data.buildWindows_testCommands =
                        ((TomlArray) root.getOrDefault("build_windows.test_cmds", emptyStrArray))
                                .toArray(emptyStrArray);

                data.buildLinux_preCheck =
                        ((TomlArray) root.getOrDefault("build_linux.pre_check", emptyStrArray)).toArray(emptyStrArray);
                data.buildLinux_commands =
                        ((TomlArray) root.getOrDefault("build_linux.cmds", emptyStrArray)).toArray(emptyStrArray);
                data.buildLinux_testCommands =
                        ((TomlArray) root.getOrDefault("build_linux.test_cmds", emptyStrArray)).toArray(emptyStrArray);

                data.buildMacOS_preCheck =
                        ((TomlArray) root.getOrDefault("build_macos.pre_check", emptyStrArray)).toArray(emptyStrArray);
                data.buildMacOS_commands =
                        ((TomlArray) root.getOrDefault("build_macos.cmds", emptyStrArray)).toArray(emptyStrArray);
                data.buildMacOS_testCommands =
                        ((TomlArray) root.getOrDefault("build_macos.test_cmds", emptyStrArray)).toArray(emptyStrArray);

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

                // Hash
                StringBuilder sb = new StringBuilder();
                String line;

                br.reset();
                while((line = br.readLine()) != null)
                    sb.append(line);

                String hash = Security.unsecureSha512(sb.toString());

                return new FinalTuple<>(data, hash);
            }
            else
                Log.error("Drink file" + namePath + "does not exist.");
        }
        catch(IOException | ClassCastException e) {
            Log.error("Failed to import drink file " + namePath + " and process it.");
            Log.exception(e);
        }

        return null;
    }

    //TODO This should be called on the builder thread? Sync.
    public static FinalTuple<DrinkData, String> processInternalDrink(DrinkData drink) {
        //TODO Allow for internal build file processing.
        throw new RuntimeException("Not implemented!");
    }
}
