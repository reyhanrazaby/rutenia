package org.debatkusir.rutenia;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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

        if (!isDataCreated()) {
            createData();
        }
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
        localDatabase.insertPlace(0, "Term. Kampung Rambutan", "Kampung Rambutan", "-6.308872, 106.882092", true);
        localDatabase.insertPlace(1, "Terminal Depok", "Depok", "-6.391478, 106.824384", true);
        localDatabase.insertPlace(2, "Pasar Pal", "Depok", "-6.357071, 106.857980", true);
        localDatabase.insertPlace(3, "Stasiun UI", "Depok", "-6.360431, 106.831865", false);
        localDatabase.insertPlace(4, "Depok Town Square", "Depok", "-6.372647, 106.832035", false);
        localDatabase.insertPlace(5, "Margo City", "Depok", "-6.372948, 106.834569", false);
        localDatabase.insertPlace(6, "Kebun Binatang Ragunan", "Jakarta Selatan", "-6.312489, 106.820350", false);
        localDatabase.insertPlace(7, "Term. Pasar Minggu", "Jakarta Selatan", "-6.283253, 106.843760", true);
        localDatabase.insertPlace(8, "Pasar Minggu", "Jakarta Selatan", "-6.284911, 106.843246", false);
        localDatabase.insertPlace(9, "Lenteng Agung", "Jakarta Selatan", "-6.306523, 106.838947", false);
        localDatabase.insertPlace(10, "Cijantung", "Jakarta Timur", "-6.316828, 106.863524", false);
        localDatabase.insertPlace(11, "Cisalak", "Depok", "-6.378484, 106.866079", true);
        localDatabase.insertPlace(12, "Term. Kp. Melayu", "Jakarta Selatan", "-6.223998, 106.866241", true);
        localDatabase.insertPlace(13, "Mampang", "Depok", "-6.387976, 106.792629", false);
        localDatabase.insertPlace(14, "Ciputat", "Tangerang Selatan", "-6.288644, 106.728120", true);
        localDatabase.insertPlace(15, "Cileungsi", "Bogor", "-6.400568, 106.959686", true);
        localDatabase.insertPlace(16, "Mekarsari", "Bogor", "-6.409835, 106.985382", false);
        localDatabase.insertPlace(17, "Terminal Cibinong", "Bogor", "-6.467253, 106.854189", true);
        localDatabase.insertPlace(18, "Citeureup", "Bogor", "-6.483445, 106.870263", true);
        localDatabase.insertPlace(19, "Parung", "Bogor", "-6.417400, 106.731204", true);
        localDatabase.insertPlace(20, "Beji", "Depok", "-6.368919, 106.808216", true);
        localDatabase.insertPlace(21, "Bojong Gede", "Bogor", "-6.477403, 106.803525", false);
        localDatabase.insertPlace(22, "Citayam", "Bogor", "-6.442998, 106.804674", true);
        localDatabase.insertPlace(23, "Jatimulya", "Depok", "-6.444794, 106.829666", true);
        localDatabase.insertPlace(24, "Ciracas", "Jakarta Timur", "-6.330394, 106.866732", true);
        localDatabase.insertPlace(25, "Cibubur", "Jakarta Timur", "-6.330620, 106.867041", true);
        localDatabase.insertPlace(26, "Bekasi", "Bekasi", "-6.234166, 106.993296", false);
        localDatabase.insertPlace(27, "Jagakarsa", "Jakarta Selatan", "-6.330979, 106.833599", true);


        /* PEMBUATAN DATA ANGKOT */
        localDatabase.insertAngkot(1000, "D11", "fotod11", 2, 1);
        localDatabase.insertAngkot(1001, "129", "foto129", 7, 16);
        localDatabase.insertAngkot(1002, "112", "foto112", 1, 0);
        localDatabase.insertAngkot(1003, "110", "foto110", 2, 14);
        localDatabase.insertAngkot(1004, "121", "foto121", 0, 15);
        localDatabase.insertAngkot(1005, "37", "foto37", 0, 17);
        localDatabase.insertAngkot(1006, "41", "foto41", 0, 18);
        localDatabase.insertAngkot(1007, "D03", "fotod03", 1, 19);
        localDatabase.insertAngkot(1008, "D04", "fotod04", 1, 20);
        localDatabase.insertAngkot(1009, "D05", "fotod05", 1, 21);
        localDatabase.insertAngkot(1010, "D06", "fotod06", 1, 11);
        localDatabase.insertAngkot(1011, "D07A", "fotod07a", 1, 22);
        localDatabase.insertAngkot(1012, "D09", "fotod09", 1, 23);
        localDatabase.insertAngkot(1013, "KWK S15", "fotokwks15", 7, 10);
        localDatabase.insertAngkot(1014, "KWK T12", "fotokwkt12", 0, 24);
        localDatabase.insertAngkot(1015, "KWK T18", "fotokwkt18", 0, 25);
        localDatabase.insertAngkot(1016, "Mikrolet 26", "fotomikrolet26", 12, 26);
        localDatabase.insertAngkot(1017, "Mikrolet 6", "fotomikrolet6", 12, 11);
        localDatabase.insertAngkot(1018, "Mikrolet M16", "fotomikroletm16", 7, 12);
        localDatabase.insertAngkot(1019, "Mikrolet M17", "fotomikroletm17", 7, 9);
        localDatabase.insertAngkot(1020, "Mikrolet M36", "fotomikroletm36", 7, 27);

        /* PEMBUATAN DATA WAYPOINT */
        try {
            String[] wayPointData = {
                    readTextFile(context, R.raw.waypoint_d11),
                    readTextFile(context, R.raw.waypoint_129),
                    readTextFile(context, R.raw.waypoint_112),
                    readTextFile(context, R.raw.waypoint_110),
                    readTextFile(context, R.raw.waypoint_121),
                    readTextFile(context, R.raw.waypoint_37),
                    readTextFile(context, R.raw.waypoint_41),
                    readTextFile(context, R.raw.waypoint_d03),
                    readTextFile(context, R.raw.waypoint_d04),
                    readTextFile(context, R.raw.waypoint_d05),
                    readTextFile(context, R.raw.waypoint_d06),
                    readTextFile(context, R.raw.waypoint_d07a),
                    readTextFile(context, R.raw.waypoint_d09),
                    readTextFile(context, R.raw.waypoint_kwk_s15),
                    readTextFile(context, R.raw.waypoint_kwk_t12),
                    readTextFile(context, R.raw.waypoint_kwk_t18),
                    readTextFile(context, R.raw.waypoint_mikrolet_26),
                    readTextFile(context, R.raw.waypoint_mikrolet_6),
                    readTextFile(context, R.raw.waypoint_mikrolet_m16),
                    readTextFile(context, R.raw.waypoint_mikrolet_m17),
                    readTextFile(context, R.raw.waypoint_mikrolet_m36)
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
        editor.commit();
    }

    public boolean isDataCreated() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MySP", Context.MODE_PRIVATE);
        return pref.getBoolean("isDatabaseCreated", false);
    }
}
