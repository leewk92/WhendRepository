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
import net.whend.soodal.whend.model.top.Grid_Search_Schedule;
import net.whend.soodal.whend.view.A2_UserProfileActivity;
import net.whend.soodal.whend.view.A5_WhoFollowsScheduleActivity;

import java.util.ArrayList;

/**
 * Wall 에서 일정을 간단한 카드 형식 리스트로 보여주기 위한 어답터
 * Created by wonkyung on 15. 7. 9.
 */
public class Grid_Search_Adapter extends ArrayAdapter<Grid_Search_Schedule> {

    private ArrayList<Grid_Search_Schedule> GS_Schedule_list;
    private Context context;

    public Grid_Search_Adapter(Context context, int textViewResourceId, ArrayList<Grid_Search_Schedule> lists){
        super(context, textViewResourceId, lists);
        this.GS_Schedule_list = lists;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_gridsearch_schedule, null);
        }


        // 리스너 함수들
        ImageView grid_image = (ImageView)v.findViewById(R.id.gridsearch_image);
        TextView grid_text = (TextView)v.findViewById(R.id.gridsearch_text);

        return v;
    }
}
