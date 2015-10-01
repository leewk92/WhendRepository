package net.whend.soodal.whend.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.Concise_Schedule_Adapter;
import net.whend.soodal.whend.model.base.HashTag;
import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.model.top.Concise_Schedule;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wonkyung on 15. 7. 14.
 */
public class A7_SpecificHashTagActivity extends AppCompatActivity {

    ArrayList<Concise_Schedule> CSchedule_list= new ArrayList<Concise_Schedule>();
    ListView listview;
    private static JSONObject outputSchedulesJson;
    private Concise_Schedule_Adapter adapter;
    int id,count_schedule,count_upcoming_schedule;
    static boolean is_follow;
    private String title, photo;
    private int follower_count;
    static String nextURL;
    Context context = this;
    TextView title_view,follower_count_view,schedule_count_view,uploaded_schedule_count_view;
    ImageView follow_button,title_photo;
    View follower_count_clickablelayout,comming_count_clickablelayout,schedule_count_clickablelayout;
    HashTag h = new HashTag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a7_specifichashtag_layout);

        Intent intent = new Intent(this.getIntent());
        id = intent.getIntExtra("id", 0);
/*        title = intent.getStringExtra("title");
        follower_count = intent.getIntExtra("follower_count", 0);
        photo = intent.getStringExtra("photo");
        count_schedule = intent.getIntExtra("count_schedule",0);
        count_upcoming_schedule = intent.getIntExtra("count_upcoming_schedule",0);
        is_follow = intent.getBooleanExtra("is_follow",true);
*/
        title_view=(TextView)findViewById(R.id.title);
        follower_count_view = (TextView)findViewById(R.id.follower_count);
        schedule_count_view = (TextView)findViewById(R.id.schedule_count);
        uploaded_schedule_count_view = (TextView)findViewById(R.id.comming_count);
        follow_button = (ImageView)findViewById(R.id.follow_button);

        follower_count_clickablelayout = (View)findViewById(R.id.follower_count_clickablelayout);
        comming_count_clickablelayout = (View)findViewById(R.id.comming_count_clickablelayout);
        schedule_count_clickablelayout = (View)findViewById(R.id.schedule_count_clickablelayout);
        title_photo=(ImageView)findViewById(R.id.photo);
/*
        if(!photo.equals("")) {

            Picasso.with(this).load(photo).into(title_photo);
        }
*/

        follow_button.setClickable(false);
/*
        title_view.setText("#" +title);
        follower_count_view.setText(follower_count+"");
        schedule_count_view.setText(count_schedule+"");
        uploaded_schedule_count_view.setText(count_upcoming_schedule+"");
        if(is_follow == true)
            follow_button.setImageResource(R.drawable.like_on);
        else
            follow_button.setImageResource(R.drawable.like);
*/
        adapter = new Concise_Schedule_Adapter(this, R.layout.item_concise_schedule, CSchedule_list);
        listview = (ListView) findViewById(R.id.listview_schedule);
        listview.setAdapter(adapter);

        listview.setOnScrollListener(new EndlessScrollListener());
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /*
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Concise_Schedule item = arrayCSchedule.get(position);

                            boolean waiting=false;
                            if ( a != null && a.getStatus() != AsyncTask.Status.FINISHED) {
                                refreshMailtask.cancel(true);
                                waiting=true;
                            }
                            if ( waiting ) {
                                MailItemListActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MailItemListActivity.this, "데이터 로딩중입니다. 잠시 기다리세요.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }

                            Intent intent = new Intent(getActivity(), A3_SpecificScheduleActivity.class);
                            intent.putExtra("id", arrayCSchedule.get(position).getId());
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.abc_popup_exit);

                            // 이후 생략
                        }
            */
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(context, A3_SpecificScheduleActivity.class);
                intent.putExtra("id", CSchedule_list.get(position).getId());
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.abc_popup_exit);
            }
        });

        String url = "http://119.81.176.245/hashtags/"+id+"/list/";

        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(this, url,"GET");
        a.doExecution();

        String url2 = "http://119.81.176.245/hashtags/"+id+"/";
        HTTPRestfulUtilizerExtender_hashtags b = new HTTPRestfulUtilizerExtender_hashtags(this, url2, "GET");
        b.doExecution();


    }


    // 좋아요 누를 때 리스너
    public void LikeButtonClickListener(ImageView follow_button) {

        final ImageView iv = follow_button;
        follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (is_follow == false) {
    //                Toast toast1 = Toast.makeText(context, "Like Button Clicked", Toast.LENGTH_SHORT);
    //                toast1.show();
                    is_follow = true;
                    iv.setImageResource(R.drawable.like_on);

                    String url = "http://119.81.176.245/hashtags/"+id+"/follow/";
                    HTTPRestfulUtilizerExtender_follow a = new HTTPRestfulUtilizerExtender_follow(context, url,"PUT");
                    a.doExecution();
                    follower_count++;
                    ((TextView)findViewById(R.id.follower_count)).setText(String.valueOf(follower_count));


                } else if (is_follow == true) {
    //                Toast toast2 = Toast.makeText(context, "Like Button Unclicked", Toast.LENGTH_SHORT);
    //                toast2.show();
                    is_follow = false;
                    iv.setImageResource(R.drawable.like);

                    String url = "http://119.81.176.245/hashtags/"+id+"/follow/";
                    HTTPRestfulUtilizerExtender_follow a = new HTTPRestfulUtilizerExtender_follow(context, url,"PUT");
                    a.doExecution();
                    follower_count--;
                    ((TextView) findViewById(R.id.follower_count)).setText(String.valueOf(follower_count));

                }
            }
        });
    }

    // 팔로워 누를때 리스너
    public void FollowerClickListener(View follower_count_clickablelayout){
        follower_count_clickablelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(A7_SpecificHashTagActivity.this, A5_WhoFollowsScheduleActivity.class);
                intent.putExtra("url", "http://119.81.176.245/hashtags/"+h.getId()+"/followers/");
                startActivity(intent);
            }
        });

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
                    Log.d("comein","comein");
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
                        s.setUploaded_user_id(tmp_ith.getInt("user_id"));
                        s.setPhoto_dir_fromweb((tmp_ith.getString("photo") == "null") ? "" : tmp_ith.getString("photo").substring(0, tmp_ith.getString("photo").length() - 4) + ".800x200.jpg");
                        s.setPhoto_full_fromweb((tmp_ith.getString("photo") == "null") ? "" : tmp_ith.getString("photo"));
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

                        CSchedule_list.add(cs);
                        adapter.notifyDataSetChanged();
                    }

                }catch(Exception e){

                }
            }
        }
    }
    class HTTPRestfulUtilizerExtender_follow extends HTTPRestfulUtilizer {

        public HTTPRestfulUtilizerExtender_follow(Context mContext, String url, String HTTPRestType) {
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
                setOutputString(PUT(url, getInputBundle()));

                return getOutputString();
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

            }
        }
    }

    // 엔드리스 스크롤 리스너
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
                    HTTPRestfulUtilizerExtender_loadmore b = new HTTPRestfulUtilizerExtender_loadmore(A7_SpecificHashTagActivity.this,nextURL,"GET");
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

    class HTTPRestfulUtilizerExtender_hashtags extends HTTPRestfulUtilizer {

        // Constructor for GET
        public HTTPRestfulUtilizerExtender_hashtags(Context mContext, String url, String HTTPRestType) {
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
                    Log.d("comein","comein");
                    outputSchedulesJson = getOutputJsonObject();
                    //JSONArray results = outputSchedulesJson.getJSONArray("results");
                    JSONObject tmp_ith=outputSchedulesJson;
                   // nextURL = outputSchedulesJson.getString("next");


                    h.setId(tmp_ith.getInt("id"));
                    title= (tmp_ith.getString("title"));
                    follower_count = (tmp_ith.getInt("count_follower"));
                    photo = ((tmp_ith.getString("photo") == "null") ? "" : tmp_ith.getString("photo").substring(0, tmp_ith.getString("photo").length() - 4) + ".800x200.jpg");
                    h.setContent(tmp_ith.getString("content"));
                    count_schedule=(tmp_ith.getInt("count_schedule"));
                    count_upcoming_schedule =(tmp_ith.getInt("count_upcoming_schedule"));
                    is_follow = (tmp_ith.getInt("is_follow") == 1 ? true : false);

                    title_view.setText("#" + title);
                    follower_count_view.setText(follower_count + "");
                    schedule_count_view.setText(count_schedule + "");
                    uploaded_schedule_count_view.setText(count_upcoming_schedule + "");
                    if(is_follow == true)
                        follow_button.setImageResource(R.drawable.like_on);
                    else
                        follow_button.setImageResource(R.drawable.like);
                    if(!photo.equals("")) {
                        Picasso.with(getmContext()).load(photo).into(title_photo);
                    }
                    LikeButtonClickListener(follow_button);
                    follow_button.setClickable(true);
                    FollowerClickListener(follower_count_clickablelayout);
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
                    Log.d("comein","comein");
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

                        CSchedule_list.add(cs);
                        adapter.notifyDataSetChanged();
                    }

                }catch(Exception e){

                }
            }
        }
    }



}