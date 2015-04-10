package de.erasys.paolo.mysecondapp_usingfragments;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import de.erasys.paolo.mysecondapp_usingfragments.content.MessagesContentProvider;
import de.erasys.paolo.mysecondapp_usingfragments.content.MessagesTable;

public class MainActivity extends ActionBarActivity
        implements  SendMessageFragment.OnMessageSentListener,
                    ChatHistoryFragment.OnEditMessageListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ChatHistoryFragment chatHistoryFragment = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // if in portrait mode need to go back to chat history fragment
        if (findViewById(R.id.fragment_container) != null) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_activity_actions, menu);
            return super.onCreateOptionsMenu(menu);
        } else {
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onMessageSent(String subject, String message, long msgId) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
    Log.d(getLocalClassName(), "onMessageSent and id is " + msgId);

        ContentValues values = new ContentValues();
        values.put(MessagesTable.COLUMN_SUBJECT, subject);
        values.put(MessagesTable.COLUMN_MESSAGE, message);
        if (msgId != NO_ID) {
            getContentResolver().update(MessagesContentProvider.CONTENT_URI, values, MessagesTable.COLUMN_ID + "=" + msgId, null);
        } else {
            getContentResolver().insert(MessagesContentProvider.CONTENT_URI, values);
        }

        // if in portrait mode need to go back to chat history fragment
        if (findViewById(R.id.fragment_container) != null) {
            loadChatHistoryFragment();
        }
//        if (findViewById(R.id.fragment_container) == null) {
//            // we're in two-pane layout if fragment_container doesnt exist
//
//            ChatHistoryFragment chatHistoryFragment = (ChatHistoryFragment)
//              getSupportFragmentManager().findFragmentById(R.id.fragment_chat_history);
//
//            // Call a method in the ChatHistoryFragment to update its content
//            chatHistoryFragment.updateChatHistoryView(subject, message);
//        } else {
//            // Otherwise, we're in the one-pane layout and must swap frags...
//            loadChatHistoryFragment(subject, message);
//        }
    }

    private void loadChatHistoryFragment() {
        if (this.chatHistoryFragment == null)  {

            ChatHistoryFragment previousChatHistoryFragment = (ChatHistoryFragment)
                    getSupportFragmentManager().findFragmentByTag("portraitChatFragment");

            this.chatHistoryFragment = (chatHistoryFragment != null) ?
                    previousChatHistoryFragment : new ChatHistoryFragment();
            Log.d(LOG_TAG, "new chatHistoryFragment");
        } else {
            Log.d(LOG_TAG, "found chatHistoryFragment");
        }

//        if (message != null && subject != null) {
//            Bundle args = new Bundle();
//            args.putString(ChatHistoryFragment.SENT_SUBJECT, subject);
//            args.putString(ChatHistoryFragment.SENT_MESSAGE, message);
//            this.chatHistoryFragment.setArguments(args);
//        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, this.chatHistoryFragment, "portraitChatFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void onEditMessage(long msgId) {
        loadSendMessageFragment(msgId);
    }

    private void loadSendMessageFragment(long msgId) {
        // Create a new Fragment to be placed in the activity layout
        SendMessageFragment sendMessageFragment = new SendMessageFragment();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments


        Bundle args = new Bundle();
        args.putLong(SendMessageFragment.MSG_ID, msgId);
        sendMessageFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, sendMessageFragment, "portraitSendMsgFragment");
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "Activity onCreate");

        // if we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {
            return;
        }

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {
            Log.d(LOG_TAG, "Activity onCreate found fragment_container (ie. in portrait mode)");
            loadChatHistoryFragment();
        }
        Log.d(LOG_TAG, "Activity onCreate End");
    }


    @Override
    protected void onRestart() {
        Log.d(LOG_TAG, "Activity onRestart");
        super.onRestart();
    }

    @Override
    public void onStart() {
        Log.d(LOG_TAG, "Activity onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "Activity onResume");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Activity onDestroy");
    }


}
