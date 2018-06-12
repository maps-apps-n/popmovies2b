package com.example.android.popmovies2b;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popmovies2b.Contract.Entry;


public class DataHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 9;
    private static final String DATABASE_NAME = "movies.db";

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        final String MOVIES_DATATABLE = "CREATE TABLE " + Entry.TABLE_NAME
                + " (" +
                Entry._ID + " INTEGER PRIMARY KEY," +
                Entry.COLUMN_MOV_ID + " INTEGER NOT NULL, " +
                Entry.COLUMN_MOV_TITLE + " TEXT NOT NULL, " +
                Entry.COLUMN_MOV_POS_PATH + " TEXT NOT NULL, " +
                Entry.COLUMN_MOV_DESC + " TEXT NOT NULL, " +
                Entry.COLUMN_MOV_VOTE_AVE + " TEXT NOT NULL, " +
                Entry.COLUMN_MOV_RELEASED + " TEXT NOT NULL" +
                " );";
        database.execSQL(MOVIES_DATATABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + Entry.TABLE_NAME);
        onCreate(database);
    }

    public static ContentValues getMovieContentValues(Movies movies) {
        ContentValues content = new ContentValues();
        content.put(Entry.COLUMN_MOV_ID, movies.getMovieId());
        content.put(Entry.COLUMN_MOV_TITLE, movies.getMovieTitle());
        content.put(Entry.COLUMN_MOV_POS_PATH, movies.getMoviePoster());
        content.put(Entry.COLUMN_MOV_DESC, movies.getMovieOverview());
        content.put(Entry.COLUMN_MOV_VOTE_AVE, movies.getDetailedVoteAverage());
        content.put(Entry.COLUMN_MOV_RELEASED, movies.getMovieRelease());
        return content;
    }
}