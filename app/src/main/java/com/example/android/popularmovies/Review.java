package com.example.android.popularmovies;

import java.net.URL;
import java.util.AbstractCollection;

/**
 * Created by cremindes on 19/03/17.
 */

public class Review {

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

}
