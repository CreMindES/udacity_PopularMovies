/**
 * Created by cremindes on 21/03/17.
 */

package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.data.FavMovieContract.FavMovieEntry;

public class FavMovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favMovies.db";
    private static final int VERSION = 3;

    public FavMovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + FavMovieEntry.TABLE_NAME + " (" +
                FavMovieEntry.COLUMN_ID           + " INTEGER PRIMARY KEY, " +
                FavMovieEntry.COLUMN_TITLE        + " TEXT NOT NULL, " +
                FavMovieEntry.COLUMN_TITLE_ORIG   + " TEXT NOT NULL, " +
                FavMovieEntry.COLUMN_PLOT         + " TEXT NOT NULL, " +
                FavMovieEntry.COLUMN_BACKDROPPATH + " TEXT NOT NULL, " +
                FavMovieEntry.COLUMN_POSTERPATH   + " TEXT NOT NULL, " +
                FavMovieEntry.COLUMN_RATING       + " REAL NOT NULL, " +
                FavMovieEntry.COLUMN_RELEASEDATE  + " INTEGER NOT NULL );";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavMovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
