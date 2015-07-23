package net.whend.soodal.whend.form;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.top.Search_HashTag;
import net.whend.soodal.whend.model.top.Search_User;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import java.util.ArrayList;

/**
 * Created by wonkyung on 2015-07-13.
 */
public class SearchUser_Adapter extends ArrayAdapter<Search_User> {
    private ArrayList<Search_User> SUser_list;
    private Context context;

    public SearchUser_Adapter(Context context, int textViewResourceId, ArrayList<Search_User> lists) {
        super(context, textViewResourceId, lists);
        this.SUser_list = lists;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_searchuser, null);
        }
        AdjustDataToLayout(v,position);

        // 리스너 함수들
        ImageView follow_button = (ImageView) v.findViewById(R.id.follow_button);
        LikeButtonClickListener(follow_button,v, position);

        return v;
    }

    // 좋아요 누를 때 리스너
    public void LikeButtonClickListener(ImageView follow_button,View rootView, int position){
        final View rv = rootView;
        final int pos = position;
        final ImageView iv = follow_button;
        follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SUser_list.get(pos).getIsFollow() == false) {
                    Toast toast1 = Toast.makeText(context, "Like Button Clicked", Toast.LENGTH_SHORT);
                    toast1.show();
                    SUser_list.get(pos).clickFollow();
                    iv.setImageResource(R.drawable.like_on);

                    String url = "http://119.81.176.245/userinfos/"+SUser_list.get(pos).getUser().getId()+"/follow/";
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url,"PUT");
                    a.doExecution();
                    ((TextView) rv.findViewById(R.id.follower_count)).setText(String.valueOf(SUser_list.get(pos).getUser().getCount_follower()));

                } else if (SUser_list.get(pos).getIsFollow() == true) {
                    Toast toast2 = Toast.makeText(context, "Like Button Unclicked", Toast.LENGTH_SHORT);
                    toast2.show();
                    SUser_list.get(pos).clickFollow();
                    iv.setImageResource(R.drawable.like);

                    String url = "http://119.81.176.245/userinfos/"+SUser_list.get(pos).getUser().getId()+"/follow/";
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url,"PUT");
                    a.doExecution();
                    ((TextView) rv.findViewById(R.id.follower_count)).setText(String.valueOf(SUser_list.get(pos).getUser().getCount_follower()));

                }

            }
        });

    }
    public void AdjustDataToLayout(View v, int position) {

        String username = SUser_list.get(position).getUser().getUsername();
        int count_follower = SUser_list.get(position).getUser().getCount_follower();
        int count_following_user = (SUser_list.get(position).getUser().getCount_following_user()
                + (SUser_list.get(position).getUser().getCount_following_hashtag()));
        Log.d("count_follower",count_follower+"");
        Log.d("count_following_user", count_following_user + "");
        ((TextView) v.findViewById(R.id.username)).setText(username);
        ((TextView) v.findViewById(R.id.follower_count)).setText(count_follower+"");
        ((TextView) v.findViewById(R.id.following_count)).setText(count_following_user+"");
        if(SUser_list.get(position).getIsFollow() == true)
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
