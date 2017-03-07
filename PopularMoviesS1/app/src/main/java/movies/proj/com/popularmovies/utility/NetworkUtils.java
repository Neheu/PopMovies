package movies.proj.com.popularmovies.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.inputmethod.InputConnection;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Neha on 2/5/2017.
 */
public class NetworkUtils {
    public static final String TAG = NetworkUtils.class.getName();
    /* The format we want our API to return */
    private static final String format = "json";
    private static final String apiParam = "api_key";
    private static final String popular = "popular";
    private static final String topRated = "top_rated";
    private static final String video = "videos";


    /*
    Build URL that is required to get the data from movie server,
    by using api key
    @return path (url) of movie server.
     */
    public static URL buildUrl(int type) {
        String appendPath = ConstantsUtility.MOVIE_SORT_ENDPOINT[type];
        Uri uri = Uri.parse(ConstantsUtility.BASE_URL).buildUpon()
                .appendEncodedPath(appendPath)
                .appendQueryParameter(apiParam, ConstantsUtility.API_KEY)
                .build();
        URL movieUrl = null;
        try {
            movieUrl = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return movieUrl;
    }

    public static URL buildUrlTrailes(int id) {
        String appendPath = video;

        Uri uri = Uri.parse(ConstantsUtility.BASE_URL).buildUpon()
                .appendPath(String.valueOf(id))
                .appendEncodedPath(appendPath)
                .appendQueryParameter(apiParam, ConstantsUtility.API_KEY)
                .build();
        URL movieUrl = null;
        try {
            movieUrl = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return movieUrl;
    }

    /*
    The method will talk with movie server using URL,
    to get movies related data
    @param URL, the movie url
    @return content of HTTP Response
     */
    public static String getResponseFromHttUrl(URL url) throws IOException {
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inStream = httpConnection.getInputStream();
            Scanner scanner = new Scanner(inStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput)
                return scanner.next();
            else return null;
        } finally {
            httpConnection.disconnect();
        }
    }

    /* This method will provide the state about if any internet network is connected*/
    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager
                cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

}
