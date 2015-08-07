package net.whend.soodal.whend.view.setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.AppPrefs;

public class S3_Profile extends AppCompatActivity {

    TextView user_name, gender;
    EditText real_name, email, phone;
    LinearLayout dark, gender_select;
    Animation fade_in, fade_out, blink;
    RadioButton man, woman, etc;

    LinearLayout linear_email, linear_phone;
    TextView textview_email, textview_phone;

    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s3_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        toolbar.setTitle(" ");

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_s);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

        fade_in = AnimationUtils.loadAnimation(this, R.anim.abc_fade_in);
        fade_out = AnimationUtils.loadAnimation(this, R.anim.abc_fade_out);

        user_name = (TextView) findViewById(R.id.user_name);
        gender = (TextView) findViewById(R.id.gender);

        real_name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);

        man = (RadioButton) findViewById(R.id.man);
        woman = (RadioButton) findViewById(R.id.woman);
        etc = (RadioButton) findViewById(R.id.etc);


        AppPrefs appPrefs = new AppPrefs(this);
        user_name.setText(appPrefs.getUsername());

        dark = (LinearLayout) findViewById(R.id.s3_dark);
        dark.setVisibility(View.GONE);

        gender_select = (LinearLayout)findViewById(R.id.select_gender);
        gender_select.setVisibility(View.GONE);

        linear_email = (LinearLayout) findViewById(R.id.linear_email);
        linear_phone = (LinearLayout) findViewById(R.id.linear_phone);
        textview_email = (TextView) findViewById(R.id.textView_email);
        textview_phone = (TextView) findViewById(R.id.textView_phone);

        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(gender_select.getVisibility() != View.VISIBLE) {
                    gender_select.startAnimation(fade_in);
                    gender_select.setVisibility(View.VISIBLE);

                    linear_email.startAnimation(fade_out);
                    linear_phone.startAnimation(fade_out);
                    textview_email.startAnimation(fade_out);
                    textview_phone.startAnimation(fade_out);
                    email.startAnimation(fade_out);
                    phone.startAnimation(fade_out);


                    linear_email.setVisibility(View.INVISIBLE);
                    linear_phone.setVisibility(View.INVISIBLE);
                    textview_email.setVisibility(View.INVISIBLE);
                    textview_phone.setVisibility(View.INVISIBLE);
                    email.setVisibility(View.INVISIBLE);
                    phone.setVisibility(View.INVISIBLE);
                }else{
                    gender_select.startAnimation(fade_out);
                    gender_select.setVisibility(View.GONE);

                    linear_email.startAnimation(fade_in);
                    linear_phone.startAnimation(fade_in);
                    textview_email.startAnimation(fade_in);
                    textview_phone.startAnimation(fade_in);
                    email.startAnimation(fade_in);
                    phone.startAnimation(fade_in);


                    linear_email.setVisibility(View.VISIBLE);
                    linear_phone.setVisibility(View.VISIBLE);
                    textview_email.setVisibility(View.VISIBLE);
                    textview_phone.setVisibility(View.VISIBLE);
                    email.setVisibility(View.VISIBLE);
                    phone.setVisibility(View.VISIBLE);
                }

            }
        });

        man.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                gender.setText("����");
                gender_select.startAnimation(fade_out);
                gender_select.setVisibility(View.GONE);

                linear_email.startAnimation(fade_in);
                linear_phone.startAnimation(fade_in);
                textview_email.startAnimation(fade_in);
                textview_phone.startAnimation(fade_in);
                email.startAnimation(fade_in);
                phone.startAnimation(fade_in);


                linear_email.setVisibility(View.VISIBLE);
                linear_phone.setVisibility(View.VISIBLE);
                textview_email.setVisibility(View.VISIBLE);
                textview_phone.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                phone.setVisibility(View.VISIBLE);

                etc.setChecked(false);
                woman.setChecked(false);

            }
        });

        woman.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                gender.setText("����");
                gender_select.startAnimation(fade_out);
                gender_select.setVisibility(View.GONE);

                linear_email.startAnimation(fade_in);
                linear_phone.startAnimation(fade_in);
                textview_email.startAnimation(fade_in);
                textview_phone.startAnimation(fade_in);
                email.startAnimation(fade_in);
                phone.startAnimation(fade_in);


                linear_email.setVisibility(View.VISIBLE);
                linear_phone.setVisibility(View.VISIBLE);
                textview_email.setVisibility(View.VISIBLE);
                textview_phone.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                phone.setVisibility(View.VISIBLE);


                man.setChecked(false);
                etc.setChecked(false);
            }
        });

        etc.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                gender.setText("��Ÿ");

                gender_select.startAnimation(fade_out);
                gender_select.setVisibility(View.GONE);

                linear_email.startAnimation(fade_in);
                linear_phone.startAnimation(fade_in);
                textview_email.startAnimation(fade_in);
                textview_phone.startAnimation(fade_in);
                email.startAnimation(fade_in);
                phone.startAnimation(fade_in);


                linear_email.setVisibility(View.VISIBLE);
                linear_phone.setVisibility(View.VISIBLE);
                textview_email.setVisibility(View.VISIBLE);
                textview_phone.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                phone.setVisibility(View.VISIBLE);

                man.setChecked(false);
                woman.setChecked(false);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_s3__profile, menu);
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

            dark.setVisibility(View.VISIBLE);
            dark.startAnimation(fade_in);

            Thread movetoback = new Thread() {
                public void run() {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally{
                        finish();
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    }
                }
            };

            movetoback.start();



            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
