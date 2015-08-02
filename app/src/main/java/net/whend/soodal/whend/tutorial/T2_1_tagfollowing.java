package net.whend.soodal.whend.tutorial;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
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

public class T2_1_tagfollowing extends AppCompatActivity {

    ImageView t2_tab1, t2_tab2, t2_tab3, t2_tab4, t2_tab5;
    ImageView t2_finger;
    TextView t2_text1, t2_text2;
    LinearLayout t2_dark1, t2_dark2, t2_dark3, t2_dark4, t2_dark5, t2_dark6;
    Animation fade_in, fade_out, blink;
    int i = 0;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            tutorial2();
        }
    };

    private void tutorial2() {

        switch (i) {
            case 0:
                break;
            case 1:
                t2_dark1.setVisibility(View.VISIBLE);
                t2_dark2.setVisibility(View.VISIBLE);
                t2_dark3.setVisibility(View.VISIBLE);
                t2_dark4.setVisibility(View.VISIBLE);
                t2_dark5.setVisibility(View.VISIBLE);
                t2_dark6.setVisibility(View.VISIBLE);

                t2_text1.setText("이 화면은\nWhenD의 홈 화면입니다.");
                t2_text1.setVisibility(View.VISIBLE);
                t2_text1.startAnimation(fade_in);
                break;

            case 2:

                t2_text1.setText("팔로잉한 친구 및 태그의\n일정들이 올라옵니다.");
                t2_text1.startAnimation(fade_in);
                break;

            case 3:

                t2_text1.setText("가장 중요한 기능인\n검색 및 태그 팔로잉을 해보겠습니다.");
                t2_text1.startAnimation(fade_in);
                break;

            case 4:
                t2_text1.startAnimation(fade_out);

                t2_text1.setVisibility(View.INVISIBLE);
                t2_dark3.setVisibility(View.GONE);

                t2_text2.setText("두번째 탭인 검색 탭을 클릭하세요.");
                t2_text2.setVisibility(View.VISIBLE);
                t2_text2.startAnimation(fade_in);
                t2_finger.setVisibility(View.VISIBLE);
                t2_finger.startAnimation(blink);
                break;


        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t2_1_tagfollowing);


        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        blink = AnimationUtils.loadAnimation(this, R.anim.blink);

        t2_dark1 = (LinearLayout) findViewById(R.id.t2_dark1);
        t2_dark2 = (LinearLayout) findViewById(R.id.t2_dark2);
        t2_dark3 = (LinearLayout) findViewById(R.id.t2_dark3);
        t2_dark4 = (LinearLayout) findViewById(R.id.t2_dark4);
        t2_dark5 = (LinearLayout) findViewById(R.id.t2_dark5);
        t2_dark6 = (LinearLayout) findViewById(R.id.t2_dark6);

        t2_dark1.setVisibility(View.INVISIBLE);
        t2_dark2.setVisibility(View.INVISIBLE);
        t2_dark3.setVisibility(View.INVISIBLE);
        t2_dark4.setVisibility(View.INVISIBLE);
        t2_dark5.setVisibility(View.INVISIBLE);
        t2_dark6.setVisibility(View.INVISIBLE);

        t2_tab1 = (ImageView) findViewById(R.id.t2_tab1);
        t2_tab2 = (ImageView) findViewById(R.id.t2_tab2);
        t2_tab3 = (ImageView) findViewById(R.id.t2_tab3);
        t2_tab4 = (ImageView) findViewById(R.id.t2_tab4);
        t2_tab5 = (ImageView) findViewById(R.id.t2_tab5);

        t2_finger = (ImageView) findViewById(R.id.t2_finger);
        t2_finger.setVisibility(View.INVISIBLE);

        t2_text1 = (TextView) findViewById(R.id.t2_text1);
        t2_text2 = (TextView) findViewById(R.id.t2_text2);

        t2_text1.setVisibility(View.INVISIBLE);
        t2_text2.setVisibility(View.INVISIBLE);

        t2_tab2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(T2_1_tagfollowing.this, T2_2_tagfollowing.class);
                startActivity(i);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                finish();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_t2_tagfollowing, menu);
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
                for (; i < 10; i++) {
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
