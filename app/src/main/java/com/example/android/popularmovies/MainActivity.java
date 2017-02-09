package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
// import android.support.v7.app.AlertController;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.MovieAdapterOnClickHandler
{
    private static final String TAG = TheMovieDBAPI.class.getSimpleName();

    private RecyclerView mainRecyclerView;
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;

    public enum SortBy {
        POPULAR( "Popular", 0, TheMovieDBAPI.SORT_BY_POPOLARITY ),
        TOPRATED( "Top Rated", 1, TheMovieDBAPI.SORT_BY_TOP_RATED );

        private String stringValue;
        private int intValue;
        private String mQueryParam;
        private SortBy( String toString, int value, String queryParam ) {
            stringValue = toString;
            intValue = value;
            mQueryParam = queryParam;
        }

        @Override
        public String toString() {
            return stringValue;
        }

        public String toQueryParam() {
            return mQueryParam;
        }
    }

    private SortBy sortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sortBy = SortBy.POPULAR;

        GridLayoutManager gridLayoutManager = new GridLayoutManager( this, 3 );
        movieAdapter = new MovieAdapter( this );

        mainRecyclerView = ( RecyclerView ) findViewById( R.id.activity_main_recycle_view );
        mainRecyclerView.setHasFixedSize( true );
        mainRecyclerView.setLayoutManager( gridLayoutManager );
        mainRecyclerView.setAdapter( movieAdapter );

        progressBar = (ProgressBar) findViewById( R.id.activity_main_progress_bar );

        loadMovieData( sortBy );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        int id = item.getItemId();
        switch( id ) {
            case R.id.action_sort_by: {
                if( sortBy == SortBy.POPULAR ) { sortBy = SortBy.TOPRATED; }
                else { sortBy = SortBy.POPULAR; }
                setTitle( sortBy.stringValue + " Movies" );
                loadMovieData( sortBy );
            }
        }

        return super.onOptionsItemSelected( item );
    }

    public void loadMovieData( SortBy sortBy ) {
        new FetchTheMovieDB().execute( sortBy );
    }

    public class FetchTheMovieDB extends AsyncTask< SortBy, Void, ArrayList<MyMovie> >
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // show progress bar
            progressBar.setVisibility( View.VISIBLE );
        }

        @Override
        protected ArrayList<MyMovie> doInBackground( SortBy... params ) {
            if (params.length == 0) {
                return null;
            }

            SortBy sortBy = params[0];
            ArrayList<MyMovie> theMovieDBResponse = null;

            try {
                theMovieDBResponse = TheMovieDBAPI.getMovieList( sortBy );
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return theMovieDBResponse;
        }

        @Override
        protected void onPostExecute( ArrayList<MyMovie> movieData ) {
            super.onPostExecute( movieData );

            if( movieData != null) {
                movieAdapter.setMovieData( movieData );
            }

            progressBar.setVisibility( View.INVISIBLE );
        }
    }

    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks.
     *
     * @param movie The movie that that was clicked
     */
    @Override
    public void onClick( MyMovie movie ) {
        Log.d( TAG, "onClick" );
        Context context = this;
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass );
        intentToStartDetailActivity.putExtra( "Movie", movie );
        startActivity( intentToStartDetailActivity );
    }
}
