package movies.proj.com.popularmovies.activity.movie_details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import movies.proj.com.popularmovies.R;
import movies.proj.com.popularmovies.data.PopularMovies;
import movies.proj.com.popularmovies.utility.ConstantsUtility;

/**
 * Created by Neha on 09-03-2017.
 */

public class DetailTabFrgment extends Fragment {
    @BindView(R.id.movie_poster)
    ImageView poster;
    @BindView(R.id.movie_overview)
    TextView overView;
    @BindView(R.id.movie_release_date)
    TextView releaseDate;
    @BindView(R.id.tmdb_rating)
    TextView tmdbRating;
    @BindView(R.id.movie_user_vote)
    TextView userVote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Returning the layout file after inflating
        View rootView = inflater.inflate(R.layout.layout_movie_detail, container, false);
        ButterKnife.bind(this, rootView);
        PopularMovies data = ConstantsUtility.SELECTED_MOVIE_DETAIL_DATA;
        String imgUrl = ConstantsUtility.POSTER_IMAGE_BASE + data.posterPath;

        Picasso.with(getActivity()).load(imgUrl).into(poster);
        overView.setText(data.overview);
        releaseDate.setText(data.releaseDate);
        int rating = (data.voteCount * 5) / 100;
        userVote.setText(String.valueOf(rating));
        tmdbRating.setText(getString(R.string.rating) + "- " + String.valueOf(data.voteAverage) + "/10");
        return rootView;
    }
}
