package de.erasys.paolo.mysecondapp_usingfragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatHistoryFragment extends Fragment {

    private static final String LOG_TAG = ChatHistoryFragment.class.getSimpleName() ;

    public final static String SENT_MESSAGE = "de.erasys.paolo.mysecondapp_usingfragments.MESSAGE";
    public final static String HISTORY = "de.erasys.paolo.mysecondapp_usingfragments.HISTORY";

    // this is the Adapter being used to display the chat history data
    MessagesAdapter messagesAdapter = null;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param message The sent message
     * @return A new instance of fragment ChatHistoryFragment.
     */
    public static ChatHistoryFragment newInstance(String message) {
        ChatHistoryFragment fragment = new ChatHistoryFragment();
        Bundle args = new Bundle();
        args.putString(SENT_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    public ChatHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
Log.d(LOG_TAG, "onCreateView");
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chat_history, container, false);
        final ListView listView = (ListView) view.findViewById(R.id.chat_history);

        if (messagesAdapter == null) {
          ArrayList<String> history = null;
         if (savedInstanceState != null) history = savedInstanceState.getStringArrayList(HISTORY);
          if (history == null) history = new ArrayList<>();
          messagesAdapter = new MessagesAdapter(getActivity(), history);
        }

        if (getArguments() != null) {
          String message = getArguments().getString(SENT_MESSAGE);
          if (message != null) messagesAdapter.add(message);
        }

        listView.setAdapter(messagesAdapter);
Log.d(LOG_TAG, "onCreateView End ");
        return view;
    }

    @Override
    public void onStart() {
        Log.d(LOG_TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(LOG_TAG, "onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {

        super.onDetach();

    }

    public void updateChatHistoryView(String message) {
      messagesAdapter.add(message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(LOG_TAG, "onSaveInstanceState");
        ArrayList<String> messages = new ArrayList<>();
        for (int i = 0; i < messagesAdapter.getCount(); i++)
          messages.add((String)messagesAdapter.getItem(i));
        savedInstanceState.putStringArrayList(HISTORY, messages);
    }

}
