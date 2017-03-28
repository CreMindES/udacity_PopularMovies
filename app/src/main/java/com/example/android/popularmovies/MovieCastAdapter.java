package com.example.android.popularmovies;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.popularmovies.databinding.CastItemBinding;
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

        private final CastItemBinding binding;

        public MovieCastAdapterViewHolder(CastItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public MovieCastAdapter.MovieCastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final CastItemBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.cast_item, parent, false);
        return new MovieCastAdapter.MovieCastAdapterViewHolder( binding );
    }

    @Override
    public void onBindViewHolder( MovieCastAdapter.MovieCastAdapterViewHolder holder, int position ) {
        Cast cast = movieCastData.get( position );
        holder.binding.setCast(cast);
        holder.binding.executePendingBindings();

        Context context = holder.binding.movieCastItemProfileImage.getContext();
        Picasso.with( context )
                .load( cast.getProfileURL().toString() )
                .placeholder( R.drawable.placeholder_100x150 )
                .error( R.drawable.placeholder_100x150 )
                .into( holder.binding.movieCastItemProfileImage );
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

