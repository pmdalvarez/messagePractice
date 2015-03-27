package de.erasys.paolo.mysecondapp_usingfragments;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.erasys.paolo.mysecondapp_usingfragments.content.MessagesContentProvider;
import de.erasys.paolo.mysecondapp_usingfragments.content.MessagesTable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SendMessageFragment.OnMessageSentListener} interface
 * to handle interaction events.
 * Use the {@link SendMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendMessageFragment extends Fragment {

    private static final String LOG_TAG = SendMessageFragment.class.getSimpleName() ;

    public final static String MSG_ID = "de.erasys.paolo.mysecondapp_usingfragments.MSG_ID";

    private OnMessageSentListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SendMessageFragment.
     */
    public static SendMessageFragment newInstance() {
        SendMessageFragment fragment = new SendMessageFragment();
        return fragment;
    }

    public SendMessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_message, container, false);
        Button button = (Button) view.findViewById(R.id.button_send);

        Bundle args = getArguments();
        final long msgId = args.getLong(MSG_ID);

        // if msd id defined then let's initialise text values
        initializeTextFields(view, msgId);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText subjectTextField = (EditText)getView().findViewById(R.id.edit_subject);
                EditText messageTextField = (EditText)getView().findViewById(R.id.edit_message);
                String subject = subjectTextField.getText().toString();
                String message = messageTextField.getText().toString();

                if (subject != null && !subject.isEmpty() && message != null && !message.isEmpty()) {
                    mListener.onMessageSent(subject, message, msgId);
                    // clear input
                    subjectTextField.setText("");
                    messageTextField.setText("");
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.error_no_message, Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    private void initializeTextFields(View v, long msgId){
Log.d(LOG_TAG, "initializeTextFields with msdId " + msgId);
        if (msgId != ChatHistoryFragment.OnEditMessageListener.NO_ID) {

            Uri uri = Uri.parse(MessagesContentProvider.CONTENT_URI + "/"
                    + msgId);
            String[] columns = new String[]{MessagesTable.COLUMN_SUBJECT, MessagesTable.COLUMN_MESSAGE};
            Cursor cursor = getActivity().getContentResolver().query(uri, columns, null, null, null);
Log.d(LOG_TAG, "initializeTextFields ran query");
            if (cursor != null && cursor.getCount() > 0) {
Log.d(LOG_TAG, "initializeTextFields found cursor! ");
                cursor.moveToFirst();
                EditText subjectTextField = (EditText)v.findViewById(R.id.edit_subject);
                EditText messageTextField = (EditText)v.findViewById(R.id.edit_message);
                subjectTextField.setText(cursor.getString(0));
                messageTextField.setText(cursor.getString(1));
            }

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnMessageSentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onMessageSentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnMessageSentListener {
        public void onMessageSent(String subject, String message, long msgId);
    }

}
