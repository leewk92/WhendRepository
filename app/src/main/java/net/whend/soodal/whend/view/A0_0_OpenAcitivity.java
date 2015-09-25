package net.whend.soodal.whend.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.util.CalendarProviderUtil;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import static java.lang.Thread.sleep;

public class A0_0_OpenAcitivity extends AppCompatActivity {
    public Context mContext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a0_0_open_layout);

        Thread moveTomain = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    AppPrefs appPrefs = new AppPrefs(mContext);
                    // creating account
                    CalendarProviderUtil cpu = new CalendarProviderUtil(mContext);
                    cpu.addAccountOfCalendar();
                    if(appPrefs.getToken()== "" ) {       //로그인창으로 이동

                        Intent i = new Intent(A0_0_OpenAcitivity.this, A0_1_LoginActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);

                        finish();
                    }else{      // MainActivity로 이동

                        Bundle inputBundle = new Bundle();
                        inputBundle.putCharSequence("username",appPrefs.getUsername());
                        inputBundle.putCharSequence("password",appPrefs.getPassword());

                        String url = "http://119.81.176.245/rest-auth/login/";

                        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(mContext, url,"POST",inputBundle);
                        a.doExecution();


                    }
                }
            }
        };

            moveTomain.start();
    }




    class HTTPRestfulUtilizerExtender extends HTTPRestfulUtilizer {

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
               // appPrefs.setToken("");

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
                if(result.split(" ")[0].contains("recvfrom")){
                    Log.d("recvfrom","recvfrom");
                    AppPrefs appPrefs = new AppPrefs(mContext);
                    appPrefs.setToken("");
                    appPrefs.setUsername("");
                    appPrefs.setPassword("");
                    appPrefs.setUser_id(0);
                    Intent i = new Intent(A0_0_OpenAcitivity.this, A0_1_LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);

                    finish();
                }

                try{
                    AppPrefs appPrefs = new AppPrefs(mContext);
                    String tmp_password1 = getOutputJsonObject().getString("password");
                    if(tmp_password1.contentEquals("[\"This field may not be blank.\"]")){
                        if(appPrefs.getUsername().contentEquals("")==false) {
                            Log.d("TokenToken",appPrefs.getToken());
                            Log.d("TokenId",appPrefs.getUsername());
                            Log.d("TokenIdId",appPrefs.getUser_id()+"");
                            Intent i = new Intent(A0_0_OpenAcitivity.this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                            finish();
                        }else{
                            Intent i = new Intent(A0_0_OpenAcitivity.this, A0_1_LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                            finish();
                        }
                    }
                }catch(Exception e){Log.d("Catch Exception",e+"");}

//                try{
//                    String tmp_non_field_errors = getOutputJsonObject().getString("non_field_errors");
//                    if(tmp_non_field_errors.contentEquals("[\"Unable to log in with provided credentials.\"]")){
//                        Intent i = new Intent(A0_0_OpenAcitivity.this, A0_1_LoginActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(i);
//                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
//                        finish();
//                    }
//
//                }catch(Exception e){
//
//                }

                try{

                    //       JSONObject tmpJson = new JSONObject(getOutputString());
                    //       String token = tmpJson.getString("key");
                    String token = getOutputJsonObject().getString("key");

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
                   // toast1.show();

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

//                try{
//                    String tmp_non_field_errors = getOutputJsonObject().getString("non_field_errors");
//                    if(tmp_non_field_errors.contentEquals("[\"Unable to log in with provided credentials.\"]")){
//                        Intent i = new Intent(A0_0_OpenAcitivity.this, A0_1_LoginActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(i);
//                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
//                        finish();
//                    }
//
//                }catch(Exception e){
//
//                }

                try{
                    int user_id = getOutputJsonObject().getInt("user_id");

                    // save user id .
                    AppPrefs appPrefs = new AppPrefs(mContext);
                    appPrefs.setUser_id(user_id);

                    // creating account
//                    CalendarProviderUtil cpu = new CalendarProviderUtil(getmContext());
//                    cpu.addAccountOfCalendar();

                    Toast toast1 = Toast.makeText(mContext, "로그인 성공.", Toast.LENGTH_SHORT);
                    toast1.setGravity(0,0,100);
                    //toast1.show();

                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("text", String.valueOf("URL"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);

                    finish();


                }catch(Exception e){
                    // 안되면 로그인화면으로 이동
        /*            AppPrefs appPrefs = new AppPrefs(mContext);
                    appPrefs.setToken("");
                    appPrefs.setUsername("");
                    appPrefs.setPassword("");
                    appPrefs.setUser_id(0);
                    Intent i = new Intent(A0_0_OpenAcitivity.this, A0_1_LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);

                    finish();
*/

                    String url = "http://119.81.176.245/rest-auth/login/";
                    AppPrefs appPrefs = new AppPrefs(mContext);
                    appPrefs.setToken("");
                    appPrefs.setUser_id(0);
                    Bundle inputBundle = new Bundle();
                    inputBundle.putCharSequence("username",appPrefs.getUsername());
                    inputBundle.putCharSequence("password",appPrefs.getPassword());
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(mContext, url,"POST",inputBundle);
                    a.doExecution();

                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_a0_0__open_acitivity, menu);
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
}
