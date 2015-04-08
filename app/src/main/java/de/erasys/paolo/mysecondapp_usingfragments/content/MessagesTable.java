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

}
