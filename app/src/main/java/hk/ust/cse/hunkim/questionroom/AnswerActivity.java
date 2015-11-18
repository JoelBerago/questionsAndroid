package hk.ust.cse.hunkim.questionroom;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import hk.ust.cse.hunkim.questionroom.db.ImageHelper;
import hk.ust.cse.hunkim.questionroom.question.Answer;
import hk.ust.cse.hunkim.questionroom.question.Question;

/**
 * Created by Joel on 17/11/2015.
 */
public class AnswerActivity extends BaseActivity {
    public static final String QUESTION = "QUESTION";
    private Question question;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ImageButton sendButton = (ImageButton) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAnswer(view);
            }
        });

        TextView roomText = (TextView) findViewById(R.id.txt_room_name);
        roomText.setText("Answers");

        EditText inputBox = (EditText) findViewById(R.id.messageInput);
        inputBox.setHint("Give an answer");

        Intent intent = getIntent();
        assert (intent != null);

        Bundle b = intent.getExtras();
        question = (Question) b.getSerializable(QUESTION);

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
            mChatListAdapter = new AnswerListAdapter(this, R.layout.questionsecond, question, question.getAnswers());
        listView.setAdapter(mChatListAdapter);

        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });
    }

    protected void sendAnswer(View view) {
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        Answer answer;
        if (!input.equals("")) {
            answer = new Answer(input);

            // Clear inputText.
            inputText.setText("");

            if (!ImageHelper.picturePath.equals("")) {
                mChatListAdapter.uploadPhoto(ImageHelper.picturePath, answer, question.getId());
            } else {
                mChatListAdapter.push(answer, question.getId());
            }
        }
    }
}
