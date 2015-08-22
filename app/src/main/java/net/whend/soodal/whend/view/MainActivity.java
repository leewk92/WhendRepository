package net.whend.soodal.whend.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.base.User;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.util.BaseContainerFragment;
import net.whend.soodal.whend.util.gcm.QuickstartPreferences;
import net.whend.soodal.whend.util.gcm.RegistrationIntentService;
import net.whend.soodal.whend.view.ContainerFragment.F1_Wall_Container;
import net.whend.soodal.whend.view.ContainerFragment.F2_Search_Container;
import net.whend.soodal.whend.view.ContainerFragment.F3_Upload_Container;
import net.whend.soodal.whend.view.ContainerFragment.F4_Notify_Container;
import net.whend.soodal.whend.view.ContainerFragment.F5_Mypage_Container;

import android.support.v4.content.LocalBroadcastManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import net.whend.soodal.whend.util.BadgeView;
//import com.readystatesoftware.viewbadger.BadgeView;


public class MainActivity extends AppCompatActivity {
    private FragmentTabHost mTabHost;
    private EditText search_text;
    private ImageView search_btn;
    private ImageView back_btn;
    private AppPrefs appPrefs;
    private int previousTab;
    BadgeView unread_count;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";


    // 핸들러, 플래그 선언 for back key로 종료
    private Handler mHandler;
    private boolean mFlag = false;

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);    //facebook
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);  //facebook
        //gcm
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

        if(appPrefs.getUnreadNotificationCount() > 0){
            unread_count.setText(appPrefs.getUnreadNotificationCount() + "");
            if (unread_count.isShown())
                unread_count.show();
            else
                unread_count.show(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        initView();
    }

    private void initView(){

        // gcm
        appPrefs = new AppPrefs(this);
        if(appPrefs.getGcm_token()==""){
            registBroadcastReceiver();
            getInstanceIdToken();
        }

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


        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("", getApplicationContext().getResources().getDrawable(R.drawable.menu_mainhome)),
                F1_Wall.class, null);

        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("", getApplicationContext().getResources().getDrawable(R.drawable.menu_search)),
                F2_Search.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator(""),
                F3_Upload.class, null);

        View notify_view = LayoutInflater.from(MainActivity.this).inflate(R.layout.notify_tab, null);
        unread_count = new BadgeView(this, notify_view.findViewById(R.id.notify_icon_layout) );
        unread_count.setTextSize(10);
        unread_count.setBadgeMargin(0, 15);
        unread_count.setBadgePosition(BadgeView.POSITION_TOP_LEFT);

        mTabHost.addTab(mTabHost.newTabSpec("tab4").setIndicator(notify_view),
                F4_Notify.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab5").setIndicator("", getApplicationContext().getResources().getDrawable(R.drawable.menu_mypage)),
                F5_Mypage.class, null);

        mTabHost.getTabWidget().setStripEnabled(false);
        mTabHost.getTabWidget().setDividerDrawable(null);

        mTabHost.getTabWidget().getChildAt(2).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.menu_importcalendar));

        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {

            if(appPrefs.getUnreadNotificationCount() > 0){
                unread_count.setText(appPrefs.getUnreadNotificationCount() + "");
                unread_count.show(true);
            }

            if(i==2)
                mTabHost.getTabWidget().getChildAt(i).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.menu_importcalendar));
            else
                mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#727272"));
        }
        mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#212121"));
        previousTab = 0;

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                if (mTabHost.getCurrentTab() == 3) {
                    appPrefs.setUnreadNotificationCount(0);
                    if (unread_count.isShown())
                        unread_count.hide(true);
                    else
                        unread_count.hide();
                } else {
                    if (appPrefs.getUnreadNotificationCount() > 0) {
                        unread_count.setText(appPrefs.getUnreadNotificationCount() + "");
                        if (unread_count.isShown())
                            unread_count.show();
                        else
                            unread_count.show(true);
                    }
                }

                for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
                    if (i == 2)
                        mTabHost.getTabWidget().getChildAt(i).setBackground(getApplicationContext().getResources().getDrawable(R.drawable.menu_importcalendar));
                    else
                        mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#727272")); //unselected
                }

                mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#212121")); //selected



                /// 뻘짓..... 컨테이너를 이용한 status 저장을 구현하려고했으나 실패
/*
                ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag("tab"+(previousTab+1))).replaceFragmentByIndex(mTabHost.getCurrentTab(), false);

                Log.e("Previous",""+previousTab);*/
                /*
                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                if (tabId.equals("tab1")){
                    t.replace(R.id.realtabcontent, new F1_Wall());
                }else if (tabId.equals("tab2")){
                    t.replace(R.id.realtabcontent, new F2_Search());
                }else if (tabId.equals("tab3")){
                    t.replace(R.id.realtabcontent, new F3_Upload());
                }else if (tabId.equals("tab4")){
                    t.replace(R.id.realtabcontent, new F4_Notify());
                }else if (tabId.equals("tab5")){
                    t.replace(R.id.realtabcontent, new F5_Mypage());
                }
                t.addToBackStack(null);
                t.commit();*/
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
    }



    /* Instance ID를 이용하여 디바이스 토큰을 가져오는 RegistrationIntentService를 실행한다.
    */
    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    /**
     * LocalBroadcast 리시버를 정의한다. 토큰을 획득하기 위한 READY, GENERATING, COMPLETE 액션에 따라 UI에 변화를 준다.
     */
    public void registBroadcastReceiver(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(action.equals(QuickstartPreferences.REGISTRATION_COMPLETE)){
                    // 액션이 COMPLETE일 경우
                    String gcm_token = intent.getStringExtra("token");
                    //Log.d("gcm_token",gcm_token);

                }

            }
        };
    }


    /**
     * Google Play Service를 사용할 수 있는 환경이지를 체크한다.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //2초안에 백키 눌르면 종료
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(!mFlag) {
                Toast.makeText(this, "'뒤로'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
                mFlag = true;
                mHandler.sendEmptyMessageDelayed(0, 2000);
                return false;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
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

    /*
    @Override
    public void onBackPressed() {
        boolean isPopFragment = false;
        String currentTabTag = mTabHost.getCurrentTabTag();
        Log.d("Tag",currentTabTag);
        if (currentTabTag.equals("tab1")) {
            isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag("tab1")).popFragment();
        } else if (currentTabTag.equals("tab2")) {
            isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag("tab2")).popFragment();
        } else if (currentTabTag.equals("tab3")) {
            isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag("tab3")).popFragment();
        } else if (currentTabTag.equals("tab4")) {
            isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag("tab4")).popFragment();
        } else if (currentTabTag.equals("tab5")) {
            isPopFragment = ((BaseContainerFragment)getSupportFragmentManager().findFragmentByTag("tab5")).popFragment();
        }
        if (!isPopFragment) {
            finish();
        }
    }
    */


}
