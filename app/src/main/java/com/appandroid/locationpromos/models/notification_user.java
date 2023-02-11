package com.appandroid.locationpromos.models;

public class notification_user {

    int user_id;
    int brand_id;
    public notification_user() {

    }
    public notification_user(int brand_id, int user_id) {
        this.brand_id = brand_id;
        this.user_id  = user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    @Override
    public boolean equals(Object obj)
    {

        if(this == obj)
            return true;
        if(obj == null || obj.getClass()!= this.getClass())
            return false;

        notification_user notuse = (notification_user) obj;

        return (notuse.brand_id == this.brand_id && notuse.user_id == this.user_id);
    }

    @Override
    public int hashCode()
    {

        return this.user_id+this.brand_id;
    }



}
