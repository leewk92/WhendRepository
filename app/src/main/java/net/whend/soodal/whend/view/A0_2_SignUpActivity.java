package net.whend.soodal.whend.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.tutorial.T1_welcome;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.util.CalendarProviderUtil;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

/** 이 클래스는 마치 서버와 데이터 주고받기 튜토리얼
 * Created by wonkyung on 15. 7. 13.
 */
public class A0_2_SignUpActivity extends AppCompatActivity {
    private EditText username_view;
    private EditText email_view;
    private EditText password1_view, password2_view;
    private Button signupButton_view;
    private TextView result_view;
    private String email;
    private String password;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a0_2_signup_layout);
        mContext = this.getApplicationContext();
        username_view = (EditText) findViewById(R.id.signup_username);
        email_view = (EditText) findViewById(R.id.signup_email);
        password1_view = (EditText) findViewById(R.id.signup_password1);
        password2_view = (EditText) findViewById(R.id.signup_password2);
        signupButton_view = (Button) findViewById(R.id.signup_button);
        signupButton_view.setOnClickListener(buttonListener);
        result_view = (TextView) findViewById(R.id.result);

        password2_view.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch(actionId) {
                    case EditorInfo.IME_ACTION_DONE:

                        signupButton_view.performClick();
                        break;
                    default:
                        return false;
                }
                return true;

            }
        });

    }

    public View.OnClickListener buttonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {


            Bundle inputBundle = new Bundle();
            inputBundle.putCharSequence("username",username_view.getText());
            inputBundle.putCharSequence("email",email_view.getText());
            inputBundle.putCharSequence("password1",password1_view.getText());
            inputBundle.putCharSequence("password2",password2_view.getText());

            String url = "http://119.81.176.245/rest-auth/registration/";
            //String url = "http://119.81.176.245/rest-auth/login/";
            //String url = "http://119.81.176.245/schedules/";

            /*// 이렇게 하면 받아오기전에 setText해서 안뜸
             HTTPRestfulUtilizer a = new HTTPRestfulUtilizer(url, "GET");
             a.doExecution();
             result_view.setText(a.getOutputString());
            */

            // 이렇게 해야 동기화 끝나고 행동을 함.
            HTTPRestfulUtilizerExtender_login a = new HTTPRestfulUtilizerExtender_login(mContext, url,"POST",inputBundle);
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
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
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
// 회원가입하고 그 아이디로 로그인 시도.
    class HTTPRestfulUtilizerExtender_login extends HTTPRestfulUtilizer{

        public boolean canRegister = true;
        // Constructor for POST
        public HTTPRestfulUtilizerExtender_login(Context mContext, String url, String HTTPRestType, Bundle inputBundle) {
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
                result_view.setText(result);

                try{
                    String tmp_all = getOutputJsonObject().getString("__all__");
                    if(tmp_all.contentEquals("[\"You must type the same password each time.\"]")){
                        Toast toast1 = Toast.makeText(mContext, "같은 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT);
                        toast1.setGravity(0,0,100);
                        toast1.show();
                        canRegister=false;
                    }

                }catch(Exception e){Log.d("Catch Exception",e+"");}
                try{
                    String tmp_email = getOutputJsonObject().getString("email");
                    if(tmp_email.contentEquals("[\"Enter a valid email address.\"]")){
                        Toast toast1 = Toast.makeText(mContext, "이메일을 형식에 맞게 작성해주세요.", Toast.LENGTH_SHORT);
                        toast1.setGravity(0,0,100);
                        toast1.show();
                        canRegister=false;
                    }

                }catch(Exception e){Log.d("Catch Exception",e+"");}
                try{
                    String tmp_username = getOutputJsonObject().getString("username");
                    if(tmp_username.contentEquals("[\"This username is already taken. Please choose another.\"]")){
                        Toast toast1 = Toast.makeText(mContext, "중복된 아이디입니다.", Toast.LENGTH_SHORT);
                        toast1.setGravity(0,0,100);
                        toast1.show();
                        canRegister=false;
                    }else if(tmp_username.contentEquals("[\"This field is required.\"]")){
                        Toast toast1 = Toast.makeText(mContext, "아이디를 입력해주세요.", Toast.LENGTH_SHORT);
                        toast1.setGravity(0,0,100);
                        toast1.show();
                        canRegister=false;
                    }
                }catch(Exception e){Log.d("Catch Exception",e+"");}

                try{
                    String tmp_password1 = getOutputJsonObject().getString("password1");
                    if(tmp_password1.contentEquals("[\"This field is required.\"]")){
                        Toast toast1 = Toast.makeText(mContext, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT);
                        toast1.setGravity(0,0,100);
                        toast1.show();
                        canRegister=false;
                    }else if(tmp_password1.contentEquals("[\"Password must be a minimum of 6 characters.\"]")){
                        Toast toast1 = Toast.makeText(mContext, "비밀번호는 6자 이상이어야 합니다.", Toast.LENGTH_SHORT);
                        toast1.setGravity(0,0,100);
                        toast1.show();
                        canRegister=false;
                    }

                }catch(Exception e){Log.d("Catch Exception",e+"");}

                try{
                    String tmp_password2 = getOutputJsonObject().getString("password2");
                    if(tmp_password2.contentEquals("[\"This field is required.\"]")){
                        Toast toast1 = Toast.makeText(mContext, "확인을 위한 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT);
                        toast1.setGravity(0,0,100);
                        toast1.show();
                        canRegister=false;
                    }

                }catch(Exception e){Log.d("Catch Exception",e+"");}

                try{

                    if(canRegister==true){
                        Bundle loginBundle = new Bundle();
                        loginBundle.putCharSequence("username",getInputBundle().getCharSequence("username"));
                        loginBundle.putCharSequence("password", getInputBundle().getCharSequence("password1"));
                        String loginUrl =  "http://119.81.176.245/rest-auth/login/";

                        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(mContext, loginUrl,"POST",loginBundle);
                        a.doExecution();

                    }

                }catch(Exception e){
                    Log.d("Catch Exception",e+"");
                }


            }
        }
    }


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
            protected String doInBackground(String... strings) {
                String url = strings[0];
                String sHTTPRestType = strings[1];
                setOutputString(POST(url, getInputBundle()));
                return getOutputString();

            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                result_view.setText(result);

                try{
                    String token = getOutputJsonObject().getString("key");
                    if(token == null){
                        //Toast toast1 = Toast.makeText(mContext, "로그인을 할 수 없습니다. ", Toast.LENGTH_SHORT);
                        //toast1.show();
                    }
                    else {
                        // 유저네임과 토큰을 저장.

                        AppPrefs appPrefs = new AppPrefs(mContext);
                        appPrefs.setUsername(getInputBundle().getCharSequence("username").toString());
                        appPrefs.setToken(token);

                        String getUserIdUrl = "http://119.81.176.245/userinfos/";
                        // save user id
                        HTTPRestfulUtilizerExtender2 a = new HTTPRestfulUtilizerExtender2(mContext,getUserIdUrl,"GET");
                        a.doExecution();
                        Toast toast1 = Toast.makeText(mContext, "회원가입 성공", Toast.LENGTH_SHORT);
                        toast1.setGravity(0,0,100);
                        toast1.show();
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
/*
                    String url = "http://119.81.176.245/userinfos/"+user_id+"/follow/";
                    HTTPRestfulUtilizerExtender_follow a = new HTTPRestfulUtilizerExtender_follow(getmContext(), url,"PUT");
                    a.doExecution();
*/

                    Intent intent = new Intent(mContext, T1_welcome.class);
                    intent.putExtra("text", String.valueOf("URL"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    finish();
                }catch(Exception e){}

            }
        }
    }

    class HTTPRestfulUtilizerExtender_follow extends HTTPRestfulUtilizer {

        public HTTPRestfulUtilizerExtender_follow(Context mContext, String url, String HTTPRestType) {
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
            protected String doInBackground(String... strings) {
                String url = strings[0];
                String sHTTPRestType = strings[1];
                setOutputString(PUT(url, getInputBundle()));

                return getOutputString();
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

            }
        }
    }


}
