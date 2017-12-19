package com.mateus.tripadvisorapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lucas on 19/12/2017.
 */

public class Comentarios implements Parcelable{
    private String id;
    private float rating;
    private Usuario user;
    private String text;
    private String url;

    public Comentarios(String id, float rating, Usuario user, String text, String url) {
        this.id = id;
        this.rating = rating;
        this.user = user;
        this.text = text;
        this.url = url;
    }

    protected Comentarios(Parcel in) {
        user = (Usuario) in.readParcelable(Usuario.class.getClassLoader());   //Nao sei se isso ta certo
        id = in.readString();
        rating = in.readFloat();
        text = in.readString();
        url = in.readString();
    }

    public static final Parcelable.Creator<Comentarios> CREATOR = new Parcelable.Creator<Comentarios>() {
        @Override
        public Comentarios createFromParcel(Parcel in) {
            return new Comentarios(in);
        }

        @Override
        public Comentarios[] newArray(int size) {
            return new Comentarios[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        parcel.writeParcelable(user, i);     //Nao sei se isso ta certo
        parcel.writeString(id);
        parcel.writeFloat(rating);
        parcel.writeString(text);
        parcel.writeString(url);
    }
}
