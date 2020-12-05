package tech.harmless.simplecupbuilder.data;

import lombok.Getter;

import java.util.Map;

public class DrinkData {

    // --- drink_info ---
    @Getter
    protected String drinkInfo_name;
    @Getter
    protected String drinkInfo_id;
    @Getter
    protected String drinkInfo_buildFileVersion;

    // --- git ---
    @Getter
    protected String git_url;
    @Getter
    protected String git_branch;
    @Getter
    protected String git_sshKeyPassword;

    // internal_build_file does not need a variable.

    // --- export ---
    @Getter
    protected String[] export_files;

    // --- build_ops ---
    @Getter
    protected String buildOps_wrkDir;
    @Getter
    protected boolean buildOps_fullCleanBuild;
    @Getter
    protected String[] buildOps_CleanBeforeBuild;
    @Getter
    protected boolean buildOps_buildWindows;
    @Getter
    protected boolean buildOps_buildLinux;
    @Getter
    protected boolean buildOps_buildMacos;
    @Getter
    protected boolean buildOps_splitBuild;
    @Getter
    protected String buildOps_OneOsBuild;

    // --- build ---
    @Getter
    protected String[] build_preCheck;
    @Getter
    protected String[] build_commands;
    @Getter
    protected String[] build_testCommands;

    // --- build_windows ---
    @Getter
    protected String[] buildWindows_preCheck;
    @Getter
    protected String[] buildWindows_commands;
    @Getter
    protected String[] buildWindows_testCommands;

    // --- build_linux ---
    @Getter
    protected String[] buildLinux_preCheck;
    @Getter
    protected String[] buildLinux_commands;
    @Getter
    protected String[] buildLinux_testCommands;

    // --- build_macos ---
    @Getter
    protected String[] buildMacos_preCheck;
    @Getter
    protected String[] buildMacos_commands;
    @Getter
    protected String[] buildMacos_testCommands;

    // --- add_path ---
    @Getter
    protected String[] addPath_windows;
    @Getter
    protected String[] addPath_linux;
    @Getter
    protected String[] addPath_macos;

    // --- add_env ---
    @Getter
    protected Map<String, String> envMap;

    // --- alias ---
    @Getter
    protected Map<String, String> aliasMap;
}
