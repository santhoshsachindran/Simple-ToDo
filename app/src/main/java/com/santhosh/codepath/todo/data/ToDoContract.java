package com.santhosh.codepath.todo.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ToDoContract {
    public static final String CONTENT_AUTHORITY = "com.santhosh.codepath.todo";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public ToDoContract() {
    }

    public static final class ListEntry implements BaseColumns {
        public static final String TABLE_NAME = "list";

        public static final String COLUMN_LIST_TITLE = "list_title";
        public static final String COLUMN_LIST_DATE = "list_date";
        public static final String COLUMN_LIST_NOTE = "list_note";
        public static final String COLUMN_LIST_PRIORITY = "list_priority";
        public static final String COLUMN_LIST_STATUS = "list_status";

        public static final int CONTENT_GENERIC = 1;
        public static final int CONTENT_ID = 2;
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String CONTENT_TYPE_DIR =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_TYPE_ITEM =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private static final String INTEGER = " INTEGER";
        private static final String TEXT = " TEXT";
        private static final String PRIMARY_KEY = " PRIMARY KEY";
        private static final String COMMA = ",";
        private static final String NOT_NULL = " NOT NULL";
        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " ("
                        + _ID + INTEGER + PRIMARY_KEY + " AUTOINCREMENT" + NOT_NULL + COMMA
                        + COLUMN_LIST_TITLE + TEXT + NOT_NULL + COMMA
                        + COLUMN_LIST_DATE + TEXT + NOT_NULL + COMMA
                        + COLUMN_LIST_NOTE + TEXT + COMMA
                        + COLUMN_LIST_PRIORITY + TEXT + COMMA
                        + COLUMN_LIST_STATUS + TEXT
                        + " )";
    }
}
