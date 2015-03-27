package de.erasys.paolo.mysecondapp_usingfragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.erasys.paolo.mysecondapp_usingfragments.content.MessagesContentProvider;
import de.erasys.paolo.mysecondapp_usingfragments.content.MessagesTable;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatHistoryFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = ChatHistoryFragment.class.getSimpleName() ;

    private static final int DELETE_ID = Menu.FIRST + 1;

    private OnEditMessageListener mListener;

    // this is the Adapter being used to display the chat history data
    SimpleCursorAdapter mAdapter = null;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChatHistoryFragment.
     */
    public static ChatHistoryFragment newInstance() {
        ChatHistoryFragment fragment = new ChatHistoryFragment();
        return fragment;
    }

    public ChatHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chat_history, container, false);
Log.d(LOG_TAG, "onCreateView");

        Button button = (Button) view.findViewById(R.id.button_new_message);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEditMessage(null);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
Log.d(LOG_TAG, "onActivityCreated");
        final ListView listView = (ListView) getView().findViewById(R.id.chat_history);
        fillData();
        registerForContextMenu(listView); // can register for context menu only after filling data
//
//        // listener for delete message
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            public boolean onItemLongClick(AdapterView lv, View v, int position, long id) {
//                Intent i = new Intent(getActivity(), MainActivity.class);
//                Uri todoUri = Uri.parse(MessagesContentProvider.CONTENT_URI + "/" + id);
//                i.putExtra(MessagesContentProvider.CONTENT_ITEM_TYPE, todoUri);
//
//                startActivity(i);
//                return true;
//            }
//        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                        .getMenuInfo();
                Uri uri = Uri.parse(MessagesContentProvider.CONTENT_URI + "/"
                        + info.id);
                getActivity().getContentResolver().delete(uri, null, null);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnEditMessageListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onEditMessageListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
//        Log.d(LOG_TAG, "onSaveInstanceState");
//        ArrayList<String> messages = new ArrayList<>();
//        for (int i = 0; i < messagesAdapter.getCount(); i++)
//          messages.add((String)messagesAdapter.getItem(i));
//        savedInstanceState.putStringArrayList(HISTORY, messages);
    }


    public interface OnEditMessageListener {
        public void onEditMessage(String msgId);
    }


    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                MessagesTable.COLUMN_ID,
                MessagesTable.COLUMN_DATE,
                MessagesTable.COLUMN_SUBJECT,
                MessagesTable.COLUMN_MESSAGE };
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                MessagesContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private void fillData() {
Log.d(LOG_TAG, "fillData");
        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[] {
            MessagesTable.COLUMN_DATE,
            MessagesTable.COLUMN_SUBJECT,
            MessagesTable.COLUMN_MESSAGE
        };
        // Fields on the UI to which we map
        int[] to = new int[] {
            R.id.chat_history_item_date,
            R.id.chat_history_item_subject,
            R.id.chat_history_item_message
        };

        getLoaderManager().initLoader(0, null, this);
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.chat_history_item, null, from, to, 0);

        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int column) {
                int dateCol = cursor.getColumnIndex( MessagesTable.COLUMN_DATE);
                if (column == dateCol) {
                  String msgDate = cursor.getString(dateCol);
                  try {
                      TextView tv = (TextView) view;
                      Date dateObj  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(msgDate);
                      String dateStr = new SimpleDateFormat("d MMM yy HH:mm:ss").format(dateObj);
                      tv.setText(dateStr);
                      return true;
                  } catch (ParseException e) {
                      // don't format the date
                      Log.d(LOG_TAG, "setViewValue PARSE ERROR column value " + column + " date string is " + cursor.getString(column));
                  }

                }
                return false;
            }
        });

        final ListView listView = (ListView) getView().findViewById(R.id.chat_history);
        if (listView != null) listView.setAdapter(mAdapter);
    }

}
