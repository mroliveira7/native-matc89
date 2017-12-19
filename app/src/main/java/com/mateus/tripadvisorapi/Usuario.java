package com.mateus.tripadvisorapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lucka on 19/12/2017.
 */

public class Usuario implements Parcelable {
    private String id;
    private String name;
    private String img_url;

    public Usuario(String id, String name, String img_url) {
        this.id = id;
        this.name = name;
        this.img_url = img_url;
    }

    protected Usuario(Parcel in) {
        id = in.readString();
        name = in.readString();
        img_url = in.readString();
    }

    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

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

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(img_url);
    }
}
