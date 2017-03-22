package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by cremindes on 21/03/17.
 */

public class FavMovieContract {

    public static final String AUTHORITY = "com.example.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAV_MOVIES = "fav_movies";

    public static final class FavMovieEntry implements BaseColumns {
        
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV_MOVIES).build();

        public static final String TABLE_NAME = "fav_movies";

        public static final String COLUMN_ID           = "mid";
        public static final String COLUMN_TITLE        = "title";
        public static final String COLUMN_TITLE_ORIG   = "title_orig";
        public static final String COLUMN_PLOT         = "plot";
        public static final String COLUMN_BACKDROPPATH = "backdroppath";
        public static final String COLUMN_POSTERPATH   = "posterpath";
        public static final String COLUMN_RATING       = "rating";
        public static final String COLUMN_RELEASEDATE  = "releasedate";
    }
}
