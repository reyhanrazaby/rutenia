package org.debatkusir.rutenia;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

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
    }

    public static Context getAppContext() {
        return context;
    }
}
