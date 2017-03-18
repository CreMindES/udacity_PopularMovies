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
 * Created by cremindes on 09/02/17.
 */

public class MovieCastAdapter
        extends RecyclerView.Adapter< MovieCastAdapter.MovieCastAdapterViewHolder > {

    private static final String TAG = TheMovieDBAPI.class.getSimpleName();
    private ArrayList<Cast> movieCastData;

    public MovieCastAdapter() { }

    public class MovieCastAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView nameTextView;
        public final TextView characterTextView;
        public final ImageView profileImageView;

        public MovieCastAdapterViewHolder( View view ) {
            super(view);
            nameTextView      = (TextView)  view.findViewById( R.id.movie_cast_item_name );
            characterTextView = (TextView)  view.findViewById( R.id.movie_cast_item_character );
            profileImageView  = (ImageView) view.findViewById( R.id.movie_cast_item_profile_image );
        }
    }

    @Override
    public MovieCastAdapter.MovieCastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        Context context = parent.getContext();
        int layoutIdOfItem = R.layout.cast_item;
        LayoutInflater inflater = LayoutInflater.from( context );
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate( layoutIdOfItem, parent, shouldAttachToParentImmediately );
        return new MovieCastAdapter.MovieCastAdapterViewHolder( view );
    }

    @Override
    public void onBindViewHolder( MovieCastAdapter.MovieCastAdapterViewHolder holder, int position ) {
        Cast cast = movieCastData.get( position );
        Context context = holder.profileImageView.getContext();
        Picasso.with( context )
                .load( cast.getProfileURL().toString() )
                .placeholder( R.drawable.placeholder_100x150 )
                .error( R.drawable.placeholder_100x150 )
                .into( holder.profileImageView );

        holder.nameTextView.setText( cast.getName() );
        holder.characterTextView.setText( cast.getCharacter() );
    }

    @Override
    public int getItemCount() {
        if( movieCastData == null ) { return 0; };
        return movieCastData.size();
    }

    public void setMovieData( ArrayList<Cast> newMovieCastData ) {
        Log.d( TAG, String.valueOf( newMovieCastData.size() ) );
        movieCastData = newMovieCastData;
        notifyDataSetChanged();
    }
}

