package com.example.android.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.databinding.ActivityMovieDetailBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetailActivity extends AppCompatActivity
        implements MovieVideoAdapter.MovieVideoAdapterOnClickHandler {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    private Intent intent;
    private MyMovie movie;
    private MovieCastAdapter movieCastAdapter;
    private MovieVideoAdapter movieVideoAdapter;
    private ActivityMovieDetailBinding mBinding;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_movie_detail );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        intent = getIntent();
        movie = intent.getParcelableExtra( "Movie" );
        setTitle( movie.getTitle() );

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        mBinding.movieDetailTitle.setText( movie.getTitle() );
        mBinding.movieDetailYear.setText( DateFormat.format( "yyyy", movie.getDate() ).toString() );
        mBinding.movieDetailMonthDate.setText( DateFormat.format( "MMMM d", movie.getDate() ).toString() );
        mBinding.movieDetailRating.setText( String.valueOf( movie.getRating() ) );
        mBinding.movieDetailPlot.setText( movie.getPlot() );
        mBinding.movieDetailProgressBar.setVisibility( View.INVISIBLE );

        Context contextBackdrop = mBinding.movieDetailBackdrop.getContext();
        Context contextPoster   = mBinding.movieDetailPoster.getContext();
        Picasso.with( contextBackdrop )
                .load( movie.getbackdropURL().toString() )
                .placeholder( R.drawable.placeholder_500x350 )
                .error( R.drawable.placeholder_500x350 )
                .into( mBinding.movieDetailBackdrop );
        Picasso.with( contextPoster )
                .load( movie.getPosterURL().toString() )
                .placeholder( R.drawable.placeholder_100x150 )
                .error( R.drawable.placeholder_100x150 )
                .into( mBinding.movieDetailPoster );


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this,
                LinearLayoutManager.HORIZONTAL, false );
        LinearLayoutManager linearLayoutManagerVideo = new LinearLayoutManager( this,
                LinearLayoutManager.HORIZONTAL, false );

        movieCastAdapter  = new MovieCastAdapter();
        movieVideoAdapter = new MovieVideoAdapter( this );

        mBinding.movieDetailCastRecycleView.setHasFixedSize( true );
        mBinding.movieDetailCastRecycleView.setLayoutManager( linearLayoutManager );
        mBinding.movieDetailCastRecycleView.setAdapter( movieCastAdapter );

        mBinding.movieDetailVideoRecycleView.setHasFixedSize( true );
        mBinding.movieDetailVideoRecycleView.setLayoutManager( linearLayoutManagerVideo );
        mBinding.movieDetailVideoRecycleView.setAdapter( movieVideoAdapter );

        new FetchTheMovieCastDB().execute ( new Integer( movie.getId() ) );
        new FetchTheMovieVideoDB().execute( new Integer( movie.getId() ) );
    }

    /**
     * This method is overridden by our activity class in order to handle RecyclerView item
     * clicks.
     *
     * @param movieVideo The movie video that that was clicked
     */
    @Override
    public void onClick( MovieVideo movieVideo ) {
        Log.d( TAG, "onClick" );
        Context context = this;
        try {
            Intent intent = new Intent( Intent.ACTION_VIEW, movieVideo.getYoutubeAppUri() );
            intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity( intent );

        } catch (ActivityNotFoundException e) {
            // youtube is not installed. Will be opened in other available apps
            Intent i = new Intent( Intent.ACTION_VIEW, movieVideo.getYoutubeWebUri() );
            startActivity( i );
        }
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch( item.getItemId() ) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected( item );
    }

    public class FetchTheMovieCastDB extends AsyncTask< Integer, Void, ArrayList<Cast> >
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // show progress bar
            mBinding.movieDetailProgressBar.setVisibility( View.VISIBLE );
        }

        @Override
        protected ArrayList<Cast> doInBackground( Integer... movieId ) {
            if( movieId.length == 0 ) { return null; }

            ArrayList<Cast> theMovieDBResponse = null;

            try {
                theMovieDBResponse = TheMovieDBAPI.getMovieCast( movieId[0].intValue() );
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return theMovieDBResponse;
        }

        @Override
        protected void onPostExecute( ArrayList<Cast> movieCastData ) {
            super.onPostExecute( movieCastData );

            if( movieCastData != null) {
                movieCastAdapter.setMovieData( movieCastData );
            } else {
                Toast.makeText( getApplicationContext(), "Unable to fetch cast list.", Toast.LENGTH_LONG ).show();
            }

            mBinding.movieDetailProgressBar.setVisibility( View.INVISIBLE );
        }
    }

    public class FetchTheMovieVideoDB extends AsyncTask< Integer, Void, ArrayList<MovieVideo> >
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // show progress bar
            // mBinding.movieDetailProgressBar.setVisibility( View.VISIBLE );
        }

        @Override
        protected ArrayList<MovieVideo> doInBackground( Integer... movieId ) {
            if( movieId.length == 0 ) { return null; }

            ArrayList<MovieVideo> theMovieDBResponse = null;

            try {
                theMovieDBResponse = TheMovieDBAPI.getMovieVideoList( movieId[0].intValue() );
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return theMovieDBResponse;
        }

        @Override
        protected void onPostExecute( ArrayList<MovieVideo> movieVideoListData ) {
            super.onPostExecute( movieVideoListData );

            if( movieVideoListData != null) {
                movieVideoAdapter.setMovieData( movieVideoListData );
            } else {
                Toast.makeText( getApplicationContext(), "Unable to fetch video list.", Toast.LENGTH_LONG ).show();
            }

            // mBinding.movieDetailProgressBar.setVisibility( View.INVISIBLE );
        }
    }
}
