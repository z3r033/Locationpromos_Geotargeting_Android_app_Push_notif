package com.appandroid.locationpromos.models;

public class BrandModel {
        int brand_id;

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    int retailerId ;
         String brand_title;
    byte[]  brand_image ;
    String brand_desc;
    String latitude ;
    String logitude ;
    String notification_text;


    public int getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(int retailerId) {
        this.retailerId = retailerId;
    }

    public String getBrand_title() {
        return brand_title;
    }

    public void setBrand_title(String brand_title) {
        this.brand_title = brand_title;
    }

    public byte[] getBrand_image() {
        return brand_image;
    }

    public void setBrand_image(byte[] brand_image) {
        this.brand_image = brand_image;
    }

    public String getBrand_desc() {
        return brand_desc;
    }

    public void setBrand_desc(String brand_desc) {
        this.brand_desc = brand_desc;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLogitude() {
        return logitude;
    }

    public void setLogitude(String logitude) {
        this.logitude = logitude;
    }

    public String getNotification_text() {
        return notification_text;
    }

    public void setNotification_text(String notification_text) {
        this.notification_text = notification_text;
    }


}
