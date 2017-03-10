package movies.proj.com.popularmovies.activity.movie_details;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import movies.proj.com.popularmovies.R;
import movies.proj.com.popularmovies.adapters.PopularMoviesReviewsAdapter;
import movies.proj.com.popularmovies.data.MovieReviews;
import movies.proj.com.popularmovies.data.MovieTrailers;
import movies.proj.com.popularmovies.data.PopularMoviesContantProvider;
import movies.proj.com.popularmovies.data.PopularMoviesContract;
import movies.proj.com.popularmovies.utility.ConstantsUtility;
import movies.proj.com.popularmovies.utility.NetworkUtils;
import movies.proj.com.popularmovies.utility.PopularMovieJsonUtil;

import static movies.proj.com.popularmovies.utility.DatabaseUtils.TABLE_REVIEWS;
import static movies.proj.com.popularmovies.utility.DatabaseUtils.TABLE_TRAILERS;

/**
 * Created by Neha on 09-03-2017.
 */

public class ReviewsTabFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<MovieReviews>> {
    @BindView(R.id.trailer_list)
    RecyclerView rv_trailer;
    @BindView(R.id.no_reviews)
    TextView noReview;
    private ArrayList<MovieReviews> reviewsList;
    private PopularMoviesReviewsAdapter adapter;
    private LoaderManager.LoaderCallbacks<ArrayList<MovieReviews>> callback = this;
    private Bundle bundleForLoader = null;
    private final int LOADER_ID = 1100;
    int mMovie_id;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Returning the layout file after inflating
        View rootView = inflater.inflate(R.layout.review_recycle_list_layout, container, false);
        ButterKnife.bind(this, rootView);
        rv_trailer.setHasFixedSize(true);
        mMovie_id = ConstantsUtility.SELECTED_MOVIE_DETAIL_DATA.id;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_trailer.setLayoutManager(linearLayoutManager);
        adapter = new PopularMoviesReviewsAdapter(getActivity());
        rv_trailer.setAdapter(adapter);
        if(savedInstanceState!=null) {
            reviewsList = savedInstanceState.getParcelableArrayList(ConstantsUtility.INTENT_MOVIE_LIST);
            adapter.setResultDataList(reviewsList);
            adapter.notifyDataSetChanged();

        }
        else
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, bundleForLoader, callback);

        return rootView;
    }

    @Override
    public Loader<ArrayList<MovieReviews>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<MovieReviews>>(getActivity()) {
            ArrayList<MovieReviews> mMoviesDataList;

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
            public ArrayList<MovieReviews> loadInBackground() {
                if (NetworkUtils.isConnectedToNetwork(getActivity())) {
                    String resultString = getDataFromServer(mMovie_id);
                    return getPopularMoviesJsonObjectFromString(resultString);
                } else
                    return populateReviewsFromDatabase(mMovie_id);
            }

            /**
             //             * Sends the result of the load to the registered listener.
             //             *
             //             * @param data The result of the load
             //             */
            public void deliverResult(ArrayList<MovieReviews> data) {
                mMoviesDataList = data;
                super.deliverResult(data);
            }
        };
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ConstantsUtility.INTENT_MOVIE_LIST,reviewsList);
    }
    private String getDataFromServer(int id) {
        String dataString = null;
        URL url = NetworkUtils.buildUrl(id, "reviews");
        try {
            dataString = NetworkUtils.getResponseFromHttUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataString;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieReviews>> loader, ArrayList<MovieReviews> data) {
        reviewsList = data;
        progressBar.setVisibility(View.GONE);

        if (reviewsList.size() == 0)
            noReview.setVisibility(View.VISIBLE);
        else {
            adapter.setResultDataList(data);
            adapter.notifyDataSetChanged();
            noReview.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieReviews>> loader) {

    }

    /* Parse and get the data from server.*/
    private ArrayList<MovieReviews> getPopularMoviesJsonObjectFromString(String dataToParse) {
        return PopularMovieJsonUtil.parseReviewssData(getActivity(), dataToParse);
    }

    /* If Offline , get data saved into database*/
    private ArrayList<MovieReviews> populateReviewsFromDatabase(int id) {
        ArrayList<MovieReviews> reviewsList = new ArrayList<>();
        PopularMoviesContantProvider.tableToProcess(TABLE_REVIEWS);
        Cursor cursor = getActivity().getContentResolver().query(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI, null, PopularMoviesContract.PopularMoviesEntry.MOVIE_ID
                + " ="+id, null, null);
        if (cursor != null && cursor.moveToFirst())

            do {
                MovieReviews holder = new MovieReviews(
                        cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.REVIEW_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.MOVIE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.REVIEW_AUTHOR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.REVIEW_CONTENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PopularMoviesContract.PopularMoviesEntry.REVIEW_URL))


                );


                reviewsList.add(holder);
            }
            while (cursor.moveToNext());
        return reviewsList;
    }

}
