package com.mateus.tripadvisorapi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mateus on 22/12/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int version = 1;

    public static final String TABLE_RESTAURANT = "restaurant";
    public static final String TABLE_FAVORITOS = "favoritos";
    public static final String TABLE_RESTAURANT_ID = "restaurant_id";


    public static final String COLUMN_ID_LOCATION = "id_location";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LON = "lon";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_IMG_URL = "img_url";
    public static final String COLUMN_URL = "url";

    public DatabaseHelper(Context context) {
        super(context, "banco.db", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE " + TABLE_RESTAURANT + "(" +
                    "_id            INTEGER     PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_ID_LOCATION  + " TEXT        UNIQUE," +
                    COLUMN_TITLE        + " TEXT        NOT NULL," +
                    COLUMN_ADDRESS      + " TEXT        NOT NULL," +
                    COLUMN_CITY         + " TEXT        NOT NULL," +
                    COLUMN_LAT          + " REAL        NOT NULL," +
                    COLUMN_LON          + " REAL        NOT NULL," +
                    COLUMN_RATING       + " REAL," +
                    COLUMN_PRICE        + " TEXT," +
                    COLUMN_PHONE        + " TEXT," +
                    COLUMN_IMG_URL      + " TEXT," +
                    COLUMN_URL          + " TEXT       UNIQUE);");

        db.execSQL("CREATE TABLE " + TABLE_FAVORITOS + "(" +
                "_id            INTEGER     PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ID_LOCATION  + " TEXT        UNIQUE," +
                COLUMN_TITLE        + " TEXT        NOT NULL," +
                COLUMN_ADDRESS      + " TEXT        NOT NULL," +
                COLUMN_RATING       + " REAL," +
                COLUMN_PRICE        + " TEXT," +
                COLUMN_PHONE        + " TEXT,"+
                COLUMN_IMG_URL      + " TEXT);"
        );

        db.execSQL("CREATE TABLE " + TABLE_RESTAURANT_ID + "(" +
                "_id            INTEGER     PRIMARY KEY AUTOINCREMENT," +
                COLUMN_ID_LOCATION  + " TEXT        UNIQUE);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
