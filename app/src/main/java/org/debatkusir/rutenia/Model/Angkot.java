package org.debatkusir.rutenia.Model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Reyhan on 12/14/2016.
 */

public class Angkot {
    int id;
    String name;
    String photo;
    int transitStop1;
    int transitStop2;

    public Angkot(int id, String name, String photo, int transitStop1, int transitStop2) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.transitStop1 = transitStop1;
        this.transitStop2 = transitStop2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getTransitStop1() {
        return transitStop1;
    }

    public void setTransitStop1(int transitStop1) {
        this.transitStop1 = transitStop1;
    }

    public int getTransitStop2() {
        return transitStop2;
    }

    public void setTransitStop2(int transitStop2) {
        this.transitStop2 = transitStop2;
    }
}
