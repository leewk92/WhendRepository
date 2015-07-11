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
        /*
        // 리스너 함수들
        View user = (View)v.findViewById(R.id.user_clickableLayout);
        View comment_writer = (View)v.findViewById(R.id.comment_writer);
        ImageView like_button = (ImageView)v.findViewById(R.id.like_button);
        ImageView follow_button = (ImageView)v.findViewById(R.id.follow_button);
        UserProfileClickListener(user, position);
        UserProfileClickListener(comment_writer, position);
        LikeButtonClickListener(like_button, position);
        FollowButtonClickListener(follow_button, position);
*/
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

}
