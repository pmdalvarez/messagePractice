package de.erasys.paolo.mysecondapp_usingfragments.content;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by paolo on 27.03.15.
 */
public class MessagesTable {

    // Database table
    public static final String TABLE = "messages";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_SUBJECT = "subject";
    public static final String COLUMN_MESSAGE = "message";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE
            + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + COLUMN_SUBJECT + " TEXT NOT NULL, "
            + COLUMN_MESSAGE + " TEXT NOT NULL"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(MessagesTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(database);
    }

}
