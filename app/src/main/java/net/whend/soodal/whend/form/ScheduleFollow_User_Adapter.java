package net.whend.soodal.whend.form;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.top.ScheduleFollow_User;
import net.whend.soodal.whend.view.A2_UserProfileActivity;

import java.util.ArrayList;

/**
 * Created by wonkyung on 15. 7. 12.
 */
public class ScheduleFollow_User_Adapter extends ArrayAdapter<ScheduleFollow_User> {
    private ArrayList<ScheduleFollow_User> User_list;
    private Context context;

    public ScheduleFollow_User_Adapter(Context context, int textViewResourceId, ArrayList<ScheduleFollow_User> lists){
        super(context, textViewResourceId, lists);
        this.User_list = lists;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_schedulefollow_user, null);
        }


        // 리스너 함수들
        View user = (View)v.findViewById(R.id.user_clickableLayout);
        ImageView like_button = (ImageView)v.findViewById(R.id.like_button);
        UserProfileClickListener(user, position);
        LikeButtonClickListener(like_button, position);

        return v;
    }

    // 유저 이름 누를때 리스너
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


    // 좋아요 누를 때 리스너
    public void LikeButtonClickListener(ImageView likebutton, int position){

        final int pos = position;
        final ImageView iv = likebutton;
        likebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(User_list.get(pos).getIsFollow() == false){
                    Toast toast1 = Toast.makeText(context, "Like Button Clicked", Toast.LENGTH_SHORT);
                    toast1.show();
                    User_list.get(pos).clickFollow();
                    iv.setImageResource(R.drawable.notice_on);          // 바꿔야됨 나중에
                }
                else if(User_list.get(pos).getIsFollow() == true){
                    Toast toast2 = Toast.makeText(context, "Like Button Unclicked", Toast.LENGTH_SHORT);
                    toast2.show();
                    User_list.get(pos).clickFollow();
                    iv.setImageResource(R.drawable.like);
                }

            }
        });

    }
}
