package movies.proj.com.popularmovies.activity.movie_details;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import movies.proj.com.popularmovies.adapters.PopMoviesTrailersAdapter;
import movies.proj.com.popularmovies.R;
import movies.proj.com.popularmovies.data.MovieTrailers;
import movies.proj.com.popularmovies.utility.ConstantsUtility;
import movies.proj.com.popularmovies.utility.NetworkUtils;
import movies.proj.com.popularmovies.utility.PopularMovieJsonUtil;

/**
 * Created by Neha on 04-03-2017.
 */
public class TrailersTabFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<MovieTrailers>>,
        PopMoviesTrailersAdapter.onTrailerThumbClickHandler {
    private ArrayList<MovieTrailers> trailersList;
    private Context context;
    private int id;
    private PopMoviesTrailersAdapter.onTrailerThumbClickHandler clickHandler;
    @BindView(R.id.trailer_list)
    RecyclerView rv_trailer;
    private ProgressDialog progressDialog;
    private LoaderManager.LoaderCallbacks<ArrayList<MovieTrailers>> callback = this;
    private Bundle bundleForLoader = null;
    private int mMovie_id;
    private PopMoviesTrailersAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Returning the layout file after inflating
        View rootView = inflater.inflate(R.layout.trailer_tab_layout, container, false);
        ButterKnife.bind(this, rootView);
        clickHandler = this;
        rv_trailer.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_trailer.setLayoutManager(linearLayoutManager);
        adapter = new PopMoviesTrailersAdapter(getActivity(), clickHandler);
        rv_trailer.setAdapter(adapter);
        getActivity().getSupportLoaderManager().initLoader(111, bundleForLoader, callback);

        return rootView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovie_id = ConstantsUtility.SELECTED_MOVIE_ID;
    }

    @Override
    public Loader<ArrayList<MovieTrailers>> onCreateLoader(final int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<MovieTrailers>>(getActivity()) {
            ArrayList<MovieTrailers> mMoviesDataList;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (mMoviesDataList != null) {
                    deliverResult(mMoviesDataList);
                } else {

                    forceLoad();
                }
            }

            @Override
            public ArrayList<MovieTrailers> loadInBackground() {
                String resultString = getDataFromServer(mMovie_id);
                return getPopularMoviesJsonObjectFromString(resultString);
            }

            /**
             //             * Sends the result of the load to the registered listener.
             //             *
             //             * @param data The result of the load
             //             */
            public void deliverResult(ArrayList<MovieTrailers> data) {
                mMoviesDataList = data;
                super.deliverResult(data);
            }
        };

    }

    private ArrayList<MovieTrailers> getPopularMoviesJsonObjectFromString(String dataToParse) {
        return PopularMovieJsonUtil.parseTrailersData(getActivity(), dataToParse);
    }

    private String getDataFromServer(int id) {
        String dataString = null;
        URL url = NetworkUtils.buildUrlTrailes(id);
        try {
            dataString = NetworkUtils.getResponseFromHttUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataString;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieTrailers>> loader, ArrayList<MovieTrailers> data) {
        trailersList = data;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.setResultDataList(trailersList);
                rv_trailer.setAdapter(adapter);
                adapter.notifyDataSetChanged();            }
        });


    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieTrailers>> loader) {

    }

    @Override
    public void onClick(MovieTrailers dataHoler) {
        watchYoutubeVideo(dataHoler.key);
    }


    public void watchYoutubeVideo(String key) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + key));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }
}
