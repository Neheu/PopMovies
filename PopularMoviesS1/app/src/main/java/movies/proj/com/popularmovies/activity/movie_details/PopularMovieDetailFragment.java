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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import movies.proj.com.popularmovies.R;
import movies.proj.com.popularmovies.data.PopularMovies;
import movies.proj.com.popularmovies.utility.ConstantsUtility;

/**
 * Created by Neha on 01-03-2017.
 */

public class PopularMovieDetailFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    @SuppressWarnings("unused")
    public static final String TAG = PopularMovieDetailFragment.class.getSimpleName();
    private PopularMovies mMovie;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager viewPager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Get data from intents set by parent activity*/
        //if (savedInstanceState == null) {
            Bundle arguments = getArguments();
            if (arguments != null && arguments.containsKey(ConstantsUtility.INTENT_MOVIE_DATA)) {
                mMovie = arguments.getParcelable(ConstantsUtility.INTENT_MOVIE_DATA);
                ConstantsUtility.SELECTED_MOVIE_DETAIL_DATA = mMovie;
            }
        //}

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
        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putInt(ConstantsUtility.MOVIE_ID,
                    mMovie.id);
            Fragment fragment = new Fragment();
            fragment.setArguments(arguments);
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.pager, fragment)
                    .commit();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);
        ButterKnife.bind(this, rootView);

        // mMovieOverviewView.setText(mMovie.overview);
        //mMovieReleaseDateView.setText(mMovie.releaseDate);
//        int rating = (mMovie.voteCount * 5) / 100;
//        userRating.setText("Vote - " + String.valueOf(rating));
//        mMovieVote.setText(getString(R.string.rating) + "- " + String.valueOf(mMovie.voteAverage) + "/10");
//
//        Picasso.with(getActivity())
//                .load(ConstantsUtility.POSTER_IMAGE_BASE + mMovie.posterPath)
//                .config(Bitmap.Config.RGB_565)
//                .into(mMoviePosterView);


        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);
        TrailerReviewsAdapter mPagerAdapter = new TrailerReviewsAdapter(getChildFragmentManager());
        mPagerAdapter.titlesList.add(getString(R.string.movie_detail));
        mPagerAdapter.titlesList.add(getString(R.string.trailers));
        mPagerAdapter.titlesList.add(getString(R.string.reviews));
        viewPager.setOffscreenPageLimit(3);

        viewPager.setAdapter(mPagerAdapter);


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
        Fragment fragment = null;
        switch (tab.getPosition()) {
            case 0:
                fragment = new DetailTabFrgment();
                break;
            case 1:
                fragment = new TrailersTabFragment();
                break;
            case 2:
                fragment = new DetailTabFrgment();
                break;
        }
        viewPager.setCurrentItem(tab.getPosition());
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.pager, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    private class TrailerReviewsAdapter extends FragmentStatePagerAdapter {
        ArrayList<String> titlesList;

        public TrailerReviewsAdapter(FragmentManager fm) {
            super(fm);
            titlesList = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    DetailTabFrgment movieDetail = new DetailTabFrgment();
                    return movieDetail;
                case 1:
                    TrailersTabFragment trailersTabFragment = new TrailersTabFragment();
                    return trailersTabFragment;
                case 2:
                    ReviewsTabFragment reviewsTabFragment = new ReviewsTabFragment();
                    return reviewsTabFragment;
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titlesList.get(position);
        }

        @Override
        public int getCount() {
            return titlesList.size();
        }
    }

}
