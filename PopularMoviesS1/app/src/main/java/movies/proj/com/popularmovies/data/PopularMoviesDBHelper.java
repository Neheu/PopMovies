package movies.proj.com.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import movies.proj.com.popularmovies.utility.DatabaseUtils;

import static movies.proj.com.popularmovies.utility.DatabaseUtils.DATABASE_NAME;
import static movies.proj.com.popularmovies.utility.DatabaseUtils.TABLE_FAV_MOVIES;
import static movies.proj.com.popularmovies.utility.DatabaseUtils.TABLE_MOVIES;
import static movies.proj.com.popularmovies.utility.DatabaseUtils.TABLE_REVIEWS;
import static movies.proj.com.popularmovies.utility.DatabaseUtils.TABLE_TRAILERS;

/**
 * Created by ${Neha} on 2/23/2017.
 */

public class PopularMoviesDBHelper extends SQLiteOpenHelper {

    Context context;

    public PopularMoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DatabaseUtils.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create table to store movies data
        db.execSQL(DatabaseUtils.CREATE_TABLE_MOVIES);
        db.execSQL(DatabaseUtils.CREATE_TABLE_FAVORITE_MOVIES);
        db.execSQL(DatabaseUtils.CREATE_TABLE_MOVIES_TRAILER);
        db.execSQL(DatabaseUtils.CREATE_TABLE_MOVIES_REVIEWS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseUtils.DROP_TABLE_MOVIES);
        db.execSQL(DatabaseUtils.DROP_TABLE_FAV_MOVIES);
        db.execSQL(DatabaseUtils.DROP_TABLE_TRAILERS);
        db.execSQL(DatabaseUtils.DROP_TABLE_REVIEWS);
        onCreate(db);
    }

    public void insertMoviesListToDb(PopularMovies dataHolder) {

        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();
        // Put the movie data into the ContentValues
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.POSTER_PATH, dataHolder.posterPath);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.BACKDROP_PATH, dataHolder.backdropPath);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.HAS_VIDEO, dataHolder.hasVideo);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.IS_ADULT, dataHolder.isAdult);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.MOVIE_ID, dataHolder.id);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.ORIGINAL_LANGUAGE, dataHolder.orignalLanguage);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.ORIGINAL_TITLE, dataHolder.orignalTitle);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.OVERVIEW, dataHolder.overview);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.POPULARITY, dataHolder.popularity);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.RELEASE_DATE, dataHolder.releaseDate);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.TITLE, dataHolder.title);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.VOTE_AVERAGE, dataHolder.voteAverage);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.VOTE_COUNT, dataHolder.voteCount);
//        contentValues.put(PopularMoviesContract.PopularMoviesEntry.IS_MARKED_FAVORITE, false);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.SORT_TYPE, dataHolder.sortType);
        // Insert the content values via a ContentResolver
        PopularMoviesContantProvider.tableToProcess(TABLE_MOVIES);
        context.getContentResolver().insert(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI, contentValues);
    }

    public void insertMovieTrailer(MovieTrailers dataHolder) {

        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();
        // Put the movie data into the ContentValues

        contentValues.put(PopularMoviesContract.PopularMoviesEntry.MOVIE_ID, dataHolder.movie_id);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.TRAILER_ID, dataHolder.id);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.TRAILER_KEY, dataHolder.key);
        PopularMoviesContantProvider.tableToProcess(TABLE_TRAILERS);

        context.getContentResolver().insert(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI, contentValues);
    }

    public void insertMovieReview(MovieReviews dataHolder) {

        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();
        // Put the movie data into the ContentValues

        contentValues.put(PopularMoviesContract.PopularMoviesEntry.MOVIE_ID, dataHolder.movieId);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.REVIEW_ID, dataHolder.reviewId);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.REVIEW_AUTHOR, dataHolder.author);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.REVIEW_CONTENT, dataHolder.content);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.REVIEW_URL, dataHolder.url);

        PopularMoviesContantProvider.tableToProcess(TABLE_REVIEWS);

        context.getContentResolver().insert(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI, contentValues);
    }

    boolean isMarked;

    public boolean isMovieAlreadyMarkedAsFav(int id) {
        ContentResolver mContentResolver = context.getContentResolver();
        String selection = PopularMoviesContract.PopularMoviesEntry.MOVIE_ID + " = " + id;
        PopularMoviesContantProvider.tableToProcess(TABLE_FAV_MOVIES);
        Cursor cursor = mContentResolver.query(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI,
                new String[]{PopularMoviesContract.PopularMoviesEntry.MOVIE_ID}, selection, null,
                null);

        if (cursor.getCount() > 0) {
            isMarked = true;
            cursor.close();
        } else {
            isMarked = false;
        }

        return isMarked;
    }

}

