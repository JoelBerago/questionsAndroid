package hk.ust.cse.hunkim.questionroom;

import android.app.ListActivity;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import hk.ust.cse.hunkim.questionroom.db.DBHelper;
import hk.ust.cse.hunkim.questionroom.db.DBUtil;
import hk.ust.cse.hunkim.questionroom.db.ImageHelper;
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

        Intent intent = getIntent();
        assert (intent != null);

        TextView txt=(TextView) findViewById(R.id.txt_room_name);

        // Make it a bit more reliable
        roomName = intent.getStringExtra(JoinActivity.ROOM_NAME).toLowerCase();
        if (roomName == null || roomName.length() == 0) {
            roomName = "all";
        }
        if(mChatListAdapter==null)
            mChatListAdapter = new QuestionListAdapter(this, R.layout.question);

        setTitle("Room name: " + roomName);
        txt.setText("ROOM: "+roomName);

        // Resize your main character using Picasso
        ImageView iv = (ImageView) findViewById(R.id.profileCharacter);
        Picasso.with(this)
                .load(R.drawable.temp_char)
                .resize(150, 240)   // image can stretch up to 240x140 max.
                .centerInside()
                .into(iv);

        // Setup our input methods. Enter key on the keyboard or pushing the send button
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        findViewById(R.id.uploadImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = getListView();

        if(mChatListAdapter==null)
         mChatListAdapter = new QuestionListAdapter(this, R.layout.question);
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

    //temporary, for testing
    public boolean hasAdapter()
    {
        if (mChatListAdapter==null)
            return false;
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        mChatListAdapter.cleanup();
    }

    private void sendMessage() {
        EditText inputText = (EditText) findViewById(R.id.messageInput);

        String input = inputText.getText().toString();
        Question question;
        if (!input.equals("")) {
            question = new Question(roomName, input);

            // Clear inputText.
            inputText.setText("");

            if (!ImageHelper.picturePath.equals("")) {
                mChatListAdapter.uploadPhoto(ImageHelper.picturePath, question);

            } else {
                mChatListAdapter.push(question);
            }


        }
    }

    private void selectImage() {
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
                        .placeholder(R.drawable.like24)
                        .resize(200, 200)   // image can stretch up to 240x140 max.
                        .centerInside()
                        .into(iv);
                Log.i("picture", "should be displayed");
            }

        }
    }

    public void updateLikes(String id) {
        /* TODO: Update Likes
        if (dbUtil.contains(id)) {
            Log.e("Dupkey", "Key is already in the DB!");
            return;
        }

        final Firebase echoRef = mFirebaseRef.child(id).child("echo");
        echoRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long echoValue = (Long) dataSnapshot.getValue();
                        Log.e("Echo update:", "" + echoValue);

                        echoRef.setValue(echoValue + 1);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                }
        );


        // Update SQLite DB
        dbUtil.put(id);
        */
    }

    public void Close(View view) {
        finish();
    }
}
