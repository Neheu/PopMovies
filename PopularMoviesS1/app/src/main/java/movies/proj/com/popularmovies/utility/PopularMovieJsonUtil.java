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

import movies.proj.com.popularmovies.data.MovieReviews;
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
    public static ArrayList<PopularMovies> parseMoviesData(final Context context, final String moviesJsonString, final int sortType) {


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
                        jsonObject.getBoolean(ConstantsUtility.MOVIE_VIDEO), jsonObject.getDouble(ConstantsUtility.MOVIE_VOTE_AVERAGE), sortType);
                new PopularMoviesDBHelper(context).insertMoviesListToDb(dataHolder);
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
            int id = moviesJsonObject.getInt("id");
            JSONArray moviesJsonArray = moviesJsonObject.getJSONArray(ConstantsUtility.MOVIE_RESULT);
            for (int i = 0; i < moviesJsonArray.length(); i++) {
                JSONObject jsonObject = moviesJsonArray.getJSONObject(i);

                MovieTrailers dataHolder = new MovieTrailers(jsonObject.getString(ConstantsUtility.TRAILER_ID),
                        jsonObject.getString(ConstantsUtility.TRAILER_KEY), id);
                // new PopularMoviesDBHelper(context).insertMoviesListToDb(dataHolder);
                listOfMovies.add(dataHolder);
                new PopularMoviesDBHelper(context).insertMovieTrailer(dataHolder);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listOfMovies;

    }

    public static ArrayList<MovieReviews> parseReviewssData(final Context context, final String moviesJsonString) {


        ArrayList<MovieReviews> listOfMovies = new ArrayList<>();

        try {
            JSONObject moviesJsonObject = new JSONObject(moviesJsonString);
            int id = moviesJsonObject.getInt("id");
            if (moviesJsonObject.has(ConstantsUtility.MOVIE_RESULT)) {
                JSONArray moviesJsonArray = moviesJsonObject.getJSONArray(ConstantsUtility.MOVIE_RESULT);
                for (int i = 0; i < moviesJsonArray.length(); i++) {
                    JSONObject jsonObject = moviesJsonArray.getJSONObject(i);

                    MovieReviews dataHolder = new MovieReviews(jsonObject.getString(ConstantsUtility.REVIEW_ID),
                            id, jsonObject.getString(ConstantsUtility.REVIEW_AUTHOR),
                            jsonObject.getString(ConstantsUtility.REVIEW_CONTENT),
                            jsonObject.getString(ConstantsUtility.REVIEW_URL));
                    // new PopularMoviesDBHelper(context).insertMoviesListToDb(dataHolder);
                    listOfMovies.add(dataHolder);
                    new PopularMoviesDBHelper(context).insertMovieReview(dataHolder);
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return listOfMovies;

    }

}
