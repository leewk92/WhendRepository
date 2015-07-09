package net.whend.soodal.whend.form;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.top.Upload_Schedule;

import java.util.ArrayList;

/**
 * Upload 창을 위한 어답터
 * Created by JB on 15. 7. 10.
 */
public class Upload_Schedule_Adapter extends ArrayAdapter<Upload_Schedule> {

    private ArrayList<Upload_Schedule> Schedule_list;
    private Context context;

    public Upload_Schedule_Adapter(Context context, int textViewResourceId, ArrayList<Upload_Schedule> lists){
        super(context, textViewResourceId, lists);
        this.Schedule_list = lists;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_upload_schedule, null);
        }

        return v;
    }
}
