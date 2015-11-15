package hk.ust.cse.hunkim.questionroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import javax.security.auth.login.LoginException;

import hk.ust.cse.hunkim.questionroom.services.LogInResponse;
import hk.ust.cse.hunkim.questionroom.services.LogInService;
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

        SharedPreferences pref = getSharedPreferences(PREFS_NAME, 0);

        //check if user already logged in
        if (pref.getString("logged", null)!=null && pref.getString("logged", null).toString().equals("logged")) {
            Log.i("LOGIN","saved log-in");
            Intent intent = new Intent(LogIn.this, JoinActivity.class);
            startActivity(intent);
            intent.putExtra("session", pref.getInt("session", -1)); //-1 invalid session ID!
        }

        //not logged in yet
        Button signIn = (Button) findViewById(R.id.btn_signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = (EditText) findViewById(R.id.email);
                EditText password = (EditText) findViewById(R.id.password);
                TextView text_error= (TextView) findViewById(R.id.errorMessage);
                handleSignIn(email.getText().toString(),password.getText().toString(), text_error);

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

                if(response.body().error!=""){
                    Log.i("LOGIN","error");
                    //TextView text_error= (TextView) view.findViewById(R.id.errorMessage);
                    TextView txt=(TextView) view;
                    txt.setText("Log-in Failed. Please try again.");
                }

                else{
                    Log.i("LOGIN","valid log-in");
                    SharedPreferences pref = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("logged","logged");
                    editor.putInt("session", response.body().session);
                    editor.commit();

                    Log.i("LOGIN","session: "+String.valueOf(pref.getInt("session", -1)));

                    Intent intent = new Intent(LogIn.this, JoinActivity.class);
                    startActivity(intent);
                    intent.putExtra("session", pref.getInt("session", -1));

                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("LOGIN", "Failed at SignIn", t);
            }
        });
    }

}


