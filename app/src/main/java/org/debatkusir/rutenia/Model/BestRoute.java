package org.debatkusir.rutenia.Model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Reyhan on 12/14/2016.
 */

public class BestRoute {
    int idAngkot;
    LatLng originNearestWaypoint;
    int originNearestWaypointIdx;
    LatLng destNearestWaypoint;
    int destNearestWaypointIdx;
    ArrayList<LatLng> route;

    public BestRoute(int idAngkot, LatLng originNearestWaypoint, LatLng destNearestWaypoint, ArrayList<LatLng> route, int originNearestWaypointIdx, int destNearestWaypointIdx) {
        this.destNearestWaypoint = destNearestWaypoint;
        this.idAngkot = idAngkot;
        this.originNearestWaypoint = originNearestWaypoint;
        this.route = route;
        this.originNearestWaypointIdx = originNearestWaypointIdx;
        this.destNearestWaypointIdx = destNearestWaypointIdx;
    }

    public LatLng getDestNearestWaypoint() {
        return destNearestWaypoint;
    }

    public void setDestNearestWaypoint(LatLng destNearestWaypoint) {
        this.destNearestWaypoint = destNearestWaypoint;
    }

    public int getDestNearestWaypointIdx() {
        return destNearestWaypointIdx;
    }

    public void setDestNearestWaypointIdx(int destNearestWaypointIdx) {
        this.destNearestWaypointIdx = destNearestWaypointIdx;
    }

    public int getIdAngkot() {
        return idAngkot;
    }

    public void setIdAngkot(int idAngkot) {
        this.idAngkot = idAngkot;
    }

    public LatLng getOriginNearestWaypoint() {
        return originNearestWaypoint;
    }

    public void setOriginNearestWaypoint(LatLng originNearestWaypoint) {
        this.originNearestWaypoint = originNearestWaypoint;
    }

    public int getOriginNearestWaypointIdx() {
        return originNearestWaypointIdx;
    }

    public void setOriginNearestWaypointIdx(int originNearestWaypointIdx) {
        this.originNearestWaypointIdx = originNearestWaypointIdx;
    }

    public ArrayList<LatLng> getRoute() {
        return route;
    }

    public void setRoute(ArrayList<LatLng> route) {
        this.route = route;
    }
}
