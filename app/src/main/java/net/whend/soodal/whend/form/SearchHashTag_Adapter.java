package net.whend.soodal.whend.form;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.top.Concise_Schedule;
import net.whend.soodal.whend.model.top.Search_HashTag;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;
import net.whend.soodal.whend.view.A2_UserProfileActivity;

import java.util.ArrayList;

/**
 * Created by wonkyung on 2015-07-13.
 */
public class SearchHashTag_Adapter extends ArrayAdapter<Search_HashTag> {
    private ArrayList<Search_HashTag> SHashTag_list;
    private Context context;

    public SearchHashTag_Adapter(Context context, int textViewResourceId, ArrayList<Search_HashTag> lists) {
        super(context, textViewResourceId, lists);
        this.SHashTag_list = lists;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_searchhashtag, null);
        }
        AdjustDataToLayout(v, position);

        // 리스너 함수들
        ImageView follow_button = (ImageView) v.findViewById(R.id.follow_button);
        LikeButtonClickListener(follow_button,v, position);

        return v;
    }

    // 좋아요 누를 때 리스너
    public void LikeButtonClickListener(ImageView follow_button,View rootview ,int position) {
        final View rv = rootview;
        final int pos = position;
        final ImageView iv = follow_button;
        follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SHashTag_list.get(pos).getIsFollow() == false) {
   //                 Toast toast1 = Toast.makeText(context, "Like Button Clicked", Toast.LENGTH_SHORT);
   //                 toast1.show();
                    SHashTag_list.get(pos).clickFollow();
                    iv.setImageResource(R.drawable.like_on);

                    String url = "http://119.81.176.245/hashtags/"+SHashTag_list.get(pos).getHashTag().getId()+"/follow/";
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url,"PUT");
                    a.doExecution();
                    ((TextView) rv.findViewById(R.id.follower_count)).setText(String.valueOf(SHashTag_list.get(pos).getHashTag().getFollower_count()));


                } else if (SHashTag_list.get(pos).getIsFollow() == true) {
    //                Toast toast2 = Toast.makeText(context, "Like Button Unclicked", Toast.LENGTH_SHORT);
    //                toast2.show();
                    SHashTag_list.get(pos).clickFollow();
                    iv.setImageResource(R.drawable.like);

                    String url = "http://119.81.176.245/hashtags/"+SHashTag_list.get(pos).getHashTag().getId()+"/follow/";
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url,"PUT");
                    a.doExecution();
                    ((TextView) rv.findViewById(R.id.follower_count)).setText(String.valueOf(SHashTag_list.get(pos).getHashTag().getFollower_count()));

                }

            }
        });

    }

    public void AdjustDataToLayout(View v, int position) {
        int count_schedule = SHashTag_list.get(position).getHashTag().getCount_schedule();
        int follower_count = SHashTag_list.get(position).getHashTag().getFollower_count();
        Log.d("count_schedule", count_schedule + "");
        Log.d("follower_count",follower_count+"");

        ((TextView) v.findViewById(R.id.title)).setText("#"+SHashTag_list.get(position).getHashTag().getTitle());
        ((TextView) v.findViewById(R.id.schedule_count)).setText(count_schedule+"");
        ((TextView) v.findViewById(R.id.follower_count)).setText(follower_count+"");
        if(SHashTag_list.get(position).getIsFollow() == true)
            ((ImageView)v.findViewById(R.id.follow_button)).setImageResource(R.drawable.like_on);
        else
            ((ImageView)v.findViewById(R.id.follow_button)).setImageResource(R.drawable.like);
    }


    class HTTPRestfulUtilizerExtender extends HTTPRestfulUtilizer {

        public HTTPRestfulUtilizerExtender(Context mContext, String url, String HTTPRestType) {
            setmContext(mContext);
            setUrl(url);
            setHTTPRestType(HTTPRestType);
            task = new HttpAsyncTaskExtenders();
            Log.d("HTTP Constructor url", url);
            // new HttpAsyncTask().execute(url,HTTPRestType);
        }

        @Override
        public void doExecution(){
            task.execute(getUrl(), getHTTPRestType());
        }
        class HttpAsyncTaskExtenders extends HTTPRestfulUtilizer.HttpAsyncTask{
            @Override
            protected String doInBackground(String... strings) {
                String url = strings[0];
                String sHTTPRestType = strings[1];
                setOutputString(PUT(url, getInputBundle()));

                return getOutputString();
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

            }
        }
    }

}
