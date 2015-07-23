package net.whend.soodal.whend.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.FragmentManager;
import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.base.User;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by wonkyung on 15. 7. 11.
 */
public class A2_UserProfileActivity extends AppCompatActivity {


    JSONObject outputSchedulesJson;

    User u = new User();

    private FragmentTabHost mTabHost;
    private int user_id;

    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.abc_popup_enter, R.anim.abc_fade_out);
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a2_userprofile_layout);

        Intent intent=new Intent(this.getIntent());
        user_id=intent.getIntExtra("id",0);                   // 훗날 유저 정보를 받기위한 URL을 받아올 때 사용할것이니라.
        Log.d("user_id",user_id+"");
    //    TextView textView=(TextView)findViewById(R.id.textview);
    //    textView.setText(s);

        Toolbar toolbar = (Toolbar) findViewById(R.id.user_toolbar);
        setSupportActionBar(toolbar);


        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager() , R.id.realtabcontent);
        Bundle inputBundle = new Bundle();
        inputBundle.putInt("id",user_id);
        mTabHost.addTab(mTabHost.newTabSpec("searchschedule").setIndicator("MY"),
                F5_1_MyTimeline.class, inputBundle);
        mTabHost.addTab(mTabHost.newTabSpec("searchhashtag").setIndicator("관심"),
                F5_2_MyLikeSchedules.class, inputBundle);
        mTabHost.addTab(mTabHost.newTabSpec("searchuser").setIndicator("분석"),
                F2_1_3_SearchUser.class, null);

        String url = "http://119.81.176.245/userinfos/"+user_id;

        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(this,url,"GET");
        a.doExecution();

        View schedule_count_clickablelayout = findViewById(R.id.schedule_count_clickablelayout);
        View follower_count_clickablelayout = findViewById(R.id.follower_count_clickablelayout);
        View following_count_clickablelayout =findViewById(R.id.following_count_clickablelayout);

        FollowerClickListener(follower_count_clickablelayout);
        FollowingClickListener(following_count_clickablelayout);
        ScheduleClickListener(schedule_count_clickablelayout);
    }
    // 게시물 누를때 리스너
    public void ScheduleClickListener(View schedule_count_clickablelayout){
        schedule_count_clickablelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(A2_UserProfileActivity.this, A10_ShowSchedulesActivity.class);
                intent.putExtra("id", u.getId());
                startActivity(intent);
            }
        });

    }
    // 팔로워 누를때 리스너
    public void FollowerClickListener(View follower_count_clickablelayout){
        follower_count_clickablelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(A2_UserProfileActivity.this, A5_WhoFollowsScheduleActivity.class);
                intent.putExtra("url", "http://119.81.176.245/userinfos/"+u.getId()+"/followers/");
                startActivity(intent);
            }
        });

    }
    // 팔로잉 누를때 리스너
    public void FollowingClickListener(View follower_count_clickablelayout){
        follower_count_clickablelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(A2_UserProfileActivity.this, A9_ShowFollowingObjectsActivity.class);
                intent.putExtra("url", u.getId());
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
                    JSONObject tmp_ith = outputSchedulesJson;


                    tmp_ith = outputSchedulesJson;
                    u.setId(tmp_ith.getInt("user_id"));
                    u.setUsername(tmp_ith.getString("user_name"));
                    u.setPhoto(tmp_ith.getString("photo") == null ? "" : tmp_ith.getString("photo"));
                    u.setCount_following_user(tmp_ith.getInt("count_following_user"));
                    u.setCount_following_hashtag(tmp_ith.getInt("count_following_hashtag"));
                    u.setCount_follower(tmp_ith.getInt("count_follower"));
                    u.setCount_uploaded_schedule(tmp_ith.getInt("count_uploaded_schedule"));
                    JSONArray tmpjsonarray = tmp_ith.getJSONArray("following_user");
                    if(tmpjsonarray!=null) {
                        int[] following_user = new int[tmpjsonarray.length()];
                        // Extract numbers from JSON array.
                        for (int i = 0; i < tmpjsonarray.length(); ++i) {
                            following_user[i] = tmpjsonarray.optInt(i);
                        }
                        u.setFollowing_user(following_user);
                    }

                    tmpjsonarray = tmp_ith.getJSONArray("following_hashtag");

                    if(tmpjsonarray!=null) {
                        int[] following_hashtag = new int[tmpjsonarray.length()];
                        // Extract numbers from JSON array.
                        for (int i = 0; i < tmpjsonarray.length(); ++i) {
                            following_hashtag[i] = tmpjsonarray.optInt(i);
                        }
                        u.setFollowing_hashtag(following_hashtag);
                    }

                    tmpjsonarray = tmp_ith.getJSONArray("like_schedule");

                    if(tmpjsonarray!=null) {
                        int[] like_schedule = new int[tmpjsonarray.length()];
                        // Extract numbers from JSON array.
                        for (int i = 0; i < tmpjsonarray.length(); ++i) {
                            like_schedule[i] = tmpjsonarray.optInt(i);
                        }
                        u.setLike_schedule(like_schedule);
                    }

                    tmpjsonarray = tmp_ith.getJSONArray("following_schedule");
                    if(tmpjsonarray!=null) {
                        int[] following_schedule = new int[tmpjsonarray.length()];
                        // Extract numbers from JSON array.
                        for (int i = 0; i < tmpjsonarray.length(); ++i) {
                            following_schedule[i] = tmpjsonarray.optInt(i);
                        }
                        u.setFollowing_schedule(following_schedule);
                    }
                }catch(Exception e){

                }
                ((TextView)findViewById(R.id.title)).setText(u.getUsername());

                ((TextView)findViewById(R.id.username)).setText(u.getUsername());
                ((TextView)findViewById(R.id.follower_count)).setText(u.getCount_follower() + "");
                ((TextView) findViewById(R.id.schedule_count)).setText(u.getCount_uploaded_schedule() + "");
                ((TextView)findViewById(R.id.following_count)).setText(String.valueOf(u.getCount_following_hashtag() + u.getCount_following_user()));

            }
        }
    }


}
