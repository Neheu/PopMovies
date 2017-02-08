package movies.proj.com.popularmoviess1.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import movies.proj.com.popularmoviess1.PopularMoviesAdapter;
import movies.proj.com.popularmoviess1.R;
import movies.proj.com.popularmoviess1.core.PopularMovies;
import movies.proj.com.popularmoviess1.utility.NetworkUtils;
import movies.proj.com.popularmoviess1.utility.PopularMovieJsonUtil;
import movies.proj.com.popularmoviess1.utility.Utils;

public class PopularMoviesActivity extends AppCompatActivity implements PopularMoviesAdapter.onMovieThumbClickHandler {
    private RecyclerView mRecyclerView;
    private PopularMoviesAdapter popularMoviesAdapter;
    private ProgressBar pBar;
    private ArrayList<PopularMovies> listTobeSort;
    private PopularMoviesAdapter.onMovieThumbClickHandler clickHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Initialize all the required resources by calling initResources()
        initResources();

        //connect to movie server to get data
        new GetPopularMoviesList().execute(true);

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

                        // Collections.sort(listTobeSort, new MoviesSortComparatorUtils(true));
                        new GetPopularMoviesList().execute(true);

                        break;
                    case 1:
//                                Collections.sort(listTobeSort, new MoviesSortComparatorUtils(false));
                        new GetPopularMoviesList().execute(false);

                        break;
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
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        pBar = (ProgressBar) findViewById(R.id.progress_bar);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, new Utils().numberOfColumsForGridView(this));
        mRecyclerView.setLayoutManager(gridLayoutManager);
        clickHandler = this;
    }

    private String getDataFromServer(boolean type) {
        String dataString = null;
        URL url = NetworkUtils.buildUrl(type);
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
        return PopularMovieJsonUtil.getParseDataFromJSon(dataToParse);
    }

    @Override
    public void onClick(PopularMovies dataHolder) {
        Intent intent = new Intent(PopularMoviesActivity.this, PopularMovieDetailActivity.class);
        intent.putExtra("title", dataHolder.getTitle());
        intent.putExtra("release_date", dataHolder.getReleaseDate());
        intent.putExtra("rating", dataHolder.getVoteAverage());
        intent.putExtra("thumb_url", dataHolder.getPosterPath());
        intent.putExtra("rating", dataHolder.getVoteAverage());
        intent.putExtra("overview", dataHolder.getOverview());
        startActivity(intent);

    }

    private class GetPopularMoviesList extends AsyncTask<Boolean, Void, ArrayList<PopularMovies>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //set visible progressbar as data is loading from server.
            setProgressBarVisible();
        }

        @Override
        protected ArrayList<PopularMovies> doInBackground(Boolean... params) {
            //connect to url and get data in string 'resultString'
            String resultString = getDataFromServer(params[0]);
            return getPopularMoviesJsonObjectFromString(resultString);
        }

        @Override
        protected void onPostExecute(ArrayList<PopularMovies> resultHolder) {
            super.onPostExecute(resultHolder);
            listTobeSort = resultHolder;
            //populate data recieved from server to adapter
            popularMoviesAdapter = new PopularMoviesAdapter(PopularMoviesActivity.this, listTobeSort, clickHandler);
            mRecyclerView.setAdapter(popularMoviesAdapter);
            //set progressbar visibility gone,as data is loaded
            setDataGridVisible();

        }
    }


}
