package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;

import com.example.android.popularmovies.api.TheMovieDBAPI;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cremindes on 07/02/17.
 */

public class MyMovie implements Parcelable {
    private int id;
    private String title;
    private String originalTitle;
    private String plot;
    private Date date;
    private String dateString;    // TODO: replace with proper class
    private String posterId;
    private String backdropId;
    private URL posterURL;
    private URL backdropURL;
    private double rating;
    private boolean mIsFavourite;
    private ArrayList<MovieVideo> videoList;

    public MyMovie() {
        setTitle( "Title" );
        setPlot( "Plot" );
        setDate( "2017-01-01" );
        setRating( 0.0 );
        setPosterURL( makeUrl( "http://udacity.com" ) );
        setFavourite( false );
    }

    public void setId            ( int x    ) { if( x  > 0    ) { id            = x;   } }
    public void setTitle         ( String s ) { if( s != null ) { title         = s;   } }
    public void setOriginalTitle ( String s ) { if( s != null ) { originalTitle = s;   } }
    public void setPlot          ( String s ) { if( s != null ) { plot          = s;   } }
    public void setDate          ( Date   d ) { if( d != null ) { date          = d;   } }
    public void setDate          ( String s ) { if( s != null ) { date          = stringToDate(s); } }
    public void setRating        ( double r ) { if( r  > 0    ) { rating        = r;   } }
    public void setRating        ( String r ) {
        if( r != null ) {
            double rr = Double.parseDouble( r );
            if( 0 < rr && rr < 10 ) {
                rating = rr;
            }
        }
    }
    public void setPosterURL  ( URL  url ) { if( url != null ) { posterURL   = url; } }
    public void setBackdropURL( URL  url ) { if( url != null ) { backdropURL = url; } }
    public void setPosterUrlId( String newPosterId ) {
        if(newPosterId != null) {
            posterId  = newPosterId;
            posterURL = makeUrl( TheMovieDBAPI.API_IMAGE_HEADER_MEDIUM + posterId );
        }
    }
    public void setBackdropUrlId ( String newBackdropId ) {
        if(newBackdropId != null) {
            backdropId  = newBackdropId;
            backdropURL = makeUrl( TheMovieDBAPI.API_IMAGE_HEADER_LARGE + backdropId );
        }
    }
    public void setVideoList( ArrayList<MovieVideo> v ) { videoList = v; }
    public void setFavourite( boolean b ) { mIsFavourite = b; }


    public int    getId()            { return id;              }
    public String getTitle()         { return title;           }
    public String getOriginalTitle() { return originalTitle;   }
    public String getPlot()          { return plot;            }
    public Date   getDate()          { return date;            }
    public double getRating()        { return rating;          }
    public String getPosterId()      { return posterId;        }
    public String getBackdropId()    { return backdropId;      }
    public URL getPosterURL()        { return posterURL;       }
    public URL getbackdropURL()      { return backdropURL;     }
    public boolean isFavourite()     { return mIsFavourite;    }
    public ArrayList<MovieVideo> getVideoList() { return videoList; }


    public static java.net.URL makeUrl(String urlString) {
        try {
            return new java.net.URL(urlString);
        } catch (java.net.MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    };

    public static Date stringToDate( String dateString ) {
        Date d;
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        try {
            d = dateFormat.parse( dateString );
        } catch ( ParseException e ) {
            e.printStackTrace();
            return null;
        }
        return d;
    }

    public MyMovie( Parcel p ) {
        id     = p.readInt();
        title  = p.readString();
        originalTitle = p.readString();
        plot   = p.readString();
        date   = stringToDate( p.readString() );
        rating = Double.valueOf( p.readString() );
        posterId    = p.readString();
        backdropId  = p.readString();
        posterURL   = makeUrl( p.readString() );
        backdropURL = makeUrl( p.readString() );
        mIsFavourite = p.readInt() == 1 ? true : false;
    }

    public static final Parcelable.Creator<MyMovie> CREATOR = new Parcelable.Creator<MyMovie>() {
        @Override
        public MyMovie createFromParcel( Parcel parcel ) {
            return new MyMovie( parcel );
        }

        @Override
        public MyMovie[] newArray(int size) {
            return new MyMovie[0];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt   ( id     );
        parcel.writeString( title  );
        parcel.writeString( originalTitle );
        parcel.writeString( plot   );
        parcel.writeString( DateFormat.format("yyyy-MM-dd", date).toString() );
        parcel.writeString( String.valueOf(rating) );
        parcel.writeString( posterId );
        parcel.writeString( backdropId );
        parcel.writeString( posterURL.toString() );
        parcel.writeString( backdropURL.toString() );
        parcel.writeInt( mIsFavourite ? 1 : 0 );
    }

    @Override
    public int describeContents() {
        return 0;
    }
};
