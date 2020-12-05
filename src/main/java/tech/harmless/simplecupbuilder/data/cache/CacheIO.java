package tech.harmless.simplecupbuilder.data.cache;

import java.util.HashMap;

//TODO Needs to be synced.
public final class CacheIO {

    public static Object sync = new Object();
    public static HashMap<String, CacheData> cache = new HashMap<>();

    public static CacheData importCache() {
        throw new RuntimeException("Not implemented!");
        //return null;
    }

    public static boolean exportCache() {
        throw new RuntimeException("Not implemented!");
    }
}
