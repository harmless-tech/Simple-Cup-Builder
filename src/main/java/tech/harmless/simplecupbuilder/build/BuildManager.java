package tech.harmless.simplecupbuilder.build;

import tech.harmless.simplecupbuilder.data.CupData;
import tech.harmless.simplecupbuilder.data.DataIO;
import tech.harmless.simplecupbuilder.data.DrinkData;
import tech.harmless.simplecupbuilder.data.cache.CacheIO;
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

        //TODO Check for repeating ids. They should throw a fatal error.
        String[] drinkIds = cupData.getOptions_drinks();

        // Cache adding and removing.
        CacheIO.addDrinks(cupData.getOptions_drinks());
        for(String id : CacheIO.prune(cupData.getOptions_drinks()))
            Log.info("Drink " + id + " was removed from the cache.");

        // Import drinks async.
        //TODO Use ? instead???
        CompletableFuture<FinalTuple<DrinkData, String>>[] async = new CompletableFuture[drinkIds.length];

        for(int i = 0; i < drinkIds.length; i++) {
            final int ai = i;
            async[i] = CompletableFuture.supplyAsync(() -> DataIO.processDrink(drinkIds[ai]));
        }

        for(CompletableFuture<FinalTuple<DrinkData, String>> fut : async) {
            FinalTuple<DrinkData, String> tuple = fut.join();
            if(tuple.getX() != null) {
                DrinkData data = tuple.getX();
                drinks.put(data.getDrinkInfo_id(), data);

                CacheIO.setDrinkFileHash(data.getDrinkInfo_id(), tuple.getY());
            }
            else
                Log.error("Drink data failed to import."); //TODO Add id?
        }

        //TODO More?
    }

    @Override
    public void run() {
        shouldRun = true;
        buildThreads = new ArrayList<>();

        cupData = null;
        drinks = new HashMap<>();
        
        Log.debug("Hey!");

        importData();
        Log.debug("Run: " + shouldRun);

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
