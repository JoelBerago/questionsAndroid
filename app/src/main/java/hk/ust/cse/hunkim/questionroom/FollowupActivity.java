package hk.ust.cse.hunkim.questionroom;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import hk.ust.cse.hunkim.questionroom.db.ImageHelper;
import hk.ust.cse.hunkim.questionroom.question.Answer;
import hk.ust.cse.hunkim.questionroom.question.FollowUp;
import hk.ust.cse.hunkim.questionroom.question.Question;

/**
 * Created by Joel on 17/11/2015.
 */
public class FollowupActivity extends BaseActivity {
    public static final String ANSWER = "ANSWER";
    private Answer answer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final ImageButton sendButton = (ImageButton) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFollowup(view);
            }
        });

        Intent intent = getIntent();
        assert (intent != null);

        Bundle b = intent.getExtras();
        answer = (Answer) b.getSerializable(ANSWER);

        /* TODO: Decide Keep?
        // Make it a bit more reliable
        roomName = intent.getStringExtra(JoinActivity.ROOM_NAME).toLowerCase();
        if (roomName == null || roomName.length() == 0) {
            roomName = "all";
        }
        if(mChatListAdapter==null)
            mChatListAdapter = new QuestionListAdapter(this, R.layout.question, new ArrayList<Question>());

        setTitle("Room name: " + roomName);

        TextView txt=(TextView) findViewById(R.id.txt_room_name);
        txt.setText("ROOM: " + roomName);
        */

        /* TODO: Reimplement
        // Setup our input methods. Enter key on the keyboard or pushing the send button
        final EditText inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendQuestion(getListView());
                }
                return true;
            }
        });

        final ImageButton sendButton = (ImageButton) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendQuestion(view);
            }
        });
        */
    }

    @Override
    public void onStart() {
        super.onStart();

        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = getListView();

        if(mChatListAdapter==null)
            mChatListAdapter = new FollowupListAdapter(this, R.layout.question, answer, answer.getFollow_ups());
        listView.setAdapter(mChatListAdapter);

        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });
    }

    protected void sendFollowup(View view) {
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        FollowUp followUp;
        if (!input.equals("")) {
            followUp = new FollowUp(input);

            // Clear inputText.
            inputText.setText("");

            if (!ImageHelper.picturePath.equals("")) {
                mChatListAdapter.uploadPhoto(ImageHelper.picturePath, followUp, answer.getId());
            } else {
                mChatListAdapter.push(followUp, answer.getId());
            }
        }
    }
}
