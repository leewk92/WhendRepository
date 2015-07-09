package net.whend.soodal.whend.form;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.top.Concise_Schedule;

import java.util.ArrayList;

/**
 * Wall 에서 일정을 간단한 카드 형식 리스트로 보여주기 위한 어답터
 * Created by wonkyung on 15. 7. 9.
 */
public class Concise_Schedule_Adapter extends ArrayAdapter<Concise_Schedule> {

    private ArrayList<Concise_Schedule> Schedule_list;
    private Context context;

    public Concise_Schedule_Adapter(Context context, int textViewResourceId, ArrayList<Concise_Schedule> lists){
        super(context, textViewResourceId, lists);
        this.Schedule_list = lists;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_concise_schedule, null);
        }

        return v;
    }
}
