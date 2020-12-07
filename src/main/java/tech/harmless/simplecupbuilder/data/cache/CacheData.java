package tech.harmless.simplecupbuilder.data.cache;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

//TODO Allow this to be exported and imported.
class CacheData {

    protected final String id;

    // General Info
    protected String name;
    protected String fileHash; //TODO Is this even needed? maybe use to update while the program is running?
    //protected String iFileHash; //TODO Allow for internal files to be hashed.

    // Build Info
    protected String commitHash;
    protected int buildNumber;
    protected boolean lastBuildSuccess;
    protected List<String> archiveNames;

    public CacheData(@NotNull String id) {
        this.id = id;

        this.name = "";
        this.fileHash = "";
        //this.iFileHash = "";

        commitHash = "";
        buildNumber = -1;
        lastBuildSuccess = false;
        archiveNames = new ArrayList<>();
    }
}
