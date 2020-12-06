package tech.harmless.simplecupbuilder.data.cache;

import tech.harmless.simplecupbuilder.utils.EmptyTypes;
import tech.harmless.simplecupbuilder.utils.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//TODO Needs to be synced.
public final class CacheIO {

    private static final Object syncObj = new Object(); // Must sync with this object when accessing cache.
    private static final HashMap<String, CacheData> cache = new HashMap<>();
    private static final int cacheVersion = 1;

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

    public static void addDrink(String id) {
        if(id != null) {
            CacheData data = new CacheData(id);

            synchronized(syncObj) {
                if(!cache.containsKey(id))
                    cache.put(id, data);
            }
        }
    }

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

            return removeList.toArray(EmptyTypes.STRING_ARRAY);
        }

        return EmptyTypes.STRING_ARRAY;
    }

    public static void setDrinkFileHash(String id, String hash) {
        if(id != null && hash != null) {
            synchronized(syncObj) {
                CacheData data = cache.get(id);

                if(data != null)
                    data.fileHash = hash;
                else
                    Log.warn("Drink " + id + " is not in the cache, but something tried to change the file hash.");
            }
        }
    }
}
