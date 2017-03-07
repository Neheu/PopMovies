package movies.proj.com.popularmovies.utility;

import movies.proj.com.popularmovies.data.PopularMovies;

/**
 * Created by Neha on 2/6/2017.
 */
public class ConstantsUtility {
    public static int SELECTED_MOVIE_ID = 0;

    //--------------- Network Constants---------------//

    public static String BASE_URL = "http://api.themoviedb.org/3/movie";
    public static final String POPULAR_URL = "popular";
    public static final String TOP_RATED_URL = "top_rated";
    public static final String API_KEY = "864a8cd60556e8c00b769d63261384c1";
    public static final String IMG_BASE_URL = "http://image.tmdb.org/t/p/";


    //------------------------ Picasso Image Constants---------//
    public static String[] IMG_SIZES_LIST = {"w92", "w154", "w185", "w342", "w500", "w780"};
    public static final String POSTER_IMAGE_BASE = IMG_BASE_URL + IMG_SIZES_LIST[4];
    public static final String BACKDROP_IMG_BASE_URL = "http://image.tmdb.org/t/p/original";


    //------------- Intents Constants ---------------------//
//    public static final String INTENT_TITLE = "title";
//    public static final String INTENT_RELEASE_DATE = "release_date";
//    public static final String INTENT_THUMB_URL = "thumb_url";
//    public static final String INTENT_RATING = "rating";
//    public static final String INTENT_OVERVIEW = "overview";
    public static final String INTENT_MOVIE_LIST = "movie_list";
    public static final String INTENT_MOVIE_DATA = "intent_movies";

    //-------------- Movie Json Parse Constants -----------//
    public static final String MOVIE_POSTER_PATH = "poster_path";
    public static final String MOVIE_OVERVIEW = "overview";
    public static final String MOVIE_RELEASE_DATE = "release_date";
    public static final String MOVIE_ID = "id";
    public static final String MOVIE_ORIGINAL_TITLE = "original_title";
    public static final String MOVIE_VOTE_AVERAGE = "vote_average";
    public static final String MOVIE_RESULT = "results";
    public static final String MOVIE_POPULARITY = "popularity";
    public static final String MOVIE_ADULT = "adult";
    public static final String MOVIE_GENRE_ID = "genre_ids";
    public static final String MOVIE_ORIGINAL_LANGUAGE = "original_language";
    public static final String MOVIE_TITLE = "title";
    public static final String MOVIE_BACKDROP_PATH = "backdrop_path";
    public static final String MOVIE_VOTE_COUNT = "vote_count";
    public static final String MOVIE_VIDEO = "video";

    public static final String TRAILER_ID = "id";
    public static final String TRAILER_KEY = "key";

    /*Define type of sorting,
    * 0- Popular
    * 1- High rated
    * 2- Myfavorites*/
    public static int[] MOVIE_SORT_TYPE = new int[]{0, 1, 2};
    /* Define end point to get particular type of movie list*/
    public static String[] MOVIE_SORT_ENDPOINT = new String[]{POPULAR_URL, TOP_RATED_URL};


}
