package net.whend.soodal.whend.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.top.Search_HashTag;
import net.whend.soodal.whend.model.top.Search_User;

import java.util.ArrayList;

/**
 * Created by wonkyung on 2015-07-13.
 */
public class SearchUser_Adapter extends ArrayAdapter<Search_User> {
    private ArrayList<Search_User> SUser_list;
    private Context context;

    public SearchUser_Adapter(Context context, int textViewResourceId, ArrayList<Search_User> lists) {
        super(context, textViewResourceId, lists);
        this.SUser_list = lists;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_searchuser, null);
        }


        // 리스너 함수들
        ImageView follow_button = (ImageView) v.findViewById(R.id.follow_button);
        LikeButtonClickListener(follow_button, position);

        return v;
    }

    // 좋아요 누를 때 리스너
    public void LikeButtonClickListener(ImageView follow_button, int position){

        final int pos = position;
        final ImageView iv = follow_button;
        follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SUser_list.get(pos).getIsFollow() == false){
                    Toast toast1 = Toast.makeText(context, "Like Button Clicked", Toast.LENGTH_SHORT);
                    toast1.show();
                    SUser_list.get(pos).clickFollow();
                    iv.setImageResource(R.drawable.like_on);
                }
                else if(SUser_list.get(pos).getIsFollow() == true){
                    Toast toast2 = Toast.makeText(context, "Like Button Unclicked", Toast.LENGTH_SHORT);
                    toast2.show();
                    SUser_list.get(pos).clickFollow();
                    iv.setImageResource(R.drawable.like);
                }

            }
        });

    }
}
