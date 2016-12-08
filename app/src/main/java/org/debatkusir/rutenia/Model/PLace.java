package org.debatkusir.rutenia.Model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Reyhan on 12/7/2016.
 */

public class Place {
    String id;
    String name;
    String city;
    LatLng coordinate;
    boolean isTransitStop;

    public Place(String city, LatLng coordinate, String id, boolean isTransitStop, String name) {
        this.city = city;
        this.coordinate = coordinate;
        this.id = id;
        this.isTransitStop = isTransitStop;
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LatLng getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(LatLng coordinate) {
        this.coordinate = coordinate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isTransitStop() {
        return isTransitStop;
    }

    public void setTransitStop(boolean transitStop) {
        isTransitStop = transitStop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
