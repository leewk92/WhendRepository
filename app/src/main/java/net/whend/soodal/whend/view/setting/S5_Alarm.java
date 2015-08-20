package net.whend.soodal.whend.view.setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.AppPrefs;

public class S5_Alarm extends AppCompatActivity {

    private Switch alarm_onoff;
    private Spinner time_list;
    private LinearLayout alarm_list;
    private int time_before = 1440;

    private String[] time = {
            "1일 전",
            "1시간 전",
            "2시간 전",
            "6시간 전",
            "2일 전",
            "일주일 전"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s5_alarm);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_alarm);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });


        final AppPrefs appPrefs = new AppPrefs(this);

        alarm_onoff = (Switch) findViewById(R.id.alarm_switch);
        alarm_list = (LinearLayout) findViewById(R.id.alarm_list);
        time_list = (Spinner) findViewById(R.id.alarm_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, time);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        time_list.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = time_list.getSelectedItemPosition();
                        switch(position){
                            case 0:
                                time_before = 1440; // 1day
                                break;
                            case 1:
                                time_before = 60;  // 60mins
                                break;
                            case 2:
                                time_before = 120;
                                break;
                            case 3:
                                time_before = 360; // 6 hours
                                break;
                            case 4:
                                time_before = 2880;
                                break;
                            case 5:
                                time_before = 10080; // 7days
                                break;
                            default:
                                time_before = 1440;
                                break;
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }

                }
        );

        time_list.setAdapter(adapter);

        if(appPrefs.getAlarm_setting()) {
            alarm_onoff.setChecked(true);
            switch(appPrefs.getAlarm_time()){
                case 1440:
                    time_list.setSelection(0);
                    break;
                case 60:
                    time_list.setSelection(1);
                    break;
                case 120:
                    time_list.setSelection(2);
                    break;
                case 360:
                    time_list.setSelection(3);
                    break;
                case 2880:
                    time_list.setSelection(4);
                    break;
                case 10080:
                    time_list.setSelection(5);
                    break;
                default:
                    time_list.setSelection(0);
                    break;
            }
            alarm_list.setVisibility(View.VISIBLE);
        }else {
            alarm_onoff.setChecked(false);
            alarm_list.setVisibility(View.GONE);
        }

        alarm_onoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    appPrefs.setAlarm_setting(true,1440);
                    alarm_list.setVisibility(View.VISIBLE);
                    alarm_list.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_down_in_rate));
                } else {
                    appPrefs.setAlarm_setting(false, 0);
                    Toast toast = Toast.makeText(getApplicationContext(), "일정 알람을 해제합니다.", Toast.LENGTH_SHORT);
                    toast.show();

                    alarm_list.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_up_out_rate));
                    alarm_list.setVisibility(View.INVISIBLE);

                }

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_s5_alarm, menu);
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

            AppPrefs appPrefs = new AppPrefs(this);

            if (alarm_onoff.isChecked()){
                appPrefs.setAlarm_setting(true,time_before);
                finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }else{
                appPrefs.setAlarm_setting(false, 0);
                finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }



            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
