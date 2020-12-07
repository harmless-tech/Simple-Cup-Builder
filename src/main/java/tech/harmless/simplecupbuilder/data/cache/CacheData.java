package tech.harmless.simplecupbuilder.data.cache;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

//TODO Allow this to be exported and imported.
class CacheData {

    protected final String id;

    // Build Info
    protected String commitHash;
    protected int buildNumber;
    protected boolean lastBuildSuccess;
    protected List<String> archiveNames;

    public CacheData(@NotNull String id) {
        this.id = id;

        commitHash = "";
        buildNumber = -1;
        lastBuildSuccess = false;
        archiveNames = new ArrayList<>();
    }
}
