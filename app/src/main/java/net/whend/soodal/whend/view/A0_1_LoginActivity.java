package net.whend.soodal.whend.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import net.whend.soodal.whend.util.CalendarProviderUtil;

import java.util.Arrays;

/** 이 클래스는 마치 서버와 데이터 주고받기 튜토리얼
 * Created by wonkyung on 15. 7. 13.
 */
public class A0_1_LoginActivity extends AppCompatActivity {
    private EditText username_view;
    private EditText email_view;
    private EditText password_view;
    private Button loginButton_view, signupButton_view, login_facebook;
    private TextView result_view;
    private String email;
    private String password;
    private Context mContext;
 //   private LoginButton loginButton;
    CallbackManager callbackManager;
    String fb_id, fb_email, fb_name;

    // 핸들러, 플래그 선언 for back key로 종료
    private Handler mHandler;
    private boolean mFlag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.a0_1_login_layout);
        mContext = this.getApplicationContext();
        username_view = (EditText) findViewById(R.id.login_username);
        password_view = (EditText) findViewById(R.id.login_password);
        loginButton_view = (Button) findViewById(R.id.login_button);
        signupButton_view = (Button) findViewById(R.id.signup_button);

        login_facebook = (Button) findViewById(R.id.login_facebook);
        login_facebook.setOnClickListener(new View.OnClickListener()    {
            @Override
            public void onClick (View v){

                Toast toast1 = Toast.makeText(mContext, "준비중입니다!", Toast.LENGTH_SHORT);
                toast1.setGravity(0, 0, 100);
                toast1.show();
            }
        });

//        loginButton = (LoginButton) findViewById(R.id.login_facebook);
        loginButton_view.setOnClickListener(loginButtonListener);
        signupButton_view.setOnClickListener(signupButtonListener);

        result_view = (TextView) findViewById(R.id.result);

        password_view.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:

                        loginButton_view.performClick();
                        break;
                    default:
                        return false;
                }
                return true;

            }
        });
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0) {
                    mFlag = false;
                }
            }
        };

/*
//facebook login button
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends"));
        // If using in a fragment
        //   loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("FBToken", loginResult.getAccessToken().getToken());
                Log.d("FBUserId", loginResult.getAccessToken().getUserId());
                Log.d("FBSources", loginResult.getAccessToken().getSource().toString());

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            fb_id = (String) response.getJSONObject().get("id");//페이스북 아이디값
                            fb_name = (String) response.getJSONObject().get("name");//페이스북 이름
                           // email = (String) response.getJSONObject().get("email");//이메일
                            Log.d("FB_object",object.toString());

                        } catch (JSONException e) {
                        // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        // new joinTask().execute(); //자신의 서버에서 로그인 처리를 해줍니다
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
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
                            try {
                                JSONObject jsonObject = response.getJSONObject();
                                System.out.println("getFriendsData onCompleted : jsonObject " + jsonObject);
                                JSONObject summary = jsonObject.getJSONObject("summary");
                                System.out.println("getFriendsData onCompleted : summary total_count - " + summary.getString("total_count"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
        }*/

    }
    //2초안에 백키 눌르면 종료
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(!mFlag) {
                Toast.makeText(this, "'뒤로'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
                mFlag = true;
                mHandler.sendEmptyMessageDelayed(0, 2000);
                return false;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    public View.OnClickListener signupButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, A0_2_SignUpActivity.class);
            intent.putExtra("text", String.valueOf("URL"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);

        }
    };
    public View.OnClickListener loginButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            loginButton_view.setClickable(false);
            Bundle inputBundle = new Bundle();
            inputBundle.putCharSequence("username",username_view.getText());
            inputBundle.putCharSequence("password",password_view.getText());

            String url = "http://119.81.176.245/rest-auth/login/";
            //String url = "http://119.81.176.245/rest-auth/login/";
            //String url = "http://119.81.176.245/schedules/";

            /*// 이렇게 하면 받아오기전에 setText해서 안뜸
             HTTPRestfulUtilizer a = new HTTPRestfulUtilizer(url, "GET");
             a.doExecution();
             result_view.setText(a.getOutputString());
            */

            // 이렇게 해야 동기화 끝나고 행동을 함.
            HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(mContext, url,"POST",inputBundle);
            a.doExecution();
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /*
        class HTTPRestfulUtilizerExtender extends HTTPRestfulUtilizer{

            // Constructor for GET
            public HTTPRestfulUtilizerExtender(String url, String HTTPRestType) {

                setUrl(url);
                setHTTPRestType(HTTPRestType);
                task = new HttpAsyncTaskExtenders();
                Log.d("HTTP Constructor url", url);
                // new HttpAsyncTask().execute(url,HTTPRestType);
            }

            @Override
            public void doExecution(){
                task.execute(getUrl(),getHTTPRestType());
            }
            class HttpAsyncTaskExtenders extends HTTPRestfulUtilizer.HttpAsyncTask{
                @Override
                protected String doInBackground(String... strings) {
                    String url = strings[0];
                    String sHTTPRestType = strings[1];
                    setOutputString(GET(url));

                    return getOutputString();
                }
                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    result_view.setText(result);
                }
            }
        }
    */



    class HTTPRestfulUtilizerExtender extends HTTPRestfulUtilizer{

        // Constructor for POST
        public HTTPRestfulUtilizerExtender(Context mContext, String url, String HTTPRestType, Bundle inputBundle) {
            setmContext(mContext);
            setUrl(url);
            setHTTPRestType(HTTPRestType);
            setInputBundle(inputBundle);
            task = new HttpAsyncTaskExtenders();
            Log.d("HTTP Constructor url", url);
            // new HttpAsyncTask().execute(url,HTTPRestType);
        }

        @Override
        public void doExecution(){
            task.execute(getUrl(), getHTTPRestType());
        }
        class HttpAsyncTaskExtenders extends HTTPRestfulUtilizer.HttpAsyncTask{

            @Override
            protected void onPreExecute() {
                AppPrefs appPrefs = new AppPrefs(mContext);
                appPrefs.setToken("");

                super.onPreExecute();
            }


            @Override
            protected String doInBackground(String... strings) {
                String url = strings[0];
                String sHTTPRestType = strings[1];
                setOutputString(POST(url, getInputBundle()));

                return getOutputString();

            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);


                try{

             //       JSONObject tmpJson = new JSONObject(getOutputString());
             //       String token = tmpJson.getString("key");
                    String token = getOutputJsonObject().getString("key");
     //               result_view.setText(result);
                    // 유저네임과 토큰을 저장.
                    AppPrefs appPrefs = new AppPrefs(mContext);
                    appPrefs.setUsername(getInputBundle().getCharSequence("username").toString());
                    appPrefs.setPassword(getInputBundle().getCharSequence("password").toString());
                    appPrefs.setToken(token);
                    String getUserIdUrl = "http://119.81.176.245/userinfos/";
                    // save user id
                    HTTPRestfulUtilizerExtender2 a = new HTTPRestfulUtilizerExtender2(mContext,getUserIdUrl,"GET");
                    a.doExecution();

                }catch(Exception e){
                    Toast toast1 = Toast.makeText(mContext, "존재하지 않는 아이디거나 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT);
                    toast1.setGravity(0,0,100);
                    toast1.show();
                    loginButton_view.setClickable(true);
                }finally{

                }

            }
        }
    }

    class HTTPRestfulUtilizerExtender2 extends HTTPRestfulUtilizer{

        // Constructor for GET
        public HTTPRestfulUtilizerExtender2(Context mContext, String url, String HTTPRestType) {
            setmContext(mContext);
            setUrl(url);
            setHTTPRestType(HTTPRestType);
            task = new HttpAsyncTaskExtenders();
            Log.d("HTTP Constructor url", url);
            // new HttpAsyncTask().execute(url,HTTPRestType);
        }

        @Override
        public void doExecution(){
            task.execute(getUrl(), getHTTPRestType());
        }
        class HttpAsyncTaskExtenders extends HTTPRestfulUtilizer.HttpAsyncTask{

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
            }


            @Override
            protected String doInBackground(String... strings) {
                String url = strings[0];
                String sHTTPRestType = strings[1];
                setOutputString(GET(url));

                return getOutputString();

            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                try{
                    int user_id = getOutputJsonObject().getInt("user_id");

                    // save user id .
                    AppPrefs appPrefs = new AppPrefs(mContext);
                    appPrefs.setUser_id(user_id);

                    // creating account
                    CalendarProviderUtil cpu = new CalendarProviderUtil(getmContext());
                    cpu.addAccountOfCalendar();

                    Toast toast1 = Toast.makeText(mContext, "로그인 성공.", Toast.LENGTH_SHORT);
                    toast1.setGravity(0,0,100);
                    toast1.show();

                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("text", String.valueOf("URL"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    finish();


                }catch(Exception e){
                    Log.d("login exception",e.toString());


                }finally{
                    loginButton_view.setClickable(true);
                }

            }
        }
    }


}
