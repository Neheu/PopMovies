package movies.proj.com.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import movies.proj.com.popularmovies.R;
import movies.proj.com.popularmovies.data.MovieReviews;
import movies.proj.com.popularmovies.data.MovieTrailers;
import movies.proj.com.popularmovies.utility.ConstantsUtility;

/**
 * Created by Neha on 09-03-2017.
 */

public class PopularMoviesReviewsAdapter extends RecyclerView.Adapter<PopularMoviesReviewsAdapter.MoviesReviewViewHolder> {
    ArrayList<MovieReviews> reviewsList = new ArrayList<>();
    Context context;

    @Override
    public PopularMoviesReviewsAdapter.MoviesReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.movie_review_tab_items;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View layoutView = layoutInflater.inflate(layoutId, parent, shouldAttachToParentImmediately);
        return new PopularMoviesReviewsAdapter.MoviesReviewViewHolder(layoutView);
    }

    public PopularMoviesReviewsAdapter(Context context) {
        this.context = context;

    }

    public void setResultDataList(final ArrayList<MovieReviews> trailerList) {
        this.reviewsList = trailerList;

    }

    @Override
    public void onBindViewHolder(PopularMoviesReviewsAdapter.MoviesReviewViewHolder holder, int position) {
        holder.author.setText(reviewsList.get(position).author);
        holder.review.setText(reviewsList.get(position).content);

    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    class MoviesReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView review, author;

        public MoviesReviewViewHolder(View itemView) {
            super(itemView);
            review = (TextView) itemView.findViewById(R.id.tv_review);
            author = (TextView) itemView.findViewById(R.id.tv_review_author);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            MovieReviews moviesDataHolder = reviewsList.get(getAdapterPosition());
//            clickHandler.onClick(moviesDataHolder);
        }
    }
}
