package net.whend.soodal.whend.form;

import android.content.Context;
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
import net.whend.soodal.whend.model.base.FacebookFriend;
import net.whend.soodal.whend.model.top.Search_User;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.util.CircleTransform;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import java.util.ArrayList;

/**
 * Created by wonkyung on 2015-07-13.
 */
public class FacebookFriend_Adapter extends ArrayAdapter<FacebookFriend> {
    private ArrayList<FacebookFriend> SUser_list;
    private Context context;
    private ImageView user_photo;
    public FacebookFriend_Adapter(Context context, int textViewResourceId, ArrayList<FacebookFriend> lists) {
        super(context, textViewResourceId, lists);
        this.SUser_list = lists;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_facebookfriend, null);
        }
        user_photo = (ImageView)v.findViewById(R.id.user_photo);

        AdjustDataToLayout(v,position);


        // 리스너 함수들
        ImageView follow_button = (ImageView) v.findViewById(R.id.follow_button);

        return v;
    }

    public void LikeButtonClickListener(ImageView follow_button,View rootView, int position){
        final View rv = rootView;
        final int pos = position;
        final ImageView iv = follow_button;
        follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(getItem(pos).isFollow() == false){
                    //    Toast toast1 = Toast.makeText(context, "Like Button Clicked", Toast.LENGTH_SHORT);
                    //    toast1.show();
                    iv.setImageResource(R.drawable.like_on);
                    String url = "http://119.81.176.245/userinfos/" + SUser_list.get(pos).getUser_id() + "/follow/";
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url, "PUT");
                    a.doExecution();
                    getItem(pos).clickFollow();
                }
                else if(getItem(pos).isFollow() == true){
                    //    Toast toast2 = Toast.makeText(context, "Like Button Unclicked", Toast.LENGTH_SHORT);
                    //    toast2.show();
                    iv.setImageResource(R.drawable.like);
                    String url = "http://119.81.176.245/userinfos/" + SUser_list.get(pos).getUser_id() + "/follow/";
                    HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(context, url, "PUT");
                    a.doExecution();
                    getItem(pos).clickFollow();

                }
            }
        });

    }
    public void AdjustDataToLayout(View v, int position) {


        ((TextView) v.findViewById(R.id.facebook_username)).setText(SUser_list.get(position).getFacebook_username());
        ((TextView) v.findViewById(R.id.whend_username)).setText(SUser_list.get(position).getWhend_username());

        if(SUser_list.get(position).isFollow()==false)
            ((ImageView)v.findViewById(R.id.follow_button)).setImageResource(R.drawable.like);
        else
            ((ImageView)v.findViewById(R.id.follow_button)).setImageResource(R.drawable.like_on);

        if(SUser_list.get(position).getFacebook_photo()!="null") {
            Picasso.with(context).load("https://graph.facebook.com/"+SUser_list.get(position).getUser_keyid()+"/picture?type=large").transform(new CircleTransform()).into((ImageView) v.findViewById(R.id.user_photo));

        }else{
            // 기본이미지 로드.
            user_photo = (ImageView) v.findViewById(R.id.user_photo);
            user_photo.setImageResource(R.drawable.userimage_default);
        }
        LikeButtonClickListener((ImageView)v.findViewById(R.id.follow_button),v,position);
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
