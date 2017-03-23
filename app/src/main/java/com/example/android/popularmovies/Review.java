/**
 * Created by cremindes on 19/03/17.
 */

package com.example.android.popularmovies;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractCollection;

public class Review implements Parcelable {

    private String mId;
    private String mAuthor;
    private String mContent;
    private URL mUrl;

    public String getId()      { return mId;      }
    public String getAuthor()  { return mAuthor;  }
    public String getContent() { return mContent; }
    public URL getUrl()        { return mUrl;     }

    public void setId( String id ) { if( id != null ) { mId = id; } }
    public void setAuthor( String author ) { if( author != null ) { mAuthor = author; } }
    public void setContent( String content ) { if( content != null ) { mContent = content; } }
    public void setUrl( URL url ) { mUrl = url; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString( mId );
        dest.writeString( mAuthor );
        dest.writeString( mContent );
        dest.writeString( mUrl.toString() );
    }
    public Review() { }

    public Review( Parcel p ) {
        mId      = p.readString();
        mAuthor  = p.readString();
        mContent = p.readString();
        try {
            mUrl = new URL(p.readString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel( Parcel parcel ) {
            return new Review( parcel );
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[0];
        }
    };
}
