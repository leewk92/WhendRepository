package net.whend.soodal.whend.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.ScheduleFollow_User_Adapter;
import net.whend.soodal.whend.model.top.ScheduleFollow_User;

import java.util.ArrayList;

/**
 * Created by wonkyung on 15. 7. 12.
 */
public class A5_WhoFollowsScheduleActivity extends Activity {

    ArrayList <ScheduleFollow_User> User_list;
    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a5_whofollowsschedule);

        Intent intent=new Intent(this.getIntent());
        String s=intent.getStringExtra("text");                   // 훗날 유저 정보를 받기위한 URL을 받아올 때 사용할것이니라.
        //    TextView textView=(TextView)findViewById(R.id.textview);
        //    textView.setText(s);

        User_list = new ArrayList<ScheduleFollow_User>();
        ScheduleFollow_User a= new ScheduleFollow_User();
        User_list.add(a);
        User_list.add(a);
        User_list.add(a);

        ScheduleFollow_User_Adapter adapter = new ScheduleFollow_User_Adapter(this,R.layout.item_schedulefollow_user,User_list);
        listview = (ListView)findViewById(R.id.listview_schedulefollow_user);
        listview.setAdapter(adapter);
    }
}
