package tech.harmless.simplecupbuilder.utils.types;

/**
 * A class that holds all of the exit codes that this program can produce.
 */
public final class ExitCodes {
    public static final int OKAY = 0;
    public static final int LOG_SETUP_FAILURE = -1;
    public static final int PROCESS_START_FAILURE = -2;
    public static final int LOCK_SETUP_FAILURE = -3;
    public static final int LOCK_RELEASE_FAILURE = -4;
    public static final int NO_HASH = -5;
}
