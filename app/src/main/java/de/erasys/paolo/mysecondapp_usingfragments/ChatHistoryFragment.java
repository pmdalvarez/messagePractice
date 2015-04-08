package de.erasys.paolo.mysecondapp_usingfragments;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
    MessagesAdapter mAdapter = null;

    public static interface OnEditMessageListener {
        public static long NO_ID = 0;
        public void onEditMessage(long msgId);
    }

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
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
Log.d(LOG_TAG, "onActivityCreated");
        final ListView listView = (ListView) getView().findViewById(R.id.chat_history);
        fillData();
        registerForContextMenu(listView); // can register for context menu only after filling data
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mListener.onEditMessage(id);
            }
        });

        // if in portrait mode then add send new message fragment
        if (getActivity().findViewById(R.id.fragment_container) != null) {
Log.d(LOG_TAG, "onActivityCreated found fragment_container (ie. in portrait mode)");
            loadNewMsgFragment();
        }
    }

    private void loadNewMsgFragment() {
        Log.d(LOG_TAG, "loading new msg fragment! ");
        SendMessageFragment sendMessageFragment = new SendMessageFragment();
        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        Bundle args = new Bundle();
        args.putLong(SendMessageFragment.MSG_ID, ChatHistoryFragment.OnEditMessageListener.NO_ID);
        sendMessageFragment.setArguments(args);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container_new_msg, sendMessageFragment).commit();
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
                Activity activity = getActivity();
                if (activity == null)   break;
                activity.getContentResolver().delete(uri, null, null);
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
        getLoaderManager().initLoader(0, null, this);
        mAdapter = new MessagesAdapter(getActivity(), null, MessagesAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        final ListView listView = (ListView) getView().findViewById(R.id.chat_history);
        if (listView != null) listView.setAdapter(mAdapter);
    }

}
