package net.whend.soodal.whend.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.Grid_Search_Adapter;
import net.whend.soodal.whend.form.Grid_Search_For_Start_Adapter;
import net.whend.soodal.whend.model.base.HashTag;
import net.whend.soodal.whend.model.base.User;
import net.whend.soodal.whend.model.top.Grid_Search_Schedule;
import net.whend.soodal.whend.model.top.Search_HashTag;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;
import net.whend.soodal.whend.util.observablescrollview.ObservableScrollView;
import net.whend.soodal.whend.util.observablescrollview.ScrollViewListener;
import net.whend.soodal.whend.util.quitview.QuiltView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class A0_5_TagFollowingStart extends AppCompatActivity implements ScrollViewListener {

    ArrayList<Search_HashTag> arrayGSchedule = new ArrayList<Search_HashTag>();
    private Grid_Search_For_Start_Adapter grid_search_adapter;
    boolean loading=true;
    int threshold=250;
    int page=0;

    static String nextURL;
    public QuiltView quiltView;
    private LinearLayout next_layout;
    private JSONObject outputSchedulesJson;
    private User u = new User();

    public void onResume() {
        super.onResume();
        loading = true;
        page = 0;
        threshold = 250;
        //threshold

        arrayGSchedule.clear();
        grid_search_adapter.notifyDataSetChanged();

        quiltView = (QuiltView) findViewById(R.id.quilt);
        quiltView.setChildPadding(-1);

        String url = "http://119.81.176.245/hashtags/";
        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(this,url,"GET");
        a.doExecution();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a0_5_tagfollowingstart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.a0_5_toolbar);
        setSupportActionBar(toolbar);

        String url = "http://119.81.176.245/userinfos/";

        HTTPRestfulUtilizerExtender2 a = new HTTPRestfulUtilizerExtender2(this,url,"GET");
        a.doExecution();

        grid_search_adapter = new Grid_Search_For_Start_Adapter(this, R.layout.item_gridsearch_schedule_forstart, arrayGSchedule, u);
        next_layout=(LinearLayout)findViewById(R.id.a0_5_toolbar_next);
        next_layout.setVisibility(View.GONE);

        next_layout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
               Intent intent = new Intent(A0_5_TagFollowingStart.this, MainActivity.class);

                startActivity(intent);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                finish();

            }
        });
    }

    class HTTPRestfulUtilizerExtender2 extends HTTPRestfulUtilizer {

        // Constructor for GET
        public HTTPRestfulUtilizerExtender2(Context mContext, String url, String HTTPRestType) {
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
                    JSONObject tmp_ith = outputSchedulesJson;


                    tmp_ith = outputSchedulesJson;
                    u.setId(tmp_ith.getInt("user_id"));
                    u.setUsername(tmp_ith.getString("user_name"));
                    u.setUser_photo(tmp_ith.getString("photo") == "null" ? "" : tmp_ith.getString("photo").substring(0, tmp_ith.getString("photo").length() - 4) + ".100x100.jpg");
                    u.setCount_following_user(tmp_ith.getInt("count_following_user"));
                    u.setCount_following_hashtag(tmp_ith.getInt("count_following_hashtag"));
                    u.setCount_follower(tmp_ith.getInt("count_follower"));
                    u.setCount_uploaded_schedule(tmp_ith.getInt("count_uploaded_schedule"));

                    u.setFirstname(tmp_ith.getString("first_name"));
                    u.setLastname(tmp_ith.getString("last_name"));
                    u.setStatus(tmp_ith.getString("status"));
                } catch (Exception e) {

                }

            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_a0_5__tag_following_start, menu);
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
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {

        Log.d("scroll_y", y + " " + oldy);
        Log.d("scroll_position",scrollView.getVerticalScrollbarPosition()+"");
        Log.d("scroll_Y",scrollView.getY()+"");
        Log.d("scroll_getHeight",scrollView.getHeight()+"");
        if(y > threshold && loading){

            loading = false;
            String url = "http://119.81.176.245/hashtags/";
            HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(this,nextURL,"GET");
            a.doExecution();
            threshold = threshold +270*3;
            Log.d("scroll_threshold",threshold +"");
        }
        //Toast.makeText(getActivity(),"scrolled",Toast.LENGTH_SHORT);
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
                    for(int i=0; i<results.length() ;i++){
                        HashTag h = new HashTag();
                        tmp_ith = results.getJSONObject(i);

                        h.setId(tmp_ith.getInt("id"));
                        h.setTitle(tmp_ith.getString("title"));
                        h.setFollower_count(tmp_ith.getInt("count_follower"));
                        h.setPhoto((tmp_ith.getString("photo") == "null") ? "" : tmp_ith.getString("photo").substring(0, tmp_ith.getString("photo").length() - 4) + ".800x200.jpg");
                        h.setContent(tmp_ith.getString("content"));
                        h.setCount_schedule(tmp_ith.getInt("count_schedule"));
                        h.setCount_upcoming_schedule(tmp_ith.getInt("count_upcoming_schedule"));
                        h.setIs_Follow(tmp_ith.getInt("is_follow")==1?true:false);


                        Search_HashTag gs = new Search_HashTag(h);
                        arrayGSchedule.add(gs);
                        grid_search_adapter.notifyDataSetChanged();
                    }

                    for(int i=10*page; i< grid_search_adapter.getCount(); i++) {
                        quiltView.addPatchView2(grid_search_adapter.getView(i, null, null));
                    }
                }catch(Exception e){

                }finally{

                    ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.quilt_scroll);
                    scrollView.setScrollViewListener(A0_5_TagFollowingStart.this);

                }
                loading = true;
                page++;
            }
        }
    }
}
