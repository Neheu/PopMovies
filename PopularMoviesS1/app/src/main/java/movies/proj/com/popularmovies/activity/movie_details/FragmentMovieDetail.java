package movies.proj.com.popularmovies.activity.movie_details;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import movies.proj.com.popularmovies.R;
import movies.proj.com.popularmovies.data.MovieTrailers;
import movies.proj.com.popularmovies.data.PopularMovies;
import movies.proj.com.popularmovies.utility.ConstantsUtility;

/**
 * Created by Neha on 01-03-2017.
 */

public class FragmentMovieDetail extends Fragment implements TabLayout.OnTabSelectedListener , LoaderManager.LoaderCallbacks<ArrayList<MovieTrailers>>{
    @SuppressWarnings("unused")
    public static final String TAG = FragmentMovieDetail.class.getSimpleName();
    private PopularMovies mMovie;

    @BindView(R.id.movie_overview)
    TextView mMovieOverviewView;
    @BindView(R.id.movie_release_date)
    TextView mMovieReleaseDateView;
    @BindView(R.id.movie_poster)
    ImageView mMoviePosterView;
    @BindView(R.id.movie_user_vote)
    TextView mMovieVote;

    //    @BindView(R.id.tv_rating)
//    RatingBar mUserRating;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.tv_rating)
    TextView userRating;

    public FragmentMovieDetail() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Get data from intents set by parent activity*/
        if (getArguments().containsKey(ConstantsUtility.INTENT_MOVIE_DATA)) {
            mMovie = getArguments().getParcelable(ConstantsUtility.INTENT_MOVIE_DATA);
        }
        ConstantsUtility.SELECTED_MOVIE_ID = mMovie.id;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout)
                activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null && activity instanceof PopularMovieDetailActivity) {
            appBarLayout.setTitle(mMovie.title);
        }

        ImageView movieBackdrop = ((ImageView) activity.findViewById(R.id.movie_backdrop));
        if (movieBackdrop != null) {
            Picasso.with(activity)
                    .load(ConstantsUtility.BACKDROP_IMG_BASE_URL + mMovie.backdropPath)
                    .config(Bitmap.Config.RGB_565)
                    .into(movieBackdrop);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);
        ButterKnife.bind(this, rootView);

        mMovieOverviewView.setText(mMovie.overview);
        mMovieReleaseDateView.setText(mMovie.releaseDate);
        int rating = (mMovie.voteCount * 5) / 100;
        userRating.setText("Vote - " + String.valueOf(rating));
        mMovieVote.setText(getString(R.string.rating) + "- " + String.valueOf(mMovie.voteAverage) + "/10");

        Picasso.with(getActivity())
                .load(ConstantsUtility.POSTER_IMAGE_BASE + mMovie.posterPath)
                .config(Bitmap.Config.RGB_565)
                .into(mMoviePosterView);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.trailers)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.reviews)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.setAdapter(new TrailerReviewsAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount()));
        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*
        ArrayList<Trailer> trailers = mTrailerListAdapter.getTrailers();
        if (trailers != null && !trailers.isEmpty()) {
        outState.putParcelableArrayList(EXTRA_TRAILERS, trailers);
        }

        ArrayList<Review> reviews = mReviewListAdapter.getReviews();
        if (reviews != null && !reviews.isEmpty()) {
        outState.putParcelableArrayList(EXTRA_REVIEWS, reviews);
        }
        */
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie_detail, menu);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public Loader<ArrayList<MovieTrailers>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieTrailers>> loader, ArrayList<MovieTrailers> data) {

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieTrailers>> loader) {

    }

    private class TrailerReviewsAdapter extends FragmentStatePagerAdapter {
        int tabCount;

        public TrailerReviewsAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    TrailersTabFragment tab1 = new TrailersTabFragment();
                    return tab1;
                case 1:
                    TrailersTabFragment tab2 = new TrailersTabFragment();
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

}
