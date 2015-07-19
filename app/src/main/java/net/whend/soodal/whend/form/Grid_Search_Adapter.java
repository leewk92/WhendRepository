package net.whend.soodal.whend.form;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.top.Grid_Search_Schedule;
import net.whend.soodal.whend.view.A3_SpecificScheduleActivity;
import net.whend.soodal.whend.view.A7_SpecificHashTagActivity;

import java.util.ArrayList;


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

        Grid_Search_Schedule grid_search_schedule = GS_Schedule_list.get(position);

        LinearLayout grid_search_layout = (LinearLayout) v.findViewById(R.id.grid_search_layout);


        ImageView grid_image = (ImageView)v.findViewById(R.id.gridsearch_image);
        String grid_image_string;
        TextView grid_text = (TextView)v.findViewById(R.id.gridsearch_text);


        if (grid_search_schedule.getTag() != null){
            //grid_image_string = grid_search_schedule.getTag().getPhoto();

            grid_search_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, A7_SpecificHashTagActivity.class);
                    context.startActivity(intent);
                }
            });
            grid_text.setText("#" + grid_search_schedule.getTag().getTitle());
        }


        return v;
    }
}
