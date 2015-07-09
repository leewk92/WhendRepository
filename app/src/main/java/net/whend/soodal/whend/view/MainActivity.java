package net.whend.soodal.whend.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.MenuItem;

import net.whend.soodal.whend.R;


public class MainActivity extends FragmentActivity {
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("", getApplicationContext().getResources().getDrawable(R.drawable.tab1_selector)),
                F1_Wall.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("", getApplicationContext().getResources().getDrawable(R.drawable.tab2_selector)),
                F2_Search.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator("", getApplicationContext().getResources().getDrawable(R.drawable.upload_full)),
                F3_Upload.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab4").setIndicator("", getApplicationContext().getResources().getDrawable(R.drawable.tab3_selector)),
                F4_Notify.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab5").setIndicator("", getApplicationContext().getResources().getDrawable(R.drawable.tab4_selector)),
                F5_Mypage.class, null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
