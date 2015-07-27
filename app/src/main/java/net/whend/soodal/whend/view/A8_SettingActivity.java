package net.whend.soodal.whend.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

public class A8_SettingActivity extends AppCompatActivity {
    public Context mContext = this;
    ImageView search_btn, back_btn, setting_btn;
    EditText search_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a8_setting_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        toolbar.setTitle("");
        TextView toolbartext = (TextView) findViewById(R.id.toolbar_textview);
        toolbartext.setText("설정");

        setSupportActionBar(toolbar);



        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

        TextView setting_logout = (TextView)findViewById(R.id.setting_logout);
        LogoutListener(setting_logout);

    }

    // 로그아웃 리스너
    // 외 15명 누를 때 리스너
    public void LogoutListener(View setting_logout){
        setting_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setMessage("정말 로그아웃 하시겠습니까?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("로그아웃 하기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String url = "http://119.81.176.245/rest-auth/logout/";
                                HTTPRestfulUtilizerExtender_logout hl = new HTTPRestfulUtilizerExtender_logout(mContext, url, "POST",new Bundle());
                                hl.doExecution();
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_a8__setting, menu);
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
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }



    class HTTPRestfulUtilizerExtender_logout extends HTTPRestfulUtilizer {

        // Constructor for POST
        public HTTPRestfulUtilizerExtender_logout(Context mContext, String url, String HTTPRestType, Bundle inputBundle) {
            setmContext(mContext);
            setUrl(url);
            setHTTPRestType(HTTPRestType);
            setInputBundle(inputBundle);
            task = new HttpAsyncTaskExtenders();
            Log.d("HTTP Constructor url", url);
            // new HttpAsyncTask().execute(url,HTTPRestType);
        }

        @Override
        public void doExecution(){
            task.execute(getUrl(), getHTTPRestType());
        }
        class HttpAsyncTaskExtenders extends HTTPRestfulUtilizer.HttpAsyncTask{

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
            }


            @Override
            protected String doInBackground(String... strings) {
                String url = strings[0];
                String sHTTPRestType = strings[1];
                setOutputString(POST(url, getInputBundle()));

                return getOutputString();

            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);


                try{

                    //       JSONObject tmpJson = new JSONObject(getOutputString());
                    //       String token = tmpJson.getString("key");
                    String token = getOutputJsonObject().getString("success");
                    if(token == null){

                        Toast toast1 = Toast.makeText(mContext, "로그아웃을 할 수 없습니다.", Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                    else {

                        AppPrefs appPrefs = new AppPrefs(mContext);
                        appPrefs.setToken("");
                        appPrefs.setUsername("");
                        appPrefs.setUser_id(0);
                        Toast toast1 = Toast.makeText(mContext, "로그아웃 하였습니다.", Toast.LENGTH_SHORT);
                        toast1.show();

                        Intent intent = new Intent(mContext, A0_1_LoginActivity.class);

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        finish();
                    }

                }catch(Exception e){}

            }
        }
    }

}