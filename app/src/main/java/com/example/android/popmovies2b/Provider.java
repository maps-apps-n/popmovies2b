package com.example.android.popmovies2b;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class Provider extends ContentProvider {

    private static final UriMatcher uri = buildUriMatcher();
    private DataHelper helper;

    private static final int MOVIE = 100;
    private static final int MOVIES_INCL_ID = 200;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher urimatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Contract.CONTENT_AUTHORITY;

        urimatcher.addURI(authority, Contract.Entry.TABLE_NAME, MOVIE);
        urimatcher.addURI(authority, Contract.Entry.TABLE_NAME + "/#", MOVIES_INCL_ID);
        return urimatcher;
    }

    @Override
    public boolean onCreate() {
        helper = new DataHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = Provider.uri.match(uri);
        switch (match) {
            case MOVIE: {
                return Contract.Entry.CONTENT_TYPE;
            }
            case MOVIES_INCL_ID: {
                return Contract.Entry.CONTENT_ITEM_TYPE;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (Provider.uri.match(uri)) {
            case MOVIE: {
                cursor = helper.getReadableDatabase().query(
                        Contract.Entry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                getContext().getContentResolver().notifyChange(uri, null);
                return cursor;
            }
            case MOVIES_INCL_ID: {
                cursor = helper.getReadableDatabase().query(
                        Contract.Entry.TABLE_NAME,
                        projection,
                        Contract.Entry.COLUMN_MOV_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                getContext().getContentResolver().notifyChange(uri, null);
                return cursor;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase database = helper.getWritableDatabase();
        Uri returnuri;
        switch (Provider.uri.match(uri)) {
            case MOVIE: {
                long id = database.insert(Contract.Entry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnuri = Contract.Entry.buildUri(id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnuri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase litedata = helper.getWritableDatabase();
        final int match = Provider.uri.match(uri);
        int delNo;
        switch (match) {
            case MOVIE:
                delNo = litedata.delete(Contract.Entry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIES_INCL_ID:
                delNo = litedata.delete(Contract.Entry.TABLE_NAME, Contract.Entry.COLUMN_MOV_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return delNo;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues content, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase litedata = helper.getWritableDatabase();
        int upNo = 0;
        if (content == null) {
            throw new IllegalArgumentException("The is no saved content");
        }

        switch (Provider.uri.match(uri)) {
            case MOVIE: {
                upNo = litedata.update(Contract.Entry.TABLE_NAME,
                        content,
                        selection,
                        selectionArgs);
                break;
            }
            case MOVIES_INCL_ID: {
                upNo = litedata.update(Contract.Entry.TABLE_NAME,
                        content,
                        Contract.Entry.COLUMN_MOV_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        if (upNo > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return upNo;
    }
}
