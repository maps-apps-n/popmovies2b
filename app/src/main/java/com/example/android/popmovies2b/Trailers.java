package com.example.android.popmovies2b;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailers implements Parcelable {

    private String movieId;
    private String trailerId;
    private String key;

    public Trailers() { }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;


    public String getTrailerId() {
        return trailerId;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(trailerId);
        dest.writeString(key);
        dest.writeString(name);
    }

    protected Trailers(Parcel in) {
        movieId = in.readString();
        trailerId = in.readString();
        key = in.readString();
        name = in.readString();
    }

    public static final Creator<Trailers> CREATOR = new Creator<Trailers>() {
        @Override
        public Trailers createFromParcel(Parcel in) {
            return new Trailers(in);
        }

        @Override
        public Trailers[] newArray(int size) {
            return new Trailers[size];
        }
    };
}
