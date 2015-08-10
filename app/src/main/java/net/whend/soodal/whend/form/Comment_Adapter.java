package net.whend.soodal.whend.form;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.base.Comment;
import net.whend.soodal.whend.view.A2_UserProfileActivity;

import java.util.ArrayList;

/**
 * Created by wonkyung on 15. 7. 11.
 */
public class Comment_Adapter extends ArrayAdapter<Comment> {
    private ArrayList<Comment> Comment_list;
    private Context context;
    private ImageView user_photo;

    public Comment_Adapter(Context context, int textViewResourceId, ArrayList<Comment> lists) {
        super(context, textViewResourceId, lists);
        this.Comment_list = lists;
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_comments, null);
        }
        user_photo= ((ImageView)v.findViewById(R.id.user_photo));
        AdjustDataToLayout(v,position);

        // 리스너 함수들

        View user_clickableLayout = (View) v.findViewById(R.id.user_clickableLayout);
        TextView username = (TextView)v.findViewById(R.id.comment_writer);
        TextView content = (TextView)v.findViewById(R.id.comment_content);
        UserProfileClickListener(user_clickableLayout, position);

        return v;
    }

    // 유저 이름 누를 때 리스너
    public void UserProfileClickListener(View userview, final int position) {
        userview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, A2_UserProfileActivity.class);
                intent.putExtra("id", String.valueOf(Comment_list.get(position).getWrite_userid()));

                Activity activity = (Activity) context;
                activity.startActivity(intent);

                activity.overridePendingTransition(R.anim.abc_popup_enter, R.anim.abc_popup_exit);
            }
        });

    }

    public void AdjustDataToLayout(final View v, int position) {

        ((TextView) v.findViewById(R.id.comment_writer)).setText(Comment_list.get(position).getWrite_username());
        ((TextView) v.findViewById(R.id.comment_content)).setText(Comment_list.get(position).getContents());

    }
}