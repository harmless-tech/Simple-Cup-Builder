package tech.harmless.simplecupbuilder.build;

import tech.harmless.simplecupbuilder.data.CupData;
import tech.harmless.simplecupbuilder.data.DataIO;
import tech.harmless.simplecupbuilder.data.DrinkData;
import tech.harmless.simplecupbuilder.data.cache.CacheIO;
import tech.harmless.simplecupbuilder.utils.EmptyTypes;
import tech.harmless.simplecupbuilder.utils.Log;
import tech.harmless.simplecupbuilder.utils.tuples.FinalTuple;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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

    public final Object monitorObject = new Object();
    public final Queue<String> cmdQueue = new ConcurrentLinkedQueue<>(); // Auto synced. No need to use sync obj.
    //TODO Switch to a custom cmd object. Put parser in Console file.
    public boolean signaled = false;

    private final Object buildSync = new Object();

    private boolean shouldRun;
    private boolean building;
    private Date buildStartTime;
    private Thread masterBuildThread; //TODO Use async instead of threads? //TODO Allow multiple (parallel) builds?
    private List<Thread> buildThreads; //TODO Use async instead of threads? //TODO Allow multiple (parallel) builds?
    //TODO Something to talk to the user console.
    //TODO A timer of sorts.

    private CupData cupData;
    private Map<String, DrinkData> drinks;

    @Override
    public void run() {
        // Init
        shouldRun = true;
        building = false;
        buildStartTime = new Date(0L);
        masterBuildThread = new Thread();
        buildThreads = new ArrayList<>();

        cupData = null;
        drinks = new HashMap<>();

        // Setup
        importData();

        // Setup the update timer.
        ScheduledExecutorService se = Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> scheduledFuture = se.scheduleAtFixedRate(() -> {
            synchronized(monitorObject) {
                signaled = true;
                monitorObject.notify();
            }
        }, cupData.getOptions_gitUpdateTimer() * 60, cupData.getOptions_gitUpdateTimer() * 60, TimeUnit.SECONDS);

        while(shouldRun) {
            //TODO Process console data.

            if((buildStartTime.getTime() + cupData.getOptions_gitUpdateTimer() * 60 * 1000) <= new Date().getTime()) {
                Log.info("Timer done. Checking if build need to be done.");
                Log.warn("Some console commands may be slow to process until builds are done.");
                buildStartTime = new Date();

                if(!masterBuildThread.isAlive()) {
                    masterBuildThread = new Thread(null, this::build, "Master Build Thread");
                    masterBuildThread.start();
                }
                else
                    Log.warn("Master Build Thread is still alive and so new builds cannot be done." +
                            " Maybe consider making the git update timer longer.");

                //TODO Do build stuff.
                //TODO If builds are occurring still skip.
            }

            synchronized(monitorObject) {
                while(!signaled) {
                    Log.debug("waiting");
                    try {
                        monitorObject.wait();
                    }
                    catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                signaled = false;
            }
        }

        // Cleanup
        scheduledFuture.cancel(true);
        //TODO Stop all other build threads. thread.interr Then wait.
    }

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
    }

    private void build() {
        Log.debug("starty");
        try {
            Thread.sleep(10000);
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
