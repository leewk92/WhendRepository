package net.whend.soodal.whend.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class A0_3_SignUpFromFacebook extends AppCompatActivity {

    private String data;
    private String fb_keyid;
    private String fb_name_string;
    private String fb_picture;
    private String ImageAbsolutePath;
    JSONArray facebookfriends;
    JSONArray picture_parser;
    JSONObject picture;
    public JSONArray facebookfriend_jsonarray;

    private ImageView user_photo;
    private TextView fb_name;
    private EditText user_name;
    private Button sign_up;
    public boolean canResistor = true;
    private Context mContext;

    public Bitmap getFacebookProfilePicture(String fb_keyid){
        URL imageURL = null;
        try {
            imageURL = new URL("https://graph.facebook.com/" + fb_keyid + "/picture?type=large");
            Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
            Log.d("createdBitmapfromFacebook","success");
            return bitmap;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

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
            fb_keyid = intent.getStringExtra("fb_keyid");
            //Log.d("fb",fb_picture+"");
        }catch(Exception e){}

        user_photo = (ImageView) findViewById(R.id.a03_fb_pic);
        fb_name = (TextView) findViewById(R.id.a03_fb_name);
        user_name = (EditText) findViewById(R.id.signup_username);
        sign_up = (Button) findViewById(R.id.signup_button);

        fb_name.setText(fb_name_string);

        Picasso.with(mContext).load("https://graph.facebook.com/" + fb_keyid + "/picture?type=large").transform(new CircleTransform()).into(user_photo);


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
                canResistor = true;
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

                try{
                    String tmp_username = getOutputJsonObject().getString("username");
                    if(tmp_username.contentEquals("[\"This username is already taken. Please choose another.\"]")){
                        Toast toast1 = Toast.makeText(mContext, "중복된 아이디입니다.", Toast.LENGTH_SHORT);
                        toast1.setGravity(0, 0, 100);
                        toast1.show();
                        canResistor = false;
                    }else if(tmp_username.contentEquals("[\"This field is required.\"]")){
                        Toast toast1 = Toast.makeText(mContext, "아이디를 입력해주세요.", Toast.LENGTH_SHORT);
                        toast1.setGravity(0, 0, 100);
                        toast1.show();
                        canResistor = false;
                    }else if(tmp_username.contentEquals("[\"This field must be unique.\"]")){
                        Toast toast1 = Toast.makeText(mContext, "중복된 아이디입니다.", Toast.LENGTH_SHORT);
                        toast1.setGravity(0,0,100);
                        toast1.show();
                        canResistor = false;
                    }else if(tmp_username.contains("Enter a valid username. This value may")){
                        Toast toast1 = Toast.makeText(mContext, "영어로 아이디를 만들어주세요.", Toast.LENGTH_SHORT);
                        toast1.setGravity(0,0,100);
                        toast1.show();
                        canResistor = false;
                    }
                    //signupButton_view.setClickable(true);
                    //  if(progress.isShowing())
                    //      progress.dismiss();
                }catch(Exception e){Log.d("Catch Exception",e+"");}

                if(canResistor == true) {
                    String getUserIdUrl = "http://119.81.176.245/userinfos/";
                    // save user id
                    AppPrefs appPrefs = new AppPrefs(mContext);
                    appPrefs.setUsername(getInputBundle().getCharSequence("username").toString());

                    HTTPRestfulUtilizerExtender_facebookLogin3 d = new HTTPRestfulUtilizerExtender_facebookLogin3(mContext, getUserIdUrl, "GET");
                    d.doExecution();
                }
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


                    String url = "http://119.81.176.245/userinfos/"+appPrefs.getUser_id()+"/";
                    Bundle inputBundle = new Bundle();
//                    inputBundle.putIntegerArrayList("following_hashtag", u.getFollowing_hashtag_AL());
//                    inputBundle.putIntegerArrayList("following_schedule",u.getFollowing_schedule_AL());
//                    inputBundle.putIntegerArrayList("following_user", u.getFollowing_user_AL());
//                    inputBundle.putIntegerArrayList("like_schedule", u.getLike_schedule_AL());


                    URL fbpicture_url = new URL(fb_picture);
                    Bitmap profile_image = BitmapFactory.decodeStream(fbpicture_url.openConnection().getInputStream());
                    ImageAbsolutePath = createImageFromBitmap(profile_image);

                    HTTPRestfulUtilizerExtender4 a = new HTTPRestfulUtilizerExtender4(mContext,url,"PUT",inputBundle,ImageAbsolutePath);
                    a.doExecution();


                    //Intent intent = new Intent(mContext, A0_3_SignUpFromFacebook.class);



                }catch(Exception e){
                    Log.d("login exception", e.toString());


                }

            }
        }
    }



    //프로필올리기
    class HTTPRestfulUtilizerExtender4 extends HTTPRestfulUtilizer {

        //Constructor
        HTTPRestfulUtilizerExtender4(Context mContext, String url, String HTTPRestType, Bundle inputBundle, String photo){

            setInputBundle(inputBundle);
            setmContext(mContext);
            setUrl(url);
            setHTTPRestType(HTTPRestType);
            setPhoto(photo);

            task = new HttpAsyncTaskExtenders();
            Log.d("HTTP Constructor2 url", url);
        }

        public void doExecution(){
            task.execute(getUrl(), getHTTPRestType());
        }
        private class HttpAsyncTaskExtenders extends HTTPRestfulUtilizer.HttpAsyncTask {
            protected void onPreExecute() {
                super.onPreExecute();
            }

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

                Intent intent = new Intent(mContext, A0_4_FacebookFriendActivity.class);
                intent.putExtra("facebookfriend", facebookfriends.toString());          //bundle data.
                intent.putExtra("goto",1);
                Log.d("facebookfriend_putExtra",facebookfriends.toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(intent);
                finish();

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



    public String createImageFromBitmap(Bitmap bmp) {

        long currentTime = 0;
        FileOutputStream fileOutputStream = null;

        try {

            // create a File object for the parent directory
            File wallpaperDirectory = new File(this.getCacheDir().getPath());

            // have the object build the directory structure, if needed.
            wallpaperDirectory.mkdirs();

            //Capture is folder name and file name with date and time
            fileOutputStream = new FileOutputStream(String.format(
                    this.getCacheDir().getPath()+"/whend%d.jpg",

                    currentTime = System.currentTimeMillis()));

            // Here we Resize the Image ...
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100,
                    byteArrayOutputStream); // bm is the bitmap object
            byte[] bsResized = byteArrayOutputStream.toByteArray();


            try {
                fileOutputStream.write(bsResized);
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

        return this.getCacheDir().getPath()+"/whend"+ currentTime + ".jpg";
    }
}