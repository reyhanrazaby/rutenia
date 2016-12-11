package org.debatkusir.rutenia;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Reyhan on 12/8/2016.
 */

public class Rutenia extends Application {
    private static Context context;
    private static LocalDatabase localDatabase;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        localDatabase = new LocalDatabase();
        Log.d("anjay","create");

        localDatabase.insertPlace(0, "Terminal Kampung Rambutan", "Kampung Rambutan", "0,0", true);
        localDatabase.insertPlace(1, "Terminal Depok", "Depok", "0,0", true);
        localDatabase.insertPlace(2, "Pasar Pal", "Depok", "0,0", true);

        localDatabase.insertAngkot(0, "D11", "angkot1", 1, 2);
        localDatabase.insertAngkot(2, "D13", "angkot3", 1, 0);
        localDatabase.insertAngkot(3, "D14", "angkot4", 2, 0);
    }

    public static Context getAppContext() {
        return context;
    }

    public static LocalDatabase getLocalDatabase() {
        return localDatabase;
    }
}
