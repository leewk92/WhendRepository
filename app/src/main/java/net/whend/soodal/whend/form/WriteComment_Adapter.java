package net.whend.soodal.whend.form;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.base.Comment;
import net.whend.soodal.whend.util.CircleTransform;
import net.whend.soodal.whend.util.SpannableStringMaker;
import net.whend.soodal.whend.util.TextViewFixTouchConsume;
import net.whend.soodal.whend.view.A2_UserProfileActivity;

import java.util.ArrayList;

/**
 * Created by wonkyung on 15. 7. 12.
 */
public class WriteComment_Adapter extends Comment_Adapter {
    private ArrayList<Comment> Comment_list;
    private Context context;
    private ImageView user_photo;
    public WriteComment_Adapter(Context context, int textViewResourceId, ArrayList<Comment> lists){
        super(context, textViewResourceId, lists);
        this.Comment_list = lists;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_writecomments, null);

        }
        user_photo = (ImageView)v.findViewById(R.id.user_photo);
        AdjustDataToLayout(v,position);

        // 리스너 함수들

        View user_clickableLayout = (View) v.findViewById(R.id.user_clickableLayout);
        TextView username = (TextView)v.findViewById(R.id.comment_writer);
        TextView content = (TextView)v.findViewById(R.id.comment_content);

        UserProfileClickListener(user_clickableLayout, position);

        return v;
    }

    // 유저 이름 누를 때 리스너
    @Override
    public void UserProfileClickListener(View userview,final int position){
        userview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, A2_UserProfileActivity.class);
                intent.putExtra("id", Comment_list.get(position).getWrite_userid());

                Activity activity = (Activity) context;
                activity.startActivity(intent);

                activity.overridePendingTransition(R.anim.abc_popup_enter, R.anim.abc_popup_exit);

            }
        });

    }
    public void AdjustDataToLayout(final View v, int position) {

        ((TextView) v.findViewById(R.id.comment_writer)).setText(Comment_list.get(position).getWrite_username());

        SpannableStringMaker ssm = new SpannableStringMaker(context,Comment_list.get(position).getContents());
        ((TextView) v.findViewById(R.id.comment_content)).setText(ssm.getSs());

        ((TextView) v.findViewById(R.id.comment_content)).setMovementMethod(TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance());
        ((TextView) v.findViewById(R.id.comment_content)).setHighlightColor(Color.TRANSPARENT);
        ((TextView) v.findViewById(R.id.comment_content)).setFocusable(false);

        if(Comment_list.get(position).getUser_photo()!="") {
            Picasso.with(context).load(Comment_list.get(position).getUser_photo()).transform(new CircleTransform()).into((ImageView) v.findViewById(R.id.user_photo));

        }else{
            // 기본이미지 로드.
            user_photo.setVisibility(View.GONE);
            //user_photo.setImageResource(R.drawable.userimage_default);
        }
    }

}
