package movies.proj.com.popularmoviess1.utility;

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


    /*
    Build URL that is required to get the data from movie server,
    by using api key
    @return path (url) of movie server.
     */
    public static URL buildUrl(boolean typePopular) {
        String appendPath;
        if (typePopular)

            appendPath = ConstantsUtility.POPULAR_URL;
        else
            appendPath = ConstantsUtility.TOP_RATED_URL;

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

}
