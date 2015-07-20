package net.whend.soodal.whend.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.support.v4.app.FragmentManager;
import net.whend.soodal.whend.R;

/**
 * Created by wonkyung on 15. 7. 11.
 */
public class A2_UserProfileActivity extends AppCompatActivity {

    private FragmentTabHost mTabHost;
    private int user_id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f5_mypage_layout);

        Intent intent=new Intent(this.getIntent());
        user_id=intent.getIntExtra("id",0);                   // 훗날 유저 정보를 받기위한 URL을 받아올 때 사용할것이니라.
    //    TextView textView=(TextView)findViewById(R.id.textview);
    //    textView.setText(s);


        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager() , R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("searchschedule").setIndicator("MY"),
                F5_1_MyTimeline.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("searchhashtag").setIndicator("관심"),
                F2_1_2_SearchHashtag.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("searchuser").setIndicator("분석"),
                F2_1_3_SearchUser.class, null);

    }


}
