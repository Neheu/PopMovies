package movies.proj.com.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import movies.proj.com.popularmovies.R;
import movies.proj.com.popularmovies.data.PopularMovies;
import movies.proj.com.popularmovies.utility.ConstantsUtility;

/**
 * Created by hp on 2/5/2017.
 */
public class PopularMoviesAdapter extends RecyclerView.Adapter<PopularMoviesAdapter.MoviesViewHolder> {
    private ArrayList<PopularMovies> resultDataList;
    private Context context;
    private onMovieThumbClickHandler clickHandler;

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.movie_grid_items;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View layoutView = layoutInflater.inflate(layoutId, parent, shouldAttachToParentImmediately);
        int height = parent.getMeasuredHeight() / 4;
        layoutView.setMinimumHeight(height);

        return new MoviesViewHolder(layoutView);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public PopularMoviesAdapter(final Context actContext, final ArrayList<PopularMovies> moviesDataList, final onMovieThumbClickHandler handler) {
        this.context = actContext;
        this.resultDataList = moviesDataList;
        this.clickHandler = handler;
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        if (resultDataList.size() != 0) {
            String imgUrl = ConstantsUtility.POSTER_IMAGE_BASE + resultDataList.get(position).posterPath;
            Picasso.with(context).load(imgUrl).into(holder.movie_thumb);

        }
    }

    @Override
    public int getItemCount() {
        return resultDataList.size();
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        ImageView movie_thumb;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            movie_thumb = (ImageView) itemView.findViewById(R.id.img_movie_thumb);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            PopularMovies moviesDataHolder = resultDataList.get(getAdapterPosition());
            clickHandler.onClick(moviesDataHolder);
        }
    }

    public interface onMovieThumbClickHandler {
        void onClick(PopularMovies dataHolder);

    }

}
