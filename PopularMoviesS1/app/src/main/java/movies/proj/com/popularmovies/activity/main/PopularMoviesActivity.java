package movies.proj.com.popularmovies.activity.main;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import movies.proj.com.popularmovies.activity.movie_details.PopularMovieDetailActivity;
import movies.proj.com.popularmovies.adapters.PopularMoviesAdapter;
import movies.proj.com.popularmovies.R;
import movies.proj.com.popularmovies.data.PopularMovies;
import movies.proj.com.popularmovies.data.PopularMoviesContantProvider;
import movies.proj.com.popularmovies.data.PopularMoviesContract;
import movies.proj.com.popularmovies.utility.ConstantsUtility;
import movies.proj.com.popularmovies.utility.NetworkUtils;
import movies.proj.com.popularmovies.utility.PopularMovieJsonUtil;
import movies.proj.com.popularmovies.utility.Utils;

import static movies.proj.com.popularmovies.utility.DatabaseUtils.TABLE_FAV_MOVIES;
import static movies.proj.com.popularmovies.utility.DatabaseUtils.TABLE_MOVIES;

public class PopularMoviesActivity extends AppCompatActivity implements PopularMoviesAdapter.onMovieThumbClickHandler,
        LoaderManager.LoaderCallbacks<ArrayList<PopularMovies>> {
    private PopularMoviesAdapter popularMoviesAdapter;
    private ArrayList<PopularMovies> listTobeSort;
    private PopularMoviesAdapter.onMovieThumbClickHandler clickHandler;
    private static final int MOVIES_LOADER_ID = 0;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar pBar;
    @BindView(R.id.layout_no_movie)
    LinearLayout no_movie_view;
    private int movieSortType = 0;
    private LoaderManager.LoaderCallbacks<ArrayList<PopularMovies>> callback = PopularMoviesActivity.this;
    private Bundle bundleForLoader = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize all the required resources by calling initResources()
        initResources();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_sort);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.choices, R.layout.spinner_text_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  /*Define type of sorting,
                    * 0- Popular
                    * 1- High rated
                    * 2- Myfavorites*/
                switch (position) {
                    case 0:
                        movieSortType = 0;
                        getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, bundleForLoader, callback);
                        break;
                    case 1:
                        movieSortType = 1;
                        getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, bundleForLoader, callback);

                        break;
                    case 2:
                        movieSortType = 2;
                        listTobeSort = getMoviesListBySortTypeFromDB();
                        if (listTobeSort.size() == 0)
                            no_movie_view.setVisibility(View.VISIBLE);
                        else
                            no_movie_view.setVisibility(View.GONE);
                        popularMoviesAdapter = new PopularMoviesAdapter(PopularMoviesActivity.this, listTobeSort, clickHandler);
                        mRecyclerView.setAdapter(popularMoviesAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setAdapter(adapter);

        return true;
    }

    /*
    Initializes all the required resources of Activity
     */
    private void initResources() {
        ButterKnife.bind(this);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, new Utils().numberOfColumsForGridView(this));
        mRecyclerView.setLayoutManager(gridLayoutManager);
        clickHandler = this;

        /*
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */

        getSupportLoaderManager().initLoader(MOVIES_LOADER_ID, bundleForLoader, callback);
    }

    private String getDataFromServer() {
        String dataString = null;
        URL url = NetworkUtils.buildUrl(movieSortType);
        try {
            dataString = NetworkUtils.getResponseFromHttUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataString;
    }

    private void setProgressBarVisible() {
        mRecyclerView.setVisibility(View.GONE);
        pBar.setVisibility(View.VISIBLE);
    }

    private void setDataGridVisible() {
        mRecyclerView.setVisibility(View.VISIBLE);
        pBar.setVisibility(View.GONE);
        no_movie_view.setVisibility(View.GONE);
    }

    private ArrayList<PopularMovies> getPopularMoviesJsonObjectFromString(String dataToParse) {
        return PopularMovieJsonUtil.parseMoviesData(PopularMoviesActivity.this, dataToParse, movieSortType);
    }

    @Override
    public void onClick(PopularMovies dataHolder) {
        Intent intent = new Intent(PopularMoviesActivity.this, PopularMovieDetailActivity.class);
        intent.putExtra(ConstantsUtility.INTENT_MOVIE_DATA, dataHolder);
        startActivity(intent);

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<PopularMovies>> loader) {

    }

    @Override
    public Loader<ArrayList<PopularMovies>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<PopularMovies>>(this) {
            ArrayList<PopularMovies> mMoviesDataList;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (mMoviesDataList != null) {
                    deliverResult(mMoviesDataList);
                } else {
                    setProgressBarVisible();
                    forceLoad();
                }
            }

            @Override
            public ArrayList<PopularMovies> loadInBackground() {
                if (NetworkUtils.isConnectedToNetwork(PopularMoviesActivity.this)) {
                    String resultString = getDataFromServer();
                    return getPopularMoviesJsonObjectFromString(resultString);
                } else {
                    return getMoviesListBySortTypeFromDB();

                }
            }

            /**
             //             * Sends the result of the load to the registered listener.
             //             *
             //             * @param data The result of the load
             //             */
            public void deliverResult(ArrayList<PopularMovies> data) {
                mMoviesDataList = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<ArrayList<PopularMovies>> loader, ArrayList<PopularMovies> data) {
        Intent moviesDataIntent = new Intent();
        listTobeSort = data;
        moviesDataIntent.putParcelableArrayListExtra(ConstantsUtility.INTENT_MOVIE_LIST, listTobeSort);
        if (listTobeSort.size() == 0) {
            no_movie_view.setVisibility(View.VISIBLE);
        } else {
            //populate data recieved from server to adapter
            popularMoviesAdapter = new PopularMoviesAdapter(PopularMoviesActivity.this, listTobeSort, clickHandler);
            mRecyclerView.setAdapter(popularMoviesAdapter);
            //set progressbar visibility gone,as data is loaded
            setDataGridVisible();
        }
    }

    private ArrayList<PopularMovies> getMoviesListBySortTypeFromDB() {
        Cursor cursor = null;
        PopularMoviesContantProvider.tableToProcess(TABLE_MOVIES);
        switch (movieSortType) {
            case 0:
                cursor = getContentResolver().query(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI, null, PopularMoviesContract.PopularMoviesEntry.SORT_TYPE
                        + " =0", null, null);
                return populateMovieList(cursor);

            case 1:
                cursor = getContentResolver().query(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI, null, PopularMoviesContract.PopularMoviesEntry.SORT_TYPE
                        + " =1", null, null);
                return populateMovieList(cursor);
            case 2:
                PopularMoviesContantProvider.tableToProcess(TABLE_FAV_MOVIES);
                cursor = getContentResolver().query(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI, null, null, null, null);
                return populateFavoriteMovieList(cursor);

        }

        return null;
    }

    private ArrayList<PopularMovies> populateMovieList(Cursor cursor) {
        ArrayList<PopularMovies> favList = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst())

            do {

                boolean adult = false, video = false;
                if (cursor.getInt(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.IS_ADULT)) == 1)
                    adult = true;

                PopularMovies holder = new PopularMovies(cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.POSTER_PATH)),
                        adult,
                        cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.OVERVIEW)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.RELEASE_DATE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.MOVIE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.ORIGINAL_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.ORIGINAL_LANGUAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.BACKDROP_PATH)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.POPULARITY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.VOTE_COUNT)),
                        video,
                        cursor.getDouble(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.VOTE_AVERAGE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.SORT_TYPE)));
                favList.add(holder);
            }
            while (cursor.moveToNext());
        return favList;
    }

    private ArrayList<PopularMovies> populateFavoriteMovieList(Cursor cursor) {
        ArrayList<PopularMovies> favList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst())
            do {
                PopularMoviesContantProvider.tableToProcess(TABLE_MOVIES);
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.MOVIE_ID));

                Cursor moviecursor = getContentResolver().query(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI,
                        null, PopularMoviesContract.PopularMoviesEntry.MOVIE_ID
                        + " =" + id, null, null);
                boolean adult = false, video = false;
                if(moviecursor!=null && moviecursor.getCount()>0) {
                    if (moviecursor.moveToFirst())
                        do {
                            if (moviecursor.getInt(moviecursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.IS_ADULT)) == 1)
                                adult = true;

                            PopularMovies holder = new PopularMovies(moviecursor.getString(moviecursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.POSTER_PATH)),
                                    adult,
                                    moviecursor.getString(moviecursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.OVERVIEW)),
                                    moviecursor.getString(moviecursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.RELEASE_DATE)),
                                    moviecursor.getInt(moviecursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.MOVIE_ID)),
                                    moviecursor.getString(moviecursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.ORIGINAL_TITLE)),
                                    moviecursor.getString(moviecursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.ORIGINAL_LANGUAGE)),
                                    moviecursor.getString(moviecursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.TITLE)),
                                    moviecursor.getString(moviecursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.BACKDROP_PATH)),
                                    moviecursor.getDouble(moviecursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.POPULARITY)),
                                    moviecursor.getInt(moviecursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.VOTE_COUNT)),
                                    video,
                                    moviecursor.getDouble(moviecursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.VOTE_AVERAGE)),
                                    moviecursor.getInt(moviecursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.SORT_TYPE)));
                            favList.add(holder);
                        }
                        while (moviecursor.moveToNext());
                }

            }
            while (cursor.moveToNext());
        return favList;
    }
}