package hk.ust.cse.hunkim.questionroom;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;


/**
 * A login screen that offers login via email/password.
 */
public class JoinActivity extends Activity {
    private int userId;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    //get access to SharedPreference
    public static final String PREFS_NAME = "LoginPrefs";


    // UI references.
    private TextView roomNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        // Set up the login form.
        roomNameView = (TextView) findViewById(R.id.room_name);

        //get the userID from SharedPreference
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, 0);
        userId=pref.getInt("userId", -1);

        //Bundle extras=getIntent().getExtras();
        //userId=extras.getInt(BaseActivity.USERID);
        Log.i("LOGIN","join activity userId: "+String.valueOf(userId));
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptJoin(View view) {
        // Reset errors.
        roomNameView.setError(null);

        // Store values at the time of the login attempt.
        String room_name = roomNameView.getText().toString();

        boolean cancel = false;

        // Check for a valid email address.
        if (TextUtils.isEmpty(room_name)) {
            roomNameView.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        if (cancel) {
            roomNameView.setText("");
            roomNameView.requestFocus();
        } else {
            // Start main activity
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.ROOM_NAME, room_name);
            startActivity(intent);
        }
    }

    public void handle_userLogout(View view){
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();

        //clear SharedPreferences
        editor.clear();
        editor.commit();

        //Go back to login page
        Intent intent=new Intent(this, LogIn.class);
        startActivity(intent);

    }
}

