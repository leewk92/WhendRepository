package net.whend.soodal.whend.view;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

/** 이 클래스는 마치 서버와 데이터 주고받기 튜토리얼
 * Created by wonkyung on 15. 7. 13.
 */
public class A0_2_SignUpActivity extends Activity {

    private EditText email_view;
    private EditText password_view;
    private Button signupButton_view;
    private TextView result_view;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a0_2_signup_layout);

        email_view = (EditText) findViewById(R.id.signup_email);
        password_view = (EditText) findViewById(R.id.signup_password);
        signupButton_view = (Button) findViewById(R.id.signup_button);
        signupButton_view.setOnClickListener(buttonListener);
        result_view = (TextView) findViewById(R.id.result);
    }

    public View.OnClickListener buttonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            Bundle inputBundle = new Bundle();
            inputBundle.putCharSequence("Username","soodal");
            inputBundle.putCharSequence("Password","Soodal2014!");

            String signupurl = "http://119.81.176.245/api-auth/login/";
            String url = "http://119.81.176.245/users/";

            /*// 이렇게 하면 받아오기전에 setText해서 안뜸
             HTTPRestfulUtilizer a = new HTTPRestfulUtilizer(url, "GET");
             a.doExecution();
             result_view.setText(a.getOutputString());
            */

            // 이렇게 해야 동기화 끝나고 행동을 함.
            HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(url,"GET");
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

    class HTTPRestfulUtilizerExtender2 extends HTTPRestfulUtilizer{

        // Constructor for GET
        public HTTPRestfulUtilizerExtender2(String url, String HTTPRestType, Bundle inputBundle) {

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
            }
        }
    }

}
