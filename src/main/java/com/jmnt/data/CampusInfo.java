package com.jmnt.data;

public class CampusInfo {
    private Coord coord;
    private String type;

    public CampusInfo(Coord coord, String type) {
        this.coord = coord;
        this.type = type;
    }

    public Coord getCoord() {
        return coord;
    }

    public String getType() {
        return type;
    }
}
