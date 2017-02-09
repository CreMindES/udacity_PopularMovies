package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by cremindes on 07/02/17.
 */

public class MovieAdapter extends RecyclerView.Adapter< MovieAdapter.MovieAdapterViewHolder >
{
    private static final String TAG = TheMovieDBAPI.class.getSimpleName();
    private ArrayList<MyMovie> movieData;

    public MovieAdapter( MovieAdapterOnClickHandler clickHandler ) {
        mClickHandler = clickHandler;
    }

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final MovieAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieAdapterOnClickHandler {
        void onClick( MyMovie movie );
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView  titleTextView;
        public final ImageView posterImageView;

        public MovieAdapterViewHolder( View view ) {
            super(view);
            titleTextView   = (TextView)  view.findViewById( R.id.movie_item_title );
            posterImageView = (ImageView) view.findViewById( R.id.movie_item_poster );
            view.setOnClickListener( this );
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            Log.d( TAG, "onClick" );
            int adapterPosition = getAdapterPosition();
            MyMovie m = movieData.get( adapterPosition );
            mClickHandler.onClick( m );
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        Context context = parent.getContext();
        int layoutIdOfItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from( context );
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate( layoutIdOfItem, parent, shouldAttachToParentImmediately );
        return new MovieAdapterViewHolder( view );
    }

    @Override
    public void onBindViewHolder( MovieAdapterViewHolder holder, int position ) {
        MyMovie movie = movieData.get( position );
        String movieTitle = movie.getTitle();
        Context context = holder.posterImageView.getContext();
        Picasso.with( context ).load( movie.getPosterURL().toString() ).into( holder.posterImageView );
        holder.titleTextView.setText( movieTitle );
    }

    @Override
    public int getItemCount() {
        if( movieData == null ) { return 0; };
        return movieData.size();
    }

    public void setMovieData( ArrayList<MyMovie> newMovieData ) {
        Log.d( TAG, String.valueOf( newMovieData.size() ) );
        movieData = newMovieData;
        notifyDataSetChanged();
    }
}
