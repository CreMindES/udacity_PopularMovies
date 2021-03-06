package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.popularmovies.api.TheMovieDBAPI;
import com.example.android.popularmovies.data.FavMovieContract;
import com.example.android.popularmovies.databinding.ActivityMainBinding;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = TheMovieDBAPI.class.getSimpleName();
    private static final String GRID_LAYOUT_MANAGER_STATE = "gridLayoutManagerState";
    private static final String GRID_LAYOUT_MANAGER_POSITION = "gridLayoutManagerPosition";
    private static final String SHOW_MOVIES_BY = "showMoviesBy";

    private ActivityMainBinding mBinding;
    private MovieAdapter movieAdapter;
    private ArrayList<MyMovie> favouriteMovieList;
    private GridLayoutManager gridLayoutManager;
    private Bundle savedInstanceState;
    private int lastGridPosition;

    public static final int SHOW_POPULAR   = 1;
    public static final int SHOW_TOP_RATED = 2;
    public static final int SHOW_FAVOURITE = 3;

    private int showMoviesBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);

        setContentView(R.layout.activity_main);
        if( savedInstanceState == null ) {
            showMoviesBy = SHOW_POPULAR;
        } else {
            showMoviesBy = savedInstanceState.getInt(SHOW_MOVIES_BY);
        }
        mySetTitle( showMoviesBy );

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(this, 3);
        } else {
            gridLayoutManager = new GridLayoutManager(this, 5);
        }
        movieAdapter = new MovieAdapter( this );

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        
        mBinding.activityMainRecycleView.setHasFixedSize( true );
        mBinding.activityMainRecycleView.setLayoutManager( gridLayoutManager );
        mBinding.activityMainRecycleView.setAdapter( movieAdapter );

        loadMovieData(showMoviesBy);
        new FetchFavouriteMovies().execute();

        if( savedInstanceState != null ) {
            gridLayoutManager.onRestoreInstanceState(
                    savedInstanceState.getParcelable(GRID_LAYOUT_MANAGER_STATE));
        }
    }

    private void onMoviesFetchedFully(Bundle savedInstanceState) {
        gridLayoutManager.scrollToPosition(lastGridPosition);

        if(savedInstanceState == null) { return; }
        gridLayoutManager.onRestoreInstanceState(
                savedInstanceState.getParcelable(GRID_LAYOUT_MANAGER_STATE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO optimize! Only re-fetch in case of change has happened
        if( showMoviesBy == SHOW_FAVOURITE ) {
            new FetchFavouriteMovies().execute();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        gridLayoutManager.scrollToPosition(lastGridPosition);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lastGridPosition = gridLayoutManager.findFirstVisibleItemPosition();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(GRID_LAYOUT_MANAGER_STATE, gridLayoutManager.onSaveInstanceState());
        outState.putInt(GRID_LAYOUT_MANAGER_POSITION, gridLayoutManager.findFirstVisibleItemPosition());
        outState.putInt(SHOW_MOVIES_BY, showMoviesBy);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState == null) { return; }
        super.onRestoreInstanceState(savedInstanceState);
        gridLayoutManager.onRestoreInstanceState(
                savedInstanceState.getParcelable(GRID_LAYOUT_MANAGER_STATE));
        lastGridPosition = savedInstanceState.getInt(GRID_LAYOUT_MANAGER_POSITION);
        showMoviesBy = savedInstanceState.getInt(SHOW_MOVIES_BY);
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
                if (showMoviesBy == SHOW_POPULAR) {
                    break;
                }
                showMoviesBy = SHOW_POPULAR;
                mySetTitle(showMoviesBy);
                loadMovieData(showMoviesBy);
                break;
            }
            case R.id.action_show_top_rated: {
                if (showMoviesBy == SHOW_TOP_RATED) {
                    break;
                }
                showMoviesBy = SHOW_TOP_RATED;
                mySetTitle(showMoviesBy);
                loadMovieData(showMoviesBy);
                break;
            }
            case R.id.action_show_favourites: {
                if (showMoviesBy == SHOW_FAVOURITE) {
                    break;
                }
                showMoviesBy = SHOW_FAVOURITE;
                mySetTitle(showMoviesBy);
                new FetchFavouriteMovies().execute();
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid sorting option");
            }
        }

        return super.onOptionsItemSelected( item );
    }

    public void mySetTitle( int showMovieBy ) {
        switch (showMovieBy) {
            case SHOW_POPULAR:
                setTitle( getString( R.string.appbar_title_popular_movies  ) );
                break;
            case SHOW_TOP_RATED:
                setTitle( getString( R.string.appbar_title_top_rated_movies) );
                break;
            case SHOW_FAVOURITE:
                setTitle( getString( R.string.appbar_title_favourite_movies) );
                break;
            default:
        }
    }

    public void loadMovieData( int showMoviesBy ) {
        new FetchTheMovieDB().execute( showMoviesBy );
        mBinding.activityMainNoMovie.setVisibility(View.INVISIBLE);
    }

    private void showFavouriteMovies() {
        movieAdapter.setMovieData(favouriteMovieList);

        int count = movieAdapter.getItemCount();
        int vis = mBinding.activityMainNoMovie.getVisibility();
        if (count == 0 && vis == View.INVISIBLE) {
            mBinding.activityMainNoMovie.setText(getString(R.string.no_favourited_movie_yet));
            mBinding.activityMainNoMovie.setVisibility(View.VISIBLE);
        } else if (count > 0 && vis == View.VISIBLE) {
            mBinding.activityMainNoMovie.setVisibility(View.INVISIBLE);
        }
    }

    private void setFavouriteFlags() {
        if( showMoviesBy == SHOW_FAVOURITE || favouriteMovieList == null ||
                movieAdapter.getItemCount() == 0 ) { return; }

        ArrayList<Integer> favouriteMovieIdList = new ArrayList<>();
        for( MyMovie fm : favouriteMovieList ) {
            favouriteMovieIdList.add(fm.getId());
        }

        movieAdapter.setFavouriteFlagsAt(favouriteMovieIdList);
    }

    // TODO: separate class plus start using SyncAdapter and hence unify our content provider
    public class FetchFavouriteMovies extends AsyncTask< Void, Void, ArrayList<MyMovie> > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // show progress bar
            mBinding.activityMainProgressBar.setVisibility( View.VISIBLE );
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
            onMoviesFetchedFully(savedInstanceState);

            mBinding.activityMainProgressBar.setVisibility( View.INVISIBLE );
        }
    }

    public class FetchTheMovieDB extends AsyncTask< Integer, Void, ArrayList<MyMovie> >
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // show progress bar
            mBinding.activityMainProgressBar.setVisibility( View.VISIBLE );
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
                onMoviesFetchedFully(savedInstanceState);
            }

            mBinding.activityMainProgressBar.setVisibility( View.INVISIBLE );
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
        intentToStartDetailActivity.putExtra( getString( R.string.parc_movie ), movie );
        startActivity( intentToStartDetailActivity );
    }
}
