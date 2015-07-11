package net.whend.soodal.whend.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.base.Comment;
import net.whend.soodal.whend.model.top.Concise_Schedule;

import java.util.ArrayList;

/**
 * Created by wonkyung on 15. 7. 11.
 */
public class Comment_Adapter extends ArrayAdapter<Comment> {
    private ArrayList<Comment> Comment_list;
    private Context context;

    public Comment_Adapter(Context context, int textViewResourceId, ArrayList<Comment> lists){
        super(context, textViewResourceId, lists);
        this.Comment_list = lists;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_comments, null);
        }


        // 리스너 함수들
        /*
        View user = (View)v.findViewById(R.id.user_clickableLayout);
        View comment_writer = (View)v.findViewById(R.id.comment_writer);
        ImageView like_button = (ImageView)v.findViewById(R.id.like_button);
        ImageView follow_button = (ImageView)v.findViewById(R.id.follow_button);
        UserProfileClickListener(user, position);
        UserProfileClickListener(comment_writer,position);
        LikeButtonClickListener(like_button, position);
        FollowButtonClickListener(follow_button, position);
        */
        return v;
    }
}
