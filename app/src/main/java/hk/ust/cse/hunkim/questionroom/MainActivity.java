package hk.ust.cse.hunkim.questionroom;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import hk.ust.cse.hunkim.questionroom.db.ImageHelper;
import hk.ust.cse.hunkim.questionroom.question.Question;

public class MainActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ImageButton sendButton = (ImageButton) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendQuestion(view);
            }
        });

        setTitle("Room name: " + roomName);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = getListView();

        if(mChatListAdapter==null)
            mChatListAdapter = new QuestionListAdapter(this, R.layout.questionfirst, new ArrayList<Question>());
        listView.setAdapter(mChatListAdapter);

        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        mChatListAdapter.pull(roomName);
    }

    public void onLogin()
    {
        //pseudocode:
        //check if user's last login was on a date prior to today
        //if so
        // User.addXP(10);
        Toast.makeText(MainActivity.this, "You gained +10 xp!", Toast.LENGTH_SHORT).show(); //"for logging in"?
        //check if previous questions or answers were liked
        //make a for loop with an iterator of BaseQuestion type perhaps
        // User.addXP(20);
        // Toast.makeText(MainActivity.this, "You gained +20 xp!", Toast.LENGTH_SHORT).show(); //"Another user liked your question/answer"?
    }

    public void sendQuestion(View view) {
        EditText inputText = (EditText) findViewById(R.id.messageInput);

        String input = inputText.getText().toString();
        Question question;
        if (!input.equals("")) {
            question = new Question(roomName, input);

            // Clear inputText.
            inputText.setText("");

            if (!ImageHelper.picturePath.equals("")) {
                mChatListAdapter.uploadPhoto(ImageHelper.picturePath, question, "");
            } else {
                mChatListAdapter.push(question, "");
            }

            //User.addXP(10);
            Toast.makeText(MainActivity.this, "You gained +10 xp!", Toast.LENGTH_SHORT).show();
        }
    }

}
