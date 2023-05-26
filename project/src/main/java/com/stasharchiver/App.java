package com.stasharchiver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.utils.ProgramConfig;
import com.utils.UTILS;
import com.stash.StashManager;;

public class App 
{
    // CONSTANTS
    public static final String PROGRAMCONFIGNAME = "config.json";
    static Gson gson;

    // Global vars
    private static String DATABASEPATH = "";
    private static String[] BACKUPPATHS;
    private static String[] BACKUPCONFIGS;
    private static StashManager StashManager;

    public static void main( String[] args )
    {
        System.out.println( "stash-archiver - Presented by TSK" );
        System.out.println("Version 1.0.0");
        System.out.println();
        run();
    }

    private static void run() {
        gson = new Gson();

        // Read program config
        readProgramConfig();

        // Initialize StashManager
        com.stash.StashManager.GetInstance(DATABASEPATH);
    }

    private static void readProgramConfig() {
        // config file is in same dir
        String path = System.getProperty("user.dir") + "\\" + PROGRAMCONFIGNAME;
        Path configPath = Path.of(path);
        String configJSON = "";
        try {
            configJSON = Files.readString(configPath);
        }  
        catch(IOException e) {
            UTILS.PRINTCRITICALERROR("Config JSON cannot be read! - " + path);
        }
        
        ProgramConfig pc = gson.fromJson(configJSON, ProgramConfig.class);
        DATABASEPATH = UTILS.ESCAPEPATH(pc.stashDatabasePath);
        BACKUPPATHS = pc.backupPaths;
        BACKUPCONFIGS = pc.backupConfigs;

        // Escape paths
        String[] escapedPaths = new String[BACKUPPATHS.length];
        for(int i=0;i<BACKUPPATHS.length;i++) {
            String s = UTILS.ESCAPEPATH(BACKUPPATHS[i]);
            escapedPaths[i] = s;
        }
        BACKUPPATHS = escapedPaths;

        escapedPaths = new String[BACKUPCONFIGS.length];
        for(int i=0;i<BACKUPCONFIGS.length;i++) {
            String s = UTILS.ESCAPEPATH(BACKUPCONFIGS[i]);
            escapedPaths[i] = s;
        }
        BACKUPCONFIGS = escapedPaths;
    }
}
