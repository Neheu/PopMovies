package movies.proj.com.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import movies.proj.com.popularmovies.R;
import movies.proj.com.popularmovies.data.MovieTrailers;

/**
 * Created by Neha on 2/8/2017.
 */
public class PopMoviesTrailersAdapter extends RecyclerView.Adapter<PopMoviesTrailersAdapter.PopMoviesTrailedViewHolder> {
    private onTrailerThumbClickHandler clickHandler;
    private ArrayList<MovieTrailers> resultDataList = new ArrayList<>();
    private Context context;

    @Override
    public PopMoviesTrailedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.movie_trailer_list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View layoutView = layoutInflater.inflate(layoutId, parent, shouldAttachToParentImmediately);
        return new PopMoviesTrailedViewHolder(layoutView);
    }

    public PopMoviesTrailersAdapter(final Context actContext, final onTrailerThumbClickHandler handler) {
        this.context = actContext;
        this.clickHandler = handler;
    }

    public void setResultDataList(final ArrayList<MovieTrailers> trailerList) {
        this.resultDataList = trailerList;
    }

    public interface onTrailerThumbClickHandler {
        void onClick(MovieTrailers dataHoler);

    }

    @Override
    public void onBindViewHolder(PopMoviesTrailedViewHolder holder, int position) {
        if (resultDataList.size() != 0) {
//            String imgUrl = ConstantsUtility.POSTER_IMAGE_BASE + resultDataList.get(position).posterPath;
//            Picasso.with(context).load(imgUrl).into(holder.trailerThumb);
            //TODO: Avoid setting integer as Text, instead of setting as text,
            //TODO: Dalvik Machine will try to find a ressource corresponding to that integer
            //TODO: and as it does not exist, it will fail
            //TODO: holder.trailerText.setText(position + 1);

            //Do a concatenation with empty String instead
            holder.trailerText.setText(""+(position + 1));

        }
    }

    @Override
    public int getItemCount() {
        return resultDataList.size();
           }

    class PopMoviesTrailedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView trailerThumb;
        TextView trailerText;

        public PopMoviesTrailedViewHolder(View itemView) {
            super(itemView);
            //trailerThumb = (ImageView) itemView.findViewById(R.id.trailer_thumbnail);
            trailerText = (TextView) itemView.findViewById(R.id.tv_trailer);

        }

        @Override
        public void onClick(View v) {
            MovieTrailers moviesDataHolder = resultDataList.get(getAdapterPosition());
            clickHandler.onClick(moviesDataHolder);
        }
    }
}
