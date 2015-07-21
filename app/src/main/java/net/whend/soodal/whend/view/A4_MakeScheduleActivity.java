package net.whend.soodal.whend.view;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.util.DateTimeFormatter;

import java.util.Calendar;


public class A4_MakeScheduleActivity extends AppCompatActivity {

    private int TAKE_FROM_CAMERA = 1;
    private int TAKE_FROM_GALLERY = 2;

    private int REQ_CODE_SELECT_IMAGE = 100;
    private Uri mImageCaptureUri;
    private String selectedImagePath;

    private ImageView schedule_photo;
    private ImageView schedule_photo_add;
    private static int MONTH,DAY,HOUR,MINUTE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String sDate, sContent, sLocation, sTime, sStarttime;
        View.OnClickListener photo_add;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.a4_make_schedule_layout);


        AppPrefs appPrefs = new AppPrefs(this);
        TextView username = (TextView)findViewById(R.id.user_fullname);
        username.setText(appPrefs.getUsername());


        EditText date = (EditText) findViewById(R.id.date);
        EditText time = (EditText) findViewById(R.id.time);
        EditText title = (EditText) findViewById(R.id.title);
        EditText location = (EditText) findViewById(R.id.location);
        EditText memo = (EditText) findViewById(R.id.memo);

        schedule_photo = (ImageView) findViewById(R.id.schedule_photo);
        schedule_photo_add = (ImageView) findViewById(R.id.schedule_photo_add);

        Intent intent = new Intent(this.getIntent());
        sDate = intent.getStringExtra("date");
        sContent = intent.getStringExtra("content");
        sLocation = intent.getStringExtra("location");
        sTime = intent.getStringExtra("time");
        sStarttime = intent.getStringExtra("datetime");
        if(sDate != null)
            date.setText(sDate);

        if(sLocation != null)
            location.setText(sLocation);

        if(sContent != null)
            title.setText(sContent);
        if(sTime !=null)
            time.setText(sTime);
        if(sStarttime== null){
            DateTimeFormatter dtf = new DateTimeFormatter();        // 현재시간
            sStarttime = dtf.getOutputString();
        }
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

        DatePickerEditviewListener(this, date, sStarttime);
        TimePickerEditviewListener(this, time, sStarttime);
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

    public void DatePickerEditviewListener(Context context, EditText date, String datetime){
        final Context mContext = context;
        final EditText dateview = date;
        final DateTimeFormatter dtf = new DateTimeFormatter(datetime);

        dateview.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment newFragment = new F6_DatePickerFrgment();
                    newFragment.show(getSupportFragmentManager(), "DatePicker");
                }else{
                    try {
                        String[] array = dateview.getText().toString().split("월");
                        MONTH = Integer.getInteger(array[0]);
                        String[] array2 = array[1].split("일");
                        DAY = Integer.getInteger(array2[0]);
                    }
                    catch(Exception e){}
                }
            }
        });
    }


    public void TimePickerEditviewListener(Context context, EditText time, String datetime){
        final Context mContext = context;
        final EditText timeview = time;
        final DateTimeFormatter dtf = new DateTimeFormatter(datetime);

        timeview.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v,boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment newFragment = new F7_TimePickerFragment();
                    newFragment.show(getSupportFragmentManager(),"TimePicker");
                }else {
                    try {
                        String[] array = timeview.getText().toString().split(":");
                        HOUR = Integer.getInteger(array[0]);
                        MINUTE = Integer.getInteger(array[1]);
                    }catch(Exception e){}
                }
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
            Toast.makeText(this,"업로드합니다",Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
