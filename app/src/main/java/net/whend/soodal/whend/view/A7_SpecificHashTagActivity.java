package net.whend.soodal.whend.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
    private static JSONObject outputSchedulesJson;
    private Concise_Schedule_Adapter adapter;
    int id,count_schedule,count_upcoming_schedule;
    private String title, photo;
    private int follower_count;
    static String nextURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a7_specifichashtag_layout);

        Intent intent = new Intent(this.getIntent());
        id = intent.getIntExtra("id", 0);                   // 훗날 유저 정보를 받기위한 URL을 받아올 때 사용할것이니라.
        title = intent.getStringExtra("title");
        follower_count = intent.getIntExtra("follower_count", 0);
        photo = intent.getStringExtra("photo");
        count_schedule = intent.getIntExtra("count_schedule",0);
        count_upcoming_schedule = intent.getIntExtra("count_upcoming_schedule",0);
        TextView title_view=(TextView)findViewById(R.id.title);
        TextView follower_count_view = (TextView)findViewById(R.id.follower_count);
        TextView schedule_count_view = (TextView)findViewById(R.id.schedule_count);
        TextView uploaded_schedule_count_view = (TextView)findViewById(R.id.comming_count);

        ImageView title_photo=(ImageView)findViewById(R.id.photo);

        if(!photo.equals("")) {

            Picasso.with(this).load(photo).into(title_photo);
        }


        title_view.setText("#" +title);
        follower_count_view.setText(follower_count+"");
        schedule_count_view.setText(count_schedule+"");
        uploaded_schedule_count_view.setText(count_upcoming_schedule+"");
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
                    outputSchedulesJson = getOutputJsonObject();
                    JSONArray results = outputSchedulesJson.getJSONArray("results");
                    JSONObject tmp_ith;
                    nextURL = outputSchedulesJson.getString("next");
                    for(int i=0; i< results.length() ;i++){
                        Schedule s = new Schedule();
                        tmp_ith = results.getJSONObject(i);
                        s.setId(tmp_ith.getInt("id"));
                        s.setTitle(tmp_ith.getString("title"));
                        s.setStarttime(tmp_ith.getString("start_time"));
                        s.setEndtime(tmp_ith.getString("end_time"));
                        s.setMemo(tmp_ith.getString("memo"));
                        s.setUploaded_username(tmp_ith.getString("user_name"));
                        s.setPhoto_dir_fromweb((tmp_ith.getString("photo") == "null") ? "" : tmp_ith.getString("photo").substring(0, tmp_ith.getString("photo").length() - 4) + ".800x200.jpg");
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