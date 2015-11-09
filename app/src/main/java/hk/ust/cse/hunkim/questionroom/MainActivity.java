package hk.ust.cse.hunkim.questionroom;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import hk.ust.cse.hunkim.questionroom.db.ImageHelper;
import hk.ust.cse.hunkim.questionroom.question.BaseQuestion;
import hk.ust.cse.hunkim.questionroom.question.Question;

public class MainActivity extends ListActivity {
    private String roomName;
    private QuestionListAdapter mChatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialized once with an Android context.
        //Firebase.setAndroidContext(this);

        setContentView(R.layout.activity_main);

        //creating a dummy method to encapsulate things that should happen when logging in.
        onLogin();

        Intent intent = getIntent();
        assert (intent != null);

        TextView txt=(TextView) findViewById(R.id.txt_room_name);

        // Make it a bit more reliable
        roomName = intent.getStringExtra(JoinActivity.ROOM_NAME).toLowerCase();
        if (roomName == null || roomName.length() == 0) {
            roomName = "all";
        }
        if(mChatListAdapter==null)
            mChatListAdapter = new QuestionListAdapter(this, R.layout.question, new ArrayList<Question>());

        setTitle("Room name: " + roomName);
        txt.setText("ROOM: " + roomName);

        // Resize your main character using Picasso
        ImageView iv = (ImageView) findViewById(R.id.profileCharacter);
        Picasso.with(this)
                .load(R.drawable.temp_char)
                .resize(150, 240)   // image can stretch up to 240x140 max.
                .centerInside()
                .into(iv);

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
    }

    @Override
    public void onStart() {
        super.onStart();

        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = getListView();

        if(mChatListAdapter==null)
            mChatListAdapter = new QuestionListAdapter(this, R.layout.question, new ArrayList<Question>());
        listView.setAdapter(mChatListAdapter);

        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        mChatListAdapter.pull(roomName);

        // TODO: Reimplement
        // Finally, a little indication of connection status
        /*
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(MainActivity.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });
        */
    }

    /*/temporary, for testing
    public boolean hasAdapter()
    {
        if (mChatListAdapter==null)
            return false;
        return true;
    }*/

    @Override
    public void onStop() {
        super.onStop();
        mChatListAdapter.cleanup();
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

    public void selectImage(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent

        startActivityForResult(galleryIntent, ImageHelper.RESULT_LOAD_IMG);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageHelper.RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            ImageHelper.picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView iv = (ImageView) findViewById(R.id.selected_picture);
            iv.setImageBitmap(null);
            iv.setImageDrawable(null);
            Log.i("picture",ImageHelper.picturePath);
            if (!ImageHelper.picturePath.equals("")) {
                Picasso.with(this)
                        .load("file://"+ImageHelper.picturePath)
                        .placeholder(R.drawable.firebase_logo)
                        .resize(200, 200)   // image can stretch up to 240x140 max.
                        .centerInside()
                        .into(iv);
                Log.i("picture", "should be displayed");
            }

        }
    }

    public void imageClicked(View view){
        final ImageView iv = (ImageView) findViewById(R.id.selected_picture);
        if(!(iv.getDrawable()==null)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Remove Image?")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        //delete image
                        public void onClick(DialogInterface dialog, int id) {
                            ImageHelper.picturePath="";
                            iv.setImageBitmap(null);
                            iv.setImageDrawable(null);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        //do nothing
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else{
            Log.i("image","no images selected");
            return;
        }
    }



    public void Close(View view) {
        finish();
    }
}
