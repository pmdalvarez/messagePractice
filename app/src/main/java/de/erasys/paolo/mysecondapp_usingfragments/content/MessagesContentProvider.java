package de.erasys.paolo.mysecondapp_usingfragments.content;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by paolo on 27.03.15.
 */
public class MessagesContentProvider extends ContentProvider {

    private static class MessagesDBHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "messagestable.db";
        private static final int DATABASE_VERSION = 1;

        // Database creation SQL statement
        private static final String DATABASE_CREATE = "create table "
                + MessagesTable.TABLE
                + "("
                + MessagesTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MessagesTable.COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + MessagesTable.COLUMN_SUBJECT + " TEXT NOT NULL, "
                + MessagesTable.COLUMN_MESSAGE + " TEXT NOT NULL"
                + ");";

        public MessagesDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Method is called during creation of the database
        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(DATABASE_CREATE);
        }

        // Method is called during an upgrade of the database,
        // e.g. if you increase the database version
        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion,
                              int newVersion) {
            Log.w(MessagesTable.class.getName(), "Upgrading database from version "
                    + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");

            // new queries put here
        }
    }

    private MessagesDBHelper dbHelper;

    // for URI Matcher
    private static final int MESSAGES = 10;
    private static final int MESSAGES_ID = 20;

    private static final String AUTHORITY = "de.erasys.paolo.mysecondapp_usingfragments.content";

    private static final String BASE_PATH = "messages";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/messages";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/message";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, MESSAGES);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", MESSAGES_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MessagesDBHelper(getContext());
        return false;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(MessagesTable.TABLE);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case MESSAGES:
                break;
            case MESSAGES_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(MessagesTable.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        long id;
        switch (uriType) {
            case MESSAGES:
                id = sqlDB.insert(MessagesTable.TABLE, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsDeleted;
        switch (uriType) {
            case MESSAGES:
                rowsDeleted = sqlDB.delete(MessagesTable.TABLE, selection,
                        selectionArgs);
                break;
            case MESSAGES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(MessagesTable.TABLE,
                            MessagesTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(MessagesTable.TABLE,
                            MessagesTable.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        int rowsUpdated;
        switch (uriType) {
            case MESSAGES:
                rowsUpdated = sqlDB.update(MessagesTable.TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case MESSAGES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(MessagesTable.TABLE,
                            values,
                            MessagesTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(MessagesTable.TABLE,
                            values,
                            MessagesTable.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = { MessagesTable.COLUMN_DATE,
                MessagesTable.COLUMN_SUBJECT, MessagesTable.COLUMN_MESSAGE,
                MessagesTable.COLUMN_ID };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

}
