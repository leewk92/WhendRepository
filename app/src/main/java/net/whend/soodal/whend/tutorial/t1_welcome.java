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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.AppPrefs;

public class t1_welcome extends AppCompatActivity {

    TextView t1_text;
    ImageView t1_finger;
    Button next_btn;
    Animation fade_in, fade_out, blink;
    String username;
    int i = 0;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            show_next();
        }
    };

    private void show_next() {

        switch(i){
            case 0:
                i++;
                next_btn.setVisibility(View.INVISIBLE);
                t1_text.setText("WhenD를 처음 가입한 " + username + "님\n환영합니다.");
                        t1_text.startAnimation(fade_in);
                break;
            case 1:
                i++;
                t1_text.setText("유익하고 재밌는 WhenD 이용을 위해\n간단한 튜토리얼을 준비했어요.");
                t1_text.startAnimation(fade_in);
                break;
            case 2:
                i++;
                t1_text.setText("아래 버튼을 눌러주세요.");
                t1_text.startAnimation(fade_in);
                next_btn.setVisibility(View.VISIBLE);
                next_btn.startAnimation(fade_in);

                break;

            case 3:
                t1_finger.setVisibility(View.VISIBLE);
                t1_finger.startAnimation(blink);


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t1_welcome);


        AppPrefs appPrefs = new AppPrefs(this);
        username= appPrefs.getUsername();
        t1_text = (TextView)findViewById(R.id.t1_textview);
        next_btn = (Button)findViewById(R.id.t1_next);

        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        blink = AnimationUtils.loadAnimation(this, R.anim.blink);

        t1_finger = (ImageView) findViewById(R.id.t1_finger);
        t1_finger.setVisibility(View.INVISIBLE);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(t1_welcome.this, T2_1_tagfollowing.class);
                startActivity(i);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Thread myThread=new Thread(new Runnable() {
                        public void run() {
                            while( i >=0){
                                try {

                                    handler.sendMessage(handler.obtainMessage());
                                    Thread.sleep(3000);

                                }
                                catch (Throwable t) {
                                }
                }
            }
        });

        myThread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_t1_welcome, menu);
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
