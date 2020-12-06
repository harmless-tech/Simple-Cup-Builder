package tech.harmless.simplecupbuilder.data;

import lombok.Getter;

import java.util.Map;

public class DrinkData {

    // --- drink_info ---
    @Getter
    protected String drinkInfo_name;
    @Getter
    protected String drinkInfo_id;

    // --- git ---
    @Getter
    protected String git_url;
    @Getter
    protected String git_branch;
    /**
     * This option may not be implemented.
     * This option is considered unsecure and unsupported.
     */
    @Getter
    @Deprecated(since = "0.0.1-ALPHA", forRemoval = true)
    protected String git_sshKeyPassword;
    @Getter
    protected String git_internal_build_file;

    // --- archive ---
    @Getter
    protected String archive_format;
    @Getter
    protected long archive_limit;
    @Getter
    protected String[] archive_files;

    // --- build_ops ---
    @Getter
    protected String buildOps_wrkDir;
    @Getter
    protected boolean buildOps_fullCleanBuild;
    @Getter
    protected String[] buildOps_removeBeforeBuild;
    @Getter
    protected boolean buildOps_splitBuild;

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
    protected String[] buildMacOS_preCheck;
    @Getter
    protected String[] buildMacOS_commands;
    @Getter
    protected String[] buildMacOS_testCommands;

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
