package movies.proj.com.popularmovies;

import android.content.Context;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import movies.proj.com.popularmovies.data.MovieTrailers;
import movies.proj.com.popularmovies.utility.ConstantsUtility;

/**
 * Created by Neha on 2/8/2017.
 */
public class PopMoviesTrailersAdapter extends RecyclerView.Adapter<PopMoviesTrailersAdapter.PopMoviesTrailedViewHolder> {
    private onTrailerThumbClickHandler clickHandler;
    private ArrayList<MovieTrailers> resultDataList;
    private Context context;

    @Override
    public PopMoviesTrailedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.movie_trailer_list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View layoutView = layoutInflater.inflate(layoutId, parent, shouldAttachToParentImmediately);
        int height = parent.getMeasuredHeight() / 4;
        layoutView.setMinimumHeight(height);

        return new PopMoviesTrailedViewHolder(layoutView);
    }

    public PopMoviesTrailersAdapter(final Context actContext, final ArrayList<MovieTrailers> trailerList, final onTrailerThumbClickHandler handler) {
        this.context = actContext;
        this.resultDataList = trailerList;
        this.clickHandler = handler;
    }


    public interface onTrailerThumbClickHandler {
        void onClick(MovieTrailers dataHoler);

    }

    @Override
    public void onBindViewHolder(PopMoviesTrailedViewHolder holder, int position) {
        if (resultDataList.size() != 0) {
            String imgUrl = ConstantsUtility.POSTER_IMAGE_BASE + resultDataList.get(position).id;
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

        }

        @Override
        public void onClick(View v) {
            MovieTrailers moviesDataHolder = resultDataList.get(getAdapterPosition());
            clickHandler.onClick(moviesDataHolder);
        }
    }
}
