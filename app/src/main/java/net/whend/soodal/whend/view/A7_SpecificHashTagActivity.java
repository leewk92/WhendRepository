package net.whend.soodal.whend.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.Concise_Schedule_Adapter;
import net.whend.soodal.whend.form.Specific_Schedule_Adapter;
import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.model.top.Concise_Schedule;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wonkyung on 15. 7. 14.
 */
public class A7_SpecificHashTagActivity extends Activity {

    ArrayList<Concise_Schedule> CSchedule_list= new ArrayList<Concise_Schedule>();
    ListView listview;
    private static JSONArray outputSchedulesJson;
    private Concise_Schedule_Adapter adapter;
    int id;
    private String title;
    private int follower_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a7_specifichashtag_layout);

        Intent intent = new Intent(this.getIntent());
        id = intent.getIntExtra("id",0);                   // 훗날 유저 정보를 받기위한 URL을 받아올 때 사용할것이니라.
        title = intent.getStringExtra("title");
        follower_count = intent.getIntExtra("follower_count",0);

        TextView title_view=(TextView)findViewById(R.id.title);
        TextView follower_count_view = (TextView)findViewById(R.id.follower_count);
        title_view.setText("#" +title);
        follower_count_view.setText(follower_count+"");

        adapter = new Concise_Schedule_Adapter(this, R.layout.item_concise_schedule, CSchedule_list);
        listview = (ListView) findViewById(R.id.listview_schedule);
        listview.setAdapter(adapter);

        String url = "http://119.81.176.245/hashtags/"+id+"/list/";

        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(this, url,"GET");
        a.doExecution();


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
            task.execute(getUrl(), getHTTPRestType());
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
                    outputSchedulesJson = getOutputJsonArray();
                    JSONObject tmp_ith;
                    Log.d("resultslegnth", String.valueOf(outputSchedulesJson.length()));
                    for(int i=0; i<outputSchedulesJson.length() ;i++){
                        Schedule s = new Schedule();
                        tmp_ith = outputSchedulesJson.getJSONObject(i);
                        s.setId(tmp_ith.getInt("id"));
                        s.setTitle(tmp_ith.getString("title"));
                        s.setStarttime(tmp_ith.getString("start_time"));
                        s.setEndtime(tmp_ith.getString("end_time"));
                        s.setMemo(tmp_ith.getString("memo"));
                        s.setUploaded_username(tmp_ith.getString("user_name"));
                        s.setPhoto_dir_fromweb(tmp_ith.getString("photo"));
                        s.setFollow_count((tmp_ith.getInt("count_follow")));
                        s.setLike_count((tmp_ith.getInt("count_like")));

                        Concise_Schedule cs = new Concise_Schedule(s);
                        cs.setIsLike(tmp_ith.getInt("like") == 1 ? true : false);
                        cs.setIsFollow(tmp_ith.getInt("follow") == 1 ? true : false);
                        CSchedule_list.add(cs);
                    }
                    adapter.notifyDataSetChanged();
                }catch(Exception e){

                }
            }
        }
    }

}