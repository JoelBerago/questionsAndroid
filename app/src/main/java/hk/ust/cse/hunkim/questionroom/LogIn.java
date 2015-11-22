package hk.ust.cse.hunkim.questionroom;

/**
 * Created by mikacuy on 11/15/15.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import javax.security.auth.login.LoginException;

import hk.ust.cse.hunkim.questionroom.services.LogInResponse;
import hk.ust.cse.hunkim.questionroom.services.LogInService;
import hk.ust.cse.hunkim.questionroom.services.RegisterService;
import hk.ust.cse.hunkim.questionroom.services.UserInfoResponse;
import hk.ust.cse.hunkim.questionroom.services.UserInfoService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.JacksonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class LogIn extends AppCompatActivity {

    public static final String PREFS_NAME = "LoginPrefs";
    protected final static String BASE_URL = "http://questions-backend.herokuapp.com/api/";
    protected final static Retrofit logIn_retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        final SharedPreferences pref = getSharedPreferences(PREFS_NAME, 0);

        //Check if user is already logged in then move to JoinActivity
        if (pref.getString("logged", null)!=null && pref.getString("logged", null).toString().equals("logged")) {
            Log.i("LOGIN", "saved log-in");
            Intent intent = new Intent(LogIn.this, JoinActivity.class);
            //intent.putExtra("userId", pref.getInt("userId", -1)); //-1 invalid user ID!
            startActivity(intent);
        }

        //Not logged in yet
        Button signIn = (Button) findViewById(R.id.btn_signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = (EditText) findViewById(R.id.email);
                EditText password = (EditText) findViewById(R.id.password);
                TextView text_error= (TextView) findViewById(R.id.errorMessage);

                handleSignIn(email.getText().toString(), password.getText().toString(), text_error);
            }
        });

        Button signInAsGuest = (Button) findViewById(R.id.btn_signInAsGuest);
        signInAsGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignInAsGuest();
            }
        });
    }

    public void handleSignIn(String email, String password, final View view){
        LogInService service = logIn_retrofit.create(LogInService.class);
        Call<LogInResponse> response;
        response = service.loginRequest(email, password);

        response.enqueue(new Callback<LogInResponse>() {
            @Override
            public void onResponse(Response<LogInResponse> response, Retrofit retrofit) {
                Log.i("LOGIN", "in onResponse");

                if (response.body().error != "") {
                    Log.i("LOGIN", "error");
                    //TextView text_error= (TextView) view.findViewById(R.id.errorMessage);
                    TextView txt = (TextView) view;
                    txt.setText(response.body().error.toString() + " Log-in Failed. Please try again.");
                } else {
                    Log.i("LOGIN", "valid log-in");
                    Log.i("LOGIN", "userId: " + String.valueOf(response.body().userId));

                    //save to shared preference
                    SharedPreferences pref = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("logged", "logged");
                    editor.putInt("userId", response.body().userId);
                    editor.commit();

                    //get user info
                    saveUserInfo(pref.getInt("userId", -1));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("LOGIN", "Failed at SignIn", t);
            }
        });
    }

    public void handleSignInAsGuest() {
        //save to shared preference
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("logged", "logged");
        editor.putInt("userId", -1);

        editor.putString("username", "");
        editor.putInt("experience", 0);
        editor.putBoolean("jailed", false);
        editor.commit();

        Intent intent = new Intent(LogIn.this, JoinActivity.class);
        startActivity(intent);
    }

    //save user object in SharedPreference--> username, experience, jailed
    public void saveUserInfo(final int userId){
        UserInfoService service=logIn_retrofit.create(UserInfoService.class);
        Call<UserInfoResponse> response;

        if (userId!=-1){
            //get userInfo in backend
            response=service.getUserInfo(userId);
            response.enqueue(new Callback<UserInfoResponse>() {
                @Override
                public void onResponse(Response<UserInfoResponse> response, Retrofit retrofit) {
                    Log.i("USER", "got userInfo response");

                    if (response.body().userId != userId) {
                        Log.i("USER", "Error id got in login not the same as user info");
                    } else {
                        Log.i("USER", "Got user info");

                        //save user info to SharedPreference
                        SharedPreferences pref = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("username", response.body().username);
                        editor.putInt("experience", response.body().experience);
                        editor.putBoolean("jailed", response.body().jailed);
                        editor.commit();

                        //Debugging
                        Log.i("USER", "userId: "+String.valueOf(response.body().userId));
                        Log.i("USER", "username: "+response.body().username.toString());
                        Log.i("USER", "experience: "+String.valueOf(response.body().experience));
                        Log.i("USER", "jailed: "+String.valueOf(response.body().jailed));


                        //Finish log in
                        Log.i("LOGIN", "userId: " + String.valueOf(pref.getInt("userId", -1)));
                        Intent intent = new Intent(LogIn.this, JoinActivity.class);
                        //intent.putExtra("userId", response.body().userId);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e("LOGIN", "Failed at SignIn", t);
                }
            });
        }

        else{
            Log.i("USER","Invalid userId");
        }

    }

    public void showSignUpBox(View view){
        LinearLayout registerBox=(LinearLayout)findViewById(R.id.registerBox);
        if(registerBox.getVisibility()!=View.VISIBLE){
            registerBox.setVisibility(View.VISIBLE);
        }
        else{
            registerBox.setVisibility(View.INVISIBLE);
        }
    }

    public void sendRegisterRequest(final View view){
        final TextView email = (TextView) findViewById(R.id.register_email);
        final TextView password= (TextView) findViewById(R.id.register_password);
        final TextView username= (TextView)findViewById(R.id.register_username);

        RegisterService service = logIn_retrofit.create(RegisterService.class);
        Call<LogInResponse> response;
        response = service.registerRequest(email.getText().toString(), password.getText().toString(), username.getText().toString());

        response.enqueue(new Callback<LogInResponse>() {
            @Override
            public void onResponse(Response<LogInResponse> response, Retrofit retrofit) {
                Log.i("REGISTER", "in callback");
                Log.i("REGISTER",email.getText().toString());
                Log.i("REGISTER",password.getText().toString());
                Log.i("REGISTER",username.getText().toString());

                TextView txt = (TextView) findViewById(R.id.registerErrorMsg);

                if (response.body().error != "") {
                    Log.i("Register", "Register error");
                    txt.setTextColor(Color.RED);
                    txt.setText(response.body().error.toString()+" Register Failed. Please try again.");
                } else {
                    Log.i("REGISTER", "valid register");
                    txt.setTextColor(Color.GREEN);
                    txt.setText("Register complete. Please sign in.");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("LOGIN", "Failed to Register", t);
            }
        });


    }
}


