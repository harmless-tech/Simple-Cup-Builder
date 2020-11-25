package tech.harmless.simplecupbuilder;

import tech.harmless.simplecupbuilder.utils.Log;

import java.util.Date;

public class SimpleCupBuilder {

    public static final String DATA_DIR = "scb/";
    public static final String TMP_DIR = "scb_tmp/";

    public static boolean DEBUG = false;

    public static void main(String[] args) {
        System.out.println("Program launch at " + new Date() + ".");
        Log.info("Program Start.");

        for(String arg : args) {
            switch(arg) {
                case "--debug" -> {
                    DEBUG = true;
                    Log.info("Enabled debug mode.");
                }
                case "--nothing" -> Log.info("Nothing happened...");
            }
        }

        //TODO Create required dirs?
    }
}
