package net.whend.soodal.whend.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.Comment_Adapter;
import net.whend.soodal.whend.model.base.Comment;
import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.model.top.Concise_Schedule;
import net.whend.soodal.whend.util.CalendarProviderUtil;
import net.whend.soodal.whend.util.CircleTransform;
import net.whend.soodal.whend.util.DateTimeFormatter;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by wonkyung on 15. 7. 11.
 */
public class A3_SpecificScheduleActivity extends AppCompatActivity {


    Concise_Schedule cs;
    ListView listview;
    private int id;
    private static JSONObject outputSchedulesJson;
    Comment_Adapter adapter;
    Context mContext = this;
    static String nextURL;
    private ArrayList<Comment> Comment_list = new ArrayList<Comment>();
    private ImageView memo_photo,user_photo;
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.abc_popup_enter, R.anim.push_right_out);
    }

/*
    public void onResume() {
        super.onResume();
        Comment_list.clear();
        String url = "http://119.81.176.245/schedules/"+id+"/";

        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(this, url,"GET");
        a.doExecution();
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a3_specificschedule_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_specificschedule);
        setSupportActionBar(toolbar);

                toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                        //finish();
                        overridePendingTransition(R.anim.abc_popup_enter, R.anim.push_right_out);
            }
        });

        Intent intent=new Intent(this.getIntent());
        id=intent.getIntExtra("id",0);                   // 훗날 유저 정보를 받기위한 URL을 받아올 때 사용할것이니라.
        //    TextView textView=(TextView)findViewById(R.id.textview);
        //    textView.setText(s);

        String url = "http://119.81.176.245/schedules/"+id+"/";

        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(this, url,"GET");
        a.doExecution();

        adapter = new Comment_Adapter(this,R.layout.item_comments,Comment_list);
        listview = (ListView) findViewById(R.id.listview_comments);
        listview.setAdapter(adapter);
        listview.setOnScrollListener(new EndlessScrollListener());
        ImageView edit = (ImageView)findViewById(R.id.edit);

        // 리스너 함수들

        View user_clickableLayout = (View)findViewById(R.id.user_clickableLayout);
        ImageView like_button = (ImageView)findViewById(R.id.like_button);
        ImageView follow_button = (ImageView)findViewById(R.id.follow_button);
        ImageView comment_button = (ImageView)findViewById(R.id.comment_button);
        View schedulefollow_user_clickablelayout = (View)findViewById(R.id.schedulefollow_user_clickablelayout);
        View schedulelike_user_clickablelayout = (View)findViewById(R.id.schedulelike_user_clickablelayout);
        TextView like_count = (TextView)findViewById(R.id.like_count);
        TextView follow_count = (TextView)findViewById(R.id.follow_count);
        memo_photo = (ImageView)findViewById(R.id.memo_photo);
        user_photo = (ImageView)findViewById(R.id.user_photo);

        EditClieckListener(edit);
        UserProfileClickListener(user_clickableLayout);
        LikeButtonClickListener(like_button, like_count);
        FollowButtonClickListener(follow_button, follow_count);
        WriteCommentClickListener(comment_button);
        WhoFollowsScheduleClickListener(schedulefollow_user_clickablelayout);
        WhoLikesScheduleClickListener(schedulelike_user_clickablelayout);

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
                        HTTPRestfulUtilizerExtender_comment b = new HTTPRestfulUtilizerExtender_comment(mContext, nextURL,"GET");
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
    // edit 버튼 리스너
    public void EditClieckListener(ImageView edit){
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setMessage("이 일정을 수정 또는 삭제할 수 있습니다.");
                builder1.setCancelable(true);
                builder1.setNegativeButton("수정하기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent = new Intent(mContext, A11_EditScheduleActivity.class);
                                intent.putExtra("id", cs.getId());
                                intent.putExtra("date_start", cs.getDate_start());
                                intent.putExtra("date_end", cs.getDate_end());
                                intent.putExtra("time_start", cs.getTime_start());
                                intent.putExtra("time_end", cs.getTime_end());
                                intent.putExtra("title", cs.getTitle());
                                intent.putExtra("location", cs.getLocation());
                                intent.putExtra("datetime_start", cs.getSchedule().getStarttime_ms());
                                intent.putExtra("datetime_end", cs.getSchedule().getEndtime_ms());
                                intent.putExtra("allday", cs.getSchedule().getAllday());
                                intent.putExtra("memo", cs.getMemo());

                                Activity activity = (Activity) mContext;
                                activity.startActivity(intent);
                                activity.overridePendingTransition(R.anim.abc_popup_enter, R.anim.abc_popup_exit);
                                dialog.cancel();
                            }
                        });
                builder1.setPositiveButton("삭제하기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String url_delete = "http://119.81.176.245/schedules/" + cs.getId() + "/";
                                HTTPRestfulUtilizerExtender_delete hd = new HTTPRestfulUtilizerExtender_delete(mContext, url_delete, "DELETE");
                                hd.doExecution();
                                dialog.cancel();

                                Intent intent = new Intent(mContext, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                mContext.startActivity(intent);
                                finish();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
    }

    // 유저 이름 누를 때 리스너
    public void UserProfileClickListener(View userview){
        userview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, A2_UserProfileActivity.class);
                intent.putExtra("id", cs.getUser_id());
                mContext.startActivity(intent);


            }
        });

    }

    // 받아보기 15명 누를 때 리스너
    public void WhoFollowsScheduleClickListener(View schedulefollow_user_clickablelayout){
        schedulefollow_user_clickablelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, A5_WhoFollowsScheduleActivity.class);
                intent.putExtra("url", String.valueOf("http://119.81.176.245/schedules/" + cs.getId() + "/followers/"));       // 나중에 해결
                mContext.startActivity(intent);
            }
        });

    }
    // 좋아요 15명 누를 때 리스너
    public void WhoLikesScheduleClickListener(View schedulelike_user_clickablelayout){
        schedulelike_user_clickablelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, A5_WhoFollowsScheduleActivity.class);
                intent.putExtra("url", String.valueOf("http://119.81.176.245/schedules/"+ cs.getId()+"/like_users/"));       // 나중에 해결
                mContext.startActivity(intent);
            }
        });

    }


    // 좋아요 누를 때 리스너
    public void LikeButtonClickListener(ImageView likebutton,TextView like_count){


        final ImageView iv = likebutton;
        final TextView lcv = like_count;
        likebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cs.getIsLike() == false){
     //               Toast toast1 = Toast.makeText(mContext, "Like Button Clicked", Toast.LENGTH_SHORT);
     //               toast1.show();
                    String url = "http://119.81.176.245/schedules/"+cs.getId()+"/like/";
                    HTTPRestfulUtilizerExtender_likefollow a = new HTTPRestfulUtilizerExtender_likefollow(mContext, url,"PUT");
                    a.doExecution();
                    cs.clickLike();
                    lcv.setText(String.valueOf(cs.getLike_count()));
                    iv.setImageResource(R.drawable.like_on);
                }
                else if(cs.getIsLike() == true){
      //              Toast toast2 = Toast.makeText(mContext, "Like Button Unclicked", Toast.LENGTH_SHORT);
      //              toast2.show();
                    String url = "http://119.81.176.245/schedules/"+cs.getId()+"/like/";
                    HTTPRestfulUtilizerExtender_likefollow a = new HTTPRestfulUtilizerExtender_likefollow(mContext, url,"PUT");
                    a.doExecution();
                    cs.clickLike();
                    lcv.setText(String.valueOf(cs.getLike_count()));
                    iv.setImageResource(R.drawable.like);
                }

            }
        });

    }

    // 받아보기 누를 때 리스너
    public void FollowButtonClickListener(ImageView followbutton, TextView follow_count){

        final ImageView iv = followbutton;
        final TextView fcv = follow_count;
        followbutton.setOnClickListener(new View.OnClickListener() {

            CalendarProviderUtil cpu = new CalendarProviderUtil(mContext);

            @Override
            public void onClick(View v) {

                if (cs.getIsFollow() == false) {
    //                Toast toast1 = Toast.makeText(mContext, "Follow Button Clicked", Toast.LENGTH_SHORT);
    //                toast1.show();
                    String url = "http://119.81.176.245/schedules/" + cs.getId() + "/follow/";
                    HTTPRestfulUtilizerExtender_likefollow a = new HTTPRestfulUtilizerExtender_likefollow(mContext, url, "PUT");
                    a.doExecution();
                    cs.clickFollow();
                    fcv.setText(String.valueOf(cs.getFollow_count()));
                    iv.setImageResource(R.drawable.export_to_calendar_onclick);
                    cpu.addScheduleToInnerCalendar(cs);
                } else if (cs.getIsFollow() == true) {
     //               Toast toast2 = Toast.makeText(mContext, "Follow Button Unclicked", Toast.LENGTH_SHORT);
    //                toast2.show();
                    String url = "http://119.81.176.245/schedules/" + cs.getId() + "/follow/";
                    HTTPRestfulUtilizerExtender_likefollow a = new HTTPRestfulUtilizerExtender_likefollow(mContext, url, "PUT");
                    a.doExecution();
                    cs.clickFollow();
                    fcv.setText(String.valueOf(cs.getFollow_count()));
                    iv.setImageResource(R.drawable.exporttocalendar);

                    cpu.deleteScheduleFromInnerCalendar(cs);
                }

            }
        });

    }

    // 댓글달기 아이콘 누를 때 리스너
    public void WriteCommentClickListener(ImageView comment_button){

        comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, A6_WriteCommentActivity.class);
                intent.putExtra("id", cs.getId());
                mContext.startActivity(intent);
            }
        });

    }


    // 레이아웃에 데이터 적용
    public void AdjustDataToLayout(Context mContext, Concise_Schedule cs) {

        ((TextView) findViewById(R.id.user_fullname)).setText(cs.getUsername());
        ((TextView) findViewById(R.id.title)).setText(cs.getTitle());
        ImageView edit = (ImageView) findViewById(R.id.edit);
        if(cs.getSchedule().isMaster() == true){
            edit.setVisibility(View.VISIBLE);
            edit.setClickable(true);
        }else {
            edit.setVisibility(View.INVISIBLE);
            edit.setClickable(false);
        }

        if(cs.getSchedule().getAllday() == true){

            View time_start_visiblelayout = findViewById(R.id.time_start_visiblelayout);
            time_start_visiblelayout.setVisibility(View.INVISIBLE);
            View time_end_visiblelayout = findViewById(R.id.time_end_visiblelayout);
            time_end_visiblelayout.setVisibility(View.INVISIBLE);
            DateTimeFormatter dtf = new DateTimeFormatter(cs.getSchedule().getEndtime_ms()-24*60*60*1000);// 종일일정이면 하루 빠져야함
            ((TextView) findViewById(R.id.date_end)).setText(dtf.getDate());
        }else{
            ((TextView) findViewById(R.id.date_end)).setText(cs.getDate_end());
        }
        ((TextView) findViewById(R.id.date_start)).setText(cs.getDate_start());
        ((TextView) findViewById(R.id.time_start)).setText(cs.getTime_start());
        ((TextView) findViewById(R.id.location)).setText(cs.getLocation());
     //   ((TextView) findViewById(R.id.date_end)).setText(cs.getDate_end());
        ((TextView) findViewById(R.id.time_end)).setText(cs.getTime_end());
        ((TextView) findViewById(R.id.comment_count)).setText(String.valueOf(cs.getComment_count()));

        ((TextView) findViewById(R.id.memo)).setText(cs.getMemo());
        ((TextView) findViewById(R.id.like_count)).setText(String.valueOf(cs.getLike_count()));
        ((TextView) findViewById(R.id.follow_count)).setText(String.valueOf(cs.getFollow_count()));
        if(cs.getIsLike() == true)
            ((ImageView)findViewById(R.id.like_button)).setImageResource(R.drawable.like_on);
        else
            ((ImageView)findViewById(R.id.like_button)).setImageResource(R.drawable.like);

        if(cs.getIsFollow() == true)
            ((ImageView)findViewById(R.id.follow_button)).setImageResource(R.drawable.export_to_calendar_onclick);
        else
            ((ImageView)findViewById(R.id.follow_button)).setImageResource(R.drawable.exporttocalendar);

        if(cs.getPhoto_dir_fromweb()!="") {
            Picasso.with(mContext).load(cs.getPhoto_dir_fromweb()).into((ImageView) findViewById(R.id.memo_photo));
        }else{
            //memo_photo.setBackgroundColor(Color.BLACK);
            Random random = new Random();
            int color = random.nextInt(4);
            switch (color){
                case 0:
                    memo_photo.setImageResource(R.drawable.memo_photo_default6);
                    break;
                case 1:
                    memo_photo.setImageResource(R.drawable.memo_photo_default2);
                    break;
                case 2:
                    memo_photo.setImageResource(R.drawable.memo_photo_default3);
                    break;
                case 3:
                    memo_photo.setImageResource(R.drawable.memo_photo_default4);
                    break;
                case 4:
                    memo_photo.setImageResource(R.drawable.memo_photo_default5);
                    break;

            }
        }

        if(cs.getUser_photo()!="") {
            Picasso.with(mContext).load(cs.getUser_photo()).transform(new CircleTransform()).into((ImageView)findViewById(R.id.user_photo));

        }else{
            // 기본이미지 로드.
            user_photo.setImageResource(R.drawable.userimage_default);
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
                    s.setPhoto_dir_fromweb((tmp_ith.getString("photo") == "null") ? "" : tmp_ith.getString("photo").substring(0, tmp_ith.getString("photo").length() - 4) + ".800x200.jpg");
                    s.setFollow_count((tmp_ith.getInt("count_follow")));
                    s.setLike_count((tmp_ith.getInt("count_like")));
                    s.setLocation((tmp_ith.getString("location")));
                    s.setComment_count((tmp_ith.getInt("count_comment")));
                    s.setUser_photo(tmp_ith.getString("user_photo") == "null" ? "" : tmp_ith.getString("user_photo").substring(0, tmp_ith.getString("user_photo").length() - 4) + ".100x100.jpg");
                    s.setAllday(tmp_ith.getBoolean("all_day"));
                    s.setMaster((tmp_ith.getInt("master") == 1 ? true : false));

                    cs = new Concise_Schedule(s);
                    cs.setIsLike(tmp_ith.getInt("like") == 1 ? true : false);
                    cs.setIsFollow(tmp_ith.getInt("follow") == 1 ? true : false);

                    AdjustDataToLayout(getmContext(),cs);

                    // getting comment
                    String url_comment = "http://119.81.176.245/schedules/"+cs.getId() + "/comments/";
                    Comment_list.clear();
                    adapter.notifyDataSetChanged();
                    HTTPRestfulUtilizerExtender_comment a = new HTTPRestfulUtilizerExtender_comment(getmContext(), url_comment,"GET");
                    a.doExecution();

                }catch(Exception e){
                }
            }
        }
    }

    class HTTPRestfulUtilizerExtender_comment extends HTTPRestfulUtilizer {

        // Constructor for GET
        public HTTPRestfulUtilizerExtender_comment(Context mContext, String url, String HTTPRestType) {
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
                    nextURL = outputSchedulesJson.getString("next");
                    JSONObject tmp_ith;


                    for (int i = 0; i < results.length() ;i++){
                        Comment s = new Comment();
                        tmp_ith = results.getJSONObject(i);
                        s.setContents(tmp_ith.getString("content"));
                        s.setWrite_username(tmp_ith.getString("user_name"));
                        s.setWrite_userid(tmp_ith.getInt("user_id"));

                        Comment_list.add(s);
                        adapter.notifyDataSetChanged();
                    }

                }catch(Exception e){

                }

            }
        }
    }
    class HTTPRestfulUtilizerExtender_likefollow extends HTTPRestfulUtilizer {

        public HTTPRestfulUtilizerExtender_likefollow(Context mContext, String url, String HTTPRestType) {
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
    class HTTPRestfulUtilizerExtender_delete extends HTTPRestfulUtilizer {

        public HTTPRestfulUtilizerExtender_delete(Context mContext, String url, String HTTPRestType) {
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
                setOutputString(DELETE(url));

                return getOutputString();
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

            }
        }
    }

}
