package net.whend.soodal.whend.form;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.top.Notify_Schedule;
import net.whend.soodal.whend.util.DateTimeFormatter;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;
import net.whend.soodal.whend.view.A2_UserProfileActivity;
import net.whend.soodal.whend.view.A7_SpecificHashTagActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ���� on 2015-07-19.
 */
public class Notify_Schedule_Adapter extends ArrayAdapter<Notify_Schedule> {

    private ArrayList<Notify_Schedule> NT_Schedule_list;
    private Context context;
    private static JSONObject outputJson;

    public Notify_Schedule_Adapter(Context context, int textViewResourceId, ArrayList<Notify_Schedule> lists) {
        super(context, textViewResourceId, lists);
        this.NT_Schedule_list = lists;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View v = convertView;
       ViewHolder holder = null;



        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_notify_schedule, null);
            holder = new ViewHolder();

            holder.actor_name_vh = (TextView) v.findViewById(R.id.actor_name);
            holder.description_vh =  (TextView) v.findViewById(R.id.description);
            holder.time_vh = (TextView) v.findViewById(R.id.time);
            holder.verb_vh =  (TextView) v.findViewById(R.id.verb);
            holder.background_vh = (LinearLayout) v.findViewById(R.id.background);
            v.setTag(holder);
        }else{
            holder = (ViewHolder) v.getTag();
        }

        v.setTag(holder);
        AdjustDataToLayout(v, position, holder);

        return v;
    }


    public void AdjustDataToLayout(View v, final int position, ViewHolder holder) {
        holder.actor_name_vh.setText(NT_Schedule_list.get(position).getActor_name());


        holder.actor_name_vh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (NT_Schedule_list.get(position).getActor_type().equals("hash tag")) {
                    String url = "http://119.81.176.245/hashtags/all/exact/?search=" + NT_Schedule_list.get(position).getActor_name();
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url, "GET", NT_Schedule_list.get(position).getActor_name());
                    a.doExecution();
                } else {
                    Intent intent = new Intent(context, A2_UserProfileActivity.class);
                    intent.putExtra("id", NT_Schedule_list.get(position).getUser_id());
                    Activity activity = (Activity) context;
                    activity.startActivity(intent);

                    activity.overridePendingTransition(R.anim.abc_popup_enter, R.anim.abc_popup_exit);
                }


            }
        });


        holder.verb_vh.setText(NT_Schedule_list.get(position).getVerb());
        holder.description_vh.setText(NT_Schedule_list.get(position).getDescription() == "null" ? "" : NT_Schedule_list.get(position).getDescription());


        DateTimeFormatter current_time = new DateTimeFormatter();
        long timepassed = current_time.getDatetime_ms() - NT_Schedule_list.get(position).getTimestamp_ms();

        holder.time_vh.setText(calculate_timepassed(timepassed));

        if(NT_Schedule_list.get(position).isUnread())
            holder.background_vh.setBackgroundColor(Color.parseColor("#B3E5FC"));



        holder.actor_name_vh.setTag(position);
        holder.background_vh.setTag(position);
        holder.description_vh.setTag(position);
        holder.verb_vh.setTag(position);
        holder.time_vh.setTag(position);

    }

    class HTTPRestfulUtilizerExtender extends HTTPRestfulUtilizer {

        String hashtag_title;
        int hashtag_id;

        // Constructor for GET
        public HTTPRestfulUtilizerExtender(Context mContext, String url, String HTTPRestType, String hashtag_title) {
            setmContext(mContext);
            setUrl(url);
            setHTTPRestType(HTTPRestType);
            task = new HttpAsyncTaskExtenders();
            this.hashtag_title = hashtag_title;
            Log.d("HTTP Constructor url", url);
            // new HttpAsyncTask().execute(url,HTTPRestType);
        }


        @Override
        public void doExecution() {
            task.execute(getUrl(), getHTTPRestType());
        }

        class HttpAsyncTaskExtenders extends HTTPRestfulUtilizer.HttpAsyncTask {
            @Override
            protected String doInBackground(String... strings) {
                String url = strings[0];
                String sHTTPRestType = strings[1];
                setOutputString(GET(url));

                return getOutputString();
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                outputJson = getOutputJsonObject();
                try {
                    int is_exist = outputJson.getInt("count");
                    if(is_exist == 1){
                        JSONArray results = outputJson.getJSONArray("results");
                        JSONObject hashtag = results.getJSONObject(0);

                        Intent intent = new Intent(mContext, A7_SpecificHashTagActivity.class);
                        intent.putExtra("id", hashtag.getInt("id"));
                        intent.putExtra("title",hashtag.getString("title"));
                        intent.putExtra("follower_count",hashtag.getInt("count_follower"));
                        intent.putExtra("photo",hashtag.getString("photo"));
                        intent.putExtra("count_schedule",hashtag.getInt("count_schedule"));
                        intent.putExtra("count_upcoming_schedule", hashtag.getInt("count_upcoming_schedule"));
                        intent.putExtra("is_follow", hashtag.getInt("is_follow") == 1 ? true : false);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }else{
                        Toast.makeText(mContext, "일정이 등록되지 않은 태그입니다", Toast.LENGTH_SHORT).show();
                    }


                }catch (JSONException e){

                }
            }
        }

    }

    static class ViewHolder {


        TextView actor_name_vh;
        TextView verb_vh;
        TextView description_vh;
        TextView time_vh;
        LinearLayout background_vh;

        int position;
    }

    public String calculate_timepassed(long time_ms){
        String temp = null;
        int time_second = (int) time_ms/1000 + 150; // 150초 차이가 남 그 이유는 아무도 모름
        int time_day;
        int time_hour;
        int time_minute;



        if(time_second > 60)
            if((time_minute = time_second/60) > 60)
               if((time_hour = time_minute/60) > 24) {
                   time_day = time_hour / 24;
                   temp = time_day + "일";
               }
               else
                   temp = time_hour + "시간";
            else
                temp = time_minute + "분";
        else
            temp = time_second + "초";


        return temp + " 전";
    }
}

