package de.erasys.paolo.mysecondapp_usingfragments;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by paolo on 20.03.15.
 */
public class MyContentProvider extends ContentProvider {

    private MyDatabaseHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new MyDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Using SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check of columns skipped

        // Set the table
        queryBuilder.setTables(MyDatabaseHelper.TABLE_MESSAGES);

        // URI Matcher also skipped, at moment URI is ignored and messages table is always set

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
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
    public Uri insert(Uri uri, ContentValues contentValues) {
        db = mDbHelper.getWritableDatabase();
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    protected static final class MyDatabaseHelper extends SQLiteOpenHelper {

        public static final String TABLE_MESSAGES = "messages";
        public static final String MESSAGES_ID = "_ID";
        public static final String COLUMN_MESSAGE = "message";

        private static final String DB_NAME = "messages.db";
        private static final int DB_VERSION = 1;

        private static final String SQL_DB_CREATE = "CREATE TABLE " +
                TABLE_MESSAGES + "(" + MESSAGES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MESSAGE + " TEXT NOT NULL)";

        MyDatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION); // null = use default SQLite cursor, 1 = version 1
        }

        public void onCreate(SQLiteDatabase db) {
            // create main table
            db.execSQL(SQL_DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(MyDatabaseHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
            onCreate(db);
        }

    }

}
