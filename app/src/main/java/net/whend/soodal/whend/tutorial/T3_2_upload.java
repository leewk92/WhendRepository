package net.whend.soodal.whend.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.view.MainActivity;

public class T3_2_upload extends AppCompatActivity {

    LinearLayout t3_dark1, t3_dark2, t3_dark3;
    Animation fade_in, fade_out, blink;
    ImageView t3_finger;
    TextView t3_text1;
    int i=0;
    String username;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            tutorial3_2();
        }
    };

    private void tutorial3_2() {

        switch (i) {
            case 0:
                i++;
                break;
            case 1:
                i++;
                t3_text1.setText("일정올리기 창에는\n본인의 일정을 쉽게 올릴 수 있습니다.");
                t3_text1.setVisibility(View.VISIBLE);
                t3_text1.startAnimation(fade_in);
                break;
            case 2:
                i++;
                t3_text1.setText("캘린더의 일정이 자동으로 검색됩니다.");
                t3_text1.startAnimation(fade_in);
                break;
            case 3:
                i++;
                t3_text1.startAnimation(fade_out);
                t3_text1.setVisibility(View.INVISIBLE);

                t3_dark1.setVisibility(View.INVISIBLE);
                t3_dark2.startAnimation(fade_in);
                t3_dark2.setVisibility(View.VISIBLE);
                break;

            case 4:
                i++;
                t3_text1.setText(username + " 님이 아는 또 다른 일정을\n친구와 공유할 수 있습니다.");
                t3_text1.startAnimation(fade_in);
                t3_text1.setVisibility(View.VISIBLE);

                t3_dark1.setVisibility(View.VISIBLE);
                t3_dark2.setVisibility(View.INVISIBLE);
                break;

            case 5:
                i++;
                t3_text1.startAnimation(fade_out);
                t3_text1.setVisibility(View.INVISIBLE);

                t3_dark1.setVisibility(View.INVISIBLE);
                t3_dark2.setVisibility(View.INVISIBLE);
                t3_dark3.startAnimation(fade_in);
                t3_dark3.setVisibility(View.VISIBLE);


                t3_finger.setVisibility(View.VISIBLE);
                t3_finger.setAnimation(blink);
                break;

            case 6:
                i++;
                t3_text1.setText("튜토리얼이 끝났습니다.\nWhenD와 함께 친구들과의 일정을 공유하세요!");
                t3_text1.startAnimation(fade_in);
                t3_text1.setVisibility(View.VISIBLE);

                t3_dark1.setVisibility(View.VISIBLE);
                t3_dark3.setVisibility(View.INVISIBLE);

                t3_finger.setVisibility(View.INVISIBLE);

                break;

            case 7:
                i++;
                break;

            case 8:
                i++;
                Intent i = new Intent(T3_2_upload.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                finish();
                break;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t3_2_upload);
        AppPrefs appPrefs = new AppPrefs(this);
        username= appPrefs.getUsername();
        t3_dark1 = (LinearLayout)findViewById(R.id.t3_dark1);
        t3_dark2 = (LinearLayout)findViewById(R.id.t3_dark2);
        t3_dark3 = (LinearLayout)findViewById(R.id.t3_dark3);

        t3_dark2.setVisibility(View.INVISIBLE);

        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        blink = AnimationUtils.loadAnimation(this, R.anim.blink);

        t3_finger = (ImageView) findViewById(R.id.t3_finger);
        t3_finger.setVisibility(View.INVISIBLE);

        t3_text1 = (TextView) findViewById(R.id.t3_text);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_t3_2_upload, menu);
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
    @Override
    protected void onStart() {
        super.onStart();

        Thread myThread = new Thread(new Runnable() {
            public void run() {
                while (i>=0) {
                    try {

                        handler.sendMessage(handler.obtainMessage());
                        Thread.sleep(3000);
                    } catch (Throwable t) {
                    }
                }
            }
        });

        myThread.start();
    }

}
