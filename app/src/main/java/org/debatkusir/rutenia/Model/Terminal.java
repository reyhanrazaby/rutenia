package org.debatkusir.rutenia.Model;

/**
 * Created by Reyhan on 12/7/2016.
 */

public class Terminal {
    String id;
    String name;
    String city;
    Coordinate coordinate;

    public Terminal(String city, Coordinate coordinate, String id, String name) {
        this.city = city;
        this.coordinate = coordinate;
        this.id = id;
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
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
}
