package org.debatkusir.rutenia;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Reyhan on 12/8/2016.
 */

public class LocalDatabase extends SQLiteOpenHelper {

    final String TABLE_PLACE = "PLACE";
    final String TABLE_ANGKOT = "ANGKOT";
    final String TABLE_ROUTE = "ROUTE";
    final String TABLE_WAYPOINT = "WAYPOINT";

    final String PLACE_ID = "Id";
    final String PLACE_NAME = "Name";
    final String PLACE_CITY = "City";
    final String PLACE_COORDINATE = "Coordinate";
    final String PLACE_IS_TRANSIT_STOP = "Is_transit_stop";

    final String ANGKOT_ID = "Id";
    final String ANGKOT_NAME = "Name";
    final String ANGKOT_PHOTO = "Photo";
    final String ANGKOT_ID_TRANSIT_STOP_1 = "Id_transit_stop_1";
    final String ANGKOT_ID_TRANSIT_STOP_2 = "Id_terminal_stop_2";

    final String ROUTE_ID = "Id";
    final String ROUTE_IS_ALTERNATIVE = "Is_alternative";
    final String ROUTE_ID_ANGKOT = "Id_angkot";

    final String WAYPOINT_ID_ROUTE = "Id_route";
    final String WAYPOINT_LATITUDE = "Latitude";
    final String WAYPOINT_LONGITUDE = "Longitude";
    final String WAYPOINT_INDEX = "Index";

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
                        "PRIMARY KEY (" + PLACE_ID + ")," +
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

        db.execSQL(
                "CREATE TABLE " + TABLE_ROUTE + " (" +
                        ROUTE_ID + " INTEGER NOT NULL, " +
                        ROUTE_ID_ANGKOT + " INTEGER NOT NULL," +
                        ROUTE_IS_ALTERNATIVE + " INTEGER NOT NULL," +
                        "PRIMARY KEY (" + ROUTE_ID + ")," +
                        "FOREIGN KEY (" + ROUTE_ID_ANGKOT + ")" +
                        "REFERENCES " + TABLE_ANGKOT + "(" + ANGKOT_ID + ")" +

                        ");"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_WAYPOINT + " (" +
                        WAYPOINT_INDEX + " INTEGER NOT NULL, " +
                        WAYPOINT_LONGITUDE + " FLOAT NOT NULL," +
                        WAYPOINT_LATITUDE + " FLOAT NOT NULL," +
                        WAYPOINT_ID_ROUTE + " INTEGER NOT NULL," +
                        "PRIMARY KEY (" + WAYPOINT_INDEX + ", " + WAYPOINT_LONGITUDE + ", " + WAYPOINT_LATITUDE + ", " + WAYPOINT_ID_ROUTE + ")," +
                        "FOREIGN KEY (" + WAYPOINT_ID_ROUTE + ")" +
                        "REFERENCES " + TABLE_ROUTE + "(" + ROUTE_ID + ")" +
                        ");"
        );
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
}
