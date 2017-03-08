package movies.proj.com.popularmovies.utility;

import movies.proj.com.popularmovies.data.PopularMoviesContract.PopularMoviesEntry;

/**
 * Created by ${Neha} on 2/23/2017.
 */

public class DatabaseUtils {
    public static final String DATABASE_NAME = "moviesDB.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_MOVIES = "movies";
    public static final String TABLE_FAV_MOVIES = "fav_movies";


    //Create table query for POPULAR MOVIES data
    public static final String CREATE_TABLE_MOVIES =
            "CREATE TABLE " + TABLE_MOVIES
                    + " ("
                    + PopularMoviesEntry._ID + " INTEGER PRIMARY KEY, "
                    + PopularMoviesEntry.POSTER_PATH + " TEXT NOT NULL, "
                    + PopularMoviesEntry.BACKDROP_PATH + " TEXT NOT NULL, "
                    + PopularMoviesEntry.IS_ADULT + " BOOLEAN NOT NULL DEFAULT 1, "
                    + PopularMoviesEntry.MOVIE_ID + " INTEGER NOT NULL, "
                    + PopularMoviesEntry.ORIGINAL_LANGUAGE + " TEXT NOT NULL,"
                    + PopularMoviesEntry.ORIGINAL_TITLE + " TEXT NOT NULL, "
                    + PopularMoviesEntry.OVERVIEW + " TEXT NOT NULL, "
                    + PopularMoviesEntry.POPULARITY + " DOUBLE NOT NULL, "
                    + PopularMoviesEntry.RELEASE_DATE + " DATETIME NOT NULL, "
                    + PopularMoviesEntry.TITLE + " TEXT NOT NULL,"
                    + PopularMoviesEntry.VOTE_AVERAGE + " DOUBLE NOT NULL, "
                    + PopularMoviesEntry.VOTE_COUNT + " INTEGER NOT NULL, "
                    + PopularMoviesEntry.HAS_VIDEO + " BOOLEAN NOT NULL DEFAULT 1,"
                    + PopularMoviesEntry.SORT_TYPE + " INTEGER NOT NULL,"
                    + "UNIQUE (" + PopularMoviesEntry.MOVIE_ID + ") ON CONFLICT REPLACE"

                    + " )";

    //Create table to store favorite marked movies
    public static final String CREATE_TABLE_FAVORITE_MOVIES =
            "CREATE TABLE " + TABLE_FAV_MOVIES
                    + " ("
                    + PopularMoviesEntry._ID + " INTEGER PRIMARY KEY, "
                    + PopularMoviesEntry.MOVIE_ID + " INTEGER NOT NULL "
                    + " )";


    //Drop movie table query
    public static final String DROP_TABLE_MOVIES = "DROP TABLE IF EXISTS " + TABLE_MOVIES;
    public static final String DROP_TABLE_FAV_MOVIES = "DROP TABLE IF EXISTS " + TABLE_FAV_MOVIES;


}
