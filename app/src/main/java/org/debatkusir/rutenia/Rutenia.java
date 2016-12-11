package org.debatkusir.rutenia;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

        //if (!isDataCreated()) {
            createData();
        //}
    }

    public static String readTextFile(Context ctx, int resId)
    {
        InputStream inputStream = ctx.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader bufferedreader = new BufferedReader(inputreader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try
        {
            while (( line = bufferedreader.readLine()) != null)
            {
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }
        }
        catch (IOException e)
        {
            return null;
        }
        return stringBuilder.toString();
    }


    public static Context getAppContext() {
        return context;
    }

    public static LocalDatabase getLocalDatabase() {
        return localDatabase;
    }

    private void createData(){
        /* PEMBUATAN DATA PLACE */
        localDatabase.insertPlace(0, "Terminal Kampung Rambutan", "Kampung Rambutan", "0,0", true);
        localDatabase.insertPlace(1, "Terminal Depok", "Depok", "-6.391499, 106.824405", true);
        localDatabase.insertPlace(2, "Pasar Pal", "Depok", "-6.357034, 106.857985", true);
        localDatabase.insertPlace(3, "Stasiun UI", "Depok", "-6.360431, 106.831865", false);
        localDatabase.insertPlace(4, "Depok Town Square", "Depok", "-6.372647, 106.832035", false);
        localDatabase.insertPlace(5, "Margo City", "Depok", "-6.372948, 106.834569", false);
        localDatabase.insertPlace(6, "Kebun Binatang Ragunan", "Jakarta Selatan", "-6.312489, 106.820350", false);
        localDatabase.insertPlace(7, "Terminal Pasar Minggu", "Jakarta Selatan", "-6.283253, 106.843760", true);
        localDatabase.insertPlace(8, "Pasar Minggu", "Jakarta Selatan", "-6.284911, 106.843246", false);

        /* PEMBUATAN DATA ANGKOT */
        localDatabase.insertAngkot(1000, "D11", "fotoD11", 2, 1);
        localDatabase.insertAngkot(1001, "112", "foto112", 1, 0);

        /* PEMBUATAN DATA WAYPOINT */
        try {
            String[] wayPointData = {
                    readTextFile(context, R.raw.waypoint_d11)
            };
            localDatabase.insertWaypoints(wayPointData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setDataCreated();
    }

    public void setDataCreated() {
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("MySP", Context.MODE_PRIVATE).edit();
        editor.putBoolean("isDatabaseCreated",true);
        editor.apply();
    }

    public boolean isDataCreated() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MySP", Context.MODE_PRIVATE);
        return pref.getBoolean("isDatabaseCreated", true);
    }
}
