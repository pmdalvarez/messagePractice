package de.erasys.paolo.mysecondapp_usingfragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by paolo on 13.03.15.
 */
public class MessagesAdapter extends BaseAdapter {

    private static final String LOG_TAG = MessagesAdapter.class.getSimpleName();

    private final Context context;
    private final ArrayList<String> values;

    public MessagesAdapter(Context context, ArrayList values) {
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int i) {
        return values.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        // used by onclick listener
        return i;
    }

    public void add(String message) {
        Log.d(LOG_TAG, "adding message " + message);
        values.add(message);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_history_item, parent, false);
        }

        TextView label = (TextView) convertView.findViewById(R.id.chat_history_item_subject);
        TextView desc = (TextView) convertView.findViewById(R.id.chat_history_item_message);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);

        label.setText("Message Number #" + (int) (position + 1) );
 Log.d(LOG_TAG, "values:" + values.toString() + " value:" + values.get(position));

        desc.setText((String)values.get(position));
        if (position % 2 == 0) {
            imageView.setImageResource(R.drawable.twitter);
        } else {
            imageView.setImageResource(R.drawable.utorrent);
        }

        return convertView;
    }

}

