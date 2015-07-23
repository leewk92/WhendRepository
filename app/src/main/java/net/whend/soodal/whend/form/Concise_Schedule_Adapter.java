package net.whend.soodal.whend.form;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ComposeShader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.model.top.Concise_Schedule;
import net.whend.soodal.whend.util.CalendarProviderUtil;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;
import net.whend.soodal.whend.util.PicassoImageTool;
import net.whend.soodal.whend.view.A2_UserProfileActivity;
import net.whend.soodal.whend.view.A5_WhoFollowsScheduleActivity;
import net.whend.soodal.whend.view.A6_WriteCommentActivity;
import net.whend.soodal.whend.view.F5_Mypage;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.SelectableChannel;
import java.util.ArrayList;

/**
 * Wall 에서 일정을 간단한 카드 형식 리스트로 보여주기 위한 어답터
 * Created by wonkyung on 15. 7. 9.
 */
public class Concise_Schedule_Adapter extends ArrayAdapter<Concise_Schedule> {

    private ArrayList<Concise_Schedule> CSchedule_list;
    private Context context;

    public Concise_Schedule_Adapter(Context context, int textViewResourceId, ArrayList<Concise_Schedule> lists){
        super(context, textViewResourceId, lists);
        this.CSchedule_list = lists;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_concise_schedule, null);
        }
        AdjustDataToLayout(v,position);
        // 리스너 함수들
        View user_clickableLayout = (View)v.findViewById(R.id.user_clickableLayout);
        //View comment_writer = (View)v.findViewById(R.id.comment_writer);
        ImageView like_button = (ImageView)v.findViewById(R.id.like_button);
        ImageView follow_button = (ImageView)v.findViewById(R.id.follow_button);
        ImageView comment_button = (ImageView)v.findViewById(R.id.comment_button);
        View schedulefollow_user_clickablelayout = (View)v.findViewById(R.id.schedulefollow_user_clickablelayout);
        TextView like_count = (TextView)v.findViewById(R.id.like_count);
        TextView follow_count = (TextView)v.findViewById(R.id.follow_count);


        UserProfileClickListener(user_clickableLayout, position);
        //UserProfileClickListener(comment_writer,position);
        LikeButtonClickListener(like_button, like_count,position);
        WriteCommentClickListener(comment_button,position);
        FollowButtonClickListener(follow_button, follow_count, position);
        WhoFollowsScheduleClickListener(schedulefollow_user_clickablelayout, position);
        return v;
    }

    // 유저 이름 누를 때 리스너
    public void UserProfileClickListener(View userview,final int position){
        userview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, A2_UserProfileActivity.class);
                intent.putExtra("id", CSchedule_list.get(position).getUser_id());
                context.startActivity(intent);


            }
        });

    }

    // 외 15명 누를 때 리스너
    public void WhoFollowsScheduleClickListener(View schedulefollow_user_clickablelayout,final int position){
        schedulefollow_user_clickablelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, A5_WhoFollowsScheduleActivity.class);
                intent.putExtra("url", String.valueOf("http://119.81.176.245/schedules/"+ CSchedule_list.get(position).getId()+"/followers/"));       // 나중에 해결
                context.startActivity(intent);
            }
        });

    }


    // 좋아요 누를 때 리스너
    public void LikeButtonClickListener(ImageView likebutton,TextView like_count, int position){

        final int pos = position;
        final ImageView iv = likebutton;
        final TextView lcv = like_count;
        likebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CSchedule_list.get(pos).getIsLike() == false){
                    Toast toast1 = Toast.makeText(context, "Like Button Clicked", Toast.LENGTH_SHORT);
                    toast1.show();
                    String url = "http://119.81.176.245/schedules/"+CSchedule_list.get(pos).getId()+"/like/";
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url,"PUT");
                    a.doExecution();
                    CSchedule_list.get(pos).clickLike();
                    lcv.setText(String.valueOf(CSchedule_list.get(pos).getLike_count()));
                    iv.setImageResource(R.drawable.like_on);
                }
                else if(CSchedule_list.get(pos).getIsLike() == true){
                    Toast toast2 = Toast.makeText(context, "Like Button Unclicked", Toast.LENGTH_SHORT);
                    toast2.show();
                    String url = "http://119.81.176.245/schedules/"+CSchedule_list.get(pos).getId()+"/like/";
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url,"PUT");
                    a.doExecution();
                    CSchedule_list.get(pos).clickLike();
                    lcv.setText(String.valueOf(CSchedule_list.get(pos).getLike_count()));
                    iv.setImageResource(R.drawable.like);
                }

            }
        });

    }

    // 받아보기 누를 때 리스너
    public void FollowButtonClickListener(ImageView followbutton, TextView follow_count, int position){

        final int pos = position;
        final ImageView iv = followbutton;
        final TextView fcv = follow_count;
        followbutton.setOnClickListener(new View.OnClickListener() {

            CalendarProviderUtil cpu = new CalendarProviderUtil(context);
            @Override
            public void onClick(View v) {

                if(CSchedule_list.get(pos).getIsFollow() == false){
                    Toast toast1 = Toast.makeText(context, "Follow Button Clicked", Toast.LENGTH_SHORT);
                    toast1.show();
                    String url = "http://119.81.176.245/schedules/"+CSchedule_list.get(pos).getId()+"/follow/";
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url,"PUT");
                    a.doExecution();
                    CSchedule_list.get(pos).clickFollow();
                    fcv.setText(String.valueOf(CSchedule_list.get(pos).getFollow_count()));
                    iv.setImageResource(R.drawable.export_to_calendar_onclick);
                    cpu.addScheduleToInnerCalendar(CSchedule_list.get(pos));
                }
                else if(CSchedule_list.get(pos).getIsFollow() == true){
                    Toast toast2 = Toast.makeText(context, "Follow Button Unclicked", Toast.LENGTH_SHORT);
                    toast2.show();
                    String url = "http://119.81.176.245/schedules/"+CSchedule_list.get(pos).getId()+"/follow/";
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url,"PUT");
                    a.doExecution();
                    CSchedule_list.get(pos).clickFollow();
                    fcv.setText(String.valueOf(CSchedule_list.get(pos).getFollow_count()));
                    iv.setImageResource(R.drawable.exporttocalendar);

                    cpu.deleteScheduleFromInnerCalendar(CSchedule_list.get(pos));
                }

            }
        });

    }

    // 댓글달기 아이콘 누를 때 리스너
    public void WriteCommentClickListener(ImageView comment_button,int position){
        final int pos = position;

        comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, A6_WriteCommentActivity.class);
                intent.putExtra("id", CSchedule_list.get(pos).getId());
                context.startActivity(intent);
            }
        });

    }

    // 레이아웃에 데이터 적용
    public void AdjustDataToLayout(final View v,int position){

        ((TextView)v.findViewById(R.id.user_fullname)).setText(CSchedule_list.get(position).getUsername());
        ((TextView)v.findViewById(R.id.title)).setText(CSchedule_list.get(position).getTitle());
        ((TextView)v.findViewById(R.id.date)).setText(CSchedule_list.get(position).getDate());
        ((TextView)v.findViewById(R.id.time)).setText(CSchedule_list.get(position).getTime());
        ((TextView)v.findViewById(R.id.memo)).setText(CSchedule_list.get(position).getMemo());
        ((TextView)v.findViewById(R.id.like_count)).setText(String.valueOf(CSchedule_list.get(position).getLike_count()));
        ((TextView)v.findViewById(R.id.follow_count)).setText(String.valueOf(CSchedule_list.get(position).getFollow_count()));
        Log.d("like",String.valueOf(CSchedule_list.get(position).getIsLike()));
        if(CSchedule_list.get(position).getIsLike() == true)
            ((ImageView)v.findViewById(R.id.like_button)).setImageResource(R.drawable.like_on);
        else
            ((ImageView)v.findViewById(R.id.like_button)).setImageResource(R.drawable.like);

        if(CSchedule_list.get(position).getIsFollow() == true)
            ((ImageView)v.findViewById(R.id.follow_button)).setImageResource(R.drawable.export_to_calendar_onclick);
        else
            ((ImageView)v.findViewById(R.id.follow_button)).setImageResource(R.drawable.exporttocalendar);


        if(CSchedule_list.get(position).getPhoto_dir_fromweb()!=null) {


            // textview의 배경으로 그림을 넣기위한 원경의 사투 but 안쓰임

            /*
            com.squareup.picasso.Target target = new com.squareup.picasso.Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                    v.findViewById(R.id.memo).setBackground(drawable);

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };
            */

            Picasso.with(context).load(CSchedule_list.get(position).getPhoto_dir_fromweb()).into((ImageView)v.findViewById(R.id.memo_photo));

        }


/*
        PicassoImageToolExtender a = new PicassoImageToolExtender(context,

                CSchedule_list.get(position).getPhoto_dir_fromweb(),
                CSchedule_list.get(position).getPhoto_dir());
        a.doSomething();
        ((TextView)v.findViewById(R.id.memo)).setBackgroundResource(a.getGeneratedId());
*/
    }


    class PicassoImageToolExtender extends PicassoImageTool{
        public PicassoImageToolExtender(Context context, String photo_dir_fromweb, String photo_dir){
            setmContext(context);
            setPhoto_dir_fromweb(photo_dir_fromweb);
            setPhoto_dir(photo_dir);
        }

    }

    class HTTPRestfulUtilizerExtender extends HTTPRestfulUtilizer {

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
                setOutputString(PUT(url, getInputBundle()));

                return getOutputString();
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

            }
        }
    }

}
