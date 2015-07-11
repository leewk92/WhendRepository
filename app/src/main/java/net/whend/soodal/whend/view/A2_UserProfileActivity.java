package net.whend.soodal.whend.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import net.whend.soodal.whend.R;

/**
 * Created by wonkyung on 15. 7. 11.
 */
public class A2_UserProfileActivity extends Activity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a2_userprofile_layout);

        Intent intent=new Intent(this.getIntent());
        String s=intent.getStringExtra("text");                   // 훗날 유저 정보를 받기위한 URL을 받아올 때 사용할것이니라.
    //    TextView textView=(TextView)findViewById(R.id.textview);
    //    textView.setText(s);
    }


}
