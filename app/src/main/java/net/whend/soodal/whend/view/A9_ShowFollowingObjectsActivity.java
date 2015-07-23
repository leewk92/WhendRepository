package net.whend.soodal.whend.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.base.User;

import org.json.JSONObject;

/**
 * Created by wonkyung on 15. 7. 23.
 */
public class A9_ShowFollowingObjectsActivity extends AppCompatActivity {

    TextView mainactivity_title;
    ImageView search_btn, back_btn, setting_btn;
    EditText search_text;
    JSONObject outputSchedulesJson;

    User u = new User();

    private FragmentTabHost mTabHost;
    private int user_id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a9_showfollowingobject_layout);

        Intent intent = new Intent(this.getIntent());
        user_id = intent.getIntExtra("id", 0);                   // 훗날 유저 정보를 받기위한 URL을 받아올 때 사용할것이니라.
        Log.d("user_id", user_id + "");
        //    TextView textView=(TextView)findViewById(R.id.textview);
        //    textView.setText(s);


        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        Bundle inputBundle = new Bundle();
        inputBundle.putInt("id", user_id);
        mTabHost.addTab(mTabHost.newTabSpec("followingHashtags").setIndicator("태그"),
                F9_ShowFollowingHashTag.class, inputBundle);
        mTabHost.addTab(mTabHost.newTabSpec("searchhashtag").setIndicator("유"),
                F2_1_3_SearchUser.class, inputBundle);
    }
}