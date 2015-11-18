package hk.ust.cse.hunkim.questionroom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import hk.ust.cse.hunkim.questionroom.db.ImageHelper;
import hk.ust.cse.hunkim.questionroom.db.UserHelper;
import hk.ust.cse.hunkim.questionroom.question.Question;

/**
 * Created by Joel on 17/11/2015.
 */
public abstract class BaseActivity extends ListActivity {
    public static final String ROOM_NAME = "Room_name";
    public static final String USERID = "userId";
    protected int userId;
    protected String roomName;
    protected DatabaseListAdapter mChatListAdapter;
    public static final String PREFS_NAME = "LoginPrefs";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        assert (intent != null);

        // Make it a bit more reliable
        roomName = intent.getStringExtra(ROOM_NAME).toLowerCase();
        if (roomName == null || roomName.length() == 0) {
            roomName = "all";
        }

        TextView txt=(TextView) findViewById(R.id.txt_room_name);
        txt.setText("ROOM: " + roomName);

        //UPDATE USER PROFILE
        //data from SharedPreference
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, 0);
        int experience=pref.getInt("experience", 0);
        String username=pref.getString("username", null);

        int level= UserHelper.getLevel(experience);
        int percentResidualExperience=(int)(UserHelper.getResidualExperience(experience)/20.0)*100;//in percent
        //update UI
        TextView username_txt=(TextView)findViewById(R.id.txtUserInfo);
        username_txt.setText(username+" Lvl "+String.valueOf(level));
        ProgressBar expBar = (ProgressBar) findViewById(R.id.experienceBar);
        expBar.setProgress(percentResidualExperience);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mChatListAdapter.cleanup();
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
            Log.i("picture", ImageHelper.picturePath);
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

    public int getUserId() { return userId; }

}
