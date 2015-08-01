package net.whend.soodal.whend.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.util.DateTimeFormatter;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


public class A4_MakeScheduleActivity extends AppCompatActivity {

    private int TAKE_FROM_CAMERA = 1;
    private int TAKE_FROM_GALLERY = 2;

    private int REQ_CODE_SELECT_IMAGE = 100;
    private Uri mImageCaptureUri;
    static private String ImageAbsolutePath=null;
    private String[] hashtags_title;
    private ArrayList<Integer> hashtags_id = new ArrayList<Integer>();
    private ImageView schedule_photo;
    private ImageView schedule_photo_add;
    private static int YEAR_start,MONTH_start,DAY_start,HOUR_start,MINUTE_start;
    private static int YEAR_end,MONTH_end,DAY_end,HOUR_end,MINUTE_end;
    static TextView date_start,time_start,date_end,time_end;
    static int completed_num=0;
    EditText title,location,memo;
    String sStartDate, sEndDate, sContent, sLocation, sStartTime,sEndTime;
    long sDatetime_start,sDatetime_end;
    Bundle inputBundle_forRequest = new Bundle();
    ProgressDialog progress;


    public void onBackPressed(){
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        completed_num=0;
        View.OnClickListener photo_add;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.a4_make_schedule_layout);


        AppPrefs appPrefs = new AppPrefs(this);
        TextView username = (TextView)findViewById(R.id.user_fullname);
        username.setText(appPrefs.getUsername());


        date_start = (TextView) findViewById(R.id.date_start);
        time_start = (TextView) findViewById(R.id.time_start);

        date_end = (TextView) findViewById(R.id.date_end);
        time_end = (TextView) findViewById(R.id.time_end);

        title = (EditText) findViewById(R.id.title);
        location = (EditText) findViewById(R.id.location);
        memo = (EditText) findViewById(R.id.memo);

        schedule_photo = (ImageView) findViewById(R.id.schedule_photo);
        schedule_photo_add = (ImageView) findViewById(R.id.schedule_photo_add);

        Intent intent = new Intent(this.getIntent());

        sStartDate = intent.getStringExtra("date_start");
        sEndDate = intent.getStringExtra("date_end");
        sStartTime = intent.getStringExtra("time_start");
        sEndTime = intent.getStringExtra("time_end");
        sContent = intent.getStringExtra("content");
        sLocation = intent.getStringExtra("location");
        sDatetime_start = intent.getLongExtra("datetime_start", 0);
        sDatetime_end = intent.getLongExtra("datetime_end", 0);


        if(sDatetime_start != 0) {
            DateTimeFormatter dtf = new DateTimeFormatter(sDatetime_start);
            date_start.setText(dtf.getDate());
            time_start.setText(dtf.getTime());

            YEAR_start = dtf.getCalendar().get(Calendar.YEAR);
            MONTH_start = dtf.getCalendar().get(Calendar.MONTH)+1;
            DAY_start = dtf.getCalendar().get(Calendar.DAY_OF_MONTH);
            HOUR_start = dtf.getCalendar().get(Calendar.HOUR_OF_DAY);
            MINUTE_start = dtf.getCalendar().get(Calendar.MINUTE);
        }else{
            // now
            DateTimeFormatter dtf = new DateTimeFormatter();
            date_start.setText(dtf.getDate());
            time_start.setText(dtf.getTime());
            YEAR_start = dtf.getCalendar().get(Calendar.YEAR);
            MONTH_start = dtf.getCalendar().get(Calendar.MONTH)+1;
            DAY_start = dtf.getCalendar().get(Calendar.DAY_OF_MONTH);
            HOUR_start = dtf.getCalendar().get(Calendar.HOUR_OF_DAY);
            MINUTE_start = dtf.getCalendar().get(Calendar.MINUTE);
        }
        if(sDatetime_end != 0) {
            DateTimeFormatter dtf = new DateTimeFormatter(sDatetime_end);
            date_end.setText(dtf.getDate());
            time_end.setText(dtf.getTime());
            YEAR_end = dtf.getCalendar().get(Calendar.YEAR);
            MONTH_end = dtf.getCalendar().get(Calendar.MONTH)+1;
            DAY_end = dtf.getCalendar().get(Calendar.DAY_OF_MONTH);
            HOUR_end = dtf.getCalendar().get(Calendar.HOUR_OF_DAY);
            MINUTE_end = dtf.getCalendar().get(Calendar.MINUTE);
        }else{
            // now
            DateTimeFormatter dtf = new DateTimeFormatter();
            date_end.setText(dtf.getDate());
            time_end.setText(dtf.getTime());
            YEAR_end = dtf.getCalendar().get(Calendar.YEAR);
            MONTH_end = dtf.getCalendar().get(Calendar.MONTH)+1;
            DAY_end = dtf.getCalendar().get(Calendar.DAY_OF_MONTH);
            HOUR_end = dtf.getCalendar().get(Calendar.HOUR_OF_DAY);
            MINUTE_end = dtf.getCalendar().get(Calendar.MINUTE);
        }

        if(sStartDate != null)
            date_start.setText(sStartDate);
        if(sEndDate != null)
            date_end.setText(sEndDate);
        if(sStartTime != null)
            time_start.setText(sStartTime);
        if(sEndTime != null)
            time_end.setText(sEndTime);
        if(sLocation != null)
            location.setText(sLocation);
        if(sContent != null)
            title.setText(sContent);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_upload);
        toolbar.setTitle("");
        TextView toolbartext = (TextView) findViewById(R.id.toolbar_textview);
        toolbartext.setText("공유하기");

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_s);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });


        // 클릭리스너

        photo_add = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"카메라로 찍기", "갤러리에서 불러오기"};

                AlertDialog.Builder builder = new AlertDialog.Builder(A4_MakeScheduleActivity.this, R.style.AppCompatAlertDialogStyle);
                        // 각 항목을 설정하고 클릭했을 때 동작을 지정함

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) { //카메라에서 찍기

                            // 카메라 호출
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());

                            // 이미지 잘라내기 위한 크기


                            float density  = getResources().getDisplayMetrics().density;

                            float width_dp = (float) getResources().getDisplayMetrics().widthPixels / density;
                            float height_dp = 120f;
                            float ratio = width_dp/height_dp;

                            System.out.println("witdhdp " + width_dp);
                            System.out.println("heightdp " + height_dp);
                            System.out.println("ratio " + ratio);

                            intent.putExtra("crop", "true");
                            intent.putExtra("aspectX", (int) (ratio * 100));
                            intent.putExtra("aspectY", 100);

                            intent.putExtra("outputX", (int)(ratio*100));
                            intent.putExtra("outputY",  100);

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

                            float density  = getResources().getDisplayMetrics().density;

                            float width_dp = (float) getResources().getDisplayMetrics().widthPixels / density;
                            float height_dp = 120f;
                            float ratio = width_dp/height_dp;

                            System.out.println("witdhdp " + width_dp);
                            System.out.println("heightdp " + height_dp);
                            System.out.println("ratio " + ratio);

                            intent.putExtra("crop", "true");
                            intent.putExtra("aspectX", (int) (ratio * 100));
                            intent.putExtra("aspectY", 100);

                            intent.putExtra("outputX", (int)(ratio*100));
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

        schedule_photo.setOnClickListener(photo_add);
        schedule_photo_add.setOnClickListener(photo_add);

        StartDatePickerTextviewListener(this, date_start, sDatetime_start);
        StartTimePickerTextviewListener(this, time_start, sDatetime_start);

        EndDatePickerTextviewListener(this, date_end, sDatetime_end);
        EndTimePickerTextviewListener(this, time_end, sDatetime_end);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {



        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_FROM_GALLERY) {
                Bundle extras = data.getExtras();
                mImageCaptureUri = data.getData(); // Get data from selected photo

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    schedule_photo.setImageBitmap(photo);

                    ImageAbsolutePath = createImageFromBitmap(photo);

                }
            }else if (requestCode == TAKE_FROM_CAMERA) {
                Bundle extras2 = data.getExtras();
                if (extras2 != null) {
                    Bitmap photo = extras2.getParcelable("data");
                    schedule_photo.setImageBitmap(photo);

                    ImageAbsolutePath = createImageFromBitmap(photo);
                }
            }
        }
    }

    public void StartDatePickerTextviewListener(Context context, TextView date, long datetime){
        final Context mContext = context;
        final TextView dateview = date;
        final DateTimeFormatter dtf = new DateTimeFormatter(datetime);

        dateview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                F6_DatePickerFragment_start newFragment = new F6_DatePickerFragment_start();
                newFragment.show(getSupportFragmentManager(), "DatePicker");

            }
        });
    }


    public void StartTimePickerTextviewListener(Context context, TextView time, long datetime){
        final Context mContext = context;
        final TextView timeview = time;
        final DateTimeFormatter dtf = new DateTimeFormatter(datetime);

        timeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                F7_TimePickerFragment_start newFragment = new F7_TimePickerFragment_start();
                newFragment.show(getSupportFragmentManager(), "TimePicker");

            }
        });
    }
    public void EndDatePickerTextviewListener(Context context, TextView date, long datetime){
        final Context mContext = context;
        final TextView dateview = date;
        final DateTimeFormatter dtf = new DateTimeFormatter(datetime);

        dateview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                F6_DatePickerFragment_end newFragment = new F6_DatePickerFragment_end();
                newFragment.show(getSupportFragmentManager(), "DatePicker");

            }
        });
    }


    public void EndTimePickerTextviewListener(Context context, TextView time, long datetime){
        final Context mContext = context;
        final TextView timeview = time;
        final DateTimeFormatter dtf = new DateTimeFormatter(datetime);

        timeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                F7_TimePickerFragment_end newFragment = new F7_TimePickerFragment_end();
                newFragment.show(getSupportFragmentManager(), "TimePicker");

            }
        });
    }

    public String createImageFromBitmap(Bitmap bmp) {

        long currentTime = 0;
        FileOutputStream fileOutputStream = null;

        try {

            // create a File object for the parent directory

            //File wallpaperDirectory = new File("/sdcard/Whend/");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_a4_make_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // 먼저 메모를 파싱한 후 해시태그 이름들을 배열에 담는다.
        // 그담에 배열의 리스트들을 각각 검색하고 검색 결과가 있다면 그 해시태그의 아이디를 가져온다.
        // 만약 검색 결과가 없다면 해시태그를 만드는 요청을 보내고 그 해시태그의 아이디를 가져온다.
        // 이렇게 각각 해시태그에 대한 아이디를 얻어서 배열에 담는다.
        // 이들을 [1,2,55,71] 형식으로 바꾸고 inputBundle.putIntArrayList 한다.

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_upload) {

            completed_num=0;

            parseMemo(memo.getText() + "");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void parseMemo(String memo_text){
        memo_text = memo_text.replaceAll("\n"," ");
        String tmpArray[] = memo_text.split("#");
        if(tmpArray != null) {

            hashtags_title = new String[tmpArray.length - 1];

            for (int i = 1; i < tmpArray.length; i++) {
                hashtags_title[i - 1] = tmpArray[i].split(" ")[0];
            }
            searchHashtags();
        }else{      //hash tag가 없을 때

            inputBundle_forRequest.putCharSequence("title",title.getText());
            inputBundle_forRequest.putCharSequence("memo", memo.getText());
            inputBundle_forRequest.putCharSequence("location",location.getText());
            Log.d("updateLocation",location.getText()+"");
            /* if(hashtags_id.size() !=0){
                inputBundle_forRequest.putIntegerArrayList("hashtag",hashtags_id);
            }*/
            Calendar cal = Calendar.getInstance();
            cal.set(YEAR_start, MONTH_start-1, DAY_start, HOUR_start, MINUTE_start);

            Log.d("getTimeinInt", YEAR_start + " " + MONTH_start + " " + DAY_start + " " + HOUR_start + " " + MINUTE_start + "");
            Log.d("getTimeinMillis", cal.getTimeInMillis()+"");

            DateTimeFormatter dtf = new DateTimeFormatter(cal.getTimeInMillis());
            inputBundle_forRequest.putCharSequence("start_time", dtf.getOutputString());

            Log.d("getTimeinString",dtf.getOutputString());
            cal.set(YEAR_end, MONTH_end-1, DAY_end, HOUR_end, MINUTE_end);
            dtf = new DateTimeFormatter(cal.getTimeInMillis());
            inputBundle_forRequest.putCharSequence("end_time",dtf.getOutputString());
            String url = "http://119.81.176.245/schedules/";
            HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(this,url,"POST",inputBundle_forRequest, ImageAbsolutePath);
            a.doExecution();
        }
    }
    public void searchHashtags(){
        String search_url = "http://119.81.176.245/hashtags/all/exact/?search=";
        for(int i=0; i<hashtags_title.length; i++){
            String tmp_url= search_url + hashtags_title[i];
            HTTPRestfulUtilizerExtender3 a = new HTTPRestfulUtilizerExtender3(this,tmp_url,"GET",hashtags_title[i]);
            a.doExecution();
        }
    }

// for 일정 올리기
    class HTTPRestfulUtilizerExtender extends HTTPRestfulUtilizer {

        private EditText dateview;
        private EditText timeview;
        // Constructor for POST
        public HTTPRestfulUtilizerExtender(Context mContext, String url, String HTTPRestType, Bundle inputBundle, String photo) {
            setmContext(mContext);
            setUrl(url);
            setHTTPRestType(HTTPRestType);
            setInputBundle(inputBundle);
            setPhoto(photo);
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


                setOutputString(POST(url, getInputBundle()));



                return getOutputString();

            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                try{
                    String memo = getOutputJsonObject().getString("memo");
                    Log.d("memo",memo);
                    if(memo.contentEquals("[\"This field may not be blank.\"]")){
                        progress.dismiss();
                        Toast.makeText(getmContext(),"메모를 입력하세요! ",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getmContext(),"업로드합니다! ",Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){}
                try{
                    String title = getOutputJsonObject().getString("title");

                    if(title.contentEquals("[\"This field must be unique.\"]")){
                        progress.dismiss();
                        Toast.makeText(getmContext(),"업로드에 실패하였습니다. ",Toast.LENGTH_SHORT).show();
                    }

                }catch(Exception e){}


                try{    // 올린 일정 받아보기 자동 설정
                    int id = getOutputJsonObject().getInt("id");
                    String url = "http://119.81.176.245/schedules/"+id+"/follow/";
                    HTTPRestfulUtilizerExtender2 b = new HTTPRestfulUtilizerExtender2(getmContext(), url,"PUT");
                    b.doExecution();
                }catch(Exception e){

                }


            }
        }
    }
// for 일정 받아보기
    class HTTPRestfulUtilizerExtender2 extends HTTPRestfulUtilizer {

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
            protected String doInBackground(String... strings) {
                String url = strings[0];
                String sHTTPRestType = strings[1];
                setOutputString(PUT(url, getInputBundle()));

                return getOutputString();
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                progress.dismiss();
                finish();
            }
        }
    }


    // for 해시태그 검색
    class HTTPRestfulUtilizerExtender3 extends HTTPRestfulUtilizer {
        String hashtag_title;
        public HTTPRestfulUtilizerExtender3(Context mContext, String url, String HTTPRestType, String hashtag_title) {
            this.hashtag_title = hashtag_title;
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
                progress = ProgressDialog.show(getmContext(), "일정올리기",
                        "일정을 올리는 중입니다.", true);
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
                    int count = getOutputJsonObject().getInt("count");
                    if(count ==0 ){ // 검색결과가 없다는 말이므로 해시태그 생성해줘야함.
                        String createHashtag_url = "http://119.81.176.245/hashtags/";
                        Bundle inputBundle_forCreateHashtag = new Bundle();
                        inputBundle_forCreateHashtag.putCharSequence("title",hashtag_title);
                        Log.d("hashtag title",hashtag_title);
                        HTTPRestfulUtilizerExtender4 a = new HTTPRestfulUtilizerExtender4(getmContext(),createHashtag_url,"POST",inputBundle_forCreateHashtag);
                        a.doExecution();
                    }else{  // 검색결과가 있다는 말. 아이디를 받아서 배열에 저장
                        Log.d("hashtag title_yesSearch",hashtag_title);
                        int id =  getOutputJsonObject().getJSONArray("results").getJSONObject(0).getInt("id");
                        Log.d("hashtag get id",id+"");
                        hashtags_id.add(id);
                        completed_num ++;
                        Log.d("completed_num",completed_num+"");
                        if( hashtags_title.length == completed_num){// 아이디를 다 얻었으면 요청
                            inputBundle_forRequest.putCharSequence("title",title.getText());
                            inputBundle_forRequest.putCharSequence("memo", memo.getText());
                            inputBundle_forRequest.putCharSequence("location",location.getText());
                            if(hashtags_id.size() !=0){
                                inputBundle_forRequest.putIntegerArrayList("hashtag",hashtags_id);
                            }
                            Calendar cal = Calendar.getInstance();
                            cal.set(YEAR_start, MONTH_start-1, DAY_start, HOUR_start, MINUTE_start);
                            Log.d("getTimeinInt", YEAR_start + " " + MONTH_start + " " + DAY_start + " " + HOUR_start + " " + MINUTE_start + "");
                            Log.d("getTimeinMillis", cal.getTimeInMillis() + "");
                            DateTimeFormatter dtf = new DateTimeFormatter(cal.getTimeInMillis());
                            inputBundle_forRequest.putCharSequence("start_time", dtf.getOutputString());
                            Log.d("getTimeinString",dtf.getOutputString());

                            cal.set(YEAR_end, MONTH_end-1, DAY_end, HOUR_end, MINUTE_end);
                            Log.d("getTimeinInt", YEAR_start + " " + MONTH_start + " " + DAY_start + " " + HOUR_start + " " + MINUTE_start + "");
                            Log.d("getTimeinMillis", cal.getTimeInMillis() + "");
                            dtf = new DateTimeFormatter(cal.getTimeInMillis());
                            inputBundle_forRequest.putCharSequence("end_time",dtf.getOutputString());
                            Log.d("getTimeinString",dtf.getOutputString());

                            String url = "http://119.81.176.245/schedules/";
                            HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(getmContext(),url,"POST",inputBundle_forRequest, ImageAbsolutePath);
                            a.doExecution();

                        }
                    }
                }catch(Exception e){
                }

            }
        }
    }

    // for 해시태그 생성
    class HTTPRestfulUtilizerExtender4 extends HTTPRestfulUtilizer {

        public HTTPRestfulUtilizerExtender4(Context mContext, String url, String HTTPRestType, Bundle inputBundle) {
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
            protected String doInBackground(String... strings) {
                String url = strings[0];
                String sHTTPRestType = strings[1];
                setOutputString(POST(url,getInputBundle()));

                return getOutputString();
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                try{
                    int id = getOutputJsonObject().getInt("id");
                    Log.d("hashtag title_search_id",id+"");
                    hashtags_id.add(id);
                    completed_num ++;
                    Log.d("completed_num",completed_num+"");
                    if( hashtags_title.length == completed_num){// 아이디를 다 얻었으면 요청
                        inputBundle_forRequest.putCharSequence("title",title.getText());
                        inputBundle_forRequest.putCharSequence("memo", memo.getText());
                        inputBundle_forRequest.putCharSequence("location",location.getText());
                        if(hashtags_id.size() !=0){
                            inputBundle_forRequest.putIntegerArrayList("hashtag",hashtags_id);
                        }
                        Calendar cal = Calendar.getInstance();
                        cal.set(YEAR_start, MONTH_start, DAY_start, HOUR_start, MINUTE_start);

                        Log.d("getTimeinInt", YEAR_start + " " + MONTH_start + " " + DAY_start + " " + HOUR_start + " " + MINUTE_start + "");
                        Log.d("getTimeinMillis", cal.getTimeInMillis() + "");

                        DateTimeFormatter dtf = new DateTimeFormatter(cal.getTimeInMillis());
                        inputBundle_forRequest.putCharSequence("start_time", dtf.getOutputString());
                        Log.d("getTimeinString", dtf.getOutputString());



                        cal.set(YEAR_end, MONTH_end, DAY_end, HOUR_end, MINUTE_end);
                        Log.d("getTimeinInt", YEAR_end + " " + MONTH_end + " " + DAY_end + " " + HOUR_end + " " + MINUTE_end + "");
                        Log.d("getTimeinMillis", cal.getTimeInMillis() + "");

                        dtf = new DateTimeFormatter(cal.getTimeInMillis());
                        inputBundle_forRequest.putCharSequence("end_time",dtf.getOutputString());
                        Log.d("getTimeinString", dtf.getOutputString());

                        String url = "http://119.81.176.245/schedules/";
                        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(getmContext(),url,"POST",inputBundle_forRequest, ImageAbsolutePath);
                        a.doExecution();

                    }

                }catch(Exception e){
                }

            }
        }
    }


    static public class F6_DatePickerFragment_start extends F6_DatePickerFragment{
        public int YEAR,MONTH,DAY;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            int year = YEAR_start;
            int month = MONTH_start-1;
            int day = DAY_start;

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);

        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            this.YEAR = year;
            this.MONTH = monthOfYear+1;
            this.DAY = dayOfMonth;
            YEAR_start = YEAR;
            MONTH_start = MONTH;
            DAY_start = DAY;
            date_start.setText(MONTH_start + "월" + DAY_start + "일");

        }
    }
    static public class F7_TimePickerFragment_start extends F7_TimePickerFragment{


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker

            int hour = HOUR_start;
            int minute = MINUTE_start;

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,true);
        }


        @Override
        //onTimeSet() callback method
        public void onTimeSet(TimePicker view, int hourOfDay, int minute){

            this.HOUR = hourOfDay;
            this.MINUTE = minute;
            HOUR_start =HOUR;
            MINUTE_start = MINUTE;
            time_start.setText(String.format("%02d", HOUR_start)+":" +String.format("%02d", MINUTE_start));
        }
    }
    static public class F6_DatePickerFragment_end extends F6_DatePickerFragment{
        public int YEAR,MONTH,DAY;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            int year = YEAR_end;
            int month = MONTH_end-1;
            int day = DAY_end;

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);

        }
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            this.YEAR = year;
            this.MONTH = monthOfYear+1;
            this.DAY = dayOfMonth;
            YEAR_end = YEAR;
            MONTH_end = MONTH;
            DAY_end = DAY;
            date_end.setText(MONTH_end + "월" + DAY_end + "일");

        }
    }
    static public class F7_TimePickerFragment_end extends F7_TimePickerFragment{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker

            int hour = HOUR_end;
            int minute = MINUTE_end;

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        @Override
        //onTimeSet() callback method
        public void onTimeSet(TimePicker view, int hourOfDay, int minute){

            this.HOUR = hourOfDay;
            this.MINUTE = minute;
            HOUR_end =HOUR;
            MINUTE_end = MINUTE;
            time_end.setText(String.format("%02d", HOUR_end)+":" +String.format("%02d", MINUTE_end));


        }
    }

}
