package net.whend.soodal.whend.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.Upload_Schedule_Adapter;
import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.model.top.Upload_Schedule;
import net.whend.soodal.whend.util.CalendarProviderUtil;

import java.util.ArrayList;

public class A1_UploadActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Upload_Schedule> arraySchedule = new ArrayList<Upload_Schedule>();

    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.abc_popup_enter, R.anim.push_down_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a1_upload_layout);
/*
        arraySchedule.add(new Upload_Schedule("2015년 2월 7일", "준삐 생일", "하루 종일", "내 방"));
        arraySchedule.add(new Upload_Schedule("2015년 6월 15일", "민수 생일", "하루 종일", "민수 방"));
        arraySchedule.add(new Upload_Schedule("2015년 12월 23일", "원경 생일", "하루 종일", "원경 방"));
*/
        getDataFromInnerCalendar();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_upload);
        TextView uploadactivity_title = (TextView) findViewById(R.id.uploadactivity_title);
        uploadactivity_title.setText("일정 올리기");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.cancel_s);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.abc_popup_enter, R.anim.push_down_out);
            }
        });


        //Recycler View 설정
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new Upload_Schedule_Adapter(this, R.layout.item_upload_schedule, arraySchedule);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_upload);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        // FAB

        ImageButton fabImageButton = (ImageButton) findViewById(R.id.fab_image_button);

        fabImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(A1_UploadActivity.this, A4_MakeScheduleActivity.class);
                startActivity(intent);


            }
        });

    }

    public void getDataFromInnerCalendar(){
        CalendarProviderUtil cpu = new CalendarProviderUtil(this);

        ArrayList<Schedule> innerScheduleList= cpu.getInnerScheduleList();
        for(int i =0; i<innerScheduleList.size(); i++){

            Upload_Schedule us = new Upload_Schedule(innerScheduleList.get(i));
            arraySchedule.add(us);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_a1_upload, menu);
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
