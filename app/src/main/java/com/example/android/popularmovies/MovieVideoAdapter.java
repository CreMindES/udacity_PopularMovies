package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by cremindes on 16/03/17.
 */

public class MovieVideoAdapter
        extends RecyclerView.Adapter< MovieVideoAdapter.MovieVideoAdapterViewHolder > {

    private static final String TAG = MovieVideoAdapter.class.getSimpleName();
    private ArrayList<MovieVideo> movieVideoListData;

    public MovieVideoAdapter( MovieVideoAdapterOnClickHandler clickHandler ) {
        mClickHandler = clickHandler;
    }

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final MovieVideoAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieVideoAdapterOnClickHandler {
        void onClick( MovieVideo movieVideo );
    }

    public class MovieVideoAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public ImageView videoThumbnailImage;
        public ImageButton videoThumbnailIcon;

        public MovieVideoAdapterViewHolder( View view ) {
            super(view);
            videoThumbnailImage = (ImageView) view.findViewById( R.id.movie_video_thumbnail );
            videoThumbnailImage.setOnClickListener(this);
            videoThumbnailIcon = (ImageButton) view.findViewById( R.id.movie_video_thumbnail_icon );
            videoThumbnailImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d( TAG, "onClick" );
            int adapterPosition = getAdapterPosition();
            MovieVideo movieVideo = movieVideoListData.get( adapterPosition );
            mClickHandler.onClick( movieVideo );
        }
    }

    @Override
    public MovieVideoAdapter.MovieVideoAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        Context context = parent.getContext();
        int layoutIdOfItem = R.layout.video_item;
        LayoutInflater inflater = LayoutInflater.from( context );
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate( layoutIdOfItem, parent, shouldAttachToParentImmediately );
        return new MovieVideoAdapter.MovieVideoAdapterViewHolder( view );
    }

    @Override
    public void onBindViewHolder( MovieVideoAdapter.MovieVideoAdapterViewHolder holder, int position ) {
        MovieVideo movieVideo = movieVideoListData.get( position );
        Context context = holder.videoThumbnailImage.getContext();
        Picasso.with( context )
                .load( movieVideo.getYoutubeThumbnailUri() )
                .placeholder( R.drawable.placeholder_100x150 )
                .error( R.drawable.placeholder_100x150 )
                .into( holder.videoThumbnailImage );

        // like http://img.youtube.com/vi/7aiUAaMbR64/0.jpg
    }

    @Override
    public int getItemCount() {
        if( movieVideoListData == null ) { return 0; };
        return movieVideoListData.size();
    }

    public void setMovieData( ArrayList<MovieVideo> newMovieVideoListData ) {
        Log.d( TAG, String.valueOf( newMovieVideoListData.size() ) );
        movieVideoListData = newMovieVideoListData;
        notifyDataSetChanged();
    }
}
