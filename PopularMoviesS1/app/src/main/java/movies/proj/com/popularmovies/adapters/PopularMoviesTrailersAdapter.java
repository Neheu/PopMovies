package movies.proj.com.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import movies.proj.com.popularmovies.R;
import movies.proj.com.popularmovies.data.MovieTrailers;
import movies.proj.com.popularmovies.utility.ConstantsUtility;

/**
 * Created by Neha on 2/8/2017.
 */
public class PopularMoviesTrailersAdapter extends RecyclerView.Adapter<PopularMoviesTrailersAdapter.PopMoviesTrailedViewHolder> {
    private onTrailerThumbClickHandler clickHandler;
    private ArrayList<MovieTrailers> resultDataList = new ArrayList<>();
    private Context context;
    private String pathPoster;

    @Override
    public PopMoviesTrailedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.movie_trailer_list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View layoutView = layoutInflater.inflate(layoutId, parent, shouldAttachToParentImmediately);
        return new PopMoviesTrailedViewHolder(layoutView);
    }

    public PopularMoviesTrailersAdapter(final Context actContext, final onTrailerThumbClickHandler handler) {
        this.context = actContext;
        this.clickHandler = handler;
    }

    public void setResultDataList(final ArrayList<MovieTrailers> trailerList) {
        this.resultDataList = trailerList;
        this.pathPoster = ConstantsUtility.SELECTED_MOVIE_DETAIL_DATA.posterPath;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface onTrailerThumbClickHandler {
        void onClick(MovieTrailers dataHoler);

    }

    @Override
    public void onBindViewHolder(PopMoviesTrailedViewHolder holder, int position) {
        if (resultDataList.size() != 0) {
            String imgUrl = ConstantsUtility.POSTER_IMAGE_BASE + pathPoster;
            Picasso.with(context).load(imgUrl).into(holder.trailerThumb);

        }
    }

    @Override
    public int getItemCount() {
        return resultDataList.size();
    }

    class PopMoviesTrailedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView trailerThumb;

        public PopMoviesTrailedViewHolder(View itemView) {
            super(itemView);
            trailerThumb = (ImageView) itemView.findViewById(R.id.trailer_thumbnail);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            MovieTrailers moviesDataHolder = resultDataList.get(getAdapterPosition());
            clickHandler.onClick(moviesDataHolder);
        }
    }

}
