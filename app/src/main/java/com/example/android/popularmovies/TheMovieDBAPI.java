package com.example.android.popularmovies;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by cremindes on 06/02/17.
 */

public class TheMovieDBAPI {
    public static final String API_KEY = "INSERT_YOUR_API_KEY_HERE";
    public static final String API_KEY_PARAM = "api_key";

    public static final String API_BASE_URL = "http://api.themoviedb.org/3/movie";
    public static final String API_IMAGE_HEADER_LARGE = "http://image.tmdb.org/t/p/w500";
    public static final String API_IMAGE_HEADER_MEDIUM = "http://image.tmdb.org/t/p/w185";
    public static final String API_IMAGE_HEADER_SMALL = "http://image.tmdb.org/t/p/w92";

    public static final String SORT_BY_TOP_RATED = "top_rated";
    public static final String SORT_BY_POPOLARITY = "popular";

    private static final String TAG = TheMovieDBAPI.class.getSimpleName();

    public static ArrayList<MyMovie> getMovieList(MainActivity.SortBy sortBy) {
        String jsonResponse = performTheMovieDBQuery(makeMovieListQuery(sortBy));
        ArrayList<MyMovie> res = null;
        if (jsonResponse != null) {
            res = new ArrayList<MyMovie>(fetchTheMovieListDBJSON(jsonResponse));
        }
        return res;
    }

    public static ArrayList<Cast> getMovieCast(int id) {
        ArrayList<Cast> castList = null;

        // Assembly query URL
        String URL = API_BASE_URL + "/" + id + "/casts";
        Uri builtUri = Uri.parse(URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Perform Query
        String jsonResponse = performTheMovieDBQuery(url);

        // Fetch relevant information
        if (jsonResponse != null) {
            castList = new ArrayList<Cast>(fetchTheMovieCastDBJSON(jsonResponse));
        }

        return castList;
    }

    private static URL makeMovieListQuery(MainActivity.SortBy sortBy) {
        // Build url
        String URL = API_BASE_URL + "/" + sortBy.toQueryParam();
        Uri builtUri = Uri.parse(URL).buildUpon()
                .appendQueryParameter("sort_by", sortBy.toQueryParam() + ".desc")
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    private static String performTheMovieDBQuery(URL url) {
        String theMovieDBResonse = null;

        // get HTTP response
        try {
            theMovieDBResonse = getHttpResponseFromURL(url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return theMovieDBResonse;
    }

    private static String getHttpResponseFromURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    private static ArrayList<MyMovie> fetchTheMovieListDBJSON(String json) {
        if (json == null) {
            return null;
        }

        ArrayList<MyMovie> res = new ArrayList<MyMovie>();

        JSONObject moviesObject;
        JSONArray moviesArray;
        try {
            moviesObject = new JSONObject(json);
            moviesArray = moviesObject.getJSONArray("results");

            for (int i = 0; i < moviesArray.length(); ++i) {
                JSONObject movieJSONObject = null;
                try {
                    movieJSONObject = moviesArray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return res;
                }

                MyMovie myMovie = new MyMovie();

                myMovie.setTitle(movieJSONObject.getString("title"));
                String plot = movieJSONObject.getString("overview");
                if (plot.compareTo("null") == 0) {
                    myMovie.setPlot("Not available");
                } else {
                    myMovie.setPlot(plot);
                }
                myMovie.setId(movieJSONObject.getInt("id"));
                myMovie.setOriginalTitle(movieJSONObject.getString("original_title"));
                myMovie.setDate(movieJSONObject.getString("release_date"));
                // myMovie.setPopularity(movieJSONObject.getString("popularity"));
                myMovie.setRating(movieJSONObject.getString("vote_average"));
                myMovie.setPosterURL(movieJSONObject.getString("poster_path"));
                myMovie.setBackdropURL(movieJSONObject.getString("backdrop_path"));

                res.add(myMovie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return res;
    }

    private static ArrayList<Cast> fetchTheMovieCastDBJSON(String json) {
        if (json == null) {
            return null;
        }

        ArrayList<Cast> castList = new ArrayList<Cast>();

        try {
            JSONObject movieCastObject = new JSONObject(json);
            JSONArray movieCastArray = movieCastObject.getJSONArray("cast");

            for (int i = 0; i < movieCastArray.length(); ++i) {
                JSONObject castJSONObject = null;
                try {
                    castJSONObject = movieCastArray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }

                Cast cast = new Cast();

                cast.setId(castJSONObject.getInt("id"));
                cast.setCastId(castJSONObject.getInt("cast_id"));
                cast.setCharacter(castJSONObject.getString("character"));
                cast.setName(castJSONObject.getString("name"));
                cast.setProfileURL(castJSONObject.getString("profile_path"));

                castList.add(cast);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return castList;
    }

}