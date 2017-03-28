package com.example.android.popularmovies;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.databinding.CastItemBinding;
import com.example.android.popularmovies.databinding.ReviewItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by cremindes on 19/03/17.
 */

public class MovieReviewAdapter 
        extends RecyclerView.Adapter< MovieReviewAdapter.MovieReviewAdapterViewHolder > {

    private static final String TAG = MovieReviewAdapter.class.getSimpleName();
    private ArrayList<Review> movieReviewListData;

    public MovieReviewAdapter( MovieReviewAdapter.MovieReviewAdapterOnClickHandler clickHandler ) {
        mClickHandler = clickHandler;
    }

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final MovieReviewAdapter.MovieReviewAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieReviewAdapterOnClickHandler {
        void onClick( Review movieReview );
    }

    public class MovieReviewAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private final ReviewItemBinding binding;

        public MovieReviewAdapterViewHolder(ReviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d( TAG, "onClick" );
            int adapterPosition = getAdapterPosition();
            Review movieReview = movieReviewListData.get( adapterPosition );
            mClickHandler.onClick( movieReview );
        }
    }

    @Override
    public MovieReviewAdapter.MovieReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType ) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final ReviewItemBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.review_item, parent, false);
        return new MovieReviewAdapter.MovieReviewAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MovieReviewAdapter.MovieReviewAdapterViewHolder holder, int position) {
        Review movieReview = movieReviewListData.get(position);
        holder.binding.setReview(movieReview);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if( movieReviewListData == null ) { return 0; };
        return movieReviewListData.size();
    }

    public void setMovieReviewListData( ArrayList<Review> newMovieReviewListData ) {
        Log.d( TAG, String.valueOf( newMovieReviewListData.size() ) );
        movieReviewListData = newMovieReviewListData;
        notifyDataSetChanged();
    }
}
