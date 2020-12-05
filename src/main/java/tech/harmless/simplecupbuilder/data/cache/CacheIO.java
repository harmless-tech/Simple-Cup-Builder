package tech.harmless.simplecupbuilder.data.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//TODO Needs to be synced.
public final class CacheIO {

    private static final Object syncObj = new Object(); // Must sync with this object when accessing cache.
    private static final HashMap<String, CacheData> cache = new HashMap<>();
    private static final int cacheVersion = 1;

    private static final String[] emptyStrArray = new String[0];

    // Cache import/export.

    public static CacheData importCache() {
        throw new RuntimeException("Not implemented!");
        //return null;
    }

    //TODO This method should be called when there is a change to the cache.
    public static boolean exportCache() {
        throw new RuntimeException("Not implemented!");
    }

    // Cache manipulation.

    public static boolean addDrink(String id, String name, String fileHash, String iFileHash) {
        if(id != null && name != null && fileHash != null && iFileHash != null) {
            CacheData data = new CacheData(id, name, fileHash, iFileHash);

            synchronized(syncObj) {
                cache.put(id, data);
            }

            return true;
        }

        return false;
    }

    //TODO Maybe this should prune and setup cache.
    public static String[] prune(String[] ids) {
        if(ids.length > 0) {
            List<String> removeList = new ArrayList<>();

            synchronized(syncObj) {
                cache.forEach((key, val) -> {
                    boolean keep = false;

                    for(int i = 0; i < ids.length && !keep; i++)
                        keep = key.equals(ids[i]);

                    if(!keep)
                        removeList.add(key);
                });

                removeList.forEach(cache::remove);
            }

            return removeList.toArray(emptyStrArray);
        }

        return emptyStrArray;
    }
}
