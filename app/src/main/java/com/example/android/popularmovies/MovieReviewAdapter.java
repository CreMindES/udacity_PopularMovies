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

        public TextView authorTextView;
        public TextView contentTextView;
        public TextView urlTextView;

        public MovieReviewAdapterViewHolder( View view ) {
            super(view);
            authorTextView  = (TextView) view.findViewById( R.id.movie_review_author );
            contentTextView = (TextView) view.findViewById( R.id.movie_review_content );
            // urlTextView     = (TextView) view.findViewById( R.id.movie_review_url );
            // videoThumbnailImage.setOnClickListener(this);
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
        Context context = parent.getContext();
        int layoutIdOfItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from( context );
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate( layoutIdOfItem, parent, shouldAttachToParentImmediately );
        return new MovieReviewAdapter.MovieReviewAdapterViewHolder( view );
    }

    @Override
    public void onBindViewHolder( MovieReviewAdapter.MovieReviewAdapterViewHolder holder, int position ) {
        Review movieReview = movieReviewListData.get( position );
        holder.authorTextView.setText( movieReview.getAuthor() );
        holder.contentTextView.setText( movieReview.getContent() );
        // holder.urlTextView.setText( movieReview.getUrl().toString() );
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
