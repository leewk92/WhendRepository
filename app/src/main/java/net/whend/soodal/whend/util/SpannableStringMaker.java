package net.whend.soodal.whend.util;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.whend.soodal.whend.view.A7_SpecificHashTagActivity;
import net.whend.soodal.whend.view.setting.S2_Version;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015-08-26.
 */
public class SpannableStringMaker {

    private String[] hashtags_title;
    private int[] hashtags_index;
    private SpannableString ss;
    private String input_string;
    private Context mContext;

    private static JSONObject outputJson;

    public SpannableStringMaker(Context mContext, String input_string){
        this.mContext = mContext;
        this.input_string = input_string;

        parseMemo(input_string);
        parseIndex(input_string, hashtags_title);

        ss = SpannableString.valueOf(input_string);

        for (int i=0; i<hashtags_title.length; i++) {
            int j=i*2;
            ss.setSpan(new hashtagSpan(hashtags_title[i]), hashtags_index[j], hashtags_index[j + 1]+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }

    public SpannableString getSs() {
        return ss;
    }

    public void setSs(SpannableString ss) {
        this.ss = ss;
    }

    public void parseMemo(String memo_text) {
        memo_text = memo_text.replaceAll("\n", " ");
        memo_text = memo_text.replaceAll("#", " #");
        String tmpArray[] = memo_text.split("#");
        if (tmpArray != null) {

            hashtags_title = new String[tmpArray.length - 1];

            for (int i = 1; i < tmpArray.length; i++)
                hashtags_title[i - 1] = tmpArray[i].split(" ")[0];
        }
    }

    public void parseIndex(String text, String[] tags){
        hashtags_index = new int[tags.length*2];        //인덱스 배열의 짝수번째에는 시작 인덱스, 홀수번째는 끝 인덱스.
        for (int i=0; i<tags.length; i++){
            int j= i*2;
            hashtags_index[j] = text.indexOf("#"+tags[i]);
            hashtags_index[j+1] = hashtags_index[j] + tags[i].length();

        }
    }

    public class hashtagSpan extends ClickableSpan {

        private String hashtag;

        public hashtagSpan(String hashtag){
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
                        Toast.makeText(mContext,"일정이 등록되지 않은 태그입니다",Toast.LENGTH_SHORT).show();
                    }


                }catch (JSONException e){

                }
            }
        }
    }
}
