package movies.proj.com.popularmoviess1.utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import movies.proj.com.popularmoviess1.core.PopularMovies;

/**
 * Created by Neha on 2/6/2017.
 */
public class PopularMovieJsonUtil {
    /*
    This method will help to parse data ,got from popular movies url and store that into PopualMovies object for further use.
    @param moviesJsonString, use to get the json string, recieved from server.
     */
    public static ArrayList<PopularMovies> getParseDataFromJSon(final String moviesJsonString) {
        String posterPath = "poster_path";
        String overview = "overview";
        String releaseDate = "release_date";
        String id = "id";
        String title = "original_title";
        String voteAvg = "vote_average";
        String result = "results";
        String popularity ="popularity";
        PopularMovies popularMoviesHolder = null;
        ArrayList<PopularMovies> listOfMovies = new ArrayList<>();

        try {
            JSONObject moviesJsonObject = new JSONObject(moviesJsonString);
            JSONArray moviesJsonArray = moviesJsonObject.getJSONArray(result);
            for (int i = 0; i < moviesJsonArray.length(); i++) {
                JSONObject jsonObject = moviesJsonArray.getJSONObject(i);
                popularMoviesHolder = new PopularMovies();
                popularMoviesHolder.setPosterPath(jsonObject.getString(posterPath));
                popularMoviesHolder.setOverview(jsonObject.getString(overview));
                //Date dateOfRelease = Utils.formatDateFromString(jsonObject.getString(releaseDate));
                popularMoviesHolder.setReleaseDate(jsonObject.getString(releaseDate));
                popularMoviesHolder.setId(jsonObject.getInt(id));
                popularMoviesHolder.setTitle(jsonObject.getString(title));
                popularMoviesHolder.setVoteAverage(jsonObject.getDouble(voteAvg));
                popularMoviesHolder.setPopularity(jsonObject.getDouble(popularity));
                listOfMovies.add(popularMoviesHolder);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listOfMovies;
    }
}
