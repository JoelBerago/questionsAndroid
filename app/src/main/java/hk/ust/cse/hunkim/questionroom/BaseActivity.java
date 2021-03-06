package hk.ust.cse.hunkim.questionroom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
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
    protected String roomName;
    protected DatabaseListAdapter mChatListAdapter;
    public static final String PREFS_NAME = "LoginPrefs";

    int experience;
    String username;
    boolean jailed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        // Intent will never be null!
        //assert (intent != null);

        // Make it a bit more reliable
        roomName = intent.getStringExtra(ROOM_NAME).toLowerCase();

        if (getUserId() == -1) {
            findViewById(R.id.listFooter).setVisibility(View.GONE);
        }

        TextView txt=(TextView) findViewById(R.id.txt_room_name);
        txt.setText("ROOM: " + roomName);

        //UPDATE USER PROFILE======================================
        //data from SharedPreference
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, 0);
        experience=pref.getInt("experience", 0);
        username= pref.getString("username", null);
        jailed=pref.getBoolean("jailed", false);
        int level= UserHelper.getLevel(experience);

        updateUser(level);
        updateCharacter(level);
        checkJailed(jailed,level);
        //===============================================================

    }

    @Override
    public void onStart() {
        super.onStart();
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

    public int getUserId() {
        SharedPreferences pref = getSharedPreferences(JoinActivity.PREFS_NAME, 0);
        return pref.getInt("userId", -1); }

    public int getExperience() {
        SharedPreferences pref = getSharedPreferences(JoinActivity.PREFS_NAME, 0);
        return pref.getInt("experience", -1); }

    public boolean getJailed(){
        SharedPreferences pref = getSharedPreferences(JoinActivity.PREFS_NAME, 0);
        jailed=pref.getBoolean("jailed", false);;
        return jailed;
    }

    public void addXP(int exp){
        SharedPreferences pref = getSharedPreferences(JoinActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();
        int experience= pref.getInt("experience", 0);
        String username=pref.getString("username", null);

        experience+=exp;
        editor.putInt("experience", experience);
        editor.commit();

        int level= UserHelper.getLevel(experience);
        //update UI
        TextView username_txt=(TextView)findViewById(R.id.txtUserInfo);
        username_txt.setText(username + " Lvl " + String.valueOf(level));

        ProgressBar expBar = (ProgressBar) findViewById(R.id.experienceBar);
        expBar.setProgress(UserHelper.getResidualExperience(experience));

        if(level%5==1){
            updateCharacter(level);
        }
    }

    public void updateCharacter(int level){
        ImageView img=(ImageView)findViewById(R.id.profileCharacter);
        UserHelper.setCharacterImage(level,img);
    }

    public void checkJailed(boolean jailed,int level){
        ImageButton sendButton = (ImageButton) findViewById(R.id.sendButton);
        ImageButton uploadImage = (ImageButton) findViewById(R.id.uploadImage);
        TextView msg_input=(TextView)findViewById(R.id.messageInput);
        if(jailed){
            sendButton.setVisibility(View.GONE);
            uploadImage.setVisibility(View.GONE);
            msg_input.setText("JAILED!");
            msg_input.setTextColor(Color.RED);
            msg_input.setEnabled(false);

            //update character image
            ImageView img=(ImageView)findViewById(R.id.profileCharacter);
            UserHelper.setCharacterJailedImage(level, img);

        }
        else{
            sendButton.setVisibility(View.VISIBLE);
            uploadImage.setVisibility(View.VISIBLE);
            msg_input.setText("");
            msg_input.setEnabled(true);

            //update character image
            ImageView img=(ImageView)findViewById(R.id.profileCharacter);
            UserHelper.setCharacterImage(level, img);
        }
    }

    public void updateUser(int level){
        //access UI components
        TextView username_txt=(TextView)findViewById(R.id.txtUserInfo);
        ProgressBar expBar = (ProgressBar) findViewById(R.id.experienceBar);
        //update UI
        username_txt.setText(username+" Lvl "+String.valueOf(level));
        expBar.setProgress(UserHelper.getResidualExperience(experience));
    }


}
