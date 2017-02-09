package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetailActivity extends AppCompatActivity {

    private Intent intent;
    private MyMovie movie;

    public ImageView backdropImageView;
    public ImageView posterImageView;
    public TextView titleTextView;
    public TextView plotTextView;
    public TextView yearTextView;
    public TextView monthDateTextView;
    public TextView ratingTextView;
    public ProgressBar progressBar;
    private RecyclerView castRecyclerView;
    private MovieCastAdapter movieCastAdapter;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_movie_detail );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        intent = getIntent();
        movie = intent.getParcelableExtra( "Movie" );
        setTitle( movie.getTitle() );

        backdropImageView = (ImageView) findViewById( R.id.movie_detail_backdrop   );
        posterImageView   = (ImageView) findViewById( R.id.movie_detail_poster     );
        titleTextView     = (TextView ) findViewById( R.id.movie_detail_title      );
        plotTextView      = (TextView ) findViewById( R.id.movie_detail_plot       );
        yearTextView      = (TextView ) findViewById( R.id.movie_detail_year       );
        monthDateTextView = (TextView ) findViewById( R.id.movie_detail_month_date );
        ratingTextView    = (TextView ) findViewById( R.id.movie_detail_rating     );
        progressBar       = (ProgressBar) findViewById( R.id.movie_detail_progress_bar );

        titleTextView.setText( movie.getTitle() );
        yearTextView.setText( DateFormat.format( "yyyy", movie.getDate() ).toString() );
        monthDateTextView.setText( DateFormat.format( "MMMM d", movie.getDate() ).toString() );
        ratingTextView.setText( String.valueOf( movie.getRating() ) );
        titleTextView.setText( movie.getTitle() );
        plotTextView.setText( movie.getPlot() );
        progressBar.setVisibility( View.INVISIBLE );

        Context contextBackdrop = backdropImageView.getContext();
        Context contextPoster   = posterImageView.getContext();
        Picasso.with( contextBackdrop )
                .load( movie.getbackdropURL().toString() )
                .into( backdropImageView );
        Picasso.with( contextPoster )
                .load( movie.getPosterURL().toString() )
                .into( posterImageView );


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this,
                LinearLayoutManager.HORIZONTAL, false );

        movieCastAdapter = new MovieCastAdapter();

        castRecyclerView = ( RecyclerView ) findViewById( R.id.movie_detail_cast_recycle_view );
        castRecyclerView.setHasFixedSize( true );
        castRecyclerView.setLayoutManager( linearLayoutManager );
        castRecyclerView.setAdapter( movieCastAdapter );

        progressBar = (ProgressBar) findViewById( R.id.movie_detail_progress_bar   );

        new FetchTheMovieCastDB().execute( new Integer( movie.getId() ) );
    }


    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch( item.getItemId() ) {
            case android.R.id.home:
                onBackPressed();
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
            progressBar.setVisibility( View.VISIBLE );
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

            progressBar.setVisibility( View.INVISIBLE );
        }
    }
}
