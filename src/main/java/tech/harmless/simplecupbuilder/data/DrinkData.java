package tech.harmless.simplecupbuilder.data;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class DrinkData {

    // --- drink_info ---
    @Getter
    @Setter
    private String drinkInfo_name;
    @Getter
    @Setter
    private String drinkInfo_id;
    @Getter
    @Setter
    private String drinkInfo_buildFileVersion;

    // --- git ---
    @Getter
    @Setter
    private String git_url;
    @Getter
    @Setter
    private String git_branch;
    @Getter
    @Setter
    private String git_sshKeyPassword;

    // internal_build_file does not need a variable.

    // --- export ---
    @Getter
    @Setter
    private String[] export_files;

    // --- build_ops ---
    @Getter
    @Setter
    private String buildOps_wrkDir;
    @Getter
    @Setter
    private boolean buildOps_fullCleanBuild;
    @Getter
    @Setter
    private String[] buildOps_CleanBeforeBuild;
    @Getter
    @Setter
    private boolean buildOps_buildWindows;
    @Getter
    @Setter
    private boolean buildOps_buildLinux;
    @Getter
    @Setter
    private boolean buildOps_buildMacos;
    @Getter
    @Setter
    private boolean buildOps_splitBuild;
    @Getter
    @Setter
    private String buildOps_OneOsBuild;

    // --- build ---
    @Getter
    @Setter
    private String[] build_preCheck;
    @Getter
    @Setter
    private String[] build_commands;
    @Getter
    @Setter
    private String[] build_testCommands;

    // --- build_windows ---
    @Getter
    @Setter
    private String[] buildWindows_preCheck;
    @Getter
    @Setter
    private String[] buildWindows_commands;
    @Getter
    @Setter
    private String[] buildWindows_testCommands;

    // --- build_linux ---
    @Getter
    @Setter
    private String[] buildLinux_preCheck;
    @Getter
    @Setter
    private String[] buildLinux_commands;
    @Getter
    @Setter
    private String[] buildLinux_testCommands;

    // --- build_macos ---
    @Getter
    @Setter
    private String[] buildMacos_preCheck;
    @Getter
    @Setter
    private String[] buildMacos_commands;
    @Getter
    @Setter
    private String[] buildMacos_testCommands;

    // --- add_path ---
    @Getter
    @Setter
    private String[] addPath_windows;
    @Getter
    @Setter
    private String[] addPath_linux;
    @Getter
    @Setter
    private String[] addPath_macos;

    // --- add_env ---
    @Getter
    @Setter
    private Map<String, String> envMap;

    // --- alias ---
    @Getter
    @Setter
    private Map<String, String> aliasMap;
}
