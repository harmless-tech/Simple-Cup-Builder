package tech.harmless.simplecupbuilder.data.cache;

//TODO Allow this to be exported.
//TODO Cache drink files???
//TODO Hash for drink files.
//TODO Split off methods.
class CacheData {

    protected String id;
    protected String name;
    protected String fileHash;
    protected String iFileHash;

    // Build Info
    protected String commitHash;
    protected int buildNumber;

    /*public CacheData() {
        drinkIdCache = new HashSet<>();
        drinkCachedCommit = new HashMap<>();
        drinkBuildNumber = new HashMap<>();
    }

    public CacheData(Set<String> drinkIdCache, Map<String, String> drinkCachedCommit,
                     Map<String, Integer> drinkBuildNumber) {
        this.drinkIdCache = drinkIdCache;
        this.drinkCachedCommit = drinkCachedCommit;
        this.drinkBuildNumber = drinkBuildNumber;
    }

    public boolean isDrinkInCache(String id) {
        return drinkIdCache.contains(id);
    }

    public String getCachedCommit(String id) {
        return drinkCachedCommit.get(id);
    }

    public Integer getDrinkBuild(String id) {
        return drinkBuildNumber.get(id);
    }

    public void addDrinkId(String id) {
        drinkIdCache.add(id);
    }

    public void updateCommit(String id, String commit) {
        drinkCachedCommit.put(id, commit);
    }

    public void incrementDrinkBuild(String id) {
        Integer i = drinkBuildNumber.get(id);

        if(i == null)
            drinkBuildNumber.put(id, 1);
        else
            drinkBuildNumber.put(id, ++i);
    }

    public void removeDrink(String id) {
        drinkIdCache.remove(id);
        drinkCachedCommit.remove(id);
        drinkBuildNumber.remove(id);
    }

    public void pruneCache(String id) {
        //TODO Add a way to prune the cache.
        throw new RuntimeException("Not implemented!");
    }*/
}
