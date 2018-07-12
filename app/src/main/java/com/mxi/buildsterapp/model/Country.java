package com.mxi.buildsterapp.model;

/**
 * Created by vishal on 27/2/18.
 */

public class Country {

    String country_name;
    String id;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Country() {

    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public Country(String country_name) {
        this.country_name = country_name;
    }
}
