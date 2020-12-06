package tech.harmless.simplecupbuilder.data;

import com.github.jezza.Toml;
import com.github.jezza.TomlArray;
import com.github.jezza.TomlTable;
import tech.harmless.simplecupbuilder.SimpleCupBuilder;
import tech.harmless.simplecupbuilder.utils.EmptyTypes;
import tech.harmless.simplecupbuilder.utils.Log;
import tech.harmless.simplecupbuilder.utils.Security;
import tech.harmless.simplecupbuilder.utils.tuples.FinalTuple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

//TODO Less dup.
public final class DataIO {

    @SuppressWarnings("Duplicates")
    public static CupData processCup() {
        try {
            CupData data = new CupData();

            File f = new File(SimpleCupBuilder.CUP_FILE);
            if(f.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(f));
                TomlTable root = Toml.from(br);

                data.options_gitUpdateTimer = (long) root.getOrDefault("options.git_update_timer", 15L);
                data.options_parallelBuilds = (boolean) root.getOrDefault("options.parallel_builds", false);
                data.options_drinks =
                        ((TomlArray) root.getOrDefault("options.drinks", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);

                data.process_windows = (String) root.getOrDefault("process.windows", "pwsh /c");

                data.addPath_windows =
                        ((TomlArray) root.getOrDefault("add_path.windows", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.addPath_linux =
                        ((TomlArray) root.getOrDefault("add_path.linux", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.addPath_macos =
                        ((TomlArray) root.getOrDefault("add_path.macos", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);

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

                data.drinkInfo_name = (String) root.getOrDefault("drink_info.name", EmptyTypes.STRING);
                data.drinkInfo_id = (String) root.getOrDefault("drink_info.id", EmptyTypes.STRING);

                data.git_url = (String) root.getOrDefault("git.url", EmptyTypes.STRING);
                data.git_branch = (String) root.getOrDefault("git.branch", "master");
                data.git_internal_build_file = (String) root.getOrDefault("git.internal_build_file", EmptyTypes.STRING);

                data.archive_format = (String) root.getOrDefault("archive.format", "zip");
                data.archive_limit = (long) root.getOrDefault("archive.limit", 5L);
                data.archive_files =
                        ((TomlArray) root.getOrDefault("archive.files", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);

                data.buildOps_wrkDir = (String) root.getOrDefault("build_ops.wrk_dir", EmptyTypes.STRING);
                data.buildOps_fullCleanBuild = (boolean) root.getOrDefault("build_ops.full_clean_build", false);
                data.buildOps_removeBeforeBuild =
                        ((TomlArray) root.getOrDefault("build_ops.remove_before_build", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.buildOps_splitBuild = (boolean) root.getOrDefault("build_ops.split_build", true);

                data.build_preCheck =
                        ((TomlArray) root.getOrDefault("build.pre_check", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.build_commands =
                        ((TomlArray) root.getOrDefault("build.cmds", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.build_testCommands =
                        ((TomlArray) root.getOrDefault("build.test_cmds", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);

                data.buildWindows_preCheck =
                        ((TomlArray) root.getOrDefault("build_windows.pre_check", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.buildWindows_commands =
                        ((TomlArray) root.getOrDefault("build_windows.cmds", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.buildWindows_testCommands =
                        ((TomlArray) root.getOrDefault("build_windows.test_cmds", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);

                data.buildLinux_preCheck =
                        ((TomlArray) root.getOrDefault("build_linux.pre_check", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.buildLinux_commands =
                        ((TomlArray) root.getOrDefault("build_linux.cmds", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.buildLinux_testCommands =
                        ((TomlArray) root.getOrDefault("build_linux.test_cmds", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);

                data.buildMacOS_preCheck =
                        ((TomlArray) root.getOrDefault("build_macos.pre_check", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.buildMacOS_commands =
                        ((TomlArray) root.getOrDefault("build_macos.cmds", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.buildMacOS_testCommands =
                        ((TomlArray) root.getOrDefault("build_macos.test_cmds", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);

                data.addPath_windows =
                        ((TomlArray) root.getOrDefault("add_path.windows", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.addPath_linux =
                        ((TomlArray) root.getOrDefault("add_path.linux", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.addPath_macos =
                        ((TomlArray) root.getOrDefault("add_path.macos", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);

                //TODO Environment.
                data.envMap = null;
                //TODO Alias.
                data.aliasMap = null;

                // Hash
                br = new BufferedReader(new FileReader(f));
                StringBuilder sb = new StringBuilder();
                String line;

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

    @SuppressWarnings("Duplicates")
    public static FinalTuple<DrinkData, String> processInternalDrink(DrinkData drink) {
        try {
            DrinkData data = new DrinkData();

            File f = new File(
                    SimpleCupBuilder.BUILD_DIR + drink.getGit_internal_build_file() + SimpleCupBuilder.CONFIG_FILE_EXT);
            if(f.exists()) {
                // Drink Data
                BufferedReader br = new BufferedReader(new FileReader(f));
                TomlTable root = Toml.from(br);

                data.archive_format = (String) root.getOrDefault("archive.format", "zip");
                data.archive_limit = (long) root.getOrDefault("archive.limit", 5L);
                data.archive_files =
                        ((TomlArray) root.getOrDefault("archive.files", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);

                data.buildOps_wrkDir = (String) root.getOrDefault("build_ops.wrk_dir", EmptyTypes.STRING);
                data.buildOps_fullCleanBuild = (boolean) root.getOrDefault("build_ops.full_clean_build", false);
                data.buildOps_removeBeforeBuild =
                        ((TomlArray) root.getOrDefault("build_ops.remove_before_build", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.buildOps_splitBuild = (boolean) root.getOrDefault("build_ops.split_build", true);

                data.build_preCheck =
                        ((TomlArray) root.getOrDefault("build.pre_check", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.build_commands =
                        ((TomlArray) root.getOrDefault("build.cmds", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.build_testCommands =
                        ((TomlArray) root.getOrDefault("build.test_cmds", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);

                data.buildWindows_preCheck =
                        ((TomlArray) root.getOrDefault("build_windows.pre_check", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.buildWindows_commands =
                        ((TomlArray) root.getOrDefault("build_windows.cmds", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.buildWindows_testCommands =
                        ((TomlArray) root.getOrDefault("build_windows.test_cmds", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);

                data.buildLinux_preCheck =
                        ((TomlArray) root.getOrDefault("build_linux.pre_check", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.buildLinux_commands =
                        ((TomlArray) root.getOrDefault("build_linux.cmds", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.buildLinux_testCommands =
                        ((TomlArray) root.getOrDefault("build_linux.test_cmds", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);

                data.buildMacOS_preCheck =
                        ((TomlArray) root.getOrDefault("build_macos.pre_check", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.buildMacOS_commands =
                        ((TomlArray) root.getOrDefault("build_macos.cmds", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.buildMacOS_testCommands =
                        ((TomlArray) root.getOrDefault("build_macos.test_cmds", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);

                data.addPath_windows =
                        ((TomlArray) root.getOrDefault("add_path.windows", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.addPath_linux =
                        ((TomlArray) root.getOrDefault("add_path.linux", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);
                data.addPath_macos =
                        ((TomlArray) root.getOrDefault("add_path.macos", EmptyTypes.TOML_ARRAY))
                                .toArray(EmptyTypes.STRING_ARRAY);

                //TODO Environment.
                data.envMap = null;
                //TODO Alias.
                data.aliasMap = null;

                // Hash
                br = new BufferedReader(new FileReader(f));
                StringBuilder sb = new StringBuilder();
                String line;

                while((line = br.readLine()) != null)
                    sb.append(line);

                String hash = Security.unsecureSha512(sb.toString());

                // Move data vars to drink.
                drink.archive_format = data.archive_format;
                drink.archive_limit = data.archive_limit;
                drink.archive_files = data.archive_files;

                drink.buildOps_wrkDir = data.buildOps_wrkDir;
                drink.buildOps_fullCleanBuild = data.buildOps_fullCleanBuild;
                drink.buildOps_removeBeforeBuild = data.buildOps_removeBeforeBuild;
                drink.buildOps_splitBuild = data.buildOps_splitBuild;

                drink.build_preCheck = data.build_preCheck;
                drink.build_commands = data.build_commands;
                drink.build_testCommands = data.build_testCommands;

                drink.buildWindows_preCheck = data.buildWindows_preCheck;
                drink.buildWindows_commands = data.buildWindows_commands;
                drink.buildWindows_testCommands = data.buildWindows_testCommands;

                drink.buildLinux_preCheck = data.buildLinux_preCheck;
                drink.buildLinux_commands = data.buildLinux_commands;
                drink.buildLinux_testCommands = data.buildLinux_testCommands;

                drink.buildMacOS_preCheck = data.buildMacOS_preCheck;
                drink.buildMacOS_commands = data.buildMacOS_commands;
                drink.buildMacOS_testCommands = data.buildMacOS_testCommands;

                drink.addPath_windows = data.addPath_windows;
                drink.addPath_linux = data.addPath_linux;
                drink.addPath_macos = data.addPath_macos;

                //TODO Environment.
                drink.envMap = null;
                //TODO Alias.
                data.aliasMap = null;

                return new FinalTuple<>(drink, hash);
            }
            else
                Log.error("Drink file" + drink.getGit_internal_build_file() + "does not exist.");
        }
        catch(IOException | ClassCastException e) {
            Log.error("Failed to import internal drink file " + drink.getGit_internal_build_file() + " and process it.");
            Log.exception(e);
        }

        return null;
    }
}
