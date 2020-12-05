package tech.harmless.simplecupbuilder.data;

import lombok.Getter;

import java.util.Map;

public class CupData {

    // --- options ---
    @Getter
    protected int options_gitUpdateTimer;
    @Getter
    protected boolean options_parallelBuilds;

    // --- process ---
    @Getter
    protected String process_windows;
    @Getter
    protected String[] drinks;

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
