package com.example.android.popularmovies;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;

/**
 * Created by cremindes on 17/03/17.
 */

public class MovieVideo implements Parcelable {

    public static final String YOUTUBE_APP_URL = "vnd.youtube://"; //+id
    public static final String YOUTUBE_WEB_URL = "https://www.youtube.com/watch?v=";//+id

    private String id;
    private String title;
    private String key;
    private String type;
    private String site;
    private Uri youtubeAppUri;
    private Uri youtubeWebUri;
    private Uri youtubeThumbnailUri;

    public String getId   () { return id;    }
    public String getTitle() { return title; }
    public String getKey  () { return key;   }
    public String getType () { return type;  }
    public String getSite () { return site;  }
    public Uri getYoutubeAppUri() { return youtubeAppUri; }
    public Uri getYoutubeWebUri() { return youtubeWebUri; }
    public Uri getYoutubeThumbnailUri() { return youtubeThumbnailUri; }

    public void setId    ( String s ) { if( s != null ) { id    = s; } }
    public void setTitle ( String s ) { if( s != null ) { title = s; } }
    public void setType  ( String s ) { if( s != null ) { type  = s; } }
    public void setSite  ( String s ) { if( s != null ) { site  = s; } }
    public void setKey   ( String s ) {
        if( s != null ) {
            key = s;
            youtubeAppUri = Uri.parse( YOUTUBE_APP_URL + key );
            youtubeWebUri = Uri.parse( YOUTUBE_WEB_URL + key );
            youtubeThumbnailUri = Uri.parse( String.format( "http://img.youtube.com/vi/%1$s/0.jpg", key ) );
        }
    }

    public MovieVideo() {  }

    public MovieVideo( Parcel p ) {
        id    = p.readString();
        title = p.readString();
        key   = p.readString();
        type  = p.readString();
        site  = p.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destParcel, int flags) {
        destParcel.writeString( id    );
        destParcel.writeString( title );
        destParcel.writeString( key   );
        destParcel.writeString( type  );
        destParcel.writeString( site  );
    }

    public static final Parcelable.Creator<MovieVideo> CREATOR = new Parcelable.Creator<MovieVideo>() {
        @Override
        public MovieVideo createFromParcel( Parcel parcel ) {
            return new MovieVideo( parcel );
        }

        @Override
        public MovieVideo[] newArray(int size) {
            return new MovieVideo[0];
        }
    };
}
