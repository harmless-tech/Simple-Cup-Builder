package tech.harmless.simplecupbuilder.cmd;

import lombok.Getter;

public class CommandReturn {

    @Getter
    private final String output;
    @Getter
    private final int exitCode;

    public CommandReturn(String output, int exitCode) {
        this.output = output;
        this.exitCode = exitCode;
    }
}
