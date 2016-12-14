package org.debatkusir.rutenia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.maps.model.LatLng;

import org.debatkusir.rutenia.Model.Angkot;
import org.debatkusir.rutenia.Model.BestRoute;
import org.debatkusir.rutenia.Model.NearbyWaypoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Reyhan on 12/8/2016.
 */

public class LocalDatabase extends SQLiteOpenHelper {

    private final String TABLE_PLACE = "PLACE";
    private final String TABLE_ANGKOT = "ANGKOT";
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

    private final String WAYPOINT_ID_ANGKOT = "Id_angkot";
    private final String WAYPOINT_LATITUDE = "Latitude";
    private final String WAYPOINT_LONGITUDE = "Longitude";
    private final String WAYPOINT_INDEX = "IndexNo";

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

        db.execSQL(
                "CREATE TABLE " + TABLE_WAYPOINT + " (" +
                        WAYPOINT_ID_ANGKOT + " INTEGER NOT NULL, " +
                        WAYPOINT_INDEX + " INTEGER NOT NULL, " +
                        WAYPOINT_LONGITUDE + " TEXT NOT NULL, " +
                        WAYPOINT_LATITUDE + " TEXT NOT NULL, " +
                        "PRIMARY KEY (" + WAYPOINT_LONGITUDE + ", " + WAYPOINT_INDEX + ", " + WAYPOINT_LATITUDE + ", " + WAYPOINT_ID_ANGKOT + ")," +
                        "FOREIGN KEY (" + WAYPOINT_ID_ANGKOT + ") " +
                        "REFERENCES " + TABLE_ANGKOT + "(" + ANGKOT_ID + ")" +
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WAYPOINT + " ;");
        onCreate(db);
    }

    public void insertAngkot(int id, String name, String photo, int idTransitStop_1, int idTransitStop_2) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ANGKOT_ID, id);
        contentValues.put(ANGKOT_NAME, name);
        contentValues.put(ANGKOT_PHOTO, photo);
        contentValues.put(ANGKOT_ID_TRANSIT_STOP_1, idTransitStop_1);
        contentValues.put(ANGKOT_ID_TRANSIT_STOP_2, idTransitStop_2);

        database.insert(TABLE_ANGKOT, null, contentValues);
        database.close();
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

    public void insertWaypoints(String[] waypointData) {
        SQLiteDatabase database = this.getWritableDatabase();

        for (int id = 0; id < waypointData.length; id++) {

            ContentValues contentValues = new ContentValues();

            String[] latLngs = waypointData[id].split("\n");
            for(int index = 0; index < latLngs.length; index++) {
                if (latLngs[index].length() > 0) {
                    String[] latLngArr = latLngs[index].split(" ");

                    contentValues.put(WAYPOINT_ID_ANGKOT, id + 1000);
                    contentValues.put(WAYPOINT_INDEX, index);
                    contentValues.put(WAYPOINT_LATITUDE, ""+latLngArr[0]);
                    contentValues.put(WAYPOINT_LONGITUDE, ""+latLngArr[1]);

                    database.insert(TABLE_WAYPOINT, null, contentValues);
                }
            }
        }
    }

    public String[] getTerminal(String searchTerm){
        final int FOUND_LIMIT = 5;
        ArrayList<String> arr = new ArrayList<>();

        String sql = "";
        sql += "SELECT * FROM " + TABLE_PLACE;
        sql += " WHERE " + PLACE_NAME + " LIKE '%" + searchTerm + "%' AND " + PLACE_IS_TRANSIT_STOP + " = 1";
        sql += " LIMIT 0," + FOUND_LIMIT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(PLACE_NAME));
                arr.add(name);

            } while (cursor.moveToNext());
        }
        String[] result = new String[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            result[i] = arr.get(i);
        }
        return result;
    }

    public String[] getAllPlaces(String searchTerm) {
        final int FOUND_LIMIT = 5;
        ArrayList<String> arr = new ArrayList<>();

        String sql = "";
        sql += "SELECT * FROM " + TABLE_PLACE;
        sql += " WHERE " + PLACE_NAME + " LIKE '%" + searchTerm + "%'";
        sql += " LIMIT 0," + FOUND_LIMIT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(PLACE_NAME));
                arr.add(name);

            } while (cursor.moveToNext());
        }
        String[] result = new String[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            result[i] = arr.get(i);
        }
        return result;
    }

    public ArrayList<LatLng> getWaypointsByAngkotName(String angkotName) {

        String sql = "";
        sql += "SELECT " + WAYPOINT_LATITUDE + "," + WAYPOINT_LONGITUDE;
        sql += " FROM " + TABLE_WAYPOINT + " , " + TABLE_ANGKOT;
        sql += " WHERE " + ANGKOT_NAME + " = '" + angkotName + "' AND " + ANGKOT_ID + " = " + WAYPOINT_ID_ANGKOT;
        sql += " ORDER BY " + WAYPOINT_INDEX + " ASC;";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        ArrayList<LatLng> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String latStr = cursor.getString(cursor.getColumnIndex(WAYPOINT_LATITUDE));
                String lngStr = cursor.getString(cursor.getColumnIndex(WAYPOINT_LONGITUDE));
                LatLng coordinate = new LatLng(Double.parseDouble(latStr), Double.parseDouble(lngStr));
                result.add(coordinate);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return result;
    }

    public ArrayList<LatLng> getAllWaypointsByIdAngkot(int idAngkot) {

        String sql = "";
        sql += "SELECT " + WAYPOINT_LATITUDE + "," + WAYPOINT_LONGITUDE;
        sql += " FROM " + TABLE_WAYPOINT + " , " + TABLE_ANGKOT;
        sql += " WHERE " + WAYPOINT_ID_ANGKOT + " = '" + idAngkot + "' AND " + ANGKOT_ID + " = " + WAYPOINT_ID_ANGKOT;
        sql += " ORDER BY " + WAYPOINT_INDEX + " ASC;";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        ArrayList<LatLng> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String latStr = cursor.getString(cursor.getColumnIndex(WAYPOINT_LATITUDE));
                String lngStr = cursor.getString(cursor.getColumnIndex(WAYPOINT_LONGITUDE));
                LatLng coordinate = new LatLng(Double.parseDouble(latStr), Double.parseDouble(lngStr));
                result.add(coordinate);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

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

    public Angkot getAngkotById (int id) {

        String sql = "";
        sql += "SELECT *";
        sql += " FROM " + TABLE_ANGKOT;
        sql += " WHERE " + ANGKOT_ID + " = '" + id + "'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(ANGKOT_NAME));
            String photo = cursor.getString(cursor.getColumnIndex(ANGKOT_PHOTO));
            int ts1 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ANGKOT_ID_TRANSIT_STOP_1)));
            int ts2 = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ANGKOT_ID_TRANSIT_STOP_2)));

            return new Angkot(id,name,photo,ts1,ts2);
        }

        return null;
    }

    public String[] getAngkotByTerminalId (int terminalId) {
        ArrayList<String> arr = new ArrayList<>();

        String sql = "";
        sql += "SELECT * FROM " + TABLE_ANGKOT;
        sql += " WHERE " + ANGKOT_ID_TRANSIT_STOP_1 + " = '" + terminalId + "' OR " + ANGKOT_ID_TRANSIT_STOP_2 + " = '" + terminalId + "'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(ANGKOT_NAME));
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

    public String[] getGambarAngkot (String nomorAngkot) {
        ArrayList<String> arr = new ArrayList<>();

        String sql = "";
        sql += "SELECT * FROM " + TABLE_ANGKOT;
        sql += " WHERE " + ANGKOT_NAME + " LIKE '%" + nomorAngkot + "%'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(ANGKOT_PHOTO));
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

    public String[] getTerminalbyTerminalId(String terminalId){
        final int FOUND_LIMIT = 5;
        ArrayList<String> arr = new ArrayList<>();

        String sql = "";
        sql += "SELECT * FROM " + TABLE_PLACE;
        sql += " WHERE " + PLACE_ID + " = " + terminalId;
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

    public String getTerminalById(int id) {
        String sql = "";
        sql += "SELECT * FROM " + TABLE_PLACE;
        sql += " WHERE " + PLACE_ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(PLACE_NAME));
        }

        return null;
    }

    public String[] getTrayek (String nomorAngkot) {
        ArrayList<String> arr = new ArrayList<>();

        String sql = "";
        sql += "SELECT * FROM " + TABLE_ANGKOT;
        sql += " WHERE " + ANGKOT_NAME + " LIKE '%" + nomorAngkot + "%'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do {
                String terminal1 = cursor.getString(cursor.getColumnIndex(ANGKOT_ID_TRANSIT_STOP_1));
                String terminal2 = cursor.getString(cursor.getColumnIndex(ANGKOT_ID_TRANSIT_STOP_2));
                arr.add(terminal1);
                arr.add(terminal2);

                count++;
            } while (cursor.moveToNext());
        }
        String[] result = new String[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            result[i] = arr.get(i);
        }
        return result;
    }

    public LatLng getCoordinatePlaceByName(String placeName) {
        String sql = "";
        sql += "SELECT " + PLACE_COORDINATE;
        sql += " FROM " + TABLE_PLACE;
        sql += " WHERE " + PLACE_NAME + " = '" + placeName + "';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        LatLng result = null;
        if (cursor.moveToFirst()) {
            do {
                String[] latLng = cursor.getString(cursor.getColumnIndex(PLACE_COORDINATE)).split(", ");
                result = new LatLng(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return result;
    }

    /* Mengembalikan NearbyWaypoint sebagai Key, dan jarak sebagai Value*/
    public HashMap<NearbyWaypoint, Double> getNearbyWaypoints(LatLng target) {

        Log.d("ceklokasi","di " + target);

        String distance = "(" + WAYPOINT_LATITUDE + " - (" + target.latitude +"))*(" + WAYPOINT_LATITUDE + " - (" + target.latitude +"))";
        distance +=  "+ (" + WAYPOINT_LONGITUDE + " - (" + target.longitude +"))*("+ WAYPOINT_LONGITUDE + " - (" + target.longitude +"))";

        final String WAYPOINT_DISTANCE = "distance";

        String sql = "";
        sql += "SELECT DISTINCT " + WAYPOINT_ID_ANGKOT + "," + WAYPOINT_INDEX + "," + " min("+ distance +") as " + WAYPOINT_DISTANCE + ", " + WAYPOINT_LATITUDE + ", " + WAYPOINT_LONGITUDE;
        sql += " FROM " + TABLE_WAYPOINT + "," + TABLE_ANGKOT;
        sql += " WHERE " + ANGKOT_ID + "=" + WAYPOINT_ID_ANGKOT + " AND " + distance + " < 0.0051";
        sql += " GROUP BY " + WAYPOINT_ID_ANGKOT +";";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        HashMap <NearbyWaypoint, Double> result = new HashMap<>();

        if (cursor.moveToFirst()) {
            do {
                int idAngkot = Integer.parseInt(cursor.getString(cursor.getColumnIndex(WAYPOINT_ID_ANGKOT)));
                int index = Integer.parseInt(cursor.getString(cursor.getColumnIndex(WAYPOINT_INDEX)));
                double lat = Double.parseDouble(cursor.getString(cursor.getColumnIndex(WAYPOINT_LATITUDE)));
                double lng = Double.parseDouble(cursor.getString(cursor.getColumnIndex(WAYPOINT_LONGITUDE)));
                double minDistance = Double.parseDouble(cursor.getString(cursor.getColumnIndex(WAYPOINT_DISTANCE)));

                Log.d("getroute","get from origin: " + idAngkot + " " + index + " " + lat + " " + lng + " " + minDistance);
                NearbyWaypoint nearbyWaypoint = new NearbyWaypoint(idAngkot, index, new LatLng(lat, lng));
                result.put(nearbyWaypoint, minDistance);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return result;
    }

    public HashMap<NearbyWaypoint, Double> getNearbyWaypointsForIdAngkot(int[] idAngkotList, LatLng target) {

        String distance = "(" + WAYPOINT_LATITUDE + " - (" + target.latitude +"))*(" + WAYPOINT_LATITUDE + " - (" + target.latitude +"))";
        distance +=  "+ (" + WAYPOINT_LONGITUDE + " - (" + target.longitude +"))*("+ WAYPOINT_LONGITUDE + " - (" + target.longitude +"))";

        final String WAYPOINT_DISTANCE = "distance";

        String sql = "";
        sql += "SELECT DISTINCT " + WAYPOINT_ID_ANGKOT + "," + WAYPOINT_INDEX + "," + " min("+ distance +") as " + WAYPOINT_DISTANCE + ", " + WAYPOINT_LATITUDE + ", " + WAYPOINT_LONGITUDE;
        sql += " FROM " + TABLE_WAYPOINT + "," + TABLE_ANGKOT + " WHERE (";
        for (int i = 0; i < idAngkotList.length; i++) {
            sql += WAYPOINT_ID_ANGKOT + " = " + idAngkotList[i];
            if (i != idAngkotList.length - 1) {
                sql += " OR ";
            }
        }
        sql += ") AND " + distance + " < 0.0051";
        sql += " GROUP BY " + WAYPOINT_ID_ANGKOT +";";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        HashMap <NearbyWaypoint, Double> result = new HashMap<>();

        if (cursor.moveToFirst()) {
            do {
                int idAngkot = Integer.parseInt(cursor.getString(cursor.getColumnIndex(WAYPOINT_ID_ANGKOT)));
                int index = Integer.parseInt(cursor.getString(cursor.getColumnIndex(WAYPOINT_INDEX)));
                double lat = Double.parseDouble(cursor.getString(cursor.getColumnIndex(WAYPOINT_LATITUDE)));
                double lng = Double.parseDouble(cursor.getString(cursor.getColumnIndex(WAYPOINT_LONGITUDE)));
                double minDistance = Double.parseDouble(cursor.getString(cursor.getColumnIndex(WAYPOINT_DISTANCE)));

                Log.d("getroute","get from destination: " + idAngkot + " " + index + " " + lat + " " + lng + " " + minDistance);
                NearbyWaypoint nearbyWaypoint = new NearbyWaypoint(idAngkot, index, new LatLng(lat, lng));
                result.put(nearbyWaypoint, minDistance);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return result;
    }

    public BestRoute getNearbyAngkotRoute(LatLng originCoordinate, LatLng destCoordinate) {

        HashMap <NearbyWaypoint, Double> nearbyWaypointsWithOrigin = getNearbyWaypoints(originCoordinate);
        Iterator originWPiterator = nearbyWaypointsWithOrigin.entrySet().iterator();

        // Kalau ga ada rute yang deket (misal kalo nyari di Ambon)
        if (nearbyWaypointsWithOrigin.isEmpty()) {
            return null;
        }

        SparseArray<Double> distanceToOrigin = new SparseArray<>(); // Key-nya idAngkot, Value-nya distance thd waypoint terdekat
        ArrayList<NearbyWaypoint> originNearbyWaypointContainer = new ArrayList<>();


        while (originWPiterator.hasNext()) {
            Map.Entry pair = (Map.Entry)originWPiterator.next();
            NearbyWaypoint waypoint = (NearbyWaypoint) pair.getKey();
            originNearbyWaypointContainer.add(waypoint);

            int idAngkot = waypoint.getIdAngkot();
            double distance = (Double) pair.getValue();

            distanceToOrigin.put(idAngkot, distance);
        }

        int arrSize = distanceToOrigin.size();
        int[] idAngkotList = new int[arrSize];

        for (int i = 0; i < arrSize; i++) {
            int key = distanceToOrigin.keyAt(i);
            idAngkotList[i] = key;
        }

        HashMap <NearbyWaypoint, Double> nearbyWaypointsWithDestination =  getNearbyWaypointsForIdAngkot(idAngkotList, destCoordinate);
        Iterator destWPiterator = nearbyWaypointsWithDestination.entrySet().iterator();

        if (nearbyWaypointsWithDestination.isEmpty()) {
            return null;
        }

        double minSumDistance = Double.MAX_VALUE;
        int idAngkotResult = 0;

        NearbyWaypoint nearbyWaypointOrigin = new NearbyWaypoint(-1, -1, new LatLng(-1,-1));
        NearbyWaypoint nearbyWaypointDest = new NearbyWaypoint(-1, -1, new LatLng(-1,-1));

        while (destWPiterator.hasNext()) {
            Map.Entry pair = (Map.Entry) destWPiterator.next();
            NearbyWaypoint waypoint = (NearbyWaypoint) pair.getKey();

            int idAngkot = waypoint.getIdAngkot();
            double distanceToDestination = (Double) pair.getValue();
            double sumDistance = distanceToDestination + distanceToOrigin.get(idAngkot);

            if (sumDistance <= minSumDistance) {
                minSumDistance = sumDistance;
                idAngkotResult = idAngkot;

                nearbyWaypointDest = waypoint;
                for (NearbyWaypoint nearbyWaypoint: originNearbyWaypointContainer) {
                    if (nearbyWaypoint.compareTo(nearbyWaypointDest) == 0) {
                        nearbyWaypointOrigin = nearbyWaypoint;
                    }
                }
            }
        }

        ArrayList<LatLng> route = getAllWaypointsByIdAngkot(idAngkotResult);
        return new BestRoute(idAngkotResult, nearbyWaypointOrigin.getWaypoint(), nearbyWaypointDest.getWaypoint(), route, nearbyWaypointOrigin.getIndex(), nearbyWaypointDest.getIndex());
    }

}

