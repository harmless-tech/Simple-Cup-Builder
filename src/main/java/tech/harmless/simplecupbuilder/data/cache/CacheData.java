package tech.harmless.simplecupbuilder.data.cache;

import java.util.ArrayList;
import java.util.List;

//TODO Allow this to be exported and imported.
class CacheData {

    protected final String id;

    // General Info
    protected String name;
    protected String fileHash;
    protected String iFileHash;

    // Build Info
    protected String commitHash;
    protected int buildNumber;
    protected boolean lastBuildSuccess;
    protected List<String> archiveNames;

    public CacheData(String id) {
        this.id = id;

        this.name = "";
        this.fileHash = "";
        this.iFileHash = "";

        commitHash = "";
        buildNumber = -1;
        lastBuildSuccess = false;
        archiveNames = new ArrayList<>();
    }
}
