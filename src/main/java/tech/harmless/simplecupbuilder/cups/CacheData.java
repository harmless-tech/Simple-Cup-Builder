package tech.harmless.simplecupbuilder.cups;

import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//TODO Allow this to be exported.
public class CacheData {

    @Getter
    private final Set<String> drinkIdCache;
    @Getter
    private final Map<String, String> drinkCachedCommit;

    public CacheData() {
        drinkIdCache = new HashSet<>();
        drinkCachedCommit = new HashMap<>();
    }

    public CacheData(Set<String> drinkIdCache, Map<String, String> drinkCachedCommit) {
        this.drinkIdCache = drinkIdCache;
        this.drinkCachedCommit = drinkCachedCommit;
    }

    public boolean isDrinkInCache(String id) {
        return drinkIdCache.contains(id);
    }

    public String getCachedCommit(String id) {
        return drinkCachedCommit.get(id);
    }

    public void addDrinkId(String id) {
        drinkIdCache.add(id);
    }

    public void updateCommit(String id, String commit) {
        drinkCachedCommit.put(id, commit);
    }

    public void removeDrink(String id) {
        drinkIdCache.remove(id);
        drinkCachedCommit.remove(id);
    }

    public void pruneCache(String id) {
        //TODO Add a way to prune the cache.
        throw new RuntimeException("Not implemented!");
    }
}
