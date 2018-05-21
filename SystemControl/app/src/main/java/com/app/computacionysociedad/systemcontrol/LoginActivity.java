package com.app.computacionysociedad.systemcontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import retrofit2.Call;

public class LoginActivity extends Activity {

    TwitterLoginButton loginButton;
    boolean login = false;
    TwitterSession session;
    public static Activity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final long SIPACI_ID = 946153172791386112L;
        final String SIPACI_USER_NAME = "sipeci911";
        setContentView(R.layout.activity_login);
        loginActivity = this;
        Twitter.initialize(this);
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
                        getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);

        TwitterAuthToken token = new TwitterAuthToken(getResources().getString(R.string.token),
                getResources().getString(R.string.secret));

        final TwitterSession MY_SESSION = new TwitterSession(token, SIPACI_ID, SIPACI_USER_NAME);
        TwitterCore.getInstance().getSessionManager().setSession(SIPACI_ID, MY_SESSION);
        TwitterSession logSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        //System.out.println("Hola: "+logSession);
        if(logSession != null){
            login(logSession);
        }

        loginButton = findViewById(R.id.login_button);
        //loginButton.setVisibility(View.VISIBLE);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                login(session);
                System.out.println("Hola: "+session);
                login = true;
                loginButton.setVisibility(View.INVISIBLE);
                //LoginActivity.this.finish();
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Toast.makeText(LoginActivity.this, "Error al iniciar sesion", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void login(TwitterSession session){
        Call<User> call = TwitterCore.getInstance().getApiClient(session).getAccountService()
                .verifyCredentials(true, true, true);
        call.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                String name = result.data.name;
                String email = result.data.email;
                String imageUri = result.data.profileImageUrl.replace("_normal", "_bigger");
                parseIntent(name, email, imageUri);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(LoginActivity.this, "Error al cargar datos", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void parseIntent(String name, String email, String imageUri){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("email", email);
        intent.putExtra("imageUri", imageUri);
        ReportFragment.session = session;
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

}
