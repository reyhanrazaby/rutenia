package org.debatkusir.rutenia.Model;

/**
 * Created by Reyhan on 12/7/2016.
 */

public class Angkot {
    String id;
    String name;
    Place transitStop1;
    Place transitStop2;

    public Angkot(String id, String name, Place transitStop1, Place transitStop2) {
        this.id = id;
        this.name = name;
        this.transitStop1 = transitStop1;
        this.transitStop2 = transitStop2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Place getTransitStop2() {
        return transitStop2;
    }

    public void setTransitStop2(Place transitStop2) {
        this.transitStop2 = transitStop2;
    }

    public Place getTransitStop1() {
        return transitStop1;
    }

    public void setTransitStop1(Place transitStop1) {
        this.transitStop1 = transitStop1;
    }
}
