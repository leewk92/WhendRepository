package net.whend.soodal.whend.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.Concise_Schedule_Adapter;
import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.model.top.Concise_Schedule;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wonkyung on 15. 7. 23.
 */
public class A10_ShowSchedulesActivity extends AppCompatActivity {

    private int id;
    private static JSONObject outputSchedulesJson;
    private ListView listview;
    private ArrayList<Concise_Schedule> arrayCSchedule = new ArrayList<Concise_Schedule>();
    private Concise_Schedule_Adapter concise_schedule_adapter;
    static String nextURL;
    TextView f1_text;

    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.abc_popup_enter, R.anim.abc_fade_out);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayCSchedule.clear();

        setContentView(R.layout.f1_wall_layout);

        Intent intent=new Intent(this.getIntent());
        id=intent.getIntExtra("id",0);

        String url = "http://119.81.176.245/userinfos/"+id+"/schedules";

        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(this, url,"GET");
        a.doExecution();


        concise_schedule_adapter = new Concise_Schedule_Adapter(this,R.layout.item_concise_schedule,arrayCSchedule);
        listview = (ListView)findViewById(R.id.listview_concise_schedule);
        listview.setOnScrollListener(new EndlessScrollListener());
        listview.setAdapter(concise_schedule_adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(A10_ShowSchedulesActivity.this, A3_SpecificScheduleActivity.class);
                intent.putExtra("id", arrayCSchedule.get(position).getId());
                startActivity(intent);
            }
        });
        f1_text = (TextView)findViewById(R.id.f1_text);
        f1_text.setText("회원님이 올린 일정이 없습니다");
        f1_text.setVisibility(View.INVISIBLE);
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
                    HTTPRestfulUtilizerExtender_loadmore b = new HTTPRestfulUtilizerExtender_loadmore(A10_ShowSchedulesActivity.this,nextURL,"GET");
                    b.doExecution();
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
        public void doExecution(){
            task.execute(getUrl(),getHTTPRestType());
        }
        class HttpAsyncTaskExtenders extends HTTPRestfulUtilizer.HttpAsyncTask{
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

                try{
                    outputSchedulesJson = getOutputJsonObject();

                    JSONArray results = outputSchedulesJson.getJSONArray("results");
                    JSONObject tmp_ith;
                    nextURL = outputSchedulesJson.getString("next");
                    for(int i=0; i<results.length();i++){
                        Schedule s = new Schedule();
                        tmp_ith = results.getJSONObject(i);
                        s.setId(tmp_ith.getInt("id"));
                        s.setTitle(tmp_ith.getString("title"));
                        s.setStarttime(tmp_ith.getString("start_time"));
                        s.setEndtime(tmp_ith.getString("end_time"));
                        s.setMemo(tmp_ith.getString("memo"));
                        s.setUploaded_username(tmp_ith.getString("user_name"));
                        s.setUploaded_user_id(tmp_ith.getInt("user_id"));
                        s.setPhoto_dir_fromweb((tmp_ith.getString("photo") == "null") ? "" : tmp_ith.getString("photo").substring(0, tmp_ith.getString("photo").length() - 4) + ".800x200.jpg");
                        s.setPhoto_full_fromweb((tmp_ith.getString("photo") == "null") ? "" : tmp_ith.getString("photo"));
                        s.setFollow_count((tmp_ith.getInt("count_follow")));
                        s.setLike_count((tmp_ith.getInt("count_like")));
                        s.setComment_count((tmp_ith.getInt("count_comment")));
                        s.setLocation((tmp_ith.getString("location")));
                        s.setUser_photo((tmp_ith.getString("user_photo") == "null") ? "" : tmp_ith.getString("user_photo").substring(0, tmp_ith.getString("user_photo").length() - 4) + ".100x100.jpg");
                        s.setAllday((tmp_ith.getBoolean("all_day")));
                        s.setMaster((tmp_ith.getInt("master") == 1 ? true : false));
                        Concise_Schedule cs = new Concise_Schedule(s);
                        cs.setIsLike((tmp_ith.getInt("like")==1)?true:false);
                        cs.setIsFollow((tmp_ith.getInt("follow") == 1) ? true : false);

                        arrayCSchedule.add(cs);
                        concise_schedule_adapter.notifyDataSetChanged();
                    }
                    if(arrayCSchedule.isEmpty())
                        f1_text.setVisibility(View.VISIBLE);
                    else
                        f1_text.setVisibility(View.INVISIBLE);
                }catch(Exception e){

                }

            }
        }
    }

    class HTTPRestfulUtilizerExtender_loadmore extends HTTPRestfulUtilizer {

        // Constructor for GET
        public HTTPRestfulUtilizerExtender_loadmore(Context mContext, String url, String HTTPRestType) {
            setmContext(mContext);
            setUrl(url);
            setHTTPRestType(HTTPRestType);
            task = new HttpAsyncTaskExtenders();
            Log.d("HTTP Constructor url", url);
            // new HttpAsyncTask().execute(url,HTTPRestType);
        }

        @Override
        public void doExecution(){
            task.execute(getUrl(),getHTTPRestType());
        }
        class HttpAsyncTaskExtenders extends HTTPRestfulUtilizer.HttpAsyncTask{
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

                try{
                    outputSchedulesJson = getOutputJsonObject();

                    JSONArray results = outputSchedulesJson.getJSONArray("results");
                    JSONObject tmp_ith;
                    nextURL = outputSchedulesJson.getString("next");
                    for(int i=0; i<results.length();i++){
                        Schedule s = new Schedule();
                        tmp_ith = results.getJSONObject(i);
                        s.setId(tmp_ith.getInt("id"));
                        s.setTitle(tmp_ith.getString("title"));
                        s.setStarttime(tmp_ith.getString("start_time"));
                        s.setEndtime(tmp_ith.getString("end_time"));
                        s.setMemo(tmp_ith.getString("memo"));
                        s.setUploaded_username(tmp_ith.getString("user_name"));
                        s.setUploaded_user_id(tmp_ith.getInt("user_id"));
                        s.setPhoto_dir_fromweb((tmp_ith.getString("photo") == "null") ? "" : tmp_ith.getString("photo").substring(0, tmp_ith.getString("photo").length() - 4) + ".800x200.jpg");
                        Log.d("photo_dir", s.getPhoto_dir_fromweb());
                        s.setFollow_count((tmp_ith.getInt("count_follow")));
                        s.setLike_count((tmp_ith.getInt("count_like")));
                        s.setComment_count((tmp_ith.getInt("count_comment")));
                        s.setLocation((tmp_ith.getString("location")));
                        s.setUser_photo((tmp_ith.getString("user_photo") == "null") ? "" : tmp_ith.getString("user_photo").substring(0, tmp_ith.getString("user_photo").length() - 4) + ".100x100.jpg");
                        s.setAllday((tmp_ith.getBoolean("all_day")));
                        s.setMaster((tmp_ith.getInt("master") == 1 ? true : false));
                        Concise_Schedule cs = new Concise_Schedule(s);
                        cs.setIsLike((tmp_ith.getInt("like")==1)?true:false);
                        cs.setIsFollow((tmp_ith.getInt("follow") == 1) ? true : false);
                        arrayCSchedule.add(cs);
                        concise_schedule_adapter.notifyDataSetChanged();
                    }

                }catch(Exception e){

                }

            }
        }
    }

}
