package net.whend.soodal.whend.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

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

    ArrayList<Concise_Schedule> CSchedule_list;
    ListView listview;
    private static JSONObject outputSchedulesJson;
    private Concise_Schedule_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a7_specifichashtag_layout);

        Intent intent = new Intent(this.getIntent());
        String s = intent.getStringExtra("text");                   // 훗날 유저 정보를 받기위한 URL을 받아올 때 사용할것이니라.
        //    TextView textView=(TextView)findViewById(R.id.textview);
        //    textView.setText(s);

        CSchedule_list = new ArrayList<Concise_Schedule>();

        adapter = new Concise_Schedule_Adapter(this, R.layout.item_concise_schedule, CSchedule_list);
        listview = (ListView) findViewById(R.id.listview_schedule);
        listview.setAdapter(adapter);

        String url = "http://119.81.176.245/schedules/";

        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(url,"GET");
        a.doExecution();

    }

    class HTTPRestfulUtilizerExtender extends HTTPRestfulUtilizer {

        // Constructor for GET
        public HTTPRestfulUtilizerExtender(String url, String HTTPRestType) {

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
                    JSONArray tmp_results = outputSchedulesJson.getJSONArray("results");
                    JSONObject tmp_ith;
                    for(int i=0; i< outputSchedulesJson.getInt("count");i++){
                        Schedule s = new Schedule();
                        tmp_ith = tmp_results.getJSONObject(i);
                        s.setTitle(tmp_ith.getString("title"));
                        s.setStarttime(tmp_ith.getString("starttime"));
                        s.setEndtime(tmp_ith.getString("endtime"));
                        s.setStarttime_ms(tmp_ith.getLong("starttime_ms"));
                        s.setEndtime_ms(tmp_ith.getLong("endtime_ms"));
                        s.setMemo(tmp_ith.getString("memo"));
                        s.setUploaded_username(tmp_ith.getString("user"));
                        Concise_Schedule cs = new Concise_Schedule(s);
                        CSchedule_list.add(cs);
                    }
                    adapter.notifyDataSetChanged();
                }catch(Exception e){

                }

            }
        }
    }

}