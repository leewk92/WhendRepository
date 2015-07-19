package net.whend.soodal.whend.form;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.top.Notify_Schedule;
import net.whend.soodal.whend.view.A3_SpecificScheduleActivity;

import java.util.ArrayList;

/**
 * Created by ���� on 2015-07-19.
 */
public class Notify_Schedule_Adapter extends ArrayAdapter<Notify_Schedule> {

        private ArrayList<Notify_Schedule> NT_Schedule_list;
        private Context context;

        public Notify_Schedule_Adapter(Context context, int textViewResourceId, ArrayList<Notify_Schedule> lists){
            super(context, textViewResourceId, lists);
            this.NT_Schedule_list = lists;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View v = convertView;
            if (v == null) {
                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = li.inflate(R.layout.item_notify_schedule, null);
            }

            Notify_Schedule notify_schedule = NT_Schedule_list.get(position);
            TextView event_string = (TextView) v.findViewById(R.id.description_text);
            event_string.setText(notify_schedule.getEvent_string());


            return v;
        }
    }

