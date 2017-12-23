package com.mateus.tripadvisorapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lucas on 13/12/2017.
 */

public class Localizacao implements Parcelable {
    private String id_location;
    private String title;
    private String address;
    private String city;
    private float lat;
    private float lon;
    private float rating;
    private String price;
    private String phone;
    private String img_url;
    private String url;

    public Localizacao(String id_location, String title, String address, String city, float lat, float lon, float rating, String price, String phone, String img_url, String url) {
        this.id_location = id_location;
        this.title = title;
        this.address = address;
        this.city = city;
        this.lat = lat;
        this.lon = lon;
        this.rating = rating;
        this.price = price;
        this.phone = phone;
        this.img_url = img_url;
        this.url = url;
    }

    public Localizacao () {

    }

    protected Localizacao(Parcel in) {
        id_location = in.readString();
        title = in.readString();
        address = in.readString();
        city = in.readString();
        lat = in.readFloat();
        lon = in.readFloat();
        rating = in.readFloat();
        price = in.readString();
        phone = in.readString();
        img_url = in.readString();
        url = in.readString();
    }

    public static final Creator<Localizacao> CREATOR = new Creator<Localizacao>() {
        @Override
        public Localizacao createFromParcel(Parcel in) {
            return new Localizacao(in);
        }

        @Override
        public Localizacao[] newArray(int size) {
            return new Localizacao[size];
        }
    };

    public String getId_location() {
        return id_location;
    }

    public void setId_location(String id_location) {
        this.id_location = id_location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id_location);
        parcel.writeString(title);
        parcel.writeString(address);
        parcel.writeString(city);
        parcel.writeFloat(lat);
        parcel.writeFloat(lon);
        parcel.writeFloat(rating);
        parcel.writeString(price);
        parcel.writeString(phone);
        parcel.writeString(img_url);
        parcel.writeString(url);
    }
}
