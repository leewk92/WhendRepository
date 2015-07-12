package net.whend.soodal.whend.form;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.top.Concise_Schedule;
import net.whend.soodal.whend.model.top.Search_HashTag;
import net.whend.soodal.whend.view.A2_UserProfileActivity;

import java.util.ArrayList;

/**
 * Created by wonkyung on 2015-07-13.
 */
public class SearchHashTag_Adapter extends ArrayAdapter<Search_HashTag> {
    private ArrayList<Search_HashTag> SHashTag_list;
    private Context context;

    public SearchHashTag_Adapter(Context context, int textViewResourceId, ArrayList<Search_HashTag> lists) {
        super(context, textViewResourceId, lists);
        this.SHashTag_list = lists;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_searchhashtag, null);
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

                if(SHashTag_list.get(pos).getIsFollow() == false){
                    Toast toast1 = Toast.makeText(context, "Like Button Clicked", Toast.LENGTH_SHORT);
                    toast1.show();
                    SHashTag_list.get(pos).clickFollow();
                    iv.setImageResource(R.drawable.like_on);
                }
                else if(SHashTag_list.get(pos).getIsFollow() == true){
                    Toast toast2 = Toast.makeText(context, "Like Button Unclicked", Toast.LENGTH_SHORT);
                    toast2.show();
                    SHashTag_list.get(pos).clickFollow();
                    iv.setImageResource(R.drawable.like);
                }

            }
        });

    }
}
