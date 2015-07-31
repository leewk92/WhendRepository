package net.whend.soodal.whend.form;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.top.Upload_Schedule;
import net.whend.soodal.whend.view.A4_MakeScheduleActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class Upload_Schedule_Adapter extends RecyclerView.Adapter<Upload_Schedule_Adapter.Upload_ViewHolder> {

    private ArrayList<Upload_Schedule> Schedule_list;
    private Context context;


    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }

    public Upload_Schedule_Adapter(Context context, int textViewResourceId, ArrayList<Upload_Schedule> lists){

        this.Schedule_list = lists;
        this.context = context;
    }

    // 각 데이터 아이템에 대한 뷰의 레퍼런스를 제공한다.
    // 복잡한 데이터 아이템은 아이템당 하나 이상의 뷰가 필요할 수도 있고,
    // 뷰 홀더는 데이터 아이템에 대한 모든 뷰들에 접근하는 방법을 제공함



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
        final Upload_Schedule schedule = Schedule_list.get(position);

        if(schedule.getDateStart().equals(schedule.getDateEnd())){
            holder.vDate_dash.setVisibility(View.GONE);
            holder.vDate_end.setVisibility(View.GONE);
        }

        if(schedule.getTime_end().equals(schedule.getTime_start())){
            holder.vTime_dash.setVisibility(View.GONE);
            holder.vTime_end.setVisibility(View.GONE);
        }


        holder.vDate_start.setText(schedule.getDateStart());
        holder.vDate_end.setText(schedule.getDateEnd());
        holder.vContent.setText(schedule.getContent());
        holder.vTime_start.setText(schedule.getTime_start());
        holder.vLocation.setText(schedule.getLocation());
        try {
            String tmpcolor = schedule.getSchedule().getColor();
            holder.vColor.setBackgroundColor(Color.parseColor("#"+tmpcolor));
        }catch (Exception e){
            holder.vColor.setBackgroundColor(Color.parseColor("#03A9F4"));
        }
        holder.setClickListener(new ItemClickListener(){

            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(context, A4_MakeScheduleActivity.class);
                intent.putExtra("date_start", schedule.getDateStart().toString());
                intent.putExtra("date_end", schedule.getDateEnd().toString());
                intent.putExtra("content", schedule.getContent().toString());
                intent.putExtra("time_start", schedule.getTime_start().toString());
                intent.putExtra("time_end", schedule.getTime_end().toString());
                intent.putExtra("location",schedule.getLocation().toString());
                intent.putExtra("text", String.valueOf("URL")); // 아마 유저정보...?
                intent.putExtra("datetime_start",schedule.getSchedule().getStarttime_ms());
                Log.d("datetime_start_upload", schedule.getSchedule().getStarttime_ms() + "");
                intent.putExtra("datetime_end",schedule.getSchedule().getEndtime_ms());
                context.startActivity(intent);
            }
        });


/*
        // 아직 색깔 랜덤
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
*/
    }

    public static class Upload_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected TextView vDate_start, vDate_end, vDate_dash;
        protected TextView vContent;
        protected TextView vTime_start, vTime_end, vTime_dash;
        protected TextView vLocation;
        protected LinearLayout vColor;
        private ItemClickListener clickListener;


        public Upload_ViewHolder(View v){
            super(v);
            vDate_start = (TextView) v.findViewById(R.id.date_start);
            vDate_dash = (TextView) v.findViewById(R.id.date_dash);
            vDate_end = (TextView) v.findViewById(R.id.date_end);
            vContent = (TextView) v.findViewById(R.id.Content_card) ;
            vTime_start = (TextView) v.findViewById(R.id.time_start);
            vTime_dash = (TextView) v.findViewById(R.id.time_dash);
            vTime_end = (TextView) v.findViewById(R.id.time_end);
            vLocation = (TextView) v.findViewById(R.id.Location_card);
            vColor = (LinearLayout) v.findViewById(R.id.color);

            v.setOnClickListener(this);

        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getPosition(), false);
        }
    }


    // 데이터셋의 크기를 리턴 (레이아웃 매니저에 의해 호출됨)
    @Override
    public int getItemCount() {

        return Schedule_list.size();
    }

}


