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
import movies.proj.com.popularmovies.data.PopularMoviesContract;
import movies.proj.com.popularmovies.utility.ConstantsUtility;
import movies.proj.com.popularmovies.utility.NetworkUtils;
import movies.proj.com.popularmovies.utility.PopularMovieJsonUtil;
import movies.proj.com.popularmovies.utility.Utils;

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
    private int movieSortType = 1;
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
                switch (position) {
                    case 0:
                        movieSortType = ConstantsUtility.MOVIE_SORT_TYPE[0];
                        getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, bundleForLoader, callback);
                        break;
                    case 1:
                        movieSortType = ConstantsUtility.MOVIE_SORT_TYPE[1];
                        getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, bundleForLoader, callback);

                        break;
                    case 2:
                        movieSortType = ConstantsUtility.MOVIE_SORT_TYPE[2];
                        getFavoriteMoviesList();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                String resultString = getDataFromServer();
                return getPopularMoviesJsonObjectFromString(resultString);
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

        //populate data recieved from server to adapter
        popularMoviesAdapter = new PopularMoviesAdapter(PopularMoviesActivity.this, listTobeSort, clickHandler);
        mRecyclerView.setAdapter(popularMoviesAdapter);
        //set progressbar visibility gone,as data is loaded
        setDataGridVisible();
    }

    private ArrayList<PopularMovies> getFavoriteMoviesList() {
        Cursor cursor = getContentResolver().query(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI, null, PopularMoviesContract.PopularMoviesEntry.IS_MARKED_FAVORITE
                + " =1", null, null);
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
                        false,
                        cursor.getDouble(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.VOTE_AVERAGE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.SORT_TYPE)));
                favList.add(holder);

            }
            while (cursor.moveToNext());

        return favList;
    }


}