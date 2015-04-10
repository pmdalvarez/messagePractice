package de.erasys.paolo.mysecondapp_usingfragments;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.erasys.paolo.mysecondapp_usingfragments.content.MessagesTable;

/**
 * Created by paolo on 13.03.15.
 */
public class MessagesAdapter extends CursorAdapter {

    private static class ViewHolder {
        TextView dateView;
        TextView subjectView;
        TextView msgView;
    }

    public MessagesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.chat_history_item, parent, false);

        ViewHolder holder   = new ViewHolder();
        holder.dateView     = (TextView)v.findViewById(R.id.chat_history_item_date);
        holder.subjectView  = (TextView)v.findViewById(R.id.chat_history_item_subject);
        holder.msgView      = (TextView)v.findViewById(R.id.chat_history_item_message);
        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View v, Context context, Cursor c) {
        String date = c.getString(c.getColumnIndexOrThrow(MessagesTable.COLUMN_DATE));
        String subject = c.getString(c.getColumnIndexOrThrow(MessagesTable.COLUMN_SUBJECT));
        String message = c.getString(c.getColumnIndexOrThrow(MessagesTable.COLUMN_MESSAGE));

        ViewHolder holder = (ViewHolder) v.getTag();
        try {
            Date dateObj  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
            String dateFormatted = new SimpleDateFormat("d MMM yy HH:mm:ss").format(dateObj);
            holder.dateView.setText(dateFormatted);
        } catch (ParseException e) {
            // don't format the date
            Log.d(this.getClass().getName(), "setViewValue PARSE ERROR date string  = " + date);
        }

        holder.subjectView.setText(subject);
        holder.msgView.setText(message);

    }
}