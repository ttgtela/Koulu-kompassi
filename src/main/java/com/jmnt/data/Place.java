package com.jmnt.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Place {
    private int place_id;
    private String licence;
    private String osm_type;
    private int osm_id;
    private float lat;
    private float lon;
    @JsonProperty("class")
    private String classType;
    private String type;
    private int place_rank;
    private float importance;
    private String addresstype;
    private String name;
    private String display_name;
    private String[] bounding_box;

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getOsm_type() {
        return osm_type;
    }

    public void setOsm_type(String osm_type) {
        this.osm_type = osm_type;
    }

    public int getOsm_id() {
        return osm_id;
    }

    public void setOsm_id(int osm_id) {
        this.osm_id = osm_id;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPlace_rank() {
        return place_rank;
    }

    public void setPlace_rank(int place_rank) {
        this.place_rank = place_rank;
    }

    public float getImportance() {
        return importance;
    }

    public void setImportance(float importance) {
        this.importance = importance;
    }

    public String getAddresstype() {
        return addresstype;
    }

    public void setAddresstype(String addresstype) {
        this.addresstype = addresstype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String[] getBounding_box() {
        return bounding_box;
    }

    public void setBounding_box(String[] bounding_box) {
        this.bounding_box = bounding_box;
    }
}
