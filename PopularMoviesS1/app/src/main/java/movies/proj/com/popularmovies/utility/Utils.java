package movies.proj.com.popularmovies.utility;

import android.content.Context;
import android.util.DisplayMetrics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Neha on 2/5/2017.
 */
public class Utils {
    //Set numbers according to device density and pixels
    public static int numberOfColumsForGridView(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float colWidth = displayMetrics.widthPixels / displayMetrics.density;
        int columns = (int) (colWidth / 180);
        return columns;
    }

    //Formate string type of date to Date object
    public static Date formatDateFromString(final String stringDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = dateFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //format string date from date object
    public static String formateStringFromDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

}
