package net.whend.soodal.whend.view.setting;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.util.DateTimeFormatter;
import net.whend.soodal.whend.view.A0_3_SignUpFromFacebook;
import net.whend.soodal.whend.view.A0_4_FacebookFriendActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by macbook on 2015. 8. 23..
 */
public class S6_SyncFacebookFriend extends AppCompatActivity {

    private TextView sync_time;
    private ImageView sync_button;
    private LoginButton facebook_loginbutton;
    CallbackManager callbackManager;
    public JSONArray facebookfriend_jsonarray;


    @Override
    protected void onResume() {
        super.onResume();

        AppPrefs appPrefs = new AppPrefs(this);
        String time_string = appPrefs.getSyncfacebookfriend_time();
        if (time_string.contentEquals("")){
            sync_time.setText("지금 페이스북 친구들을 팔로우해보세요!");
        }else {
            sync_time.setText(time_string);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s6_syncfacebookfriend);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_syncfacebookfriend);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

        sync_time = (TextView)findViewById(R.id.sync_time);
        sync_button = (ImageView)findViewById(R.id.sync_button);
        facebook_loginbutton = (LoginButton)findViewById(R.id.login_facebook);




        sync_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager lm = LoginManager.getInstance();
                lm.logOut();
                facebook_loginbutton.performClick();
            }
        });

        callbackManager = CallbackManager.Factory.create();
        facebook_loginbutton.setReadPermissions(Arrays.asList("public_profile", "user_friends"));
        // If using in a fragment
        //   loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        facebook_loginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                // App code
                Log.d("FBToken", loginResult.getAccessToken().getToken());
                Log.d("FBUserId", loginResult.getAccessToken().getUserId());
                Log.d("FBSources", loginResult.getAccessToken().getSource().toString());

                //               AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
                //                   @Override
                //                   protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                //                       updateWithToken(newAccessToken);
                //                   }
                //               };
                Log.d("where3", "3");
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code

                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday,picture");
                request.setParameters(parameters);
                request.executeAsync();


                GraphRequest request2 = GraphRequest.newMyFriendsRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONArrayCallback() {
                        @Override
                        public void onCompleted(
                                JSONArray jsonArray,
                                GraphResponse response) {
                            // Application code for users friends
                            System.out.println("getFriendsData onCompleted : jsonArray " + jsonArray);
                            System.out.println("getFriendsData onCompleted : response " + response);
                            facebookfriend_jsonarray = new JSONArray();
                            facebookfriend_jsonarray = jsonArray;

                            try {
                                JSONObject jsonObject = response.getJSONObject();
                                System.out.println("getFriendsData onCompleted : jsonObject " + jsonObject);
                                JSONObject summary = jsonObject.getJSONObject("summary");
                                System.out.println("getFriendsData onCompleted : summary total_count - " + summary.getString("total_count"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Intent intent = new Intent(S6_SyncFacebookFriend.this, A0_4_FacebookFriendActivity.class);
                            //Intent intent = new Intent(mContext, A0_4_FacebookFriendActivity.class);
                            intent.putExtra("facebookfriend", facebookfriend_jsonarray.toString());          //bundle data.
                            intent.putExtra("goto", 2);      // goto wall
                            Log.d("facebookfriend_putExtra", facebookfriend_jsonarray.toString());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            S6_SyncFacebookFriend.this.startActivity(intent);

                            finish();

                            DateTimeFormatter dtf = new DateTimeFormatter();
                            AppPrefs appPrefs = new AppPrefs(S6_SyncFacebookFriend.this);
                            appPrefs.setSyncfacebookfriend_time(dtf.getYear() +" "+ dtf.getDate() + " "+ dtf.getTime());

                        }
                    }
                );
                Bundle parameters2 = new Bundle();
                parameters2.putString("fields", "id,name,link,picture");
                request2.setParameters(parameters2);
                request2.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "로그인을 취소 하였습니다!", Toast.LENGTH_SHORT).show();
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(), "에러가 발생하였습니다", Toast.LENGTH_SHORT).show();// App code
            }

        });

    }



    // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d("FB", "requestCode  " + requestCode);
        Log.d("FB", "resultCode" + resultCode);
        Log.d("FB", "data  " + data.toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_s6_syncfacebookfriend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            finish();
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
