package tech.harmless.simplecupbuilder.cups;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class CupData {

    // --- options ---
    @Getter
    @Setter
    private int options_gitUpdateTimer;
    @Getter
    @Setter
    private boolean options_parallelBuilds;

    // --- process ---
    @Getter
    @Setter
    private String process_windows;

    @Getter
    @Setter
    private String[] drinks;

    // --- add_path ---
    @Getter
    @Setter
    private String addPath_windows;
    @Getter
    @Setter
    private String addPath_linux;
    @Getter
    @Setter
    private String addPath_macos;

    // --- add_env ---
    @Getter
    @Setter
    private Map<String, String> envMap;

    // --- alias ---
    @Getter
    @Setter
    private Map<String, String> aliasMap;
}
