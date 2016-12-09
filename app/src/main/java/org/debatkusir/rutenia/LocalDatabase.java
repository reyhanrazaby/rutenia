package org.debatkusir.rutenia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Reyhan on 12/8/2016.
 */

public class LocalDatabase extends SQLiteOpenHelper {

    private final String TABLE_PLACE = "PLACE";
    private final String TABLE_ANGKOT = "ANGKOT";
    private final String TABLE_ROUTE = "ROUTE";
    private final String TABLE_WAYPOINT = "WAYPOINT";

    private final String PLACE_ID = "Id";
    private final String PLACE_NAME = "Name";
    private final String PLACE_CITY = "City";
    private final String PLACE_COORDINATE = "Coordinate";
    private final String PLACE_IS_TRANSIT_STOP = "Is_transit_stop";

    private final String ANGKOT_ID = "Id";
    private final String ANGKOT_NAME = "Name";
    private final String ANGKOT_PHOTO = "Photo";
    private final String ANGKOT_ID_TRANSIT_STOP_1 = "Id_transit_stop_1";
    private final String ANGKOT_ID_TRANSIT_STOP_2 = "Id_terminal_stop_2";

    private final String ROUTE_ID = "Id";
    private final String ROUTE_IS_ALTERNATIVE = "Is_alternative";
    private final String ROUTE_ID_ANGKOT = "Id_angkot";

    private final String WAYPOINT_ID_ROUTE = "Id_route";
    private final String WAYPOINT_LATITUDE = "Latitude";
    private final String WAYPOINT_LONGITUDE = "Longitude";
    private final String WAYPOINT_ORDER = "Order";

    public LocalDatabase() {
        super(Rutenia.getAppContext(), "1" , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_PLACE + " (" +
                        PLACE_ID + " INTEGER NOT NULL, " +
                        PLACE_NAME + " TEXT NOT NULL," +
                        PLACE_CITY + " TEXT," +
                        PLACE_COORDINATE + " TEXT NOT NULL, " +
                        PLACE_IS_TRANSIT_STOP + " INTEGER NOT NULL, " +
                        "PRIMARY KEY (" + PLACE_ID + ")" +
                        ");"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_ANGKOT + " (" +
                        ANGKOT_ID + " INTEGER NOT NULL, " +
                        ANGKOT_NAME + " TEXT NOT NULL," +
                        ANGKOT_PHOTO + " TEXT NOT NULL," +
                        ANGKOT_ID_TRANSIT_STOP_1 + " INTEGER NOT NULL," +
                        ANGKOT_ID_TRANSIT_STOP_2 + " INTEGER NOT NULL, " +
                        "PRIMARY KEY (" + ANGKOT_ID + ")," +
                        "FOREIGN KEY (" + ANGKOT_ID_TRANSIT_STOP_1 + ")" +
                        "REFERENCES " + TABLE_PLACE + "(" + PLACE_ID + ")," +
                        "FOREIGN KEY (" + ANGKOT_ID_TRANSIT_STOP_2 + ")" +
                        "REFERENCES " + TABLE_PLACE + "(" + PLACE_ID + ")" +
                        ");"
        );
/*
        db.execSQL(
                "CREATE TABLE " + TABLE_ROUTE + " (" +
                        ROUTE_ID + " INTEGER NOT NULL, " +
                        ROUTE_ID_ANGKOT + " INTEGER NOT NULL," +
                        ROUTE_IS_ALTERNATIVE + " INTEGER NOT NULL," +
                        "PRIMARY KEY (" + ROUTE_ID + ")," +
                        "FOREIGN KEY (" + ROUTE_ID_ANGKOT + ")" +
                        "REFERENCES " + TABLE_ANGKOT + "(" + ANGKOT_ID + ")" +
                        ");"
        );*/

/*        db.execSQL(
                "CREATE TABLE " + TABLE_WAYPOINT + " (" +
                        WAYPOINT_ID_ROUTE + " INTEGER NOT NULL, " +
                        WAYPOINT_ORDER + " INTEGER NOT NULL, " +
                        WAYPOINT_LONGITUDE + " FLOAT NOT NULL, " +
                        WAYPOINT_LATITUDE + " FLOAT NOT NULL, " +
                        "PRIMARY KEY (" + WAYPOINT_LONGITUDE + ", " + WAYPOINT_ORDER + ", " + WAYPOINT_LATITUDE + ", " + WAYPOINT_ID_ROUTE + ")," +
                        "FOREIGN KEY (" + WAYPOINT_ID_ROUTE + ") " +
                        "REFERENCES " + TABLE_ROUTE + "(" + ROUTE_ID + ")" +
                        ");"
        );*/
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON;");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANGKOT + " ;");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACE + " ;");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTE + " ;");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WAYPOINT + " ;");
        onCreate(db);
    }

    public void insertPlace(int id, String name, String city, String coordinate, boolean isTransitStop) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PLACE_ID, id);
        contentValues.put(PLACE_NAME, name);
        contentValues.put(PLACE_CITY, city);
        contentValues.put(PLACE_COORDINATE, coordinate);
        contentValues.put(PLACE_IS_TRANSIT_STOP, isTransitStop ? "1" : "0");

        database.insert(TABLE_PLACE, null, contentValues);
        database.close();
    }

    public void insertAngkot(int id, String name, String photo, int idTerminal1, int idTerminal2) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ANGKOT_ID, id);
        contentValues.put(ANGKOT_NAME, name);
        contentValues.put(ANGKOT_PHOTO, photo);
        contentValues.put(ANGKOT_ID_TRANSIT_STOP_1, idTerminal1);
        contentValues.put(ANGKOT_ID_TRANSIT_STOP_2, idTerminal2);

        database.insert(TABLE_ANGKOT, null, contentValues);
        database.close();
    }

    public String[] getAngkot (int searchTerm) {
        final int FOUND_LIMIT = 5;
        ArrayList<String> arr = new ArrayList<>();

        String sql = "";
        sql += "SELECT * FROM " + TABLE_ANGKOT;
        sql += " WHERE " + ANGKOT_ID_TRANSIT_STOP_1 + " LIKE '%" + searchTerm + "%'";
        sql += " LIMIT 0," + FOUND_LIMIT;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(PLACE_NAME));
                arr.add(name);

                count++;
            } while (cursor.moveToNext());
        }
        String[] result = new String[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            result[i] = arr.get(i);
        }
        return result;
    }

    public String[] getTerminal(String searchTerm){
        final int FOUND_LIMIT = 5;
        ArrayList<String> arr = new ArrayList<>();

        String sql = "";
        sql += "SELECT * FROM " + TABLE_PLACE;
        sql += " WHERE " + PLACE_NAME + " LIKE '%" + searchTerm + "%'";
        sql += " LIMIT 0," + FOUND_LIMIT;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(PLACE_NAME));
                arr.add(name);

                count++;
            } while (cursor.moveToNext());
        }
        String[] result = new String[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            result[i] = arr.get(i);
        }
        Log.d("anjay","result: " + arr.size());
        return result;
    }

    public String[] getIdTerminal(String searchTerm){
        final int FOUND_LIMIT = 5;
        ArrayList<String> arr = new ArrayList<>();

        String sql = "";
        sql += "SELECT * FROM " + TABLE_PLACE;
        sql += " WHERE " + PLACE_NAME + " LIKE '%" + searchTerm + "%'";
        sql += " LIMIT 0," + FOUND_LIMIT;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(PLACE_ID));
                arr.add(name);

                count++;
            } while (cursor.moveToNext());
        }
        String[] result = new String[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            result[i] = arr.get(i);
        }
        Log.d("anjay","result: " + arr.size());
        return result;
    }
}

