package movies.proj.com.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URI;

import movies.proj.com.popularmovies.utility.DatabaseUtils;

import static movies.proj.com.popularmovies.utility.DatabaseUtils.DATABASE_NAME;

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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseUtils.DROP_TABLE);
        onCreate(db);
    }

    public void insetMoviesListToDb(PopularMovies dataHolder) {

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
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.IS_MARKED_FAVORITE, false);
        contentValues.put(PopularMoviesContract.PopularMoviesEntry.SORT_TYPE, dataHolder.sortType);
        // Insert the content values via a ContentResolver
        context.getContentResolver().insert(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI, contentValues);
    }

    boolean isMarked;

    public boolean isMovieAlreadyMarkedAsFav(int id) {
        ContentResolver mContentResolver = context.getContentResolver();
        int value;
        String selection = PopularMoviesContract.PopularMoviesEntry.MOVIE_ID + " = " + id;

        Cursor cursor = mContentResolver.query(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI,
                new String[]{PopularMoviesContract.PopularMoviesEntry.MOVIE_ID, PopularMoviesContract.PopularMoviesEntry.IS_MARKED_FAVORITE}, selection, null,
                null);

        if (cursor.moveToFirst()) {
            value = (cursor.getInt(cursor.getColumnIndex(PopularMoviesContract.PopularMoviesEntry.IS_MARKED_FAVORITE)));
            if (value == 1) {
                //not marked
                isMarked = true;
                cursor.close();
            } else {
                isMarked = false;
            }
        }
        return isMarked;
    }

}

