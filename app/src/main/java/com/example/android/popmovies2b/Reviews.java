package com.example.android.popmovies2b;

import android.os.Parcel;
import android.os.Parcelable;

public class Reviews implements Parcelable {

    public Reviews() { }

    private String id;
    private String author;
    private String content;

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getcontent() {
        return content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
    }

    protected Reviews(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
    }

    public static final Creator<Reviews> CREATOR = new Creator<Reviews>() {
        @Override
        public Reviews createFromParcel(Parcel in) {
            return new Reviews(in);
        }
        @Override
        public Reviews[] newArray(int size) {
            return new Reviews[size];
        }
    };
}
