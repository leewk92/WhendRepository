package net.whend.soodal.whend.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.Specific_Schedule_Adapter;
import net.whend.soodal.whend.model.top.Concise_Schedule;

import java.util.ArrayList;

/**
 * Created by wonkyung on 15. 7. 11.
 */
public class A3_SpecificScheduleActivity extends Activity {

    ArrayList<Concise_Schedule> CSchedule_list;
    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a3_specificschedule_layout);

        Intent intent=new Intent(this.getIntent());
        String s=intent.getStringExtra("text");                   // 훗날 유저 정보를 받기위한 URL을 받아올 때 사용할것이니라.
        //    TextView textView=(TextView)findViewById(R.id.textview);
        //    textView.setText(s);

        CSchedule_list = new ArrayList<Concise_Schedule>();
        Concise_Schedule a= new Concise_Schedule();
        CSchedule_list.add(a);

        Specific_Schedule_Adapter adapter = new Specific_Schedule_Adapter(this,R.layout.item_specific_schedule,CSchedule_list);
        listview = (ListView)findViewById(R.id.listview_specific_schedule);
        listview.setAdapter(adapter);
    }

}
