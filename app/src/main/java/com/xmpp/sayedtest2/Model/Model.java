package com.xmpp.sayedtest2.Model;

/**
 * Created by shameem ahsan on 10/01/2018.
 */

public class Model {

    //private variables
    int _id;
    String latitude;
    String longitude;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    // Empty constructor
    public Model(){

    }
    // constructor
    public Model(int id, String latitude, String longitude){
        this._id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // constructor
    public Model(String latitude, String longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

}