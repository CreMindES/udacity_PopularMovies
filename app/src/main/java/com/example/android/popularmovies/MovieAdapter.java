/**
 * Created by cremindes on 07/02/17.
 */

package com.example.android.popularmovies;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.databinding.MovieItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter< MovieAdapter.MovieAdapterViewHolder >
{
    private static final String TAG = MovieAdapter.class.getSimpleName();
    private LayoutInflater layoutInflater;
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

        private MovieItemBinding binding;

        public MovieAdapterViewHolder(MovieItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            Log.d(TAG, "child onClick");
            int adapterPosition = getAdapterPosition();
            MyMovie m = movieData.get(adapterPosition);
            mClickHandler.onClick(m);
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        final MovieItemBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.movie_item, viewGroup, false);
        return new MovieAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        MyMovie movie = movieData.get(position);
        holder.binding.setMovie(movie);
        holder.binding.executePendingBindings();

        Context context = holder.binding.movieItemPoster.getContext();
        Picasso.with( context )
                .load( movie.getPosterURL().toString() )
                .placeholder(R.drawable.placeholder_100x150)
                .error(R.drawable.placeholder_100x150)
                .into( holder.binding.movieItemPoster );
    }

    @Override
    public int getItemCount() {
        Log.d( TAG, "getItemCount" );
        if( movieData == null ) { return 0; };
        return movieData.size();
    }

    public void setMovieData( ArrayList<MyMovie> newMovieData ) {
        Log.d( TAG, "setMovieData size is " + String.valueOf( newMovieData.size() ) );
        movieData = newMovieData;
        notifyDataSetChanged();
    }

    public void setFavouriteFlagsAt( ArrayList<Integer> favMovieIdList ) {
        if( movieData == null ) { return; }

        for( MyMovie m : movieData ) {
            for (int fid : favMovieIdList) {
                if (fid == m.getId()) {
                    m.setFavourite(true);
                }
            }
        }
    }
}
