package net.whend.soodal.whend.form;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.top.ScheduleFollow_User;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.util.CircleTransform;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;
import net.whend.soodal.whend.view.A2_UserProfileActivity;

import java.util.ArrayList;

/**
 * Created by wonkyung on 15. 7. 12.
 */
public class ScheduleFollow_User_Adapter extends ArrayAdapter<ScheduleFollow_User> {
    private ArrayList<ScheduleFollow_User> User_list;
    private Context context;
    private ImageView user_photo;

    public ScheduleFollow_User_Adapter(Context context, int textViewResourceId, ArrayList<ScheduleFollow_User> lists) {
        super(context, textViewResourceId, lists);
        this.User_list = lists;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder = null;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_schedulefollow_user, null);

            holder = new ViewHolder();
            holder.follow_button_vh = (ImageView) v.findViewById(R.id.follow_button);
            holder.user_photo_vh = (ImageView) v.findViewById(R.id.user_photo);
            holder.fullname_vh = (TextView)v.findViewById(R.id.fullname);
            holder.user_clickablelayout_vh = v.findViewById(R.id.user_clickableLayout);
            v.setTag(holder);
        }
        else{
            holder = (ViewHolder) v.getTag();
        }

        user_photo = (ImageView)v.findViewById(R.id.user_photo);


        AdjustDataToLayout(v, position, holder);



        return v;
    }

    // 유저 이름 누를때 리스너
    public void UserProfileClickListener(View userview, final int position) {
        userview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, A2_UserProfileActivity.class);
                intent.putExtra("id", User_list.get(position).getUser().getId());
                Log.d("profileId",User_list.get(position).getUser().getId()+"");
                context.startActivity(intent);
            }
        });

    }


    static class ViewHolder {
        ImageView user_photo_vh;
        ImageView follow_button_vh;
        TextView fullname_vh;
        View user_clickablelayout_vh;
        int position;
    }

    public void AdjustDataToLayout(final View v, int position, ViewHolder holder) {

        holder.fullname_vh.setText(User_list.get(position).getUsername());
        if(User_list.get(position).getIsFollow() == true)
            holder.follow_button_vh.setImageResource(R.drawable.like_on);
        else
            holder.follow_button_vh.setImageResource(R.drawable.like);

        if(User_list.get(position).getUser().getUser_photo()!="") {
            Picasso.with(context).load(User_list.get(position).getUser().getUser_photo()).transform(new CircleTransform()).into(holder.user_photo_vh);

        }else{
            // 기본이미지 로드.
            holder.user_photo_vh.setImageResource(R.drawable.userimage_default);
        }
/*
        AppPrefs appPrefs = new AppPrefs(context);
        if(appPrefs.getUsername().equals(User_list.get(position).getUsername()) ){
            holder.follow_button_vh.setVisibility(View.INVISIBLE);
        }*/
        holder.follow_button_vh.setTag(position);
        holder.user_photo_vh.setTag(position);
        holder.fullname_vh.setTag(position);

        UserProfileClickListener(holder.user_clickablelayout_vh, position);
        LikeButtonClickListener(holder.follow_button_vh, v, position);
        //LikeButtonClickListener2(follow_button_layout, v, position);


    }


    // 좋아요 누를 때 리스너
    public void LikeButtonClickListener2(LinearLayout follow_button_layout,View rootView, int position){
        final View rv = rootView;
        final int pos = position;
        final LinearLayout layout = follow_button_layout;
        final ImageView iv = (ImageView) rv.findViewById(R.id.follow_button);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (User_list.get(pos).getIsFollow() == false) {
 //                   Toast toast1 = Toast.makeText(context, "Like Button Clicked", Toast.LENGTH_SHORT);
  //                  toast1.show();
                    User_list.get(pos).clickFollow();
                    iv.setImageResource(R.drawable.like_on);

                    String url = "http://119.81.176.245/userinfos/" + User_list.get(pos).getUser().getId() + "/follow/";
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url, "PUT");
                    a.doExecution();
                //    ((TextView) rv.findViewById(R.id.follower_count)).setText(String.valueOf(User_list.get(pos).getUser().getCount_follower()));

                } else if (User_list.get(pos).getIsFollow() == true) {
    //                Toast toast2 = Toast.makeText(context, "Like Button Unclicked", Toast.LENGTH_SHORT);
    //                toast2.show();
                    User_list.get(pos).clickFollow();
                    iv.setImageResource(R.drawable.like);

                    String url = "http://119.81.176.245/userinfos/" + User_list.get(pos).getUser().getId() + "/follow/";
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url, "PUT");
                    a.doExecution();
              //      ((TextView) rv.findViewById(R.id.follower_count)).setText(String.valueOf(User_list.get(pos).getUser().getCount_follower()));

                }

            }
        });

    }
    public void LikeButtonClickListener(ImageView follow_button,View rootView, int position){
        final View rv = rootView;
        final int pos = position;
        final ImageView iv = follow_button;
        follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (User_list.get(pos).getIsFollow() == false) {
                    //                   Toast toast1 = Toast.makeText(context, "Like Button Clicked", Toast.LENGTH_SHORT);
                    //                  toast1.show();
                    User_list.get(pos).clickFollow();
                    iv.setImageResource(R.drawable.like_on);

                    String url = "http://119.81.176.245/userinfos/" + User_list.get(pos).getUser().getId() + "/follow/";
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url, "PUT");
                    a.doExecution();
                    //    ((TextView) rv.findViewById(R.id.follower_count)).setText(String.valueOf(User_list.get(pos).getUser().getCount_follower()));

                } else if (User_list.get(pos).getIsFollow() == true) {
                    //                Toast toast2 = Toast.makeText(context, "Like Button Unclicked", Toast.LENGTH_SHORT);
                    //                toast2.show();
                    User_list.get(pos).clickFollow();
                    iv.setImageResource(R.drawable.like);

                    String url = "http://119.81.176.245/userinfos/" + User_list.get(pos).getUser().getId() + "/follow/";
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url, "PUT");
                    a.doExecution();
                    //      ((TextView) rv.findViewById(R.id.follower_count)).setText(String.valueOf(User_list.get(pos).getUser().getCount_follower()));

                }

            }
        });

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
