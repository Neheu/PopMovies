package movies.proj.com.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Neha on 2/6/2017.
 */
public class PopularMovies implements Parcelable {
    public String posterPath;
    public boolean isAdult;
    public String overview;
    public String releaseDate;
    public JSONArray genreIds[];
    public int id;
    public String orignalTitle;
    public String orignalLanguage;
    public String title;
    public String backdropPath;
    public double popularity;
    public int voteCount;
    public boolean hasVideo;
    public double voteAverage;
    public int sortType;

    private ArrayList<PopularMovies> popularMoviesArrayList;

    public PopularMovies(Parcel in) {
        posterPath = in.readString();
        isAdult = in.readByte() != 0;
        overview = in.readString();
        releaseDate = in.readString();
        id = in.readInt();
        orignalTitle = in.readString();
        orignalLanguage = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        popularity = in.readDouble();
        voteCount = in.readInt();
        hasVideo = in.readByte() != 0;
        voteAverage = in.readDouble();
        sortType = in.readInt();
    }

    public static final Creator<PopularMovies> CREATOR = new Creator<PopularMovies>() {
        @Override
        public PopularMovies createFromParcel(Parcel in) {
            return new PopularMovies(in);
        }

        @Override
        public PopularMovies[] newArray(int size) {
            return new PopularMovies[size];
        }
    };

    public PopularMovies(String path, boolean adult, String overview, String releaseDate,
                         int id, String orignalTitle, String orignalLanguage, String title,
                         String backdropPath, double popularity, int voteCount, boolean isVideo, double voteAverage, int sortType) {

        this.posterPath = path;
        isAdult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.orignalTitle = orignalTitle;
        this.orignalLanguage = orignalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.hasVideo = isVideo;
        this.voteAverage = voteAverage;
        this.sortType = sortType;

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeByte((byte) (isAdult ? 1 : 0));
        dest.writeString(overview);
        dest.writeString(releaseDate);
//        dest.writeIntArray(genreIds);
        dest.writeInt(id);
        dest.writeString(orignalTitle);
        dest.writeString(orignalLanguage);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeDouble(popularity);
        dest.writeInt(voteCount);
        dest.writeByte((byte) (hasVideo ? 1 : 0));
        dest.writeDouble(voteAverage);
        dest.writeInt(sortType);
    }
}
