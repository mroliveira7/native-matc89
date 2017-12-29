package com.mateus.tripadvisorapi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mateus on 22/12/17.
 */

public class LocalizacaoDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private String[] allColumns = {DatabaseHelper.COLUMN_ID_LOCATION, DatabaseHelper.COLUMN_TITLE,
            DatabaseHelper.COLUMN_ADDRESS, DatabaseHelper.COLUMN_CITY, DatabaseHelper.COLUMN_LAT,
            DatabaseHelper.COLUMN_LON, DatabaseHelper.COLUMN_RATING, DatabaseHelper.COLUMN_PRICE,
            DatabaseHelper.COLUMN_PHONE, DatabaseHelper.COLUMN_IMG_URL, DatabaseHelper.COLUMN_URL};

    public LocalizacaoDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insetLocalizacao(Localizacao localizacao) {
        long result;

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ID_LOCATION, localizacao.getId_location());
        values.put(DatabaseHelper.COLUMN_TITLE, localizacao.getTitle());
        values.put(DatabaseHelper.COLUMN_ADDRESS, localizacao.getAddress());
        values.put(DatabaseHelper.COLUMN_CITY, localizacao.getCity());
        values.put(DatabaseHelper.COLUMN_LAT, localizacao.getLat());
        values.put(DatabaseHelper.COLUMN_LON, localizacao.getLon());
        values.put(DatabaseHelper.COLUMN_RATING, localizacao.getRating());
        values.put(DatabaseHelper.COLUMN_PRICE, localizacao.getPrice());
        values.put(DatabaseHelper.COLUMN_PHONE, localizacao.getPhone());
        values.put(DatabaseHelper.COLUMN_IMG_URL, localizacao.getImg_url());
        values.put(DatabaseHelper.COLUMN_URL, localizacao.getUrl());

        result = db.insert(DatabaseHelper.TABLE_RESTAURANT, null, values);

        return result;
    }

    public ArrayList<Localizacao> getAllRestaurants(String city) {
        ArrayList<Localizacao> localizacoes = new ArrayList<Localizacao>();
        String query = "SELECT id_location, title, address, city, lat, lon, rating, price, phone, img_url, url" +
                " FROM " + DatabaseHelper.TABLE_RESTAURANT +
                " WHERE " + DatabaseHelper.COLUMN_CITY + " = '" + city +"'" +
                " ORDER BY " + DatabaseHelper.COLUMN_TITLE;

        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Localizacao localizacao = cursorToLocalizacao(cursor);
            localizacoes.add(localizacao);
            cursor.moveToNext();
        }

        cursor.close();
        return localizacoes;
    }

    public ArrayList<Localizacao> getAllRestaurantsByFavoritos() {
        ArrayList<Localizacao> localizacoes = new ArrayList<Localizacao>();
        String query = "SELECT res.id_location, title, address, city, lat, lon, rating, price, phone, img_url, url FROM " + DatabaseHelper.TABLE_RESTAURANT + " as res " +
                "INNER JOIN " + DatabaseHelper.TABLE_RESTAURANT_ID + " as res_id " +
                "ON res.id_location = res_id.id_location " +
                "ORDER BY " + DatabaseHelper.COLUMN_TITLE;

        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Localizacao localizacao = cursorToLocalizacao(cursor);
            localizacoes.add(localizacao);
            cursor.moveToNext();
        }

        cursor.close();
        return localizacoes;
    }

    public long  insertId(String id_favorito) {
        Log.d("teste", "id é  " + id_favorito);

        long result;
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_ID_LOCATION, id_favorito);

        result = db.insert(DatabaseHelper.TABLE_RESTAURANT_ID, null, cv);
        return result;
    }

    public boolean databaseHasCity(String city) {
        String query = "SELECT " + DatabaseHelper.COLUMN_CITY +
                " FROM " + DatabaseHelper.TABLE_RESTAURANT +
                " WHERE " + DatabaseHelper.COLUMN_CITY + " = '" + city + "'" +
                " LIMIT 1;";

        Cursor cursor = db.rawQuery(query,null);

        Log.i("COUNT", Integer.toString(cursor.getCount()));
        if (cursor.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean localizacaoIsFavorito(String id_location) {
        String query = "SELECT * " +
                " FROM " + DatabaseHelper.TABLE_RESTAURANT_ID +
                " WHERE " + DatabaseHelper.COLUMN_ID_LOCATION + " = '" + id_location + "'" +
                " LIMIT 1;";

        Cursor cursor = db.rawQuery(query,null);

        Log.i("COUNT", Integer.toString(cursor.getCount()));
        if (cursor.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public long removeId(String id_location) {
        long result = db.delete(DatabaseHelper.TABLE_RESTAURANT_ID, DatabaseHelper.COLUMN_ID_LOCATION + " = " + "'" + id_location + "'", null);
        return result;
    }

    private Localizacao cursorToLocalizacao (Cursor cursor) {
        Localizacao loc = new Localizacao();

        loc.setId_location(cursor.getString(0));
        loc.setTitle(cursor.getString(1));
        loc.setAddress(cursor.getString(2));
        loc.setCity(cursor.getString(3));
        loc.setLat(cursor.getFloat(4));
        loc.setLon(cursor.getFloat(5));
        loc.setRating(cursor.getFloat(6));
        loc.setPrice(cursor.getString(7));
        loc.setPhone(cursor.getString(8));
        loc.setImg_url(cursor.getString(9));
        loc.setUrl(cursor.getString(10));

        return loc;
    }
}
