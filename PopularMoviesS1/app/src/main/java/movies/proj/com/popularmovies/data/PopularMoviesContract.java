package movies.proj.com.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ${Neha} on 2/23/2017.
 */

public class PopularMoviesContract {
    /* Add content provider constants to the Contract
     Clients need to know how to access the task data*/

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "movies.proj.com.popularmovies";
    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "movies" directory
    public static final String PATH_MOVIES = "movies";

    public static final class PopularMoviesEntry implements BaseColumns {
        // PopularMoviesEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        //Movies table columns --
        public static final String _ID = "_id";
        public static final String POSTER_PATH = "poster_path";
        public static final String IS_ADULT = "is_adult";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "release_date";
        public static final String MOVIE_ID = "movie_id";
        public static final String ORIGINAL_TITLE = "original_title";
        public static final String ORIGINAL_LANGUAGE = "original_language";
        public static final String TITLE = "title";
        public static final String BACKDROP_PATH = "backdrop_path";
        public static final String POPULARITY = "popularity";
        public static final String VOTE_COUNT = "vote_count";
        public static final String HAS_VIDEO = "has_video";
        public static final String VOTE_AVERAGE = "vote_average";
        //        public static final String IS_MARKED_FAVORITE = "is_favorite";
        public static final String SORT_TYPE = "sort_type";
        public static final String TRAILER_ID = "trailer_id";
        public static final String TRAILER_KEY = "trailer_key";

        public static final String REVIEW_ID = "id";
        public static final String REVIEW_AUTHOR = "author";
        public static final String REVIEW_CONTENT = "content";
        public static final String REVIEW_URL = "url";
    }
}
