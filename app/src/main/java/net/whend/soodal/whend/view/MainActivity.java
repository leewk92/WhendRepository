package net.whend.soodal.whend.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;

import net.whend.soodal.whend.R;


public class MainActivity extends AppCompatActivity {
    private FragmentTabHost mTabHost;
    private EditText search_text;
    private ImageView search_btn;
    private ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        search_text = (EditText) findViewById(R.id.search_text);
        search_btn = (ImageView) findViewById(R.id.search_btn);
        back_btn = (ImageView) findViewById(R.id.back_btn);

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        search_text.setVisibility(View.INVISIBLE);
        search_btn.setVisibility(View.INVISIBLE);
        back_btn.setVisibility(View.GONE);

        setSupportActionBar(toolbar);

        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("", getApplicationContext().getResources().getDrawable(R.drawable.menu_mainhome_s)),
                F1_Wall.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("", getApplicationContext().getResources().getDrawable(R.drawable.menu_search_s)),
                F2_Search.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator(""),
                F3_Upload.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab4").setIndicator("", getApplicationContext().getResources().getDrawable(R.drawable.menu_notice_s)),
                F4_Notify.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab5").setIndicator("", getApplicationContext().getResources().getDrawable(R.drawable.menu_setting_s)),
                F5_Mypage.class, null);

        mTabHost.getTabWidget().setStripEnabled(false);
        mTabHost.getTabWidget().setDividerDrawable(null);

        mTabHost.getTabWidget().getChildAt(2).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.menu_importcalendar));

        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            if(i==2)
                mTabHost.getTabWidget().getChildAt(i).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.menu_importcalendar));
            else
                mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#727272"));
        }
        mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#212121"));

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
                    if (i == 2)
                        mTabHost.getTabWidget().getChildAt(i).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.menu_importcalendar));
                    else
                        mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#727272")); //unselected
                }

                mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#212121")); //selected
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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
