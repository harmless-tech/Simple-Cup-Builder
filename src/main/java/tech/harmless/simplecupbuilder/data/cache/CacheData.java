package tech.harmless.simplecupbuilder.data.cache;

//TODO Allow this to be exported.
class CacheData {

    protected String id;
    protected String name;
    protected String fileHash;
    protected String iFileHash;

    // Build Info
    protected String commitHash;
    protected int buildNumber;

    public CacheData(String id, String name, String fileHash, String iFileHash) {
        this.id = id;
        this.name = name;
        this.fileHash = fileHash;
        this.iFileHash = iFileHash;

        commitHash = "";
        buildNumber = -1;
    }
}
