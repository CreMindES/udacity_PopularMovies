/**
 * Created by cremindes on 21/03/17.
 */

package com.example.android.popularmovies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.android.popularmovies.data.FavMovieContract;
import com.example.android.popularmovies.data.FavMovieDbHelper;

import static com.example.android.popularmovies.data.FavMovieContract.FavMovieEntry.TABLE_NAME;

// TODO: also store the poster and backdrop images

public class FavMovieContentProvider extends ContentProvider {

    private static final String TAG = FavMovieContentProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavMovieDbHelper favMovieDbHelper;

    public static final int FAV_MOVIES = 100;
    public static final int FAV_MOVIE_WITH_ID = 101;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FavMovieContract.AUTHORITY, FavMovieContract.PATH_FAV_MOVIES, FAV_MOVIES);
        uriMatcher.addURI(FavMovieContract.AUTHORITY, FavMovieContract.PATH_FAV_MOVIES + "/#", FAV_MOVIE_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        favMovieDbHelper = new FavMovieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = favMovieDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case FAV_MOVIES:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = favMovieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAV_MOVIES:
                long id = db.insert( TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(FavMovieContract.FavMovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = favMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int favMovieDeleted;

        switch (match) {
            case FAV_MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                favMovieDeleted = db.delete(TABLE_NAME, FavMovieContract.FavMovieEntry.COLUMN_ID + "=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(favMovieDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return favMovieDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
