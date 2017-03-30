package com.example.android.popularmovies;

import com.example.android.popularmovies.api.TheMovieDBAPI;

import java.net.URL;

/**
 * Created by cremindes on 08/02/17.
 */

public class Cast {
    private int mId;
    private int mCastId;
    private String mCharacter;
    private String mName;
    private URL mProfileURL;

    public int getId() { return mId; }
    public int getCastId() { return mCastId; }
    public String getCharacter() { return mCharacter; }
    public String getName() { return mName; }
    public URL getProfileURL() { return mProfileURL; }

    public void setId( int id ) { if( id > 0 ) { mId = id; } }
    public void setCastId( int castId ) { if( castId > 0 ) { mCastId = castId; } }
    public void setCharacter( String character ) {
        if( character != null ) { mCharacter = character; }
    }
    public void setName( String name ) {
        if( name != null ) {
            mName = name;
        }
    }
    public void setProfileURL( String profilePath ) {
        if( profilePath != null ) {
            mProfileURL = MyMovie.makeUrl( TheMovieDBAPI.API_IMAGE_HEADER_SMALL + profilePath );
        }
    }
}
