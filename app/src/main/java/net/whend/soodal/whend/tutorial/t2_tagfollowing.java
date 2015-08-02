package net.whend.soodal.whend.tutorial;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.view.MainActivity;

public class t2_tagfollowing extends AppCompatActivity {

    ImageView t2_tab1, t2_tab2, t2_tab3, t2_tab4, t2_tab5;
    TextView t2_text1, t2_text2;
    LinearLayout t2_dark1, t2_dark2, t2_dark3, t2_dark4, t2_dark5;
    Animation fade_in, fade_out;
    int i = 0;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            tutorial2();
        }
    };

    private void tutorial2() {

        switch (i) {


        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t2_tagfollowing);

        t2_tab1 = (ImageView) findViewById(R.id.t2_tab1);
        t2_tab2 = (ImageView) findViewById(R.id.t2_tab2);
        t2_tab3 = (ImageView) findViewById(R.id.t2_tab3);
        t2_tab4 = (ImageView) findViewById(R.id.t2_tab4);
        t2_tab5 = (ImageView) findViewById(R.id.t2_tab5);



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
}
