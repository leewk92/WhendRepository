package net.whend.soodal.whend.form;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.base.Comment;
import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.model.top.Concise_Schedule;
import net.whend.soodal.whend.model.top.Specific_Schedule;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * 일정을 클릭하여 자세한 정보를 봐야 할 때
 * Created by wonkyung on 15. 7. 9.
 */
public class Specific_Schedule_Adapter extends Concise_Schedule_Adapter{

    private ArrayList<Concise_Schedule> CSchedule_list = new ArrayList<Concise_Schedule>();
    private ArrayList<Comment> Comment_list = new ArrayList<Comment>();
    private Context context;
    private ListView listview;
    private static JSONArray outputSchedulesJson;
    Comment_Adapter adapter;

    public Specific_Schedule_Adapter(Context context, int textViewResourceId, ArrayList<Concise_Schedule> lists){
        super(context, textViewResourceId, lists);
        this.CSchedule_list = lists;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_specific_schedule, null);
        }
        AdjustDataToLayout(v,position);

        // 리스너 함수들
        View user_clickableLayout = (View)v.findViewById(R.id.user_clickableLayout);
        ImageView like_button = (ImageView)v.findViewById(R.id.like_button);
        ImageView follow_button = (ImageView)v.findViewById(R.id.follow_button);
        ImageView comment_button = (ImageView)v.findViewById(R.id.comment_button);
        View schedulefollow_user_clickablelayout = (View)v.findViewById(R.id.schedulefollow_user_clickablelayout);
        TextView like_count = (TextView)v.findViewById(R.id.like_count);
        TextView follow_count = (TextView)v.findViewById(R.id.follow_count);

        UserProfileClickListener(user_clickableLayout, position);
        LikeButtonClickListener(like_button, like_count,position);
        FollowButtonClickListener(follow_button,follow_count, position);
        WriteCommentClickListener(comment_button, position);
        WhoFollowsScheduleClickListener(schedulefollow_user_clickablelayout, position);



        String url = "http://119.81.176.245/schedules/"+CSchedule_list.get(position).getId() + "/comments/";

        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url,"GET");
        a.doExecution();

        adapter = new Comment_Adapter(context,R.layout.item_comments,Comment_list);
        listview = (ListView) v.findViewById(R.id.listview_comments);
        listview.setAdapter(adapter);

        return v;
    }

    // 유저 이름 누를 때 리스너
    @Override
    public void UserProfileClickListener(View userview,int position) {
        super.UserProfileClickListener(userview,position);
    }

        // 외 15명 누를 때 리스너
    @Override
    public void WhoFollowsScheduleClickListener(View schedulefollow_user_clickablelayout,int position){
        super.WhoFollowsScheduleClickListener(schedulefollow_user_clickablelayout, position);

    }


    // 좋아요 누를 때 리스너
    @Override
    public void LikeButtonClickListener(ImageView likebutton, TextView like_count, int position){
        super.LikeButtonClickListener(likebutton, like_count, position);
    }

    // 받아보기 누를 때 리스너
    @Override
    public void FollowButtonClickListener(ImageView followbutton,TextView follow_count, int position){
        super.FollowButtonClickListener(followbutton,follow_count, position);
    }
    // 댓글달기 누를 때 리스너
    @Override
    public void WriteCommentClickListener(ImageView commentbutton, int position){
        super.WriteCommentClickListener(commentbutton,position);
    }


    // 레이아웃에 데이터 적용
    public void AdjustDataToLayout(final View v,int position) {

        ((TextView) v.findViewById(R.id.user_fullname)).setText(CSchedule_list.get(position).getUsername());
        ((TextView) v.findViewById(R.id.title)).setText(CSchedule_list.get(position).getTitle());

        ((TextView) v.findViewById(R.id.date_start)).setText(CSchedule_list.get(position).getDate_start());
        ((TextView) v.findViewById(R.id.time_start)).setText(CSchedule_list.get(position).getTime_start());

        ((TextView) v.findViewById(R.id.date_end)).setText(CSchedule_list.get(position).getDate_end());
        ((TextView) v.findViewById(R.id.time_end)).setText(CSchedule_list.get(position).getTime_end());

        ((TextView) v.findViewById(R.id.memo)).setText(CSchedule_list.get(position).getMemo());
        ((TextView)v.findViewById(R.id.like_count)).setText(String.valueOf(CSchedule_list.get(position).getLike_count()));
        ((TextView)v.findViewById(R.id.follow_count)).setText(String.valueOf(CSchedule_list.get(position).getFollow_count()));
        if(CSchedule_list.get(position).getIsLike() == true)
            ((ImageView)v.findViewById(R.id.like_button)).setImageResource(R.drawable.like_on);
        else
            ((ImageView)v.findViewById(R.id.like_button)).setImageResource(R.drawable.like);

        if(CSchedule_list.get(position).getIsFollow() == true)
            ((ImageView)v.findViewById(R.id.follow_button)).setImageResource(R.drawable.export_to_calendar_onclick);
        else
            ((ImageView)v.findViewById(R.id.follow_button)).setImageResource(R.drawable.exporttocalendar);

        if(CSchedule_list.get(position).getPhoto_dir_fromweb()!="") {
            Picasso.with(context).load(CSchedule_list.get(position).getPhoto_dir_fromweb()).into((ImageView) v.findViewById(R.id.memo_photo));
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
                    outputSchedulesJson = getOutputJsonArray();
                    JSONObject tmp_ith;
                    Log.d("resultslegnth",String.valueOf(outputSchedulesJson.length()));
                    for(int i=0; i<outputSchedulesJson.length() ;i++){
                        Comment s = new Comment();
                        tmp_ith = outputSchedulesJson.getJSONObject(i);
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

}
