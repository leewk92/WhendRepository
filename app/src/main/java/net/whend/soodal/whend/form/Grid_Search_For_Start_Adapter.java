package net.whend.soodal.whend.form;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.base.User;
import net.whend.soodal.whend.model.top.Grid_Search_Schedule;
import net.whend.soodal.whend.model.top.Search_HashTag;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;
import net.whend.soodal.whend.view.A0_5_TagFollowingStart;
import net.whend.soodal.whend.view.A7_SpecificHashTagActivity;

import java.util.ArrayList;


public class Grid_Search_For_Start_Adapter extends ArrayAdapter<Search_HashTag> {

    private ArrayList<Search_HashTag> GS_Schedule_list;
    private LinearLayout follow_layout;
    private Context context;
    private User user;
    private int following_hashtag_count;

    public Grid_Search_For_Start_Adapter(Context context, int textViewResourceId, ArrayList<Search_HashTag> lists, User user){
        super(context, textViewResourceId, lists);
        this.GS_Schedule_list = lists;
        this.context = context;
        this.user = user;
        this.following_hashtag_count = user.getCount_following_hashtag();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_gridsearch_schedule_forstart, null);
        }
        AdjustDataToLayout(v,position);
        final Search_HashTag grid_search_schedule = GS_Schedule_list.get(position);

        ImageView grid_image = (ImageView)v.findViewById(R.id.gridsearch_image);
        final String grid_image_string;
        TextView grid_text = (TextView)v.findViewById(R.id.gridsearch_text);
        LinearLayout tag_follow = (LinearLayout) v.findViewById(R.id.gridsearch_follow_layout);

        following_hashtag_count = user.getCount_following_hashtag();


        if (following_hashtag_count>=2)
            ((A0_5_TagFollowingStart)context).findViewById(R.id.a0_5_toolbar_next).setVisibility(View.VISIBLE);
        else
            ((A0_5_TagFollowingStart)context).findViewById(R.id.a0_5_toolbar_next).setVisibility(View.GONE);

        if (grid_search_schedule.getHashTag() != null){
            //grid_image_string = grid_search_schedule.getTag().getPhoto();

            grid_text.setText("#" + grid_search_schedule.getHashTag().getTitle());
        }

        LikeButtonClickListener2(tag_follow, v, position);

        return v;
    }

    public void LikeButtonClickListener2(LinearLayout follow_button_layout,View rootview ,int position) {
        final View rv = rootview;
        final int pos = position;
        final ImageView iv = (ImageView) rootview.findViewById(R.id.gridsearch_follow);
        final LinearLayout next = (LinearLayout) ((A0_5_TagFollowingStart) context).findViewById(R.id.a0_5_toolbar_next);

        final LinearLayout layout = follow_button_layout;
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (GS_Schedule_list.get(pos).getIsFollow() == false) {
                    //                 Toast toast1 = Toast.makeText(context, "Like Button Clicked", Toast.LENGTH_SHORT);
                    //                 toast1.show();
                    GS_Schedule_list.get(pos).clickFollow();
                    iv.setImageResource(R.drawable.like_on);

                    String url = "http://119.81.176.245/hashtags/" + GS_Schedule_list.get(pos).getHashTag().getId() + "/follow/";
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url, "PUT");
                    a.doExecution();

                    following_hashtag_count++;

                    if(following_hashtag_count>=2 && next.getVisibility() != View.VISIBLE) {
                       next.setVisibility(View.VISIBLE);
                       next.startAnimation(AnimationUtils.loadAnimation(context, R.anim.abc_popup_enter));
                    }



                } else if (GS_Schedule_list.get(pos).getIsFollow() == true) {
                    //                Toast toast2 = Toast.makeText(context, "Like Button Unclicked", Toast.LENGTH_SHORT);
                    //                toast2.show();
                    GS_Schedule_list.get(pos).clickFollow();
                    iv.setImageResource(R.drawable.like);

                    String url = "http://119.81.176.245/hashtags/" + GS_Schedule_list.get(pos).getHashTag().getId() + "/follow/";
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url, "PUT");
                    a.doExecution();

                    following_hashtag_count--;

                    if (following_hashtag_count<3 && next.getVisibility() == View.VISIBLE) {
                        next.setVisibility(View.INVISIBLE);
                        next.startAnimation(AnimationUtils.loadAnimation(context, R.anim.abc_popup_exit));
                    }


                }

            }
        });

    }

    public void AdjustDataToLayout(View v,int position) {

        Log.d("whatthe", "#" + GS_Schedule_list.get(position).getHashTag().getTitle());
        ((TextView) v.findViewById(R.id.gridsearch_text)).setText("#" + GS_Schedule_list.get(position).getHashTag().getTitle());

        if(GS_Schedule_list.get(position).getHashTag().getPhoto()!="") {
            Log.d("photoDir",GS_Schedule_list.get(position).getHashTag().getPhoto());
            Picasso.with(context).load(GS_Schedule_list.get(position).getHashTag().getPhoto()).into((ImageView)v.findViewById(R.id.gridsearch_image));

        }else
            ((ImageView)v.findViewById(R.id.gridsearch_image)).setBackgroundColor(Color.parseColor("#000000"));
        notifyDataSetChanged();

        if(GS_Schedule_list.get(position).getIsFollow() == true)
            ((ImageView)v.findViewById(R.id.gridsearch_follow)).setImageResource(R.drawable.like_on);
        else
            ((ImageView)v.findViewById(R.id.gridsearch_follow)).setImageResource(R.drawable.like);
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
