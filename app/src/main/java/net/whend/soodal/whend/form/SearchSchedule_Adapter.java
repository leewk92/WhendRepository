package net.whend.soodal.whend.form;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.top.Concise_Schedule;
import net.whend.soodal.whend.view.A2_UserProfileActivity;

import java.util.ArrayList;

/**
 * Created by wonkyung on 2015-07-12.
 */
public class SearchSchedule_Adapter extends ArrayAdapter<Concise_Schedule> {

    private ArrayList<Concise_Schedule> CSchedule_list;
    private Context context;

    public SearchSchedule_Adapter(Context context, int textViewResourceId, ArrayList<Concise_Schedule> lists) {
        super(context, textViewResourceId, lists);
        this.CSchedule_list = lists;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_searchschedule, null);
        }
        AdjustDataToLayout(v,position);

        // 리스너 함수들
        TextView username = (TextView) v.findViewById(R.id.user_fullname);

        UserProfileClickListener(username, position);

        return v;
    }


    // 유저 이름 누를 때 리스너
    public void UserProfileClickListener(View userview, int position) {
        userview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, A2_UserProfileActivity.class);
                intent.putExtra("text", String.valueOf("URL"));
                context.startActivity(intent);
            }
        });

    }

    public void AdjustDataToLayout(View v,int position){

        ((TextView)v.findViewById(R.id.user_fullname)).setText(CSchedule_list.get(position).getUsername());
        ((TextView)v.findViewById(R.id.title)).setText(CSchedule_list.get(position).getTitle());
        ((TextView)v.findViewById(R.id.date)).setText(CSchedule_list.get(position).getDate());
        ((TextView)v.findViewById(R.id.time)).setText(CSchedule_list.get(position).getTime());
        ((TextView)v.findViewById(R.id.memo)).setText(CSchedule_list.get(position).getMemo());
        //((TextView)v.findViewById(R.id.like_count)).setText(CSchedule_list.get(position).getLike_count());
    }
}