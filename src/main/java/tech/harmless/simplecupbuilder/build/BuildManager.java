package tech.harmless.simplecupbuilder.build;

import tech.harmless.simplecupbuilder.data.CupData;
import tech.harmless.simplecupbuilder.data.DataIO;
import tech.harmless.simplecupbuilder.data.DrinkData;
import tech.harmless.simplecupbuilder.data.cache.CacheIO;
import tech.harmless.simplecupbuilder.utils.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private Map<String, DrinkData> drinkData;

    //TODO This should exit if there is an error with
    private void importData() {
        //TODO Import Cache.
        //CacheIO.importCache();

        cupData = DataIO.processCup();
        if(cupData == null) {
            shouldRun = false;
            return;
        }

        //TODO Cache checkup. (prune)
    }

    @Override
    public void run() {
        shouldRun = true;
        buildThreads = new ArrayList<>();

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
