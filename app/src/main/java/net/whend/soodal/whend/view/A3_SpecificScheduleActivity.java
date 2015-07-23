package net.whend.soodal.whend.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.Specific_Schedule_Adapter;
import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.model.top.Concise_Schedule;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wonkyung on 15. 7. 11.
 */
public class A3_SpecificScheduleActivity extends Activity {

    ArrayList<Concise_Schedule> CSchedule_list = new ArrayList<Concise_Schedule>();;
    ListView listview;
    private int id;
    private static JSONObject outputSchedulesJson;
    Specific_Schedule_Adapter adapter;

    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.abc_popup_enter, R.anim.abc_fade_out);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a3_specificschedule_layout);

        Intent intent=new Intent(this.getIntent());
        id=intent.getIntExtra("id",0);                   // 훗날 유저 정보를 받기위한 URL을 받아올 때 사용할것이니라.
        //    TextView textView=(TextView)findViewById(R.id.textview);
        //    textView.setText(s);

        String url = "http://119.81.176.245/schedules/"+id+"/";

        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(this, url,"GET");
        a.doExecution();


        adapter = new Specific_Schedule_Adapter(this,R.layout.item_specific_schedule,CSchedule_list);
        listview = (ListView)findViewById(R.id.listview_specific_schedule);
        listview.setAdapter(adapter);
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
                    JSONObject tmp_ith;
                    Log.d("resultslegnth",String.valueOf(outputSchedulesJson.length()));

                    Schedule s = new Schedule();
                    tmp_ith = outputSchedulesJson;
                    s.setId(tmp_ith.getInt("id"));

                    s.setTitle(tmp_ith.getString("title"));
                    Log.d("title", s.getTitle());
                    s.setStarttime(tmp_ith.getString("start_time"));
                    s.setEndtime(tmp_ith.getString("end_time"));
                    s.setMemo(tmp_ith.getString("memo"));
                    s.setUploaded_username(tmp_ith.getString("user_name"));
                    s.setUploaded_user_id(tmp_ith.getInt("user_id"));
                    s.setPhoto_dir_fromweb((tmp_ith.getString("photo") == null) ? "" : tmp_ith.getString("photo"));
                    s.setFollow_count((tmp_ith.getInt("count_follow")));
                    s.setLike_count((tmp_ith.getInt("count_like")));

                    Concise_Schedule cs = new Concise_Schedule(s);
                    cs.setIsLike(tmp_ith.getInt("like")==1?true:false);
                    cs.setIsFollow(tmp_ith.getInt("follow")==1?true:false);
                    CSchedule_list.add(cs);

                    adapter.notifyDataSetChanged();
                }catch(Exception e){
                }
            }
        }
    }
}
