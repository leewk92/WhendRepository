package net.whend.soodal.whend.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.json.JSONObject;
import net.whend.soodal.whend.util.CalendarProviderUtil;
/** 이 클래스는 마치 서버와 데이터 주고받기 튜토리얼
 * Created by wonkyung on 15. 7. 13.
 */
public class A0_1_LoginActivity extends AppCompatActivity {
    private EditText username_view;
    private EditText email_view;
    private EditText password_view;
    private Button loginButton_view, signupButton_view;
    private TextView result_view;
    private String email;
    private String password;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a0_1_login_layout);
        mContext = this.getApplicationContext();
        username_view = (EditText) findViewById(R.id.login_username);
        password_view = (EditText) findViewById(R.id.login_password);
        loginButton_view = (Button) findViewById(R.id.login_button);
        signupButton_view = (Button) findViewById(R.id.signup_button);
        loginButton_view.setOnClickListener(loginButtonListener);
        signupButton_view.setOnClickListener(signupButtonListener);

        result_view = (TextView) findViewById(R.id.result);

        password_view.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch(actionId) {
                    case EditorInfo.IME_ACTION_DONE:

                        loginButton_view.performClick();
                        break;
                    default:
                        return false;
                }
                return true;

            }
        });

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
                    Log.d("whywhy","whyehwye");
             //       JSONObject tmpJson = new JSONObject(getOutputString());
             //       String token = tmpJson.getString("key");
                    String token = getOutputJsonObject().getString("key");
                    if(token == null){
                        Log.d("whywhy1","whyehwye");
                        Toast toast1 = Toast.makeText(mContext, "로그인을 할 수 없습니당 유유", Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                    else {
                        Log.d("whywhy2", "whyehwye");
                        result_view.setText(result);

                        // 유저네임과 토큰을 저장.
                        AppPrefs appPrefs = new AppPrefs(mContext);
                        appPrefs.setUsername(getInputBundle().getCharSequence("username").toString());
                        appPrefs.setToken(token);
                        String getUserIdUrl = "http://119.81.176.245/userinfos/";
                        // save user id
                        HTTPRestfulUtilizerExtender2 a = new HTTPRestfulUtilizerExtender2(mContext,getUserIdUrl,"GET");
                        a.doExecution();
                    }

                }catch(Exception e){}

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

                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("text", String.valueOf("URL"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    finish();


                }catch(Exception e){}

            }
        }
    }


}
