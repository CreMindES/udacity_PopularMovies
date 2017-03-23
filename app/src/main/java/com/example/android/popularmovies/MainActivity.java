package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.TextView;

import com.example.android.popularmovies.data.FavMovieContract;
import com.facebook.stetho.Stetho;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = TheMovieDBAPI.class.getSimpleName();

    private RecyclerView mainRecyclerView;
    private MovieAdapter movieAdapter;
    private ArrayList<MyMovie> favouriteMovieList;
    private ProgressBar progressBar;
    private TextView noMovieTextView;

    public static final int SHOW_POPULAR   = 1;
    public static final int SHOW_TOP_RATED = 2;
    public static final int SHOW_FAVOURITE = 3;

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
    private int showMoviesBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);

        setContentView(R.layout.activity_main);

        sortBy = SortBy.POPULAR;
        showMoviesBy = SHOW_POPULAR;

        GridLayoutManager gridLayoutManager = new GridLayoutManager( this, 3 );
        movieAdapter = new MovieAdapter( this );

        mainRecyclerView = ( RecyclerView ) findViewById( R.id.activity_main_recycle_view );
        mainRecyclerView.setHasFixedSize( true );
        mainRecyclerView.setLayoutManager( gridLayoutManager );
        mainRecyclerView.setAdapter( movieAdapter );

        progressBar = (ProgressBar) findViewById( R.id.activity_main_progress_bar );
        noMovieTextView = (TextView) findViewById( R.id.activity_main_no_movie );

        loadMovieData(showMoviesBy);
        new FetchFavouriteMovies().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO optimize! Only refetch in case of change has happaned
        if( showMoviesBy == SHOW_FAVOURITE ) {
            new FetchFavouriteMovies().execute();
        }
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
                break;
            }
            case R.id.action_show_popular: {
                showMoviesBy = SHOW_POPULAR;
                setTitle( getString( R.string.appbar_title_popular_movies ) );
                loadMovieData( showMoviesBy );
                break;
            }
            case R.id.action_show_top_rated: {
                showMoviesBy = SHOW_TOP_RATED;
                setTitle( getString( R.string.appbar_title_top_rated_movies ) );
                loadMovieData( showMoviesBy );
                break;
            }
            case R.id.action_show_favourites: {
                setTitle( getString( R.string.appbar_title_favourite_movies ) );
                showMoviesBy = SHOW_FAVOURITE;
                new FetchFavouriteMovies().execute();
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid sorting option");
            }
        }

        return super.onOptionsItemSelected( item );
    }

    public void loadMovieData( int showMoviesBy ) {
        new FetchTheMovieDB().execute( showMoviesBy );
        noMovieTextView.setVisibility(View.INVISIBLE);
    }

    private void showFavouriteMovies() {
        movieAdapter.setMovieData(favouriteMovieList);

        int count = movieAdapter.getItemCount();
        int vis = noMovieTextView.getVisibility();
        if (count == 0 && vis == View.INVISIBLE) {
            noMovieTextView.setText(getString(R.string.no_favourited_movie_yet));
            noMovieTextView.setVisibility(View.VISIBLE);
        } else if (count > 0 && vis == View.VISIBLE) {
            noMovieTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void setFavouriteFlags() {
        if( showMoviesBy == SHOW_FAVOURITE || favouriteMovieList == null ) { return; }

        ArrayList<Integer> favouriteMovieIdList = new ArrayList<>();
        for( MyMovie fm : favouriteMovieList ) {
            favouriteMovieIdList.add(fm.getId());
        }

        movieAdapter.setFavouriteFlagsAt(favouriteMovieIdList);

//        @targetAPI(24) {
//            List<int> favMovieIdList = favouriteMovieList.stream()
//                    .map(m -> m.getId()).collect(Collectors.toList());
//        }
//        ArrayList<int> favMovieIdList = favouriteMovieList.stream()
//                .map(MyMovie::getId).collect(Collectors.toList());

        // movieAdapter.setFavouriteFlags(  );
    }

    public class FetchFavouriteMovies extends AsyncTask< Void, Void, ArrayList<MyMovie> > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // show progress bar
            progressBar.setVisibility( View.VISIBLE );
        }

        @Override
        protected ArrayList<MyMovie> doInBackground(Void... params) {

            Cursor favMovieCursor = null;

            try {
                favMovieCursor = getContentResolver().query(FavMovieContract.FavMovieEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

            } catch (Exception e) {
                Log.e(TAG, "Failed to asynchronously load data.");
                e.printStackTrace();
                return null;
            }

            // create a MyMovie ArrayList from the favMovieCursor
            ArrayList<MyMovie> movieListData = new ArrayList<MyMovie>();
            while (favMovieCursor.moveToNext()) {
                MyMovie m = new MyMovie();
                m.setId(favMovieCursor.getInt(favMovieCursor.getColumnIndex(
                        FavMovieContract.FavMovieEntry.COLUMN_ID)));
                m.setTitle(favMovieCursor.getString(favMovieCursor.getColumnIndex(
                        FavMovieContract.FavMovieEntry.COLUMN_TITLE)));
                m.setOriginalTitle(favMovieCursor.getString(favMovieCursor.getColumnIndex(
                        FavMovieContract.FavMovieEntry.COLUMN_TITLE_ORIG)));
                m.setPlot(favMovieCursor.getString(favMovieCursor.getColumnIndex(
                        FavMovieContract.FavMovieEntry.COLUMN_PLOT)));
                m.setBackdropUrlId(favMovieCursor.getString(favMovieCursor.getColumnIndex(
                        FavMovieContract.FavMovieEntry.COLUMN_BACKDROPPATH)));
                m.setPosterUrlId(favMovieCursor.getString(favMovieCursor.getColumnIndex(
                        FavMovieContract.FavMovieEntry.COLUMN_POSTERPATH)));
                m.setRating(favMovieCursor.getDouble(favMovieCursor.getColumnIndex(
                        FavMovieContract.FavMovieEntry.COLUMN_RATING)));
                m.setDate(new Date(favMovieCursor.getLong(favMovieCursor.getColumnIndex(
                        FavMovieContract.FavMovieEntry.COLUMN_RELEASEDATE))*1000));
                m.setFavourite(true);

                movieListData.add(m);
            }

            return movieListData;
        }

        @Override
        protected void onPostExecute(ArrayList<MyMovie> movieListData) {

            super.onPostExecute(movieListData);
            if( movieListData != null) {
                favouriteMovieList = movieListData;
                if( showMoviesBy == SHOW_FAVOURITE ) {
                    showFavouriteMovies();
                }
            }
            setFavouriteFlags();

            progressBar.setVisibility( View.INVISIBLE );
        }
    }

    public class FetchTheMovieDB extends AsyncTask< Integer, Void, ArrayList<MyMovie> >
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // show progress bar
            progressBar.setVisibility( View.VISIBLE );
        }

        @Override
        protected ArrayList<MyMovie> doInBackground( Integer... params ) {
            if (params.length == 0) {
                return null;
            }

            int showMoviesBy = params[0];
            ArrayList<MyMovie> theMovieDBResponse = null;

            try {
                theMovieDBResponse = TheMovieDBAPI.getMovieList( showMoviesBy );
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
                setFavouriteFlags();
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
