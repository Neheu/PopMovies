package movies.proj.com.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Neha on 04-03-2017.
 */

public class MovieTrailers implements Parcelable {
    public String key;
    public String id;


    private ArrayList<MovieTrailers> popularMoviesArrayList;

    public MovieTrailers(Parcel in) {


        id = in.readString();
        key = in.readString();
    }

    public static final Creator<MovieTrailers> CREATOR = new Creator<MovieTrailers>() {
        @Override
        public MovieTrailers createFromParcel(Parcel in) {
            return new MovieTrailers(in);
        }

        @Override
        public MovieTrailers[] newArray(int size) {
            return new MovieTrailers[size];
        }
    };

    public MovieTrailers(String id, String key) {


        this.id = id;
        this.key = key;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key);

    }
}
