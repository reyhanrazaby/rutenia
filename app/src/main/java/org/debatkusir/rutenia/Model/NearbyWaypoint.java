package org.debatkusir.rutenia.Model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Reyhan on 12/7/2016.
 */

public class NearbyWaypoint implements Comparable<NearbyWaypoint>{
    int idAngkot;
    int index;
    LatLng waypoint;

    public NearbyWaypoint(int idAngkot, int index, LatLng waypoint) {
        this.idAngkot = idAngkot;
        this.index = index;
        this.waypoint = waypoint;
    }

    public int getIdAngkot() {
        return idAngkot;
    }

    public void setIdAngkot(int idAngkot) {
        this.idAngkot = idAngkot;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public LatLng getWaypoint() {
        return waypoint;
    }

    public void setWaypoint(LatLng waypoint) {
        this.waypoint = waypoint;
    }

    @Override
    public int compareTo(NearbyWaypoint o) {
        return idAngkot - o.getIdAngkot();
    }
}
