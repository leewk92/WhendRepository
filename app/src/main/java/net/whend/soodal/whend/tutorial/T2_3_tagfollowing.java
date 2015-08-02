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

public class T2_3_tagfollowing extends AppCompatActivity {


    ImageView t2_like, t2_finger, t2_export, t2_finger2;
    TextView t2_text;
    LinearLayout t2_dark1, t2_dark2, t2_dark3, t2_dark4, t2_dark5, t2_dark6;
    Animation fade_in, fade_out, blink, blink2;
    int i=0;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            tutorial2_3();
        }
    };

    private void tutorial2_3() {

        switch (i) {
            case 0:
                i++;
                break;
            case 1:
                i++;
                t2_dark4.setVisibility(View.VISIBLE);
                t2_text.setVisibility(View.VISIBLE);
                t2_text.setText("태그의 상세정보를\n다음과 같이 확인할 수 있습니다.");
                t2_text.startAnimation(fade_in);

                break;

            case 2:
                i++;

                t2_text.startAnimation(fade_out);
                t2_text.setVisibility(View.INVISIBLE);

                t2_dark4.startAnimation(fade_out);
                t2_dark4.setVisibility(View.INVISIBLE);
                break;

            case 3:
                i++;


                t2_dark1.startAnimation(blink2);
                t2_dark2.startAnimation(blink2);
                t2_dark3.startAnimation(blink2);
                t2_dark5.startAnimation(blink2);

                t2_dark1.setVisibility(View.VISIBLE);
                t2_dark2.setVisibility(View.VISIBLE);
                t2_dark3.setVisibility(View.VISIBLE);
                t2_dark5.setVisibility(View.VISIBLE);

                break;

            case 4:

                t2_dark1.clearAnimation();
                t2_dark2.clearAnimation();
                t2_dark3.clearAnimation();
                t2_dark5.clearAnimation();

                t2_text.startAnimation(fade_in);
                t2_text.setVisibility(View.VISIBLE);

                t2_dark1.setVisibility(View.INVISIBLE);
                t2_dark2.setVisibility(View.INVISIBLE);
                t2_dark3.setVisibility(View.INVISIBLE);

                t2_dark6.setVisibility(View.VISIBLE);


                t2_text.setText("축제 태그 팔로우 버튼을\n클릭하세요.");
                t2_finger.setVisibility(View.VISIBLE);
                t2_finger.startAnimation(blink);
                break;

            case 5:
                i++;

                t2_finger.clearAnimation();
                t2_finger.setVisibility(View.INVISIBLE);

                t2_text.setText("축제 태그를 팔로우했습니다\n마이페이지-팔로잉에서 확인하세요.");
                t2_text.startAnimation(fade_in);

                break;

            case 6:
                i++;
                t2_text.setText("일정을 캘린더에 직접 넣어보겠습니다.");
                t2_text.startAnimation(fade_in);

                t2_dark1.setVisibility(View.INVISIBLE);
                t2_dark2.setVisibility(View.INVISIBLE);
                t2_dark3.setVisibility(View.INVISIBLE);
                t2_dark4.setVisibility(View.VISIBLE);
                t2_dark5.setVisibility(View.INVISIBLE);

                break;

            case 7:
                t2_text.startAnimation(fade_out);
                t2_text.setVisibility(View.INVISIBLE);
                t2_dark1.setVisibility(View.INVISIBLE);
                t2_dark2.setVisibility(View.INVISIBLE);
                t2_dark3.setVisibility(View.INVISIBLE);
                t2_dark4.setVisibility(View.INVISIBLE);
                t2_dark5.setVisibility(View.INVISIBLE);

                t2_finger2.setVisibility(View.VISIBLE);
                t2_finger2.startAnimation(blink);

                break;

            case 8:
                i++;
                t2_text.setText("자신의 캘린더에 해당 일정이 추가되었습니다.\n캘린더에서 확인하세요.\n다시 눌러 일정을 뺄 수도 있습니다.");
                t2_text.startAnimation(fade_in);
                t2_text.setVisibility(View.VISIBLE);

                t2_dark1.setVisibility(View.INVISIBLE);
                t2_dark2.setVisibility(View.INVISIBLE);
                t2_dark3.setVisibility(View.INVISIBLE);
                t2_dark4.setVisibility(View.VISIBLE);
                t2_dark5.setVisibility(View.INVISIBLE);
                break;

            case 9:
                i++;
                Intent i = new Intent(T2_3_tagfollowing.this, T3_1_upload.class);
                startActivity(i);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                finish();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t2_3_tagfollowing);

        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        blink = AnimationUtils.loadAnimation(this, R.anim.blink);
        blink2 = AnimationUtils.loadAnimation(this, R.anim.blink2);

        t2_dark1 = (LinearLayout) findViewById(R.id.t2_3_dark1);
        t2_dark2 = (LinearLayout) findViewById(R.id.t2_3_dark2);
        t2_dark3 = (LinearLayout) findViewById(R.id.t2_3_dark3);
        t2_dark4 = (LinearLayout) findViewById(R.id.t2_3_dark4);
        t2_dark5 = (LinearLayout) findViewById(R.id.t2_3_dark5);
        t2_dark6= (LinearLayout) findViewById(R.id.t2_3_dark6);

        t2_dark1.setVisibility(View.INVISIBLE);
        t2_dark2.setVisibility(View.INVISIBLE);
        t2_dark3.setVisibility(View.INVISIBLE);
        t2_dark6.setVisibility(View.INVISIBLE);
        t2_dark5.setVisibility(View.INVISIBLE);

        t2_text = (TextView) findViewById(R.id.t2_3_text);
        t2_like = (ImageView) findViewById(R.id.t2_3_like);
        t2_finger = (ImageView) findViewById(R.id.t2_3_finger);

        t2_finger.setVisibility(View.INVISIBLE);
        t2_export = (ImageView) findViewById(R.id.t2_3_export);
        t2_finger2 = (ImageView) findViewById(R.id.t2_3_finger2);

        t2_finger2.setVisibility(View.INVISIBLE);

        t2_like.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                t2_like.setImageDrawable(getResources().getDrawable(R.drawable.like_on));
                i++;
            }
        });

        t2_export.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                t2_export.setImageDrawable(getResources().getDrawable(R.drawable.export_to_calendar_onclick));
                i++;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_t2_3_tagfollowing, menu);
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
