package net.whend.soodal.whend;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.MenuItem;

import net.whend.soodal.whend.template.Fragment1;
import net.whend.soodal.whend.template.Fragment2;
import net.whend.soodal.whend.template.Fragment3;
import net.whend.soodal.whend.template.Fragment4;
import net.whend.soodal.whend.template.Fragment5;


public class MainActivity extends FragmentActivity {
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("", getApplicationContext().getResources().getDrawable(R.drawable.tab1_selector)),
                Fragment1.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("", getApplicationContext().getResources().getDrawable(R.drawable.tab2_selector)),
                Fragment2.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator("", getApplicationContext().getResources().getDrawable(R.drawable.upload_full)),
                Fragment3.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab4").setIndicator("", getApplicationContext().getResources().getDrawable(R.drawable.tab3_selector)),
                Fragment4.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab5").setIndicator("", getApplicationContext().getResources().getDrawable(R.drawable.tab4_selector)),
                Fragment5.class, null);

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
