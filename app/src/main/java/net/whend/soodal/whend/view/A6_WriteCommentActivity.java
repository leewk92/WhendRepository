package net.whend.soodal.whend.view;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.WriteComment_Adapter;
import net.whend.soodal.whend.model.base.Comment;
import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.model.top.Concise_Schedule;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wonkyung on 15. 7. 12.
 */
public class A6_WriteCommentActivity extends Activity {
    ArrayList<Comment> Comment_list = new ArrayList<Comment>();
    ListView listview;
    WriteComment_Adapter adapter;
    JSONObject outputSchedulesJson;
    private int id;
    String url;
    static String nextURL;
    EditText comment_content;
    Button comment_write_button;
    Bundle inputBundle = new Bundle();
    Context mContext = this;

    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.abc_popup_enter, R.anim.abc_fade_out);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a6_writecomment_layout);
        Intent intent = new Intent(this.getIntent());
        id = intent.getIntExtra("id", 0);                   // 훗날 유저 정보를 받기위한 URL을 받아올 때 사용할것이니라.
        url = "http://119.81.176.245/schedules/" + id + "/comments/";

        adapter = new WriteComment_Adapter(this, R.layout.item_writecomments, Comment_list);

        comment_content = (EditText) findViewById(R.id.comment_content);
        comment_write_button = (Button) findViewById(R.id.comment_write_button);

        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(this, url, "GET");
        a.doExecution();

        WriteCommentButtonClickListener(this, comment_write_button, comment_content);

        listview = (ListView) findViewById(R.id.listview_comments);
        listview.setAdapter(adapter);
        listview.setOnScrollListener(new EndlessScrollListener());

        // Keyboard 위치에 따라 입력칸의 높이 다르게 만들기
        final LinearLayout Linear_listview = (LinearLayout)findViewById(R.id.linear_listview);
        final View activityRootView = findViewById(R.id.a6_writecomment);
        final View edittext_comment = findViewById(R.id.comment_content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int rootHeight = activityRootView.getRootView().getHeight();
                int presentHeight = activityRootView.getHeight();
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                int edittext_comment_height = edittext_comment.getHeight();
                Log.d("rootHeight",rootHeight+"");
                Log.d("presentHeight",presentHeight+"");
                Log.d("heightDiff",heightDiff+"");
                Log.d("editText_comment_height",edittext_comment_height+"");
                if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
                    //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)pxFromDp(mContext,183));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(rootHeight-heightDiff-edittext_comment_height*4.5));// 왜 4.5인지는 모름.. 그냥 몇번 시도 끝에 찾은 값
                    Linear_listview.setLayoutParams(params);
                }
                else{
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(rootHeight-heightDiff-edittext_comment_height*4.5));
                    Linear_listview.setLayoutParams(params);
                }
            }
        });





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
                    HTTPRestfulUtilizerExtender_loadmore b = new HTTPRestfulUtilizerExtender_loadmore(A6_WriteCommentActivity.this,nextURL,"POST");
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

        // 댓글달기 아이콘 누를 때 리스너

    public void WriteCommentButtonClickListener(final Context context, Button comment_write_button, final EditText comment_content) {


        comment_write_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String content = comment_content.getText()+"";
                if(content == ""){
                    Toast toast1 = Toast.makeText(mContext, "댓글을 입력하세요.", Toast.LENGTH_SHORT);
                    toast1.setGravity(0,0,100);
                    toast1.show();
                }else{
                    inputBundle.clear();
                    inputBundle.putCharSequence("content", comment_content.getText());

                    HTTPRestfulUtilizerExtender2 b = new HTTPRestfulUtilizerExtender2(context,url,"POST",inputBundle);
                    b.doExecution();
                }
            }
        });

    }


    // for getting comments
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
                    for(int i=0; i<results.length() ;i++){
                        Comment s = new Comment();
                        tmp_ith = results.getJSONObject(i);
                        s.setContents(tmp_ith.getString("content"));
                        s.setWrite_username(tmp_ith.getString("user_name"));
                        s.setWrite_userid(tmp_ith.getInt("user_id"));

                        Comment_list.add(s);
                    }
                    adapter.notifyDataSetChanged();
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
                    for(int i=0; i<results.length() ;i++){
                        Comment s = new Comment();
                        tmp_ith = results.getJSONObject(i);
                        s.setContents(tmp_ith.getString("content"));
                        s.setWrite_username(tmp_ith.getString("user_name"));
                        s.setWrite_userid(tmp_ith.getInt("user_id"));

                        Comment_list.add(s);
                    }
                    adapter.notifyDataSetChanged();
                }catch(Exception e){

                }

            }
        }
    }

    // for sending comment
    class HTTPRestfulUtilizerExtender2 extends HTTPRestfulUtilizer {

        // Constructor for GET
        public HTTPRestfulUtilizerExtender2(Context mContext, String url, String HTTPRestType, Bundle inputBundle) {
            setInputBundle(inputBundle);
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

        class HttpAsyncTaskExtenders extends HTTPRestfulUtilizer.HttpAsyncTask {
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

                Comment s = new Comment();
                AppPrefs appPrefs = new AppPrefs(getmContext());

                s.setContents("" + inputBundle.getCharSequence("content"));
                s.setWrite_username(appPrefs.getUsername());
                s.setWrite_userid(appPrefs.getUser_id());

                Comment_list.add(s);

                adapter.notifyDataSetChanged();
                comment_content.setText("");
            }
        }
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}



