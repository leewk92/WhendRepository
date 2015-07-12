package net.whend.soodal.whend.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.whend.soodal.whend.R;


public class A4_MakeScheduleActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a4_make_schedule_layout);

        String sDate, sContent, sLocation, sTime;

        EditText date = (EditText) findViewById(R.id.date_upload);
        EditText content = (EditText) findViewById(R.id.content_upload);
        EditText location = (EditText) findViewById(R.id.location_upload);
        LinearLayout click_location = (LinearLayout) findViewById(R.id.upload_location_clickable);



        Intent intent=new Intent(this.getIntent());
        sDate = intent.getStringExtra("date");
        sContent = intent.getStringExtra("content");
        sLocation = intent.getStringExtra("location");
        sTime = intent.getStringExtra("time");

        if(sDate != null)
            date.setText(sDate);

        if(sLocation != null)
            location.setText(sLocation);

        if(sContent != null)
            content.setText(sContent);

        click_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "장소 입력을 합니다", Toast.LENGTH_SHORT);
                toast.show();
            }
        });


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
