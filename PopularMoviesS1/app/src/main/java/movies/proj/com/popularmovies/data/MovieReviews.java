package movies.proj.com.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Neha on 09-03-2017.
 */

public class MovieReviews implements Parcelable {
    public String reviewId;
    public int movieId;
    public String author;
    public String content;
    public String url;

    public MovieReviews(String reviewId, int movieId, String author, String content, String url) {
        this.reviewId = reviewId;
        this.movieId = movieId;
        this.author = author;
        this.content = content;
        this.url = url;

    }

    protected MovieReviews(Parcel in) {
        reviewId = in.readString();
        movieId = in.readInt();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    public static final Creator<MovieReviews> CREATOR = new Creator<MovieReviews>() {
        @Override
        public MovieReviews createFromParcel(Parcel in) {
            return new MovieReviews(in);
        }

        @Override
        public MovieReviews[] newArray(int size) {
            return new MovieReviews[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reviewId);
        dest.writeInt(movieId);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }
}
