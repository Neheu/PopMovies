package movies.proj.com.popularmoviess1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

import movies.proj.com.popularmoviess1.PopMoviesTrailersAdapter;
import movies.proj.com.popularmoviess1.R;
import movies.proj.com.popularmoviess1.utility.ConstantsUtility;
import movies.proj.com.popularmoviess1.utility.Utils;

public class PopularMovieDetailActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private Intent mIntent;
    private String mThumbUrl;
    private TextView mMovieTitle;
    private TextView mReleaseDate;
    private ImageView movieathumbImageView;
    private TextView mRatingTextView;
    private TextView mOverView;
    private PopMoviesTrailersAdapter mMoviesTrailersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initializes required resources
        initResources();

    }

    private void initResources() {
        mMovieTitle = (TextView) findViewById(R.id.tv_movie_title);
        movieathumbImageView = (ImageView) findViewById(R.id.img_movie_thumb);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mRatingTextView = (TextView) findViewById(R.id.tv_rating);
        mOverView = (TextView) findViewById(R.id.tv_overview);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_trailers_list);
        mMoviesTrailersAdapter = new PopMoviesTrailersAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mIntent = getIntent();

        //check reuired data recieved by intents
        if (mIntent.hasExtra("thumb_url")) {
            mThumbUrl = ConstantsUtility.IMG_BASE_URL + ConstantsUtility.IMG_SIZES_LIST[4] + mIntent.getStringExtra("thumb_url");
            Picasso.with(this).load(mThumbUrl).into(movieathumbImageView);
        }
        if (mIntent.hasExtra("title"))
            mMovieTitle.setText(mIntent.getStringExtra("title"));
        if (mIntent.hasExtra("release_date")) {
            Date date = Utils.formatDateFromString(mIntent.getStringExtra("release_date"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            mReleaseDate.setText(String.valueOf(calendar.get(Calendar.YEAR)));

        }
        if (mIntent.hasExtra("rating"))
            mRatingTextView.setText(String.valueOf(mIntent.getDoubleExtra("rating", 0)) + "/10");
        if (mIntent.hasExtra("overview"))
            mOverView.setText(mIntent.getStringExtra("overview"));



        mRecyclerView.setLayoutManager(layoutManager);
        //add lines to seprate list itemss
        mRecyclerView.addItemDecoration(new movies.proj.com.popularmoviess1.utility.DividerItemDecoration(this,R.drawable.rv_divider));
        mRecyclerView.setAdapter(mMoviesTrailersAdapter);


    }

}
