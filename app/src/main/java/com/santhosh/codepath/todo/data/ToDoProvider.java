package com.santhosh.codepath.todo.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class ToDoProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private ToDoDbHelper mToDoDbHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ToDoContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, ToDoContract.ListEntry.TABLE_NAME, ToDoContract.ListEntry.CONTENT_GENERIC);
        matcher.addURI(authority, ToDoContract.ListEntry.TABLE_NAME + "/#", ToDoContract.ListEntry.CONTENT_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mToDoDbHelper = new ToDoDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mToDoDbHelper.getReadableDatabase();
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case ToDoContract.ListEntry.CONTENT_GENERIC:
                cursor = db.query(ToDoContract.ListEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ToDoContract.ListEntry.CONTENT_ID:
                selection = ToDoContract.ListEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(ToDoContract.ListEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case ToDoContract.ListEntry.CONTENT_GENERIC:
                return ToDoContract.ListEntry.CONTENT_TYPE_DIR;
            case ToDoContract.ListEntry.CONTENT_ID:
                return ToDoContract.ListEntry.CONTENT_TYPE_ITEM;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mToDoDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case ToDoContract.ListEntry.CONTENT_GENERIC:
                long rowId = db.insert(ToDoContract.ListEntry.TABLE_NAME, "", values);
                if (rowId > 0) {
                    returnUri = ContentUris.withAppendedId(uri, rowId);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mToDoDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int rowsDeleted;

        switch (match) {
            case ToDoContract.ListEntry.CONTENT_GENERIC:
                rowsDeleted = db.delete(ToDoContract.ListEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ToDoContract.ListEntry.CONTENT_ID:
                selection = ToDoContract.ListEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(ToDoContract.ListEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mToDoDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int rowsUpdated;

        switch (match) {
            case ToDoContract.ListEntry.CONTENT_GENERIC:
                rowsUpdated = db.update(ToDoContract.ListEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case ToDoContract.ListEntry.CONTENT_ID:
                selection = ToDoContract.ListEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = db.update(ToDoContract.ListEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
