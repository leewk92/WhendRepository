package net.whend.soodal.whend.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.util.DateTimeFormatter;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.json.JSONArray;

import java.util.Calendar;


public class A4_MakeScheduleActivity extends AppCompatActivity {

    private int TAKE_FROM_CAMERA = 1;
    private int TAKE_FROM_GALLERY = 2;

    private int REQ_CODE_SELECT_IMAGE = 100;
    private Uri mImageCaptureUri;
    private String selectedImagePath;

    private ImageView schedule_photo;
    private ImageView schedule_photo_add;
    private static int YEAR_start,MONTH_start,DAY_start,HOUR_start,MINUTE_start;
    private static int YEAR_end,MONTH_end,DAY_end,HOUR_end,MINUTE_end;
    static TextView date_start,time_start,date_end,time_end;
    EditText title,location,memo;
    String sDate, sContent, sLocation, sTime, sStarttime,sEndtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

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


        sContent = intent.getStringExtra("content");
        sLocation = intent.getStringExtra("location");
        sStarttime = intent.getStringExtra("datetime_start");
        sEndtime = intent.getStringExtra("datetime_end");


        if(sStarttime != null) {
            DateTimeFormatter dtf = new DateTimeFormatter(sStarttime);
            date_start.setText(dtf.getDate());
            time_start.setText(dtf.getTime());
            YEAR_start = dtf.getCalendar().get(Calendar.YEAR);
            MONTH_start = dtf.getCalendar().get(Calendar.MONTH)+1;
            DAY_start = dtf.getCalendar().get(Calendar.DAY_OF_MONTH);
            HOUR_start = dtf.getCalendar().get(Calendar.HOUR);
            MINUTE_start = dtf.getCalendar().get(Calendar.MINUTE);
        }else{
            // now
            DateTimeFormatter dtf = new DateTimeFormatter();
            date_start.setText(dtf.getDate());
            time_start.setText(dtf.getTime());
            YEAR_start = dtf.getCalendar().get(Calendar.YEAR);
            MONTH_start = dtf.getCalendar().get(Calendar.MONTH)+1;
            DAY_start = dtf.getCalendar().get(Calendar.DAY_OF_MONTH);
            HOUR_start = dtf.getCalendar().get(Calendar.HOUR);
            MINUTE_start = dtf.getCalendar().get(Calendar.MINUTE);
        }
        if(sEndtime != null) {
            DateTimeFormatter dtf = new DateTimeFormatter(sStarttime);
            date_end.setText(dtf.getDate());
            time_end.setText(dtf.getTime());
            YEAR_end = dtf.getCalendar().get(Calendar.YEAR);
            MONTH_end = dtf.getCalendar().get(Calendar.MONTH)+1;
            DAY_end = dtf.getCalendar().get(Calendar.DAY_OF_MONTH);
            HOUR_end = dtf.getCalendar().get(Calendar.HOUR);
            MINUTE_end = dtf.getCalendar().get(Calendar.MINUTE);
        }else{
            // now
            DateTimeFormatter dtf = new DateTimeFormatter();
            date_end.setText(dtf.getDate());
            time_end.setText(dtf.getTime());
            YEAR_end = dtf.getCalendar().get(Calendar.YEAR);
            MONTH_end = dtf.getCalendar().get(Calendar.MONTH)+1;
            DAY_end = dtf.getCalendar().get(Calendar.DAY_OF_MONTH);
            HOUR_end = dtf.getCalendar().get(Calendar.HOUR);
            MINUTE_end = dtf.getCalendar().get(Calendar.MINUTE);
        }

        if(sLocation != null)
            location.setText(sLocation);

        if(sContent != null)
            title.setText(sContent);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_upload);
        toolbar.setTitle("");
        TextView toolbartext = (TextView) findViewById(R.id.toolbar_textview);
        toolbartext.setText("공유하기");

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
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

                final CharSequence[] items = {"카메라에서 찍기", "갤러리에서 가져오기"};

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

                            float width_dpi = getResources().getDisplayMetrics().densityDpi;
                            float height_dpi = 120f;

                            float ratio = width_dpi/height_dpi;

                            intent.putExtra("crop", "true");
                            intent.putExtra("aspectX", ratio);
                            intent.putExtra("aspectY", 1);
                            intent.putExtra("outputX", 200);
                            intent.putExtra("outputY", 150);

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

        StartDatePickerTextviewListener(this, date_start, sStarttime);
        StartTimePickerTextviewListener(this, time_start, sStarttime);

        EndDatePickerTextviewListener(this, date_end, sEndtime);
        EndTimePickerTextviewListener(this, time_end, sEndtime);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_FROM_GALLERY) {
                Bundle extras = data.getExtras();
                mImageCaptureUri = data.getData(); // Get data from selected photo

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    schedule_photo.setImageBitmap(photo);
                }
            }else if (requestCode == TAKE_FROM_CAMERA) {
                Bundle extras2 = data.getExtras();
                if (extras2 != null) {
                    Bitmap photo = extras2.getParcelable("data");
                    schedule_photo.setImageBitmap(photo);
                }
            }
        }
    }

    public void StartDatePickerTextviewListener(Context context, TextView date, String datetime){
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


    public void StartTimePickerTextviewListener(Context context, TextView time, String datetime){
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
    public void EndDatePickerTextviewListener(Context context, TextView date, String datetime){
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


    public void EndTimePickerTextviewListener(Context context, TextView time, String datetime){
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_upload) {
            Bundle inputBundle = new Bundle();
            inputBundle.putCharSequence("title",title.getText());
            inputBundle.putCharSequence("memo", memo.getText());
            Calendar cal = Calendar.getInstance();
            cal.set(YEAR_start, MONTH_start, DAY_start, HOUR_start, MINUTE_start);

            Log.d("getTimeinInt", YEAR_start + " " + MONTH_start + " " + DAY_start + " " + HOUR_start + " " + MINUTE_start + "");
            Log.d("getTimeinMillis",cal.getTimeInMillis()+"");

            DateTimeFormatter dtf = new DateTimeFormatter(cal.getTimeInMillis());
            inputBundle.putCharSequence("start_time", dtf.getOutputString());

            Log.d("getTimeinString",dtf.getOutputString());
            cal.set(YEAR_end, MONTH_end, DAY_end, HOUR_end, MINUTE_end);
            dtf = new DateTimeFormatter(cal.getTimeInMillis());
            inputBundle.putCharSequence("end_time",dtf.getOutputString());
            String url = "http://119.81.176.245/schedules/";
            HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(this,url,"POST",inputBundle);
            a.doExecution();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    class HTTPRestfulUtilizerExtender extends HTTPRestfulUtilizer {

        private EditText dateview;
        private EditText timeview;
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
                        Toast.makeText(getmContext(),"메모를 입력하세요! ",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getmContext(),"업로드합니다! ",Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){}


            }
        }
    }



    static public class F6_DatePickerFragment_start extends F6_DatePickerFragment{
        public int YEAR,MONTH,DAY;
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
        public int YEAR,MONTH,DAY;
        @Override
        //onTimeSet() callback method
        public void onTimeSet(TimePicker view, int hourOfDay, int minute){

            this.HOUR = hourOfDay;
            this.MINUTE = minute;
            HOUR_start =HOUR;
            MINUTE_start = MINUTE;
            time_start.setText(HOUR_start + ":" + MINUTE_start);
        }
    }
    static public class F6_DatePickerFragment_end extends F6_DatePickerFragment{
        public int YEAR,MONTH,DAY;
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
        public int YEAR,MONTH,DAY;
        @Override
        //onTimeSet() callback method
        public void onTimeSet(TimePicker view, int hourOfDay, int minute){

            this.HOUR = hourOfDay;
            this.MINUTE = minute;
            HOUR_end =HOUR;
            MINUTE_end = MINUTE;
            time_end.setText(HOUR_end + ":" + MINUTE_end);
        }
    }

}
