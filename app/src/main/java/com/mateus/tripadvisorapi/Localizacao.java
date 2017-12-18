package com.mateus.tripadvisorapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lucas on 13/12/2017.
 */

public class Localizacao implements Parcelable {
    private String id;
    private String title;
    private String address;
    private float rating;
    private String price;
    private String phone;
    private String img_url;
    private String url;

    public Localizacao(String id, String title, String address, float rating, String price, String phone, String img_url, String url) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.rating = rating;
        this.price = price;
        this.phone = phone;
        this.img_url = img_url;
        this.url = url;
    }

    protected Localizacao(Parcel in) {
        id = in.readString();
        title = in.readString();
        address = in.readString();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(address);
        parcel.writeFloat(rating);
        parcel.writeString(price);
        parcel.writeString(phone);
        parcel.writeString(img_url);
        parcel.writeString(url);
    }
}
