package net.whend.soodal.whend.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.base.Comment;
import net.whend.soodal.whend.model.top.Concise_Schedule;

import java.util.ArrayList;

/**
 * 일정을 클릭하여 자세한 정보를 봐야 할 때
 * Created by wonkyung on 15. 7. 9.
 */
public class Specific_Schedule_Adapter extends Concise_Schedule_Adapter{

    private ArrayList<Concise_Schedule> CSchedule_list;
    private ArrayList<Comment> Comment_list;
    private Context context;
    private ListView listview;

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

        // 리스너 함수들
        View user_clickableLayout = (View)v.findViewById(R.id.user_clickableLayout);
        ImageView like_button = (ImageView)v.findViewById(R.id.like_button);
        ImageView follow_button = (ImageView)v.findViewById(R.id.follow_button);
        View schedulefollow_user_clickablelayout = (View)v.findViewById(R.id.schedulefollow_user_clickablelayout);

        UserProfileClickListener(user_clickableLayout, position);
        LikeButtonClickListener(like_button, position);
        FollowButtonClickListener(follow_button, position);
        WhoFollowsScheduleClickListener(schedulefollow_user_clickablelayout, position);

        Comment_list = new ArrayList<Comment>();
        Comment a= new Comment();
        Comment_list.add(a);
        Comment_list.add(a);
        Comment_list.add(a);
        Comment_list.add(a);


        Comment_Adapter adapter = new Comment_Adapter(context,R.layout.item_comments,Comment_list);
        listview = (ListView) v.findViewById(R.id.listview_comments);
        listview.setAdapter(adapter);

        return v;
    }
    /*
    // 유저 이름 누를 때 리스너
    public void UserProfileClickListener(View userview,int position){
        userview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, A2_UserProfileActivity.class);
                intent.putExtra("text", String.valueOf("URL"));
                context.startActivity(intent);
            }
        });

    }

    // 외 15명 누를 때 리스너
    public void WhoFollowsScheduleClickListener(View schedulefollow_user_clickablelayout,int position){
        schedulefollow_user_clickablelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, A5_WhoFollowsScheduleActivity.class);
                intent.putExtra("text", String.valueOf("URL"));
                context.startActivity(intent);
            }
        });

    }


    // 좋아요 누를 때 리스너
    public void LikeButtonClickListener(ImageView likebutton, int position){

        final int pos = position;
        final ImageView iv = likebutton;
        likebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CSchedule_list.get(pos).getIsLike() == false){
                    Toast toast1 = Toast.makeText(context, "Like Button Clicked", Toast.LENGTH_SHORT);
                    toast1.show();
                    CSchedule_list.get(pos).clickLike();
                    iv.setImageResource(R.drawable.notice_on);          // 바꿔야됨 나중에
                }
                else if(CSchedule_list.get(pos).getIsLike() == true){
                    Toast toast2 = Toast.makeText(context, "Like Button Unclicked", Toast.LENGTH_SHORT);
                    toast2.show();
                    CSchedule_list.get(pos).clickLike();
                    iv.setImageResource(R.drawable.like);
                }

            }
        });

    }

    // 받아보기 누를 때 리스너
    public void FollowButtonClickListener(ImageView followbutton, int position){

        final int pos = position;
        final ImageView iv = followbutton;
        followbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CSchedule_list.get(pos).getIsFollow() == false){
                    Toast toast1 = Toast.makeText(context, "Follow Button Clicked", Toast.LENGTH_SHORT);
                    toast1.show();
                    CSchedule_list.get(pos).clickFollow();
                    iv.setImageResource(R.drawable.notice_on);          // 바꿔야됨 나중에
                }
                else if(CSchedule_list.get(pos).getIsFollow() == true){
                    Toast toast2 = Toast.makeText(context, "Follow Button Unclicked", Toast.LENGTH_SHORT);
                    toast2.show();
                    CSchedule_list.get(pos).clickFollow();
                    iv.setImageResource(R.drawable.exporttocalendar);
                }

            }
        });

    }*/

    // 유저 이름 누를 때 리스너
    @Override
    public void UserProfileClickListener(View userview,int position) {
        super.UserProfileClickListener(userview,position);
    }

        // 외 15명 누를 때 리스너
    @Override
    public void WhoFollowsScheduleClickListener(View schedulefollow_user_clickablelayout,int position){
        super.WhoFollowsScheduleClickListener(schedulefollow_user_clickablelayout,position);

    }


    // 좋아요 누를 때 리스너
    @Override
    public void LikeButtonClickListener(ImageView likebutton, int position){
        super.LikeButtonClickListener(likebutton,position);
    }

    // 받아보기 누를 때 리스너
    @Override
    public void FollowButtonClickListener(ImageView followbutton, int position){
        super.FollowButtonClickListener(followbutton,position);
    }

}
