package movies.proj.com.popularmovies.utility;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import movies.proj.com.popularmovies.data.MovieTrailers;
import movies.proj.com.popularmovies.data.PopularMovies;
import movies.proj.com.popularmovies.data.PopularMoviesContantProvider;
import movies.proj.com.popularmovies.data.PopularMoviesContract;
import movies.proj.com.popularmovies.data.PopularMoviesDBHelper;

/**
 * Created by Neha on 2/6/2017.
 */
public class PopularMovieJsonUtil {
    /*
    This method will help to parse data ,got from popular movies url and store that into PopualMovies object for further use.
    @param moviesJsonString, use to get the json string, recieved from server.
     */
    public static ArrayList<PopularMovies> parseMoviesData(final Context context, final String moviesJsonString) {


        ArrayList<PopularMovies> listOfMovies = new ArrayList<>();

        try {
            JSONObject moviesJsonObject = new JSONObject(moviesJsonString);
            JSONArray moviesJsonArray = moviesJsonObject.getJSONArray(ConstantsUtility.MOVIE_RESULT);
            for (int i = 0; i < moviesJsonArray.length(); i++) {
                JSONObject jsonObject = moviesJsonArray.getJSONObject(i);

                PopularMovies dataHolder = new PopularMovies(jsonObject.getString(ConstantsUtility.MOVIE_POSTER_PATH),
                        jsonObject.getBoolean(ConstantsUtility.MOVIE_ADULT), jsonObject.getString(ConstantsUtility.MOVIE_OVERVIEW),
                        jsonObject.getString(ConstantsUtility.MOVIE_RELEASE_DATE), jsonObject.getInt(ConstantsUtility.MOVIE_ID),
                        jsonObject.getString(ConstantsUtility.MOVIE_ORIGINAL_TITLE), jsonObject.getString(ConstantsUtility.MOVIE_ORIGINAL_LANGUAGE),
                        jsonObject.getString(ConstantsUtility.MOVIE_TITLE), jsonObject.getString(ConstantsUtility.MOVIE_BACKDROP_PATH),
                        jsonObject.getDouble(ConstantsUtility.MOVIE_POPULARITY), jsonObject.getInt(ConstantsUtility.MOVIE_VOTE_COUNT),
                        jsonObject.getBoolean(ConstantsUtility.MOVIE_VIDEO), jsonObject.getDouble(ConstantsUtility.MOVIE_VOTE_AVERAGE));
                new PopularMoviesDBHelper(context).insetMoviesListToDb(dataHolder);
                listOfMovies.add(dataHolder);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listOfMovies;

    }
    public static ArrayList<MovieTrailers> parseTrailersData(final Context context, final String moviesJsonString) {


        ArrayList<MovieTrailers> listOfMovies = new ArrayList<>();

        try {
            JSONObject moviesJsonObject = new JSONObject(moviesJsonString);
            JSONArray moviesJsonArray = moviesJsonObject.getJSONArray(ConstantsUtility.MOVIE_RESULT);
            for (int i = 0; i < moviesJsonArray.length(); i++) {
                JSONObject jsonObject = moviesJsonArray.getJSONObject(i);

                MovieTrailers dataHolder = new MovieTrailers(jsonObject.getString(ConstantsUtility.TRAILER_ID),
                        jsonObject.getString(ConstantsUtility.TRAILER_KEY));
               // new PopularMoviesDBHelper(context).insetMoviesListToDb(dataHolder);
                listOfMovies.add(dataHolder);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listOfMovies;

    }
}
