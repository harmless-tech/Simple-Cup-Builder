package tech.harmless.simplecupbuilder.build;

import tech.harmless.simplecupbuilder.data.CupData;
import tech.harmless.simplecupbuilder.data.DataIO;
import tech.harmless.simplecupbuilder.data.DrinkData;
import tech.harmless.simplecupbuilder.data.cache.CacheIO;
import tech.harmless.simplecupbuilder.utils.EmptyTypes;
import tech.harmless.simplecupbuilder.utils.Log;
import tech.harmless.simplecupbuilder.utils.tuples.FinalTuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/*
 * Setup cup options.
 * Setup cache.
 * Import drink files.
 *
 * Timer -> TIME
 * Git check status.
 * Git pull.
 * Git update commit hash.
 * Start build process.
 */
public class BuildManager implements Runnable {

    private boolean shouldRun;
    private List<Thread> buildThreads; //TODO Use async instead of threads?
    //TODO Something to talk to the user console.
    //TODO A timer of sorts.

    private CupData cupData;
    private Map<String, DrinkData> drinks;

    private void importData() {
        //TODO Import Cache.
        //CacheIO.importCache();

        // Import cup settings.
        cupData = DataIO.processCup();
        if(cupData == null) {
            shouldRun = false;
            return;
        }
        Log.info("Successfully imported cup.");

        //TODO Check for repeating files. They should throw a fatal error.
        String[] drinkFileNames = cupData.getOptions_drinks();

        // Import drinks async.
        List<CompletableFuture<FinalTuple<DrinkData, String>>> async = new ArrayList<>();

        for(int i = 0; i < drinkFileNames.length; i++) {
            final int ai = i;
            async.add(CompletableFuture.supplyAsync(() -> DataIO.processDrink(drinkFileNames[ai])));
        }

        for(CompletableFuture<FinalTuple<DrinkData, String>> fut : async) {
            FinalTuple<DrinkData, String> tuple = fut.join();
            if(tuple.getX() != null) {
                DrinkData data = tuple.getX();
                drinks.put(data.getDrinkInfo_id(), data);

                Log.info("Successfully imported drink " + data.getDrinkInfo_id() + ".");

                //TODO Check for repeating ids, they should throw a fatal error.

                // Add to cache, if its not there already.
                CacheIO.addDrink(data.getDrinkInfo_id());
                CacheIO.setDrinkFileHash(data.getDrinkInfo_id(), tuple.getY());
            }
            else
                Log.error("Drink data failed to import.");
        }

        // Prune cache.
        String[] ids = drinks.keySet().toArray(EmptyTypes.STRING_ARRAY);
        for(String id : CacheIO.prune(ids))
            Log.info("Drink " + id + " was removed from the cache.");

        //TODO More?
    }

    @Override
    public void run() {
        shouldRun = true;
        buildThreads = new ArrayList<>();

        cupData = null;
        drinks = new HashMap<>();

        // Setup
        importData();

        while(shouldRun) {
            try {
                Thread.sleep(1000);
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }

            shouldRun = false;
        }
    }
}
