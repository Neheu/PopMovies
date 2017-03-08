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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
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

public class FragmentMovieDetail extends Fragment implements TabLayout.OnTabSelectedListener {
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
    private TrailerReviewsAdapter mPagerAdapter;

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

        //TODO : replaced "getActivity().getSupportFragmentManager()" by "getChildFragmentManager()"
        //TODO: this attaches the viewPager fragments with this fragment's lifecycle & not the activity's one

        //TODO : tabLayout.getTabCount(), i replaced it with TAB_COUNT
        final int TAB_COUNT = 2;

        mPagerAdapter = new TrailerReviewsAdapter( getChildFragmentManager(),TAB_COUNT);

        //TODO : add successively yout Titles
        mPagerAdapter.titlesArray.add(getString(R.string.trailers));
        mPagerAdapter.titlesArray.add(getString(R.string.reviews));
        viewPager.setAdapter( mPagerAdapter);

        //Sync tab with viewPager
        tabLayout.setupWithViewPager(viewPager);

        //Adding onTabSelectedListener to swipe views
        tabLayout.addOnTabSelectedListener(this);
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
        //TODO : viewPager.setCurrentItem(tab.getPosition());
        //TODO : no need to deal with this manually, TabLayout has a better way to do this
        //TODO :

        //Okay, here, we set the viewPager visible
        // And we hide everytime the user presses the device's backButton
        if(viewPager.getVisibility() == View.GONE){
            viewPager.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if(viewPager.getVisibility() == View.GONE){
            viewPager.setVisibility(View.VISIBLE);
        }
    }


    private class TrailerReviewsAdapter extends FragmentStatePagerAdapter {
        int tabCount;

        //TODO: an array that will handle titles to be shown on the TabLayout
        public ArrayList<String> titlesArray;

        public TrailerReviewsAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;

            //TODO: an array that will handle titles to be shown on the TabLayout
            titlesArray = new ArrayList<>();
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
        public CharSequence getPageTitle(int position) {

            //Do your best to not have this array empty or an OutOfIndexException will be thrown
            return titlesArray.get(position);
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

}
