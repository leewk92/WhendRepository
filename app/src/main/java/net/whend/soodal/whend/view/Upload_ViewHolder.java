package net.whend.soodal.whend.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.whend.soodal.whend.R;

/**
 * Created by CNL on 2015-07-10.
 */
public class Upload_ViewHolder extends RecyclerView.ViewHolder{
    public TextView vDate;
    public TextView vContent;
    public TextView vTime;
    public TextView vLocation;


    public Upload_ViewHolder(View v){
        super(v);
        vDate = (TextView) v.findViewById(R.id.Date_card);
        vContent = (TextView) v.findViewById(R.id.Content_card) ;
        vTime = (TextView) v.findViewById(R.id.Time_card);
        vLocation = (TextView) v.findViewById(R.id.Location_card);
    }


}
