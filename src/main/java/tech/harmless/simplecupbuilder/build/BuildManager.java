package tech.harmless.simplecupbuilder.build;

import org.jetbrains.annotations.NotNull;
import tech.harmless.simplecupbuilder.SimpleCupBuilder;
import tech.harmless.simplecupbuilder.cmd.GitCommand;
import tech.harmless.simplecupbuilder.cmd.ProcessCommand;
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
        if(cupData == null)
            return;

        // Setup the update timer.
        ScheduledExecutorService se = Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> scheduledFuture = se.scheduleAtFixedRate(() -> {
            synchronized(monitorObject) {
                signaled = true;
                monitorObject.notify();
            }
        }, cupData.getOptions_gitUpdateTimer() * 60, cupData.getOptions_gitUpdateTimer() * 60, TimeUnit.SECONDS);

        while(shouldRun) {
            // Console
            //TODO Process console data.

            // Build
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

            // Wait until this thread is notified of a change.
            synchronized(monitorObject) {
                while(!signaled) {
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

    // No need to sync, since there is only one thread so far.
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
        //TODO Check drink file hash and compare to see if it should be updated.
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

    private String[] allDrinkIds() {
        String[] r = EmptyTypes.STRING_ARRAY;
        synchronized(buildSync) {
            r = drinks.keySet().toArray(EmptyTypes.STRING_ARRAY);
        }
        return r;
    }

    private void build() {
        // Check repos.
        Log.info("Checking for repo updates...");
        String[] updatedRepos = checkForUpdatesAndCreate();
        Log.info("Checking for repo updates... Done!");

        // Internal build files.
        //TODO Check internal drink file hash and compare to see if it should be updated?
        Log.info("Updating internal build files...");
        for(String id : updatedRepos) {
            synchronized(buildSync) {
                DrinkData drink = drinks.get(id);
                if(!drink.getGit_internal_build_file().isEmpty())
                    DataIO.processInternalDrink(drink);
            }
        }
        Log.info("Updating internal build files... Done!");

        // Build!
        //TODO More build threads?
        for(String id : updatedRepos) {
            Thread thread = new Thread(null, () -> startBuild(id), "Build Thread");

            synchronized(buildSync) {
                buildThreads.add(thread);
            }

            thread.start();
            try {
                thread.join();
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }

            synchronized(buildSync) {
                buildThreads.remove(thread);
            }
        }
    }

    // Also checks for repos that don't exist yet.
    private String[] checkForUpdatesAndCreate() {
        String[] drinkIds = allDrinkIds();
        boolean[] drinkTags = new boolean[drinkIds.length];

        // Check for not yet downloaded repos.
        for(int i = 0; i < drinkIds.length; i++) {
            if(!GitCommand.status(drinkIds[i])) {
                Log.info("Drink " + drinkIds[i] + " does not have a downloaded repo. It will be downloaded now.");

                String url;
                String branch;
                synchronized(buildSync) {
                    url = drinks.get(drinkIds[i]).getGit_url();
                    branch = drinks.get(drinkIds[i]).getGit_branch();
                }

                if(GitCommand.clone(drinkIds[i], url))
                    if(GitCommand.checkout(drinkIds[i], branch)) {
                        if(GitCommand.submoduleInit(drinkIds[i])) {
                            Log.info("Downloaded repo for drink " + drinkIds[i] + ".");
                            drinkTags[i] = true;

                            String commit = GitCommand.commitHash(drinkIds[i]);
                            CacheIO.setDrinkCommitHash(drinkIds[i], commit);
                        }
                        else
                            Log.error("Could not init submodules for drink " + drinkIds[i] + ".");
                    }
                    else
                        Log.error("Could not switch branch to " + branch + " for drink " + drinkIds[i] + ".");
                else
                    Log.error("Could not clone repo for drink " + drinkIds[i] + ".");
            }
        }

        // Fetch, compare, and pull already downloaded repos.
        //TODO Should do git checkout?
        for(int i = 0; i < drinkIds.length; i++) {
            if(!drinkTags[i]) {
                if(GitCommand.fetch(drinkIds[i])) {
                    String branch;
                    synchronized(buildSync) {
                        branch = drinks.get(drinkIds[i]).getGit_branch();
                    }

                    if(GitCommand.pull(drinkIds[i], branch)) {
                        String commit = GitCommand.commitHash(drinkIds[i]);

                        if(!commit.equals(CacheIO.getDrinkCommitHash(drinkIds[i]))) {
                            if(GitCommand.submoduleInit(drinkIds[i])) {
                                Log.info("Pulled repo for drink " + drinkIds[i] + ".");
                                drinkTags[i] = true;

                                commit = GitCommand.commitHash(drinkIds[i]);
                                CacheIO.setDrinkCommitHash(drinkIds[i], commit);
                            }
                            else
                                Log.error("Could not init submodules for drink " + drinkIds[i] + ".");
                        }
                    }
                    else
                        Log.error("Could not pull repo for drink " + drinkIds[i] + ".");
                }
                else
                    Log.error("Could not fetch repo for drink " + drinkIds[i] + ".");
            }
        }

        int count = 0;
        for(boolean val : drinkTags) {
            if(val)
                count++;
        }

        String[] r = new String[count];
        int ri = 0;
        for(int i = 0; i < drinkIds.length; i++) {
            if(drinkTags[i]) {
                r[ri] = drinkIds[i];
                ri++;
            }
        }

        return r;
    }

    private void startBuild(@NotNull String id) {
        Log.info("Starting build of drink " + id + "...");

        DrinkData drink;
        synchronized(buildSync) {
            drink = drinks.get(id);
        }

        //TODO Allow for removal.
        //TODO Allow for specific builds. (Switch statement for platforms) (Same for path) (Some for process)

        // Pre-Check
        boolean preCheck = true;
        for(String cmd : drink.getBuildWindows_preCheck()) {
            //TODO Allow add path for all platforms.
            FinalTuple<Integer, String> pr = ProcessCommand
                    .run(cupData.getProcess_windows(), cmd,
                            SimpleCupBuilder.BUILD_DIR + id + drink.getBuildOps_wrkDir(), drink.getAddPath_windows(),
                            null);
            Log.process("Drink pre-check cmd: " + cmd, pr);

            preCheck = preCheck && pr.getX() == 0;
            if(!preCheck)
                break;
        }

        // Main
        if(preCheck) {
            // Build
            boolean built = true;
            for(String cmd : drink.getBuildWindows_commands()) {
                //TODO Allow add path for all platforms.
                FinalTuple<Integer, String> pr = ProcessCommand
                        .run(cupData.getProcess_windows(), cmd,
                                SimpleCupBuilder.BUILD_DIR + id + drink.getBuildOps_wrkDir(),
                                drink.getAddPath_windows(),
                                null);
                Log.process("Drink build cmd: " + cmd, pr);

                built = built && pr.getX() == 0;
                if(!built)
                    break;
            }

            if(built) {
                // Test
                boolean testing = true;
                for(String cmd : drink.getBuildWindows_testCommands()) {
                    //TODO Allow add path for all platforms.
                    FinalTuple<Integer, String> pr = ProcessCommand
                            .run(cupData.getProcess_windows(), cmd,
                                    SimpleCupBuilder.BUILD_DIR + id + drink.getBuildOps_wrkDir(),
                                    drink.getAddPath_windows(),
                                    null);
                    Log.process("Drink test cmd: " + cmd, pr);

                    testing = testing && pr.getX() == 0;
                    if(!testing)
                        break;
                }

                if(testing) {
                    // Archive
                    //TODO Allow for archiving.
                }
                else
                    Log.error("Testing failed, skipping archival!");
            }
            else
                Log.error("Build failed, skipping testing!");
        }
        else
            Log.error("Pre-Check failed, skipping build!");

        Log.info("Starting build of drink " + id + "... Done!");
    }
}
