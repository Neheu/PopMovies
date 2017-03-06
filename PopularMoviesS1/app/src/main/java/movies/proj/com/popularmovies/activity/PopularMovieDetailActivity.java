package movies.proj.com.popularmovies.activity;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import movies.proj.com.popularmovies.R;
import movies.proj.com.popularmovies.data.PopularMovies;
import movies.proj.com.popularmovies.data.PopularMoviesContract;
import movies.proj.com.popularmovies.data.PopularMoviesDBHelper;
import movies.proj.com.popularmovies.utility.ConstantsUtility;

public class PopularMovieDetailActivity extends AppCompatActivity  {

    @BindView(R.id.detail_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.mark_fav)
    FloatingActionButton markFavMovie;
    private Context context;
    private PopularMovies data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movie_detail);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        context = PopularMovieDetailActivity.this;
        data = getIntent().getExtras().getParcelable(ConstantsUtility.INTENT_MOVIE_DATA);

        checkIfMarkedFav();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(ConstantsUtility.INTENT_MOVIE_DATA,
                    getIntent().getParcelableExtra(ConstantsUtility.INTENT_MOVIE_DATA));
            FragmentMovieDetail fragment = new FragmentMovieDetail();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction().add(R.id.movie_detail_container, fragment).commit();
        }
    }

    public void markFavMovieClickHandler(View view) {
        ContentValues values = updateFavoriteMovie();
        String selection = PopularMoviesContract.PopularMoviesEntry.MOVIE_ID + " = '" + data.id + "'";

        context.getContentResolver().update(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI, values, selection, null);

    }

    private ContentValues updateFavoriteMovie() {
        ContentValues values = new ContentValues();
        boolean isAlreadyMarkedAsFav = new PopularMoviesDBHelper(context).isMovieAlreadyMarkedAsFav(data.id);
        if (isAlreadyMarkedAsFav) {
            values.put(PopularMoviesContract.PopularMoviesEntry.IS_MARKED_FAVORITE, false);
            markFavMovie.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_unfavorite));
        } else {
            values.put(PopularMoviesContract.PopularMoviesEntry.IS_MARKED_FAVORITE, true);
            markFavMovie.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite));
        }
        return values;
    }

    private void checkIfMarkedFav() {
        boolean isAlreadyMarkedAsFav = new PopularMoviesDBHelper(context).isMovieAlreadyMarkedAsFav(data.id);
        if (isAlreadyMarkedAsFav) {
            markFavMovie.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite));
        } else {
            markFavMovie.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_unfavorite));
        }
    }


}