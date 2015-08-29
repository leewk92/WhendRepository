package net.whend.soodal.whend.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.util.DateTimeFormatter;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class A4_MakeScheduleActivity extends AppCompatActivity {

    private int TAKE_FROM_CAMERA = 1;
    private int TAKE_FROM_GALLERY = 2;

    private int REQ_CODE_SELECT_IMAGE = 100;
    private Uri mImageCaptureUri;
    private String ImageAbsolutePath;
    private String[] hashtags_title;
    private ArrayList<Integer> hashtags_id = new ArrayList<Integer>();
    private ImageView schedule_photo;
    private ImageView schedule_photo_add;
    static int YEAR_start,MONTH_start,DAY_start,HOUR_start,MINUTE_start;
    static int YEAR_end,MONTH_end,DAY_end,HOUR_end,MINUTE_end;
    static TextView date_start,time_start,date_end,time_end;
    int completed_num=0;
    static boolean all_day_boolean=false;
    boolean cancelable = true;
    int all_day_int;
    Boolean sAllday;
    EditText title,location,memo;
    CheckBox all_day;
    String sStartDate, sEndDate, sContent, sLocation, sStartTime,sEndTime,sMemo;
    long sDatetime_start,sDatetime_end;
    Bundle inputBundle_forRequest = new Bundle();
    ProgressDialog progress ;
    private boolean canUpdate= true;

    public void onBackPressed(){
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        completed_num=0;
        View.OnClickListener photo_add;
        ImageAbsolutePath=null;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.a4_make_schedule_layout);


        AppPrefs appPrefs = new AppPrefs(this);

        all_day = (CheckBox) findViewById(R.id.allday_checkbox);
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
        sAllday = intent.getBooleanExtra("allday", false);
        sMemo = intent.getStringExtra("memo");
        Log.d("intent allday",sAllday+"");
        if(sAllday == true){
            all_day.setChecked(true);
            all_day_boolean=true;
            View time_start_visiblelayout = findViewById(R.id.time_start_visiblelayout);
            time_start_visiblelayout.setVisibility(View.INVISIBLE);
            View time_end_visiblelayout = findViewById(R.id.time_end_visiblelayout);
            time_end_visiblelayout.setVisibility(View.INVISIBLE);

        }else{
            all_day.setChecked(false);
            all_day_boolean=false;
        }

        if(sDatetime_start != 0) {
            DateTimeFormatter dtf = new DateTimeFormatter(sDatetime_start);
            date_start.setText(dtf.getDate());
            time_start.setText(dtf.getTime());
            Calendar utc_calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            utc_calendar.setTimeInMillis(dtf.getCalendar().getTimeInMillis());
            Log.d("calendar_utc", utc_calendar.getTimeZone().getID().toString());
            //            dtf.getCalendar().setTimeZone(TimeZone.getTimeZone("UTC"));
//            YEAR_start = dtf.getCalendar().get(Calendar.YEAR);
//            MONTH_start = dtf.getCalendar().get(Calendar.MONTH)+1;
//            DAY_start = dtf.getCalendar().get(Calendar.DAY_OF_MONTH);
//            HOUR_start = dtf.getCalendar().get(Calendar.HOUR_OF_DAY);
//            MINUTE_start = dtf.getCalendar().get(Calendar.MINUTE);
            YEAR_start = utc_calendar.get(Calendar.YEAR);
            MONTH_start = utc_calendar.get(Calendar.MONTH)+1;
            DAY_start = utc_calendar.get(Calendar.DAY_OF_MONTH);
            HOUR_start = utc_calendar.get(Calendar.HOUR_OF_DAY);
            MINUTE_start = utc_calendar.get(Calendar.MINUTE);
        }else{
            // now
            DateTimeFormatter dtf = new DateTimeFormatter();
            date_start.setText(dtf.getDate());
            time_start.setText(dtf.getTime());
            Calendar utc_calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            utc_calendar.setTimeInMillis(dtf.getCalendar().getTimeInMillis());
            Log.d("calendar_utc",utc_calendar.getTime().toString());
//            dtf.getCalendar().setTimeZone(TimeZone.getTimeZone("UTC"));

//            YEAR_start = dtf.getCalendar().get(Calendar.YEAR);
//            MONTH_start = dtf.getCalendar().get(Calendar.MONTH)+1;
//            DAY_start = dtf.getCalendar().get(Calendar.DAY_OF_MONTH);
//            HOUR_start = dtf.getCalendar().get(Calendar.HOUR_OF_DAY);
//            MINUTE_start = dtf.getCalendar().get(Calendar.MINUTE);

            YEAR_start = utc_calendar.get(Calendar.YEAR);
            MONTH_start = utc_calendar.get(Calendar.MONTH)+1;
            DAY_start = utc_calendar.get(Calendar.DAY_OF_MONTH);
            HOUR_start = utc_calendar.get(Calendar.HOUR_OF_DAY);
            MINUTE_start = utc_calendar.get(Calendar.MINUTE);
        }
        if(sDatetime_end != 0) {
            DateTimeFormatter dtf = new DateTimeFormatter(sDatetime_end);

            Calendar utc_calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            utc_calendar.setTimeInMillis(dtf.getCalendar().getTimeInMillis());
            Log.d("calendar_utc", utc_calendar.getTimeZone().getID().toString());

            //dtf.getCalendar().setTimeZone(TimeZone.getTimeZone("UTC"));

            YEAR_end = utc_calendar.get(Calendar.YEAR);
            MONTH_end = utc_calendar.get(Calendar.MONTH)+1;
            HOUR_end = utc_calendar.get(Calendar.HOUR_OF_DAY);
            MINUTE_end = utc_calendar.get(Calendar.MINUTE);
            if(all_day_boolean) {
                DAY_end = utc_calendar.get(Calendar.DAY_OF_MONTH) - 1;
                Log.d("calendar_day_end",DAY_end+"");
            }else {
                DAY_end = utc_calendar.get(Calendar.DAY_OF_MONTH);
            }
            Calendar tmp_calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            tmp_calendar.set(YEAR_end,MONTH_end-1,DAY_end,HOUR_end,MINUTE_end,0);
            Log.d("calendar_endtime",tmp_calendar.getTime().toString());
            DateTimeFormatter dtf2 = new DateTimeFormatter(tmp_calendar.getTimeInMillis());
            date_end.setText(dtf2.getDate());Log.d("calendar_dtf2date",dtf2.getDate());
            time_end.setText(dtf2.getTime());

        }else{
            // now
            DateTimeFormatter dtf = new DateTimeFormatter();

            Calendar utc_calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            utc_calendar.setTimeInMillis(dtf.getCalendar().getTimeInMillis());
            Log.d("calendar_utc", utc_calendar.getTimeZone().getID().toString());

            //dtf.getCalendar().setTimeZone(TimeZone.getTimeZone("UTC"));

            YEAR_end = utc_calendar.get(Calendar.YEAR);
            MONTH_end = utc_calendar.get(Calendar.MONTH)+1;
            HOUR_end = utc_calendar.get(Calendar.HOUR_OF_DAY);
            MINUTE_end = utc_calendar.get(Calendar.MINUTE);
            if(all_day_boolean) {
                DAY_end = utc_calendar.get(Calendar.DAY_OF_MONTH) - 1;
            }else {
                DAY_end = utc_calendar.get(Calendar.DAY_OF_MONTH);
            }
            Calendar tmp_calendar = Calendar.getInstance();
            tmp_calendar.set(YEAR_end,MONTH_end-1,DAY_end,HOUR_end,MINUTE_end,0);
            dtf = new DateTimeFormatter(tmp_calendar.getTimeInMillis());
            date_end.setText(dtf.getDate());
            time_end.setText(dtf.getTime());
        }

//        if(sStartDate != null)
//            date_start.setText(sStartDate);
//        if(sEndDate != null)
//            date_end.setText(sEndDate);
//        if(sStartTime != null)
//            time_start.setText(sStartTime);
//        if(sEndTime != null)
//            time_end.setText(sEndTime);
        if(sLocation != null)
            location.setText(sLocation);
        if(sContent != null)
            title.setText(sContent);
        if(sMemo != null)
            memo.setText(sMemo);
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

        progress = new ProgressDialog(this);

        // 클릭리스너


        all_day.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    View time_start_visiblelayout = findViewById(R.id.time_start_visiblelayout);
                    time_start_visiblelayout.setVisibility(View.INVISIBLE);
                    View time_end_visiblelayout = findViewById(R.id.time_end_visiblelayout);
                    time_end_visiblelayout.setVisibility(View.INVISIBLE);

                    Calendar utc_calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    Calendar local_calendar = Calendar.getInstance(TimeZone.getDefault());
                    utc_calendar.set(YEAR_start,MONTH_start,DAY_start,0,0,0);
                    local_calendar.setTimeInMillis(utc_calendar.getTimeInMillis());
                    all_day_boolean = true;
                    YEAR_start = utc_calendar.get(utc_calendar.YEAR);
                    MONTH_start = utc_calendar.get(utc_calendar.MONTH);
                    DAY_start = utc_calendar.get(utc_calendar.DAY_OF_MONTH); // 이것도 왜이런지 모르겠음

                    HOUR_start = utc_calendar.get(local_calendar.HOUR_OF_DAY);
                    MINUTE_start = utc_calendar.get(local_calendar.MINUTE);
                    Log.d("calendar_HOUR_START",HOUR_start+"");
                    HOUR_end = utc_calendar.get(local_calendar.HOUR_OF_DAY);
                    MINUTE_end = utc_calendar.get(local_calendar.MINUTE);

                }else {
                    View time_start_visiblelayout = findViewById(R.id.time_start_visiblelayout);
                    time_start_visiblelayout.setVisibility(View.VISIBLE);
                    View time_end_visiblelayout = findViewById(R.id.time_end_visiblelayout);
                    time_end_visiblelayout.setVisibility(View.VISIBLE);

                    Calendar utc_calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    Calendar local_calendar = Calendar.getInstance(TimeZone.getDefault());
                    utc_calendar.set(YEAR_start, MONTH_start,DAY_start,0,0,0);
                    local_calendar.setTimeInMillis(utc_calendar.getTimeInMillis());

                    all_day_boolean = false;

                    DateTimeFormatter dtf = new DateTimeFormatter(utc_calendar.getTimeInMillis());
                    HOUR_start = utc_calendar.get(Calendar.HOUR_OF_DAY);
                    MINUTE_start = utc_calendar.get(Calendar.MINUTE);
                    Log.d("calendar_HOUR_start",HOUR_start+"");
                    HOUR_end = utc_calendar.get(Calendar.HOUR_OF_DAY);
                    MINUTE_end = utc_calendar.get(Calendar.MINUTE);
                    time_start.setText(dtf.getTime());
                    time_end.setText(dtf.getTime());
                }
            }
        });
        photo_add = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"카메라로 찍기", "갤러리에서 불러오기"};

                AlertDialog.Builder builder = new AlertDialog.Builder(A4_MakeScheduleActivity.this, R.style.AppCompatAlertDialogStyle);
                        // 각 항목을 설정하고 클릭했을 때 동작을 지정함

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) { //카메라에서 찍기


                            Intent intent = new Intent();

                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            //intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                            //intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(intent, TAKE_FROM_CAMERA);

                            // 카메라 호출
                            /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());*/

                            // 이미지 잘라내기 위한 크기

                            /*
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
                            intent.putExtra("outputY",  100);*/



                        } else if (item == 1) { //갤러리에서 가져오기

                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                            intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, TAKE_FROM_GALLERY);

                            // 잘라내기 셋팅
                            /*
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
                            */

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

                Bitmap photo = null;
                try {
                    photo = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    //schedule_photo.setImageBitmap(photo);
                    Picasso.with(getApplicationContext()).load(mImageCaptureUri).fit().centerCrop().into(schedule_photo);
                    Cursor c = getContentResolver().query(Uri.parse(mImageCaptureUri.toString()),null,null,null,null);
                    c.moveToNext();
                    ImageAbsolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
                    c.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else if (requestCode == TAKE_FROM_CAMERA) {

                Bundle extras = data.getExtras();
                mImageCaptureUri = data.getData(); // Get data from selected photo

                    Picasso.with(getApplicationContext()).load(mImageCaptureUri).fit().centerCrop().into(schedule_photo);
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(mImageCaptureUri, filePathColumn, null, null, null);

                    if(cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        ImageAbsolutePath = cursor.getString(columnIndex);
                    }
                    Log.d("Path",ImageAbsolutePath);

                    cursor.close();

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
            //Log.d("make_memo",memo.getText().toString());
            if(memo.getText().toString().contentEquals("")){
                //Toast.makeText(this,"태그를 하나 이상 입력해주세요!",Toast.LENGTH_SHORT).show();
            }
            parseMemo(memo.getText() + "");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void parseMemo(String memo_text){
        memo_text = memo_text.replaceAll("\n"," ");
        memo_text = memo_text.replaceAll("#"," #");
        String tmpArray[] = memo_text.split("#");
        if(tmpArray != null) {

            hashtags_title = new String[tmpArray.length - 1];

            for (int i = 1; i < tmpArray.length; i++) {
                hashtags_title[i - 1] = tmpArray[i].split(" ")[0];
                Log.d("hashtags_title_array",hashtags_title[i-1]);
            }
            String url_getHashtagsId = "http://119.81.176.245/hashtags/getid/";
            HTTPRestfulUtilizerExtender_getHashtagsId a = new HTTPRestfulUtilizerExtender_getHashtagsId(this,url_getHashtagsId,"POST",hashtags_title);
            a.doExecution();
           // searchHashtags();
        }
        if (tmpArray.length==1){      //hash tag가 없을 때
            //Toast.makeText(this,"태그를 하나 이상 입력해주세요!",Toast.LENGTH_SHORT).show();
            /*
            Log.d("nono","nono");

            if(all_day_boolean){
                all_day_int=1;
            }else all_day_int=0;

            inputBundle_forRequest.putCharSequence("title",title.getText());
            inputBundle_forRequest.putCharSequence("memo", memo.getText());
            inputBundle_forRequest.putCharSequence("location",location.getText());
            inputBundle_forRequest.putInt("all_day", all_day_int);
            Log.d("all_day",all_day_int+"");
            Log.d("updateLocation", location.getText() + "");
            // if(hashtags_id.size() !=0){
         //       inputBundle_forRequest.putIntegerArrayList("hashtag",hashtags_id);
            }//
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            if(all_day_boolean==false)
                cal.set(YEAR_start, MONTH_start-1, DAY_start, HOUR_start, MINUTE_start,0);
            else
                cal.set(YEAR_start, MONTH_start - 1, DAY_start, HOUR_start, MINUTE_start,0);

            Log.d("getTimeinInt", YEAR_start + " " + MONTH_start + " " + DAY_start + " " + HOUR_start + " " + MINUTE_start + "");

            Log.d("getTimeinMillis", cal.getTimeInMillis() + "");

            cal.setTimeInMillis((cal.getTimeInMillis() / 1000) * 1000);
            DateTimeFormatter dtf = new DateTimeFormatter(cal.getTimeInMillis());
            inputBundle_forRequest.putCharSequence("start_time", dtf.getOutputString());

            Log.d("getTimeinString",dtf.getOutputString());
            if(all_day_boolean == false)
                cal.set(YEAR_end, MONTH_end-1, DAY_end, HOUR_end, MINUTE_end,0);
            else
                cal.set(YEAR_end, MONTH_end-1, DAY_end+1, HOUR_end, MINUTE_end,0);
            cal.setTimeInMillis((cal.getTimeInMillis()/1000)*1000);
            dtf = new DateTimeFormatter(cal.getTimeInMillis());
            inputBundle_forRequest.putCharSequence("end_time",dtf.getOutputString());
            String url = "http://119.81.176.245/schedules/";
            HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(this,url,"POST",inputBundle_forRequest, ImageAbsolutePath);
            a.doExecution();*/
        }
    }
    public void searchHashtags(){
        cancelable=true;
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

                boolean error = false;

                try{
                    String memo = getOutputJsonObject().getString("memo");
                    Log.d("memo",memo);
                    if(memo.contentEquals("[\"This field may not be blank.\"]")){
                        progress.dismiss();
                        Toast.makeText(getmContext(),"메모를 입력하세요.",Toast.LENGTH_SHORT).show();
                        error = true;
                    }
                    else{
                        progress.dismiss();
                        finish();
                        cancelable=false;
                        Toast.makeText(getmContext(),"업로드합니다!",Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){}

                try{
                    String title = getOutputJsonObject().getString("title");

                    if(title.contentEquals("[\"This field may not be blank.\"]")){
                        progress.dismiss();
                        Toast.makeText(getmContext(),"제목을 입력해주세요.",Toast.LENGTH_SHORT).show();
                        error = true;
                    }else{
                        progress.dismiss();
                        finish();
                        cancelable=false;
                        Toast.makeText(getmContext(),"업로드합니다!",Toast.LENGTH_SHORT).show();
                    }

                }catch(Exception e){}

                try{
                    String hashtag = getOutputJsonObject().getString("hashtag");
                    if(hashtag.contentEquals("[\"This field is required.\"]")){
                        progress.dismiss();
                        Toast.makeText(getmContext(),"메모에 해시태그를 하나 이상 입력해주세요.",Toast.LENGTH_SHORT).show();
                        error = true;
                    }else{
                        progress.dismiss();
                        finish();
                        cancelable=false;
                        Toast.makeText(getmContext(),"업로드합니다!",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    if (!error){
                        progress.dismiss();
                        finish();
                        cancelable=false;
                        Toast.makeText(getmContext(),"업로드합니다! ",Toast.LENGTH_SHORT).show();
                    }
                }

                if (!error){
                    progress.dismiss();
                    finish();
                    cancelable=false;
                    Toast.makeText(getmContext(),"업로드합니다! ",Toast.LENGTH_SHORT).show();
                }



/*
                try{    // 올린 일정 받아보기 자동 설정
                    int id = getOutputJsonObject().getInt("id");
                    String url = "http://119.81.176.245/schedules/"+id+"/follow/";
                    HTTPRestfulUtilizerExtender2 b = new HTTPRestfulUtilizerExtender2(getmContext(), url,"PUT");
                    b.doExecution();
                }catch(Exception e){

                }
*/

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
        String hashtag_title_inner;
        public void setHashtag_title_inner(String hashtag_title_inner){
            this.hashtag_title_inner = hashtag_title_inner;
        }
        public HTTPRestfulUtilizerExtender3(Context mContext, String url, String HTTPRestType, String hashtag_title) {
            this.hashtag_title_inner = hashtag_title;
            setmContext(mContext);
            setUrl(url);
            setHTTPRestType(HTTPRestType);
            task = new HttpAsyncTaskExtenders();
            Log.d("HTTP Constructor url", url);
            // new HttpAsyncTask().execute(url,HTTPRestType);
        }
        public HTTPRestfulUtilizerExtender3() {

        }


        @Override
        public void doExecution(){
            task.execute(getUrl(), getHTTPRestType());
        }
        class HttpAsyncTaskExtenders extends HTTPRestfulUtilizer.HttpAsyncTask{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                if(progress.isShowing()==false)
                    progress = ProgressDialog.show(getmContext(), "",
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
                        inputBundle_forCreateHashtag.putCharSequence("title", hashtag_title_inner);
                        Log.d("hashtag title",hashtag_title_inner);
                        HTTPRestfulUtilizerExtender4 a = new HTTPRestfulUtilizerExtender4(getmContext(),createHashtag_url,"POST",inputBundle_forCreateHashtag);

                        Log.d("hashtag title_bundle", inputBundle_forCreateHashtag.get("title").toString());
                        a.doExecution();

                    }else{  // 검색결과가 있다는 말. 아이디를 받아서 배열에 저장
                        Log.d("hashtag title_yesSearch",hashtag_title_inner);
                        int id =  getOutputJsonObject().getJSONArray("results").getJSONObject(0).getInt("id");
                        Log.d("hashtag get id",id+"");
                        hashtags_id.add(id);
                        completed_num ++;
                        Log.d("completed_num",completed_num+"");
                        if( hashtags_title.length == completed_num){// 아이디를 다 얻었으면 요청


                            if(all_day_boolean){
                                all_day_int=1;
                            }else all_day_int=0;


                            inputBundle_forRequest.putCharSequence("title",title.getText());
                            inputBundle_forRequest.putCharSequence("memo", memo.getText());
                            inputBundle_forRequest.putCharSequence("location",location.getText());
                            inputBundle_forRequest.putCharSequence("all_day",all_day_int+"");
                            if(hashtags_id.size() !=0){
                                inputBundle_forRequest.putIntegerArrayList("hashtag",hashtags_id);
                            }
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(0);
                            if(all_day_boolean==false)
                                cal.set(YEAR_start, MONTH_start-1, DAY_start, HOUR_start, MINUTE_start,0);
                            else
                                cal.set(YEAR_start, MONTH_start - 1, DAY_start, HOUR_start, MINUTE_start, 0);
                            Log.d("getTimeinInt", YEAR_start + " " + MONTH_start + " " + DAY_start + " " + HOUR_start + " " + MINUTE_start + "");
                            Log.d("getTimeinMillis", cal.getTimeInMillis() + "");
                            cal.setTimeInMillis((cal.getTimeInMillis() / 1000) * 1000);
                            DateTimeFormatter dtf = new DateTimeFormatter(cal.getTimeInMillis());
                            inputBundle_forRequest.putCharSequence("start_time", dtf.getOutputString());
                            Log.d("getTimeinString", dtf.getOutputString());


                            if(all_day_boolean==false)
                                cal.set(YEAR_end, MONTH_end-1, DAY_end, HOUR_end, MINUTE_end,0);
                            else
                                cal.set(YEAR_end, MONTH_end-1, DAY_end+1, HOUR_end, MINUTE_end,0);

                            Log.d("getTimeinInt", YEAR_start + " " + MONTH_start + " " + DAY_start + " " + HOUR_start + " " + MINUTE_start + "");
                            Log.d("getTimeinMillis", cal.getTimeInMillis() + "");
                            cal.setTimeInMillis((cal.getTimeInMillis() / 1000) * 1000);
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
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
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
                    int id = getOutputJsonObject().getInt("id");
                    Log.d("hashtag title_search_id",id+"");
                    hashtags_id.add(id);
                    completed_num ++;
                    Log.d("completed_num",completed_num+"");
                    if( hashtags_title.length == completed_num){// 아이디를 다 얻었으면 요청


                        if(all_day_boolean){
                            all_day_int=1;
                        }else all_day_int=0;



                        inputBundle_forRequest.putCharSequence("title",title.getText());
                        inputBundle_forRequest.putCharSequence("memo", memo.getText());
                        inputBundle_forRequest.putCharSequence("location",location.getText());
                        inputBundle_forRequest.putCharSequence("all_day",all_day_int+"");
                        if(hashtags_id.size() !=0){
                            inputBundle_forRequest.putIntegerArrayList("hashtag",hashtags_id);
                        }
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeZone(TimeZone.getDefault());
                        cal.setTimeInMillis(0);
                        if(all_day_boolean==false)
                            cal.set(YEAR_start, MONTH_start-1, DAY_start, HOUR_start, MINUTE_start,0);
                        else
                            cal.set(YEAR_start, MONTH_start - 1, DAY_start, HOUR_start, MINUTE_start,0);

                        Log.d("getTimeinInt", YEAR_start + " " + MONTH_start + " " + DAY_start + " " + HOUR_start + " " + MINUTE_start + "");
                        Log.d("getTimeinMillis", cal.getTimeInMillis() + "");
                        cal.setTimeInMillis((cal.getTimeInMillis() / 1000) * 1000);
                        DateTimeFormatter dtf = new DateTimeFormatter(cal.getTimeInMillis());
                        inputBundle_forRequest.putCharSequence("start_time", dtf.getOutputString());
                        Log.d("getTimeinString", dtf.getOutputString());


                        if(all_day_boolean==false)
                            cal.set(YEAR_end, MONTH_end - 1, DAY_end, HOUR_end, MINUTE_end,0);
                        else
                            cal.set(YEAR_end, MONTH_end -1 , DAY_end +1, HOUR_end, MINUTE_end,0);

                        Log.d("getTimeinInt", YEAR_end + " " + MONTH_end + " " + DAY_end + " " + HOUR_end + " " + MINUTE_end + "");
                        Log.d("getTimeinMillis", cal.getTimeInMillis() + "");
                        cal.setTimeInMillis((cal.getTimeInMillis()/1000)*1000);
                        dtf = new DateTimeFormatter(cal.getTimeInMillis());
                        inputBundle_forRequest.putCharSequence("end_time",dtf.getOutputString());
                        Log.d("getTimeinString", dtf.getOutputString());

                        String url = "http://119.81.176.245/schedules/";
                        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(getmContext(),url,"POST",inputBundle_forRequest, ImageAbsolutePath);
                        a.doExecution();

                    }

                }catch(Exception e){
                }

                try{// 처음부터 다시시작
                    String title = getOutputJsonObject().getString("title");
                    if(title.contentEquals("[\"This field must be unique.\"]") && cancelable){
                        cancelable=false;
                        completed_num=0;
                        hashtags_id.clear();
                        searchHashtags();
                    }

                }catch (Exception e){

                }

            }
        }
    }


    static public class F6_DatePickerFragment_start extends F6_DatePickerFragment{
        public int YEAR,MONTH,DAY;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            Calendar tmpCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            tmpCalendar.set(YEAR_start, MONTH_start-1, DAY_start, HOUR_start, MINUTE_start);
            Log.d("tmp_calendar",tmpCalendar.getTime().toString());
            Calendar tmpCalendar2 = Calendar.getInstance(TimeZone.getDefault());
            tmpCalendar2.setTimeInMillis(tmpCalendar.getTimeInMillis());
            int year = tmpCalendar2.get(tmpCalendar2.YEAR);
            int month = tmpCalendar2.get(tmpCalendar2.MONTH);
            int day = tmpCalendar2.get(tmpCalendar2.DAY_OF_MONTH);

 //           int year = YEAR_start;
 //           int month = MONTH_start-1;
 //           int day = DAY_start;


            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);

        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            this.YEAR = year;
            this.MONTH = monthOfYear+1;
            this.DAY = dayOfMonth;

            Calendar tmpCalendar = Calendar.getInstance(TimeZone.getDefault());
            tmpCalendar.set(this.YEAR, this.MONTH, this.DAY, HOUR_start, MINUTE_start);
            Calendar tmpCalendar2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            tmpCalendar2.setTimeInMillis(tmpCalendar.getTimeInMillis());
            YEAR_start = tmpCalendar2.get(tmpCalendar2.YEAR);
            MONTH_start = tmpCalendar2.get(tmpCalendar2.MONTH);
            if(all_day_boolean)
                DAY_start = tmpCalendar2.get(tmpCalendar2.DAY_OF_MONTH)+1;//이건 왜이런지 도저히 모르겠지만 이렇게 하면 잘됨.. ;;
            else
                DAY_start = tmpCalendar2.get(tmpCalendar2.DAY_OF_MONTH)+1;//이건 왜이런지 도저히 모르겠지만 이렇게 하면 잘됨.. ;;

            Log.d("calendar_set",YEAR_start + " / " + MONTH_start + " / " + DAY_start );
//            YEAR_start = YEAR;
//            MONTH_start = MONTH;
//            DAY_start = DAY;

            date_start.setText(this.MONTH + "월" + this.DAY+ "일");
            // 끝 시간도 자동설정
            date_end.setText(this.MONTH + "월" + this.DAY + "일");
            YEAR_end = YEAR_start;
            MONTH_end = MONTH_start;
            DAY_end = DAY_start;
        }
    }
    static public class F7_TimePickerFragment_start extends F7_TimePickerFragment{


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker

            Calendar tmpCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            tmpCalendar.set(YEAR_start, MONTH_start, DAY_start, HOUR_start, MINUTE_start);
            Calendar tmpCalendar2 = Calendar.getInstance(TimeZone.getDefault());
            tmpCalendar2.setTimeInMillis(tmpCalendar.getTimeInMillis());
            int hour = tmpCalendar2.get(tmpCalendar2.HOUR_OF_DAY);
            int minute = tmpCalendar2.get(tmpCalendar2.MINUTE);

           // int hour = HOUR_start;
           // int minute = MINUTE_start;

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,true);
        }


        @Override
        //onTimeSet() callback method
        public void onTimeSet(TimePicker view, int hourOfDay, int minute){

            this.HOUR = hourOfDay;
            this.MINUTE = minute;

            Calendar tmpCalendar = Calendar.getInstance(TimeZone.getDefault());
            tmpCalendar.set(YEAR_start, MONTH_start, DAY_start, this.HOUR, this.MINUTE);
            Calendar tmpCalendar2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            tmpCalendar2.setTimeInMillis(tmpCalendar.getTimeInMillis());
            HOUR_start = tmpCalendar2.get(tmpCalendar2.HOUR_OF_DAY);
            MINUTE_start = tmpCalendar2.get(tmpCalendar2.MINUTE);

//            time_start.setText(String.format("%02d", HOUR_start)+":" +String.format("%02d", MINUTE_start));
            // 끝시간 자동설정
//            time_end.setText(String.format("%02d", HOUR_start+1)+":" +String.format("%02d", MINUTE_start));

            time_start.setText(String.format("%02d", this.HOUR)+":" +String.format("%02d", this.MINUTE));
            // 끝시간 자동설정
            time_end.setText(String.format("%02d", this.HOUR+1)+":" +String.format("%02d", this.MINUTE));

            HOUR_end = HOUR_start+1;
            MINUTE_end = MINUTE_start;
        }
    }
    static public class F6_DatePickerFragment_end extends F6_DatePickerFragment{
        public int YEAR,MONTH,DAY;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            Calendar tmpCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            tmpCalendar.set(YEAR_end, MONTH_end-1, DAY_end, HOUR_end, MINUTE_end);
            Calendar tmpCalendar2 = Calendar.getInstance(TimeZone.getDefault());
            tmpCalendar2.setTimeInMillis(tmpCalendar.getTimeInMillis());
            int year = tmpCalendar2.get(tmpCalendar2.YEAR);
            int month = tmpCalendar2.get(tmpCalendar2.MONTH);
            int day = tmpCalendar2.get(tmpCalendar2.DAY_OF_MONTH);

//            int year = YEAR_end;
//            int month = MONTH_end-1;
//            int day = DAY_end;

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);

        }
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            this.YEAR = year;
            this.MONTH = monthOfYear+1;
            this.DAY = dayOfMonth;
       //     YEAR_end = YEAR;
       //     MONTH_end = MONTH;
       //     DAY_end = DAY;
        //    date_end.setText(MONTH_end + "월" + DAY_end + "일");

            Calendar tmpCalendar = Calendar.getInstance(TimeZone.getDefault());
            tmpCalendar.set(this.YEAR, this.MONTH, this.DAY, HOUR_end, MINUTE_end);
            Calendar tmpCalendar2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            tmpCalendar2.setTimeInMillis(tmpCalendar.getTimeInMillis());
            YEAR_end = tmpCalendar2.get(tmpCalendar2.YEAR);
            MONTH_end = tmpCalendar2.get(tmpCalendar2.MONTH);
            if(all_day_boolean)
                DAY_end = tmpCalendar2.get(tmpCalendar2.DAY_OF_MONTH)+1;
            else
                DAY_end = tmpCalendar2.get(tmpCalendar2.DAY_OF_MONTH)+1;

            date_end.setText(this.MONTH + "월" + this.DAY + "일");

        }
    }
    static public class F7_TimePickerFragment_end extends F7_TimePickerFragment{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker

            Calendar tmpCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            tmpCalendar.set(YEAR_end, MONTH_end, DAY_end, HOUR_end, MINUTE_end);
            Calendar tmpCalendar2 = Calendar.getInstance(TimeZone.getDefault());
            tmpCalendar2.setTimeInMillis(tmpCalendar.getTimeInMillis());
            int hour = tmpCalendar2.get(tmpCalendar2.HOUR_OF_DAY);
            int minute = tmpCalendar2.get(tmpCalendar2.MINUTE);

            //int hour = HOUR_end;
            //int minute = MINUTE_end;
            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute, true);

        }

        @Override
        //onTimeSet() callback method
        public void onTimeSet(TimePicker view, int hourOfDay, int minute){

            this.HOUR = hourOfDay;
            this.MINUTE = minute;

            Calendar tmpCalendar = Calendar.getInstance(TimeZone.getDefault());
            tmpCalendar.set(YEAR_end, MONTH_end, DAY_end, this.HOUR, this.MINUTE);
            Calendar tmpCalendar2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            tmpCalendar2.setTimeInMillis(tmpCalendar.getTimeInMillis());
            HOUR_end = tmpCalendar2.get(tmpCalendar2.HOUR_OF_DAY);
            MINUTE_end = tmpCalendar2.get(tmpCalendar2.MINUTE);

            time_end.setText(String.format("%02d", this.HOUR)+":" +String.format("%02d", this.MINUTE));

        }
    }



    // for 해시태그 검색
    class HTTPRestfulUtilizerExtender_getHashtagsId extends HTTPRestfulUtilizer {
        String[] hashtag_title_inner;

        public HTTPRestfulUtilizerExtender_getHashtagsId(Context mContext, String url, String HTTPRestType, String[] hashtags_title) {
            this.hashtag_title_inner = hashtags_title.clone();
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


        public String POST_hashtag(String url, JSONArray tmp){
            InputStream inputStream = null;
            String result = "";
            try {

                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                // 2. make POST request to the given URL
                HttpPost httpPost = new HttpPost(url);

                String json = "";

                json =tmp.toString();
                          Log.d("hashjson", json);
                // ** Alternative way to convert Person object to JSON string usin Jackson Lib
                // ObjectMapper mapper = new ObjectMapper();
                // json = mapper.writeValueAsString(person);

                // 5. set json to StringEntity

                StringEntity se = new StringEntity(json,"UTF-8");


                //MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();

                //multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                // multipartEntity.addBinaryBody("someName", file, ContentType.create("image/jpeg"), file.getName());
                //multipartEntity.addPart("content", json);
                //multipartEntity.addTextBody("content",json);
                // 6. set httpPost Entity
                httpPost.setEntity(se);
                //httpPost.setEntity(multipartEntity.build());

                // 7. Set some headers to inform server about the type of the content


                httpPost.setHeader("Accept", "application/json;charset=utf-8");
                httpPost.setHeader("Content-type", "application/json");
                AppPrefs appPrefs = new AppPrefs(getmContext());
                String token = appPrefs.getToken();
                if( token != ""){
                    httpPost.setHeader("Authorization","Token "+token);
                }
                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPost);

                // se.consumeContent();

                // 9. receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                // 10. convert inputstream to string
                if(inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    //              Log.d("HTTP POST ResultStream", result);
                }else {
                    result = "Did not work!";
                    Log.d("HTTP POST ResultStream", result);
                }
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }

            // 11. return result
            this.outputString = result;
            try {
                outputJsonObject = new JSONObject(outputString);
            }catch (Exception e){
                outputJsonObject = new JSONObject();
            }

            try {
                outputJsonArray = new JSONArray(outputString);

            }catch (Exception e){
                outputJsonArray = new JSONArray();
            }

            return result;
        }


        class HttpAsyncTaskExtenders extends HTTPRestfulUtilizer.HttpAsyncTask{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                canUpdate = true;
                if(progress.isShowing()==false)
                    progress = ProgressDialog.show(getmContext(), "",
                            "일정을 올리는 중입니다.", true);

            }

            @Override
            protected String doInBackground(String... strings) {
                String url = strings[0];
                String sHTTPRestType = strings[1];
                JSONArray tmp = new JSONArray();

                for(int i=0; i<hashtag_title_inner.length; i++){
                    JSONObject tj = new JSONObject();
                    try{
                        tj.put("title",hashtag_title_inner[i]);
                        tmp.put(tj);
                    }catch(Exception e){}
                }

                setOutputString(POST_hashtag(url,tmp));

                return getOutputString();
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);


                try{

                    JSONArray outputJsonArray = getOutputJsonArray();
                    for(int i=0; i<outputJsonArray.length(); i++){
                        JSONObject tmp = outputJsonArray.getJSONObject(i);
                        hashtags_id.add(tmp.getInt("id"));
                    }


                    if(all_day_boolean){
                        all_day_int=1;
                    }else all_day_int=0;

                    inputBundle_forRequest.putCharSequence("title",title.getText());
                    inputBundle_forRequest.putCharSequence("memo", memo.getText());
                    inputBundle_forRequest.putCharSequence("location",location.getText());
                    inputBundle_forRequest.putCharSequence("all_day", all_day_int + "");
                    if(hashtags_id.size() !=0){
                        inputBundle_forRequest.putIntegerArrayList("hashtag",hashtags_id);
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(0);
                    if(all_day_boolean==false)
                        cal.set(YEAR_start, MONTH_start-1, DAY_start, HOUR_start, MINUTE_start,0);
                    else
                        cal.set(YEAR_start, MONTH_start - 1, DAY_start, HOUR_start, MINUTE_start, 0);
                    Log.d("getTimeinInt", YEAR_start + " " + MONTH_start + " " + DAY_start + " " + HOUR_start + " " + MINUTE_start + "");
                    Log.d("getTimeinMillis", cal.getTimeInMillis() + "");
                    cal.setTimeInMillis((cal.getTimeInMillis() / 1000) * 1000);
                    DateTimeFormatter dtf = new DateTimeFormatter(cal.getTimeInMillis());
                    inputBundle_forRequest.putCharSequence("start_time", dtf.getOutputString());
                    Log.d("getTimeinString", dtf.getOutputString());


                    if(all_day_boolean==false)
                        cal.set(YEAR_end, MONTH_end-1, DAY_end, HOUR_end, MINUTE_end,0);
                    else
                        cal.set(YEAR_end, MONTH_end-1, DAY_end+1, HOUR_end, MINUTE_end,0);

                    Log.d("getTimeinInt", YEAR_start + " " + MONTH_start + " " + DAY_start + " " + HOUR_start + " " + MINUTE_start + "");
                    Log.d("getTimeinMillis", cal.getTimeInMillis() + "");
                    cal.setTimeInMillis((cal.getTimeInMillis() / 1000) * 1000);
                    dtf = new DateTimeFormatter(cal.getTimeInMillis());
                    inputBundle_forRequest.putCharSequence("end_time", dtf.getOutputString());
                    Log.d("getTimeinString", dtf.getOutputString());


                    String url = "http://119.81.176.245/schedules/";
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(getmContext(),url,"POST",inputBundle_forRequest, ImageAbsolutePath);
                    a.doExecution();




                }catch(Exception e){
                }
            }
        }
    }



}
