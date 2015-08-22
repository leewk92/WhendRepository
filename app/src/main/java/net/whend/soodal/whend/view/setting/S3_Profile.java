package net.whend.soodal.whend.view.setting;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.base.User;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.util.CircleTransform;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;
import net.whend.soodal.whend.util.RoundedImage;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class S3_Profile extends AppCompatActivity {

    private int TAKE_FROM_CAMERA = 1;
    private int TAKE_FROM_GALLERY = 2;

    TextView user_name, gender;
    EditText first_name, last_name, email, status;
    LinearLayout gender_select;
    Animation fade_in, fade_out, blink;
    RadioButton man, woman, etc;

    ProgressDialog  progress;
    String ImageAbsolutePath;
    Uri mImageCaptureUri;


    ImageView user_photo;

    LinearLayout linear_email, linear_status, photo_upload;
    TextView textview_email, textview_status;

    JSONObject outputSchedulesJson;
    User u = new User();
    Bundle inputBundle_forinfos = new Bundle();
    Bundle inputBundle_forid = new Bundle();

    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s3_profile);


        View.OnClickListener photo_add;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        toolbar.setTitle(" ");

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

        fade_in = AnimationUtils.loadAnimation(this, R.anim.abc_fade_in);
        fade_out = AnimationUtils.loadAnimation(this, R.anim.abc_fade_out);

        progress = new ProgressDialog(this);

        user_name = (TextView) findViewById(R.id.user_name);
        user_photo = (ImageView) findViewById(R.id.user_photo);

        photo_upload = (LinearLayout) findViewById(R.id.photo_upload);

        gender = (TextView) findViewById(R.id.gender);

        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        email = (EditText) findViewById(R.id.email);
        status = (EditText) findViewById(R.id.status);

        man = (RadioButton) findViewById(R.id.man);
        woman = (RadioButton) findViewById(R.id.woman);
        etc = (RadioButton) findViewById(R.id.etc);


        AppPrefs appPrefs = new AppPrefs(this);
        user_name.setText(appPrefs.getUsername());

        gender_select = (LinearLayout)findViewById(R.id.select_gender);
        gender_select.setVisibility(View.GONE);

        linear_email = (LinearLayout) findViewById(R.id.linear_email);
        linear_status = (LinearLayout) findViewById(R.id.linear_status);
        textview_email = (TextView) findViewById(R.id.textView_email);
        textview_status = (TextView) findViewById(R.id.textView_status);

        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(gender_select.getVisibility() != View.VISIBLE) {
                    gender_select.startAnimation(fade_in);
                    gender_select.setVisibility(View.VISIBLE);

                    linear_email.startAnimation(fade_out);
                    linear_status.startAnimation(fade_out);
                    textview_email.startAnimation(fade_out);
                    textview_status.startAnimation(fade_out);
                    email.startAnimation(fade_out);
                    status.startAnimation(fade_out);


                    linear_email.setVisibility(View.INVISIBLE);
                    linear_status.setVisibility(View.INVISIBLE);
                    textview_email.setVisibility(View.INVISIBLE);
                    textview_status.setVisibility(View.INVISIBLE);
                    email.setVisibility(View.INVISIBLE);
                    status.setVisibility(View.INVISIBLE);
                }else{
                    gender_select.startAnimation(fade_out);
                    gender_select.setVisibility(View.GONE);

                    linear_email.startAnimation(fade_in);
                    linear_status.startAnimation(fade_in);
                    textview_email.startAnimation(fade_in);
                    textview_status.startAnimation(fade_in);
                    email.startAnimation(fade_in);
                    status.startAnimation(fade_in);


                    linear_email.setVisibility(View.VISIBLE);
                    linear_status.setVisibility(View.VISIBLE);
                    textview_email.setVisibility(View.VISIBLE);
                    textview_status.setVisibility(View.VISIBLE);
                    email.setVisibility(View.VISIBLE);
                    status.setVisibility(View.VISIBLE);
                }

            }
        });

        man.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                gender.setText("남성");
                gender_select.startAnimation(fade_out);
                gender_select.setVisibility(View.GONE);

                linear_email.startAnimation(fade_in);
                linear_status.startAnimation(fade_in);
                textview_email.startAnimation(fade_in);
                textview_status.startAnimation(fade_in);
                email.startAnimation(fade_in);
                status.startAnimation(fade_in);


                linear_email.setVisibility(View.VISIBLE);
                linear_status.setVisibility(View.VISIBLE);
                textview_email.setVisibility(View.VISIBLE);
                textview_status.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                status.setVisibility(View.VISIBLE);

                etc.setChecked(false);
                woman.setChecked(false);

            }
        });

        woman.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                gender.setText("여성");
                gender_select.startAnimation(fade_out);
                gender_select.setVisibility(View.GONE);

                linear_email.startAnimation(fade_in);
                linear_status.startAnimation(fade_in);
                textview_email.startAnimation(fade_in);
                textview_status.startAnimation(fade_in);
                email.startAnimation(fade_in);
                status.startAnimation(fade_in);


                linear_email.setVisibility(View.VISIBLE);
                linear_status.setVisibility(View.VISIBLE);
                textview_email.setVisibility(View.VISIBLE);
                textview_status.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                status.setVisibility(View.VISIBLE);


                man.setChecked(false);
                etc.setChecked(false);
            }
        });

        etc.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                gender.setText("기타");

                gender_select.startAnimation(fade_out);
                gender_select.setVisibility(View.GONE);

                linear_email.startAnimation(fade_in);
                linear_status.startAnimation(fade_in);
                textview_email.startAnimation(fade_in);
                textview_status.startAnimation(fade_in);
                email.startAnimation(fade_in);
                status.startAnimation(fade_in);


                linear_email.setVisibility(View.VISIBLE);
                linear_status.setVisibility(View.VISIBLE);
                textview_email.setVisibility(View.VISIBLE);
                textview_status.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                status.setVisibility(View.VISIBLE);

                man.setChecked(false);
                woman.setChecked(false);
            }
        });

        photo_add = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"카메라로 찍기", "갤러리에서 불러오기"};

                AlertDialog.Builder builder = new AlertDialog.Builder(S3_Profile.this, R.style.AppCompatAlertDialogStyle);
                // 각 항목을 설정하고 클릭했을 때 동작을 지정함

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) { //카메라에서 찍기

                            // 카메라 호출
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());

                            // 이미지 잘라내기 위한 크기


                            intent.putExtra("crop", "true");
                            intent.putExtra("aspectX", 1);
                            intent.putExtra("aspectY", 1);
                            intent.putExtra("outputX", 100);
                            intent.putExtra("outputY", 100);

                            try {
                                intent.putExtra("return-data", true);
                                startActivityForResult(Intent.createChooser(intent,
                                        "Complete action using"), TAKE_FROM_CAMERA);
                            } catch (ActivityNotFoundException e) {
                                // Do nothing for now
                            }

                        } else if (item == 1) { //갤러리에서 가져오기


                            Intent intent = new Intent();
                            // Gallery 호출
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            // 잘라내기 셋팅

                            intent.putExtra("crop", "true");
                            intent.putExtra("aspectX", 1);
                            intent.putExtra("aspectY", 1);

                            intent.putExtra("outputX", 100);
                            intent.putExtra("outputY",  100);

                            try {
                                intent.putExtra("return-data", true);
                                startActivityForResult(Intent.createChooser(intent,
                                        "Complete action using"), TAKE_FROM_GALLERY);
                            } catch (ActivityNotFoundException e) {
                                // Do nothing for now
                            }
                        }

                    }
                });

                builder.show();

            }
        };
        photo_upload.setOnClickListener(photo_add);


        String url = "http://119.81.176.245/userinfos/";

        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(this,this.findViewById(android.R.id.content),url,"GET");
        a.doExecution();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_s3_profile, menu);
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

            if(man.isChecked())
                inputBundle_forinfos.putInt("gender", 1);
            else if(woman.isChecked())
                inputBundle_forinfos.putInt("gender", 2);

            inputBundle_forinfos.putCharSequence("status", status.getText());


            AppPrefs appPrefs = new AppPrefs(this);

            inputBundle_forid.putCharSequence("username",appPrefs.getUsername());
            inputBundle_forid.putCharSequence("first_name", first_name.getText());
            inputBundle_forid.putCharSequence("last_name", last_name.getText());
            inputBundle_forid.putCharSequence("email",email.getText());


            String url = "http://119.81.176.245/userinfos/";
            HTTPRestfulUtilizerExtender2 b = new HTTPRestfulUtilizerExtender2(this, url, "PUT", inputBundle_forinfos, inputBundle_forid);
            b.doExecution();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    // 정보 가져오기
    private class HTTPRestfulUtilizerExtender extends HTTPRestfulUtilizer {

        private View v;

        public HTTPRestfulUtilizerExtender(Context mContext,View rootView, String url, String HTTPRestType) {
            this.v = rootView;
            setmContext(mContext);
            setUrl(url);
            setHTTPRestType(HTTPRestType);
            task = new HttpAsyncTaskExtenders();
            Log.d("HTTP Constructor url", url);
            // new HttpAsyncTask().execute(url,HTTPRestType);
        }

        public void doExecution(){
            task.execute(getUrl(), getHTTPRestType());
        }

        private class HttpAsyncTaskExtenders extends HttpAsyncTask {

            protected String doInBackground(String... strings) {
                String url = strings[0];
                String sHTTPRestType = strings[1];
                setOutputString(GET(url));

                return getOutputString();
            }

            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                try{

                    outputSchedulesJson = getOutputJsonObject();
                    JSONObject tmp_ith = outputSchedulesJson;

                    u.setId(tmp_ith.getInt("user_id"));
                    u.setUsername(tmp_ith.getString("user_name"));
                    u.setUser_photo(tmp_ith.getString("photo") == "null" ? "" : tmp_ith.getString("photo").substring(0, tmp_ith.getString("photo").length() - 4) + ".100x100.jpg");
                    u.setGender(tmp_ith.getString("gender") == "null" ? "" : String.valueOf(tmp_ith.getInt("gender")));
                    u.setStatus(tmp_ith.getString("status"));
                    u.setEmail(tmp_ith.getString("email"));
                    u.setFirstname(tmp_ith.getString("first_name"));
                    u.setLastname(tmp_ith.getString("last_name"));

                }catch(Exception e){

                }

                Log.d("usertest", u.getUsername());
                user_name.setText(u.getUsername());
                first_name.setText(u.getFirstname());
                last_name.setText(u.getLastname());

                if(u.getGender().equals("1"))
                     gender.setText("남성");
                else if(u.getGender().equals("2"))
                     gender.setText("여성");
                else if (u.getGender().equals("null"))
                     gender.setText("-");

                status.setText(u.getStatus());
                email.setText(u.getEmail());

                if(u.getUser_photo()!="") {
                    Picasso.with(getApplicationContext()).load(u.getUser_photo()).transform(new CircleTransform()).into(user_photo);

                }else{
                    // 기본이미지 로드.
                    user_photo.setImageResource(R.drawable.userimage_default);
                }



            }


        }
    }


    // gender, status 올리기

    class HTTPRestfulUtilizerExtender2 extends HTTPRestfulUtilizer {

        //Constructor
        HTTPRestfulUtilizerExtender2(Context mContext, String url, String HTTPRestType, Bundle inputBundle1, Bundle inputBundle2){

            setInputBundle(inputBundle1);
            setmContext(mContext);
            setUrl(url);
            setHTTPRestType(HTTPRestType);

            task = new HttpAsyncTaskExtenders();
            Log.d("HTTP Constructor2 url", url);
        }



        public void doExecution(){

            task.execute(getUrl(), getHTTPRestType());
        }
        private class HttpAsyncTaskExtenders extends HTTPRestfulUtilizer.HttpAsyncTask {
            protected void onPreExecute() {
                super.onPreExecute();

                if(progress.isShowing()==false)
                    progress = ProgressDialog.show(getmContext(), "",
                            "수정 중입니다.", true);

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

                String url = "http://119.81.176.245/userid/";
                HTTPRestfulUtilizerExtender3 c = new HTTPRestfulUtilizerExtender3(getmContext(), url, "PUT", inputBundle_forid);
                c.doExecution();
            }

        }
    }


    // name, email 올리기

    class HTTPRestfulUtilizerExtender3 extends HTTPRestfulUtilizer {

        //Constructor
        HTTPRestfulUtilizerExtender3(Context mContext, String url, String HTTPRestType, Bundle inputBundle){

            setInputBundle(inputBundle);
            setmContext(mContext);
            setUrl(url);
            setHTTPRestType(HTTPRestType);

            task = new HttpAsyncTaskExtenders();
            Log.d("HTTP Constructor3 url", url);
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

                if(progress.isShowing()==true)
                    progress.dismiss();

                Toast.makeText(getApplicationContext(),"수정이 완료되었습니다",Toast.LENGTH_SHORT);
            }

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_FROM_GALLERY) {
                Bundle extras = data.getExtras();
                mImageCaptureUri = data.getData(); // Get data from selected photo

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    RoundedImage temp = new RoundedImage(photo);
                    user_photo.setImageDrawable(temp);

                    ImageAbsolutePath = createImageFromBitmap(photo);

                    //Picasso.with(getActivity()).load(ImageAbsolutePath).transform(new CircleTransform()).into(user_photo);

                    AppPrefs appPrefs = new AppPrefs(this);
                    String url = "http://119.81.176.245/userinfos/"+appPrefs.getUser_id()+"/";
                    Bundle inputBundle = new Bundle();
//                    inputBundle.putIntegerArrayList("following_hashtag", u.getFollowing_hashtag_AL());
//                    inputBundle.putIntegerArrayList("following_schedule",u.getFollowing_schedule_AL());
//                    inputBundle.putIntegerArrayList("following_user", u.getFollowing_user_AL());
//                    inputBundle.putIntegerArrayList("like_schedule", u.getLike_schedule_AL());
                    appPrefs.setPhoto(ImageAbsolutePath);
                    HTTPRestfulUtilizerExtender4 a = new HTTPRestfulUtilizerExtender4(this,url,"PUT",inputBundle,ImageAbsolutePath);
                    a.doExecution();

                    //HTTPRestfulUtilizerExtender2 a = new HTTPRestfulUtilizerExtender2(getActivity(),rootView,"url","POST","ImageAbsolutePath);
                    //a.doExecution();

                }
            }else if (requestCode == TAKE_FROM_CAMERA) {
                Bundle extras2 = data.getExtras();
                if (extras2 != null) {
                    Bitmap photo = extras2.getParcelable("data");
                    RoundedImage temp = new RoundedImage(photo);
                    user_photo.setImageDrawable(temp);

                    ImageAbsolutePath = createImageFromBitmap(photo);


                }
            }
        }
    }

    // 프로필 올리기
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


            }

        }
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
