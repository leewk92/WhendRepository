package net.whend.soodal.whend.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.Upload_Schedule_Adapter;
import net.whend.soodal.whend.model.top.Upload_Schedule;

import java.util.ArrayList;

public class A1_UploadActivity extends AppCompatActivity {

    private ListView listview;
    private ArrayList<Upload_Schedule> arraySchedule = new ArrayList<Upload_Schedule>();
    private Upload_Schedule_Adapter upload_schedule_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a1_upload);

        Upload_Schedule a = new Upload_Schedule();
        arraySchedule.add(a);
        arraySchedule.add(a);
        arraySchedule.add(a);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_upload);
        toolbar.setTitle("일정 올리기");
        setSupportActionBar(toolbar);

        listview = (ListView)findViewById(R.id.listView_upload);
        listview.setAdapter(new Upload_Schedule_Adapter(this ,R.layout.item_upload_schedule,arraySchedule));



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_a1_upload, menu);
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
