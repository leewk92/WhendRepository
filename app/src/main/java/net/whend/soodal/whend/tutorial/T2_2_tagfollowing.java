package net.whend.soodal.whend.tutorial;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.view.A0_5_TagFollowingStart;
import net.whend.soodal.whend.view.MainActivity;

public class T2_2_tagfollowing extends AppCompatActivity {

    ImageView t2_2_image, t2_2_finger;
    TextView t2_2_text;
    LinearLayout t2_dark1, t2_dark2, t2_dark3;
    Animation fade_in, fade_out, blink;
    int i=0;

    TextView skip;
    Button skip_yes, skip_no;
    LinearLayout skip_layout;

    boolean screentouched = false;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            tutorial2_2();
        }
    };

    private void tutorial2_2() {

        switch (i) {
            case 0:
                i++;
                break;
            case 1:
                i++;
                t2_dark1.setVisibility(View.VISIBLE);


                t2_2_text.setText("이 화면은\nWhenD의 검색 화면입니다.");
                t2_2_text.setVisibility(View.VISIBLE);
                t2_2_text.startAnimation(fade_in);
                break;

            case 2:
                i++;
                t2_2_text.setText("WhenD의 인기있는 태그나\n최신 태그들이 올라옵니다.");
                t2_2_text.startAnimation(fade_in);
                break;

            case 3:
                i++;
                t2_2_text.setText("인기있는 '#축제' 태그를\n팔로우 해봅시다.");
                t2_2_text.startAnimation(fade_in);
                break;

            case 4:
                i++;
                t2_2_text.startAnimation(fade_out);
                t2_dark1.setVisibility(View.INVISIBLE);
                t2_dark2.setVisibility(View.VISIBLE);
                t2_dark3.setVisibility(View.VISIBLE);

                t2_2_image.setClickable(true);
                t2_2_text.setText("축제 태그를 클릭하세요.");
                t2_2_finger.setVisibility(View.VISIBLE);
                t2_2_finger.startAnimation(blink);
                break;

            case 5:
                break;

        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent e){
        if(e.getAction() == MotionEvent.ACTION_UP){
            handler.sendMessage(handler.obtainMessage());
            screentouched = true;
            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t2_2_tagfollowing);

        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        blink = AnimationUtils.loadAnimation(this, R.anim.blink);

        t2_dark1 = (LinearLayout) findViewById(R.id.t2_2_dark1);
        t2_dark2 = (LinearLayout) findViewById(R.id.t2_2_dark2);
        t2_dark3 = (LinearLayout) findViewById(R.id.t2_2_dark3);

        t2_dark1.setVisibility(View.INVISIBLE);
        t2_dark2.setVisibility(View.INVISIBLE);
        t2_dark3.setVisibility(View.INVISIBLE);

        t2_2_finger = (ImageView) findViewById(R.id.t2_2_finger);
        t2_2_finger.setVisibility(View.INVISIBLE);
        t2_2_text = (TextView) findViewById(R.id.t2_2_text1);

        t2_2_image = (ImageView) findViewById(R.id.t2_2_image);
        t2_2_image.setClickable(false);
        t2_2_image.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (i==5){
                    Intent i = new Intent(T2_2_tagfollowing.this, T2_3_tagfollowing.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    finish();
                }

            }
        });
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0) {
                    mFlag = false;
                }
            }
        };

        // skip 부분
        skip_layout = (LinearLayout) findViewById(R.id.skip_layout);
        skip_layout.setVisibility(View.GONE);
        skip = (TextView) findViewById(R.id.skip);
        skip_yes = (Button) findViewById(R.id.skip_yes);
        skip_no = (Button) findViewById(R.id.skip_no);

        skip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (skip_layout.getVisibility() != View.VISIBLE){
                    skip_layout.setVisibility(View.VISIBLE);
                    Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_left_in_skip);
                    skip_layout.startAnimation(slide_in);


                }else{

                    Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_right_out_skip);
                    skip_layout.startAnimation(slide_out);
                    skip_layout.setVisibility(View.INVISIBLE);
                }
            }
        });

        skip_yes.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), A0_5_TagFollowingStart.class);
                intent.putExtra("text", String.valueOf("URL"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                finish();
            }
        });

        skip_no.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_right_out_skip);
                skip_layout.startAnimation(slide_out);
                skip_layout.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_t2_2_tagfollowing, menu);
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

    protected void onStart() {
        super.onStart();

        Thread myThread = new Thread(new Runnable() {
            public void run() {
                while(i>=0) {
                    try {

                        handler.sendMessage(handler.obtainMessage());
                        Thread.sleep(3000);


                        while(screentouched){
                            screentouched=false;
                            Thread.sleep(3000);
                        }

                    } catch (Throwable t) {
                    }
                }
            }
        });

        myThread.start();
    }


    // 핸들러, 플래그 선언 for back key로 종료
    private Handler mHandler;
    private boolean mFlag = false;


    //2초안에 백키 눌르면 종료
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(!mFlag) {
                Toast.makeText(this, "'뒤로'버튼을 한번 더 누르시면 튜토리얼이 종료됩니다.", Toast.LENGTH_SHORT).show();
                mFlag = true;
                mHandler.sendEmptyMessageDelayed(0, 2000);
                return false;
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("text", String.valueOf("URL"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
