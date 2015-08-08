package net.whend.soodal.whend.form;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.top.Notify_Schedule;
import net.whend.soodal.whend.view.A2_UserProfileActivity;

import java.util.ArrayList;

/**
 * Created by ���� on 2015-07-19.
 */
public class Notify_Schedule_Adapter extends ArrayAdapter<Notify_Schedule> {

    private ArrayList<Notify_Schedule> NT_Schedule_list;
    private Context context;

    public Notify_Schedule_Adapter(Context context, int textViewResourceId, ArrayList<Notify_Schedule> lists) {
        super(context, textViewResourceId, lists);
        this.NT_Schedule_list = lists;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_notify_schedule, null);
        }
        Notify_Schedule notify_schedule = NT_Schedule_list.get(position);
        TextView actor_name = (TextView) v.findViewById(R.id.actor_name);
        TextView verb = (TextView) v.findViewById(R.id.verb);
        TextView description = (TextView) v.findViewById(R.id.description);
        //TextView datetime = (TextView)v.findViewById(R.id.datetime);
        AdjustDataToLayout(v, position);


        return v;
    }


    public void AdjustDataToLayout(View v, final int position) {
        ((TextView)v.findViewById(R.id.actor_name)).setText(NT_Schedule_list.get(position).getActor_name());


        ((TextView)v.findViewById(R.id.actor_name)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, A2_UserProfileActivity.class);

                intent.putExtra("id", NT_Schedule_list.get(position).getUser_id());
                Activity activity = (Activity) context;
                activity.startActivity(intent);

                activity.overridePendingTransition(R.anim.abc_popup_enter, R.anim.abc_popup_exit);
            }
        });


        ((TextView)v.findViewById(R.id.verb)).setText(NT_Schedule_list.get(position).getVerb());
        ((TextView)v.findViewById(R.id.description)).setText(NT_Schedule_list.get(position).getDescription()=="null"?"":NT_Schedule_list.get(position).getDescription());
        //((TextView)v.findViewById(R.id.datetime)).setText(NT_Schedule_list.get(position).getDate() +NT_Schedule_list.get(position).getTime() );


    }
}

