package net.whend.soodal.whend.view;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.util.CalendarProviderUtil;
import net.whend.soodal.whend.util.CircleTransform;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class A0_3_SignUpFromFacebook extends AppCompatActivity {

    private String data;
    private String fb_name_string;
    private String fb_picture;
    JSONArray facebookfriends;
    JSONArray picture_parser;
    JSONObject picture;
    public JSONArray facebookfriend_jsonarray;

    private ImageView user_photo;
    private TextView fb_name;
    private EditText user_name;
    private Button sign_up;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a0_3_sign_up_from_facebook);
        mContext = this;

        Intent intent = new Intent(this.getIntent());
        try{
            facebookfriends = new JSONArray(intent.getStringExtra("facebookfriend"));
            fb_name_string = intent.getStringExtra("fb_name");
            fb_picture = intent.getStringExtra("fb_picture");
            Log.d("fb",fb_picture+"");
        }catch(Exception e){}

        user_photo = (ImageView) findViewById(R.id.a03_fb_pic);
        fb_name = (TextView) findViewById(R.id.a03_fb_name);
        user_name = (EditText) findViewById(R.id.signup_username);
        sign_up = (Button) findViewById(R.id.signup_button);

        fb_name.setText(fb_name_string);


         Picasso.with(mContext).load(fb_picture).transform(new CircleTransform()).into(user_photo);


        //String getUserIdUrl = "http://119.81.176.245/userinfos/";
        // save user id
        //HTTPRestfulUtilizerExtender_facebookLogin0 a = new HTTPRestfulUtilizerExtender_facebookLogin0(mContext,getUserIdUrl,"GET");
       // a.doExecution();


        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!user_name.getText().toString().equals("")){
                    String setUserIdUrl = "http://119.81.176.245/userid/";
                    Bundle inputBundle_fb2 = new Bundle();
                    inputBundle_fb2.putCharSequence("username", user_name.getText().toString());
                    // Log.d("changeUsername",fb_name);
                    // save user id
                    HTTPRestfulUtilizerExtender_facebookLogin a = new HTTPRestfulUtilizerExtender_facebookLogin(mContext,setUserIdUrl,"PUT",inputBundle_fb2);
                    a.doExecution();
                }else{
                    Toast.makeText(mContext,"아이디를 확인해주세요", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    class HTTPRestfulUtilizerExtender_facebookLogin extends HTTPRestfulUtilizer {

        // Constructor for PUT
        public HTTPRestfulUtilizerExtender_facebookLogin(Context mContext, String url, String HTTPRestType,Bundle inputBundle) {
            setInputBundle(inputBundle);
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
                setOutputString(PUT(url, inputBundle));

                return getOutputString();

            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                String getUserIdUrl = "http://119.81.176.245/userinfos/";
                // save user id
                AppPrefs appPrefs = new AppPrefs(mContext);
                appPrefs.setUsername(getInputBundle().getCharSequence("username").toString());

                HTTPRestfulUtilizerExtender_facebookLogin3 d = new HTTPRestfulUtilizerExtender_facebookLogin3(mContext,getUserIdUrl,"GET");
                d.doExecution();

                //           progress.dismiss();

            }
        }
    }

    class HTTPRestfulUtilizerExtender_facebookLogin3 extends HTTPRestfulUtilizer{

        // Constructor for GET
        public HTTPRestfulUtilizerExtender_facebookLogin3(Context mContext, String url, String HTTPRestType) {
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


                    //Intent intent = new Intent(mContext, A0_3_SignUpFromFacebook.class);
                    Intent intent = new Intent(mContext, A0_4_FacebookFriendActivity.class);
                    intent.putExtra("facebookfriend", facebookfriends.toString());          //bundle data.
                    Log.d("facebookfriend_putExtra",facebookfriends.toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    mContext.startActivity(intent);
                    finish();


                }catch(Exception e){
                    Log.d("login exception", e.toString());


                }

            }
        }
    }

    class HTTPRestfulUtilizerExtender_facebookLogin0 extends HTTPRestfulUtilizer{

        // Constructor for GET
        public HTTPRestfulUtilizerExtender_facebookLogin0(Context mContext, String url, String HTTPRestType) {
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
                    JSONObject tmp = getOutputJsonObject();
                    String user_photo_url = tmp.getString("photo") == "null" ? "" : tmp.getString("photo").substring(0, tmp.getString("photo").length() - 4) + ".100x100.jpg";

                    if(user_photo_url!="") {
                        Picasso.with(mContext).load(fb_picture).transform(new CircleTransform()).into((ImageView) findViewById(R.id.a03_fb_pic));

                    }else{
                        // 기본이미지 로드.
                        user_photo.setImageResource(R.drawable.userimage_default);
                    }
                }catch(Exception e){
                    Log.d("login exception", e.toString());


                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_a0_3__sign_up_from_facebook, menu);
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
