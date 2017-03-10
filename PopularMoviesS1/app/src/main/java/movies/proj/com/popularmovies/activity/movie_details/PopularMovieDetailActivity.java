package movies.proj.com.popularmovies.activity.movie_details;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import movies.proj.com.popularmovies.R;
import movies.proj.com.popularmovies.data.PopularMovies;
import movies.proj.com.popularmovies.data.PopularMoviesContantProvider;
import movies.proj.com.popularmovies.data.PopularMoviesContract;
import movies.proj.com.popularmovies.data.PopularMoviesDBHelper;
import movies.proj.com.popularmovies.utility.ConstantsUtility;

import static movies.proj.com.popularmovies.utility.DatabaseUtils.TABLE_FAV_MOVIES;

public class PopularMovieDetailActivity extends AppCompatActivity implements TrailersTabFragment.onShareTrailerListener {


    @BindView(R.id.mark_fav)
    @Nullable
    FloatingActionButton markFavMovie;
    @BindView(R.id.detail_toolbar)
    @Nullable
    Toolbar mToolbar;
    private Context context;
    private PopularMovies data;
    private PopularMovieDetailFragment fragment;
    private static String FRAGMENT_DETAILS_TAG = "fragment_detail";
    private String trailerToShare = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movie_detail);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        context = PopularMovieDetailActivity.this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {


            data = getIntent().getExtras().getParcelable(ConstantsUtility.INTENT_MOVIE_DATA);

            Bundle arguments = new Bundle();
            arguments.putParcelable(ConstantsUtility.INTENT_MOVIE_DATA,
                    getIntent().getParcelableExtra(ConstantsUtility.INTENT_MOVIE_DATA));
            fragment = new PopularMovieDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container, fragment, FRAGMENT_DETAILS_TAG).commit();
        } else {
            data = getIntent().getExtras().getParcelable(ConstantsUtility.INTENT_MOVIE_DATA);
            isMarkedFav = savedInstanceState.getBoolean(ConstantsUtility.IS_FAVORITE);
        }

 /* Check and update if selected movie is marked*/
        updateFavoriteStar(checkIfMarkedAsFavorite());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ConstantsUtility.INTENT_MOVIE_DATA, data);
        outState.putBoolean(ConstantsUtility.IS_FAVORITE, isMarkedFav);
    }

    private boolean isMarkedFav;

    public void markFavMovieClickHandler(View view) {
        /* Check if movie is malready marked as favorite*/
        boolean isAlreadyMarkedAsFav = checkIfMarkedAsFavorite();
        PopularMoviesContantProvider.tableToProcess(TABLE_FAV_MOVIES);

        if (isAlreadyMarkedAsFav) {
            /* Delete that id from Favorite table, to make it as unFavorite */
            context.getContentResolver().delete(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI, PopularMoviesContract.PopularMoviesEntry.MOVIE_ID
                    + " =" + data.id, null
            );
            isMarkedFav = false;
            updateFavoriteStar(isMarkedFav);

        } else {
            /* Insert id to Favorite table to mark it as Favorite*/
            ContentValues values = new ContentValues();
            values.put(PopularMoviesContract.PopularMoviesEntry.MOVIE_ID, data.id);
            context.getContentResolver().insert(PopularMoviesContract.PopularMoviesEntry.CONTENT_URI, values);
            isMarkedFav = true;
            updateFavoriteStar(isMarkedFav);

        }
    }


    private boolean checkIfMarkedAsFavorite() {
        return new PopularMoviesDBHelper(context).isMovieAlreadyMarkedAsFav(data.id);

    }

    private void updateFavoriteStar(boolean isAlreadyMarkedAsFav) {
        if (isAlreadyMarkedAsFav) {
            markFavMovie.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite));
        } else {
            markFavMovie.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_unfavorite));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            if (!TextUtils.isEmpty(trailerToShare)) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, trailerToShare);
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share_via)));
            } else {
                Toast.makeText(this, getString(R.string.no_trailer_to_share), Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void shareTrailer(String s) {
        trailerToShare = s;
    }
}
