package movies.proj.com.popularmovies.activity.movie_details;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import movies.proj.com.popularmovies.adapters.PopularMoviesTrailersAdapter;
import movies.proj.com.popularmovies.R;
import movies.proj.com.popularmovies.data.MovieTrailers;
import movies.proj.com.popularmovies.data.PopularMoviesContantProvider;
import movies.proj.com.popularmovies.data.PopularMoviesContract;
import movies.proj.com.popularmovies.utility.ConstantsUtility;
import movies.proj.com.popularmovies.utility.NetworkUtils;
import movies.proj.com.popularmovies.utility.PopularMovieJsonUtil;
import movies.proj.com.popularmovies.utility.Utils;

import static movies.proj.com.popularmovies.utility.DatabaseUtils.TABLE_TRAILERS;

/**
 * Created by Neha on 04-03-2017.
 */
public class TrailersTabFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<MovieTrailers>>,
        PopularMoviesTrailersAdapter.onTrailerThumbClickHandler {
    private static final int LOADER_ID = 1111;
    private ArrayList<MovieTrailers> trailersList;
    private int id;
    private PopularMoviesTrailersAdapter.onTrailerThumbClickHandler clickHandler;
    @BindView(R.id.trailer_list)
    RecyclerView rv_trailer;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private LoaderManager.LoaderCallbacks<ArrayList<MovieTrailers>> callback = this;
    private Bundle bundleForLoader = null;
    private int mMovie_id;
    private PopularMoviesTrailersAdapter adapter;
    private onShareTrailerListener shareTrailerListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Returning the layout file after inflating
        View rootView = inflater.inflate(R.layout.trailer_recycle_list_layout, container, false);
        ButterKnife.bind(this, rootView);
        clickHandler = this;
        mMovie_id = ConstantsUtility.SELECTED_MOVIE_DETAIL_DATA.id;
        rv_trailer.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), new Utils().numberOfColumsForGridView(getActivity()));

        rv_trailer.setLayoutManager(gridLayoutManager);
        adapter = new PopularMoviesTrailersAdapter(getActivity(), clickHandler);
        rv_trailer.setAdapter(adapter);
        if(savedInstanceState!=null) {
            trailersList = savedInstanceState.getParcelableArrayList(ConstantsUtility.INTENT_MOVIE_LIST);
            adapter.setResultDataList(trailersList);
            adapter.notifyDataSetChanged();

        }
        else
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, bundleForLoader, callback);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ConstantsUtility.INTENT_MOVIE_LIST,trailersList);
    }

    public interface onShareTrailerListener {
        public void shareTrailer(String s);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            shareTrailerListener = (onShareTrailerListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onShareTrailerListener");
        }
    }

    @Override
    public Loader<ArrayList<MovieTrailers>> onCreateLoader(final int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<MovieTrailers>>(getActivity()) {
            ArrayList<MovieTrailers> mMoviesDataList;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                progressBar.setVisibility(View.VISIBLE);
                if (mMoviesDataList != null) {
                    deliverResult(mMoviesDataList);
                } else {

                    forceLoad();
                }
            }

            @Override
            public ArrayList<MovieTrailers> loadInBackground() {
                if (NetworkUtils.isConnectedToNetwork(getActivity())) {
                    String resultString = getDataFromServer(mMovie_id);
                    return getPopularMoviesJsonObjectFromString(resultString);
                } else
                    return populateTrailerFromDatabase(mMovie_id);
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

    /* Parse and get the data from server.*/
    private ArrayList<MovieTrailers> getPopularMoviesJsonObjectFromString(String dataToParse) {
        return PopularMovieJsonUtil.parseTrailersData(getActivity(), dataToParse);
    }

    private String getDataFromServer(int id) {
        String dataString = null;
        URL url = NetworkUtils.buildUrl(id, "videos");
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
        progressBar.setVisibility(View.GONE);
        adapter.setResultDataList(trailersList);
        shareTrailerListener.shareTrailer("http://www.youtube.com/watch?v=" + trailersList.get(0).key);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieTrailers>> loader) {

    }

    @Override
    public void onClick(MovieTrailers dataHolder) {
        watchYoutubeVideo(dataHolder.key);
    }

    /* watch trailer either vie youtube app or in web browser.*/
    public void watchYoutubeVideo(String key) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + key));
        try {
            getActivity().startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            getActivity().startActivity(webIntent);
        }
    }

    /* If Offline , get data saved into database*/
    private ArrayList<MovieTrailers> populateTrailerFromDatabase(int mMovie_id) {
        ArrayList<MovieTrailers> trailersList = new ArrayList<>();
        PopularMoviesContantProvider.tableToProcess(TABLE_TRAILERS);
        Cursor cursor = getActivity().getContentResolver().query(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI, null, PopularMoviesContract.PopularMoviesEntry.MOVIE_ID
                + " =" + mMovie_id, null, null);
        if (cursor != null && cursor.moveToFirst())

            do {
                MovieTrailers holder = new MovieTrailers(
                        cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.TRAILER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.TRAILER_KEY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.MOVIE_ID))
                );
                trailersList.add(holder);
            }
            while (cursor.moveToNext());
        return trailersList;
    }

}
