package net.whend.soodal.whend.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.AppPrefs;

import static java.lang.Thread.sleep;

public class A0_0_OpenAcitivity extends AppCompatActivity {
    public Context mContext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a0_0_open_layout);

        Thread moveTomain = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    AppPrefs appPrefs = new AppPrefs(mContext);
                    if(appPrefs.getToken()== "" ) {       //로그인창으로 이동

                        Intent i = new Intent(A0_0_OpenAcitivity.this, A0_1_LoginActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);

                        finish();
                    }else{      // MainActivity로 이동
                        Intent i = new Intent(A0_0_OpenAcitivity.this, MainActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);

                        finish();
                    }
                }
            }
        };

        moveTomain.start();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_a0_0__open_acitivity, menu);
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
