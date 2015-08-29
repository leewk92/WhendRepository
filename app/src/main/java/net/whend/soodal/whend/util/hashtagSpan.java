package net.whend.soodal.whend.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.whend.soodal.whend.view.A7_SpecificHashTagActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015-08-29.
 */
public class hashtagSpan extends ClickableSpan {

    private String hashtag;
    private Context mContext;
    private static JSONObject outputJson;

    public hashtagSpan(Context mContext, String hashtag){
        this.mContext = mContext;
        this.hashtag = hashtag;
    }

    @Override
    public void onClick(View textView) {
        //mContext.startActivity(new Intent(mContext, A7_SpecificHashTagActivity.class));

        String url = "http://119.81.176.245/hashtags/all/exact/?search="+hashtag;
        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(mContext,url,"GET",hashtag );
        a.doExecution();
        //mContext.startActivity(new Intent(mContext, S2_Version.class));



        //intent.putExtra("id", grid_search_schedule.getTag().getId());
        //intent.putExtra("title",grid_search_schedule.getTag().getTitle());
        //intent.putExtra("follower_count",grid_search_schedule.getTag().getFollower_count());
        //intent.putExtra("photo",grid_search_schedule.getTag().getPhoto());
        //intent.putExtra("count_schedule",grid_search_schedule.getTag().getCount_schedule());
        //intent.putExtra("count_upcoming_schedule",grid_search_schedule.getTag().getCount_upcoming_schedule());
        //intent.putExtra("is_follow",grid_search_schedule.getTag().is_Follow());
        //context.startActivity(intent);
    }
    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
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
}