package de.erasys.paolo.mysecondapp_usingfragments;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements SendMessageFragment.OnMessageSentListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ChatHistoryFragment chatHistoryFragment = null;

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


            // Create a new Fragment to be placed in the activity layout
            SendMessageFragment sendMessageFragment = new SendMessageFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            sendMessageFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
              .add(R.id.fragment_container, sendMessageFragment).commit();
        }
Log.d(LOG_TAG, "Activity onCreate End");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onMessageSent(String message) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
        Log.d(LOG_TAG, "onMessageSent");

        if (findViewById(R.id.fragment_container) == null) {
            // we're in two-pane layout if fragment_container doesnt exist

            ChatHistoryFragment chatHistoryFragment = (ChatHistoryFragment)
              getSupportFragmentManager().findFragmentById(R.id.fragment_chat_history);

            // Call a method in the ChatHistoryFragment to update its content
            chatHistoryFragment.updateChatHistoryView(message);
        } else {
            // Otherwise, we're in the one-pane layout and must swap frags...

            if (this.chatHistoryFragment == null)  {

                ChatHistoryFragment previousChatHistoryFragment = (ChatHistoryFragment)
                        getSupportFragmentManager().findFragmentByTag("portraitChatFragment");

                this.chatHistoryFragment = (chatHistoryFragment != null) ?
                        previousChatHistoryFragment : new ChatHistoryFragment();
                Log.d(LOG_TAG, "new chatHistoryFragment");
            } else {
                Log.d(LOG_TAG, "found chatHistoryFragment");
            }

            Bundle args = new Bundle();
            args.putString(ChatHistoryFragment.SENT_MESSAGE, message);
            this.chatHistoryFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, this.chatHistoryFragment, "portraitChatFragment");
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
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
