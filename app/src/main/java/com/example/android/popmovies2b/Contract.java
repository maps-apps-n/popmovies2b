package com.example.android.popmovies2b;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;



public class Contract {
    public static final String CONTENT_AUTHORITY = "com.example.android.popmovies2b";
    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public Contract() { }

    public static final class Entry implements BaseColumns {

        public static final int MOV_ID = 1;
        public static final int MOV_TITLE = 2;
        public static final int POS_PATH = 3;
        public static final int DESC = 4;
        public static final int VOTE_AVE = 5;
        public static final int RELEASED = 6;

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_MOV_ID = "movie_id";
        public static final String COLUMN_MOV_TITLE = "original_title";
        public static final String COLUMN_MOV_POS_PATH = "poster_path";
        public static final String COLUMN_MOV_DESC = "overview";
        public static final String COLUMN_MOV_VOTE_AVE = "vote_average";
        public static final String COLUMN_MOV_RELEASED = "release_date";

        public static final Uri CONTENT_URI =
                BASE_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
