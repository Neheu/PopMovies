package movies.proj.com.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Neha on 2/8/2017.
 */
public class PopMoviesTrailersAdapter extends RecyclerView.Adapter<PopMoviesTrailersAdapter.PopMoviesTrailedViewHolder> {

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

    @Override
    public void onBindViewHolder(PopMoviesTrailedViewHolder holder, int position) {
//    holder.trailerText.setText("Trailer "+String.valueOf(position+1));
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class PopMoviesTrailedViewHolder extends RecyclerView.ViewHolder
    {
        ImageView trailerThumb;

        public PopMoviesTrailedViewHolder(View itemView) {
            super(itemView);
            trailerThumb= (ImageView) itemView.findViewById(R.id.trailer_thumbnail);

        }
    }
}
