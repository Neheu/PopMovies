package movies.proj.com.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.UserDictionary;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import movies.proj.com.popularmovies.utility.DatabaseUtils;

import static movies.proj.com.popularmovies.utility.DatabaseUtils.TABLE_NAME;

/**
 * Created by ${Neha} on 2/23/2017.
 */

public class PopularMoviesContantProvider extends ContentProvider {
    private PopularMoviesDBHelper moviesDBHelper;
    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    @Override
    public boolean onCreate() {
        Context context = getContext();
        moviesDBHelper = new PopularMoviesDBHelper(context);
        return true;
    }

    /**
     * Initialize a new matcher object without any matches,
     * then use .addURI(String authority, String path, int match) to add matches
     */
    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the task directory and a single item by ID.
         */
        uriMatcher.addURI(PopularMoviesContract.AUTHORITY, PopularMoviesContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(PopularMoviesContract.AUTHORITY, PopularMoviesContract.PATH_MOVIES + "/#", MOVIES_WITH_ID);

        return uriMatcher;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        final SQLiteDatabase db = moviesDBHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)) {
            case MOVIES: {
                cursor = db.query(
                        TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //  Get access to the task database (to write new data to)
        final SQLiteDatabase db = moviesDBHelper.getWritableDatabase();

        //  Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case MOVIES:
                //  Insert new values into the database
                // Inserting values into tasks table
                long id = db.insertWithOnConflict(TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            //Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        //Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        //  Get access to the task database (to write new data to)
        final SQLiteDatabase db = moviesDBHelper.getWritableDatabase();

        //  Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        int rowsUpdated = 0;

        switch (match) {
            case MOVIES:
                rowsUpdated = db.update(TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;

    }
}
