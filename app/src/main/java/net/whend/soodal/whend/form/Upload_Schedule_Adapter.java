package net.whend.soodal.whend.form;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.top.Upload_Schedule;

import java.util.ArrayList;
import java.util.Random;

public class Upload_Schedule_Adapter extends RecyclerView.Adapter<Upload_Schedule_Adapter.Upload_ViewHolder> {

    private ArrayList<Upload_Schedule> Schedule_list;
    private Context context;

    public Upload_Schedule_Adapter(Context context, int textViewResourceId, ArrayList<Upload_Schedule> lists){

        this.Schedule_list = lists;
        this.context = context;
    }

    // 각 데이터 아이템에 대한 뷰의 레퍼런스를 제공한다.
    // 복잡한 데이터 아이템은 아이템당 하나 이상의 뷰가 필요할 수도 있고,
    // 뷰 홀더는 데이터 아이템에 대한 모든 뷰들에 접근하는 방법을 제공함


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // 이 경우에는 각 데이터 아이템은 단지 문자열
        public TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    // 새로운 뷰 생성 (레이아웃 매니저에 의해 호출됨)
    @Override
    public Upload_ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // 새로운 뷰 생성
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_upload_schedule, parent, false);
        // 뷰의 사이즈, 마진, 패딩 및 레이아웃 파라미터 설정

        return new Upload_ViewHolder(v);
    }


    // 뷰의 컨텐츠를 교체함 (레이아웃 매니저에 의해 호출됨)
    @Override
    public void onBindViewHolder(Upload_ViewHolder holder, int position) {
        Upload_Schedule schedule = Schedule_list.get(position);
        holder.vDate.setText(schedule.getDate());
        holder.vContent.setText(schedule.getContent());
        holder.vTime.setText(schedule.getTime());
        holder.vLocation.setText(schedule.getLocation());


        Random random = new Random();
        int color = random.nextInt(4);
        switch (color){
            case 0:
                holder.vColor.setBackgroundColor(Color.parseColor("#FFC671"));
                break;
            case 1:
                holder.vColor.setBackgroundColor(Color.parseColor("#B4FF27"));
                break;
            case 2:
                holder.vColor.setBackgroundColor(Color.parseColor("#F191FF"));
                break;
            case 3:
                holder.vColor.setBackgroundColor(Color.parseColor("#699DFF"));
                break;
            case 4:
                holder.vColor.setBackgroundColor(Color.parseColor("#6BFFBA"));
                break;
        };

    }

    public static class Upload_ViewHolder extends RecyclerView.ViewHolder {

        protected TextView vDate;
        protected TextView vContent;
        protected TextView vTime;
        protected TextView vLocation;
        protected LinearLayout vColor;


        public Upload_ViewHolder(View v){
            super(v);
            vDate = (TextView) v.findViewById(R.id.Date_card);
            vContent = (TextView) v.findViewById(R.id.Content_card) ;
            vTime = (TextView) v.findViewById(R.id.Time_card);
            vLocation = (TextView) v.findViewById(R.id.Location_card);
            vColor = (LinearLayout) v.findViewById(R.id.color);
        }
    }


    // 데이터셋의 크기를 리턴 (레이아웃 매니저에 의해 호출됨)
    @Override
    public int getItemCount() {

        return Schedule_list.size();
    }
}


