package net.whend.soodal.whend.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.ScheduleFollow_User_Adapter;
import net.whend.soodal.whend.model.base.User;
import net.whend.soodal.whend.model.top.ScheduleFollow_User;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wonkyung on 15. 7. 12.
 */
public class A5_WhoFollowsScheduleActivity extends AppCompatActivity {

    ArrayList <ScheduleFollow_User> User_list= new ArrayList<ScheduleFollow_User>();;
    ListView listview;
    JSONObject outputSchedulesJson;
    ScheduleFollow_User_Adapter adapter;
    static String nextURL;
    Context mContext = this;
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.abc_popup_enter, R.anim.abc_fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a5_whofollowsschedule);

        Intent intent=new Intent(this.getIntent());
        String url=intent.getStringExtra("url");                   // 훗날 유저 정보를 받기위한 URL을 받아올 때 사용할것이니라.
        //    TextView textView=(TextView)findViewById(R.id.textview);
        //    textView.setText(s);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_whofollowschedule);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.abc_popup_enter, R.anim.push_right_out);
            }
        });

        adapter = new ScheduleFollow_User_Adapter(this,R.layout.item_schedulefollow_user,User_list);
        listview = (ListView)findViewById(R.id.listview_schedulefollow_user);
        listview.setAdapter(adapter);
        listview.setOnScrollListener(new EndlessScrollListener());
        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(this,url,"GET");
        a.doExecution();

    }


    // 끝없이 로딩 하는거
    public class EndlessScrollListener implements AbsListView.OnScrollListener {

        private int visibleThreshold = 2;
        private int currentPage = 0;
        private int previousTotal = 0;
        private boolean loading = true;

        public EndlessScrollListener() {

        }
        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    currentPage++;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // I load the next page of gigs using a background task,
                // but you can call any function here.
                Log.d("lastItemScrolled", "true");
                try{
                    if(nextURL != "null"){
                        HTTPRestfulUtilizerExtender b = new HTTPRestfulUtilizerExtender(mContext, nextURL,"GET");
                        //              b.doExecution();
                    }
                }catch(Exception e){

                }
                loading = true;
            }
        }
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }


    class HTTPRestfulUtilizerExtender extends HTTPRestfulUtilizer {

        // Constructor for GET
        public HTTPRestfulUtilizerExtender(Context mContext, String url, String HTTPRestType) {
            setmContext(mContext);
            setUrl(url);
            setHTTPRestType(HTTPRestType);
            task = new HttpAsyncTaskExtenders();
            Log.d("HTTP Constructor url", url);
            // new HttpAsyncTask().execute(url,HTTPRestType);
        }

        @Override
        public void doExecution() {
            task.execute(getUrl(), getHTTPRestType());
        }

        class HttpAsyncTaskExtenders extends HTTPRestfulUtilizer.HttpAsyncTask {
            @Override
            protected String doInBackground(String... strings) {
                String url = strings[0];
                String sHTTPRestType = strings[1];
                setOutputString(GET(url));

                return getOutputString();
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                try {
                    outputSchedulesJson = getOutputJsonObject();
                    JSONArray results = outputSchedulesJson.getJSONArray("results");
                    JSONObject tmp_ith;
                    nextURL = outputSchedulesJson.getString("next");
                    for(int i=0; i<results.length() ;i++){
                        tmp_ith = results.getJSONObject(i);
                        User u = new User();
                        u.setId(tmp_ith.getInt("user_id"));
                        u.setUsername(tmp_ith.getString("user_name"));
                        u.setUser_photo(tmp_ith.getString("photo") == "null" ? "" : tmp_ith.getString("photo").substring(0, tmp_ith.getString("photo").length() - 4) + ".100x100.jpg");


                        ScheduleFollow_User sfu = new ScheduleFollow_User(u, tmp_ith.getInt("is_follow")==1?true:false);     // 이거 아직 모름
                        User_list.add(sfu);
                        adapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {

                }

            }
        }

    }
}
