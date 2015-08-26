package net.whend.soodal.whend.view.setting;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;
import net.whend.soodal.whend.view.A7_SpecificHashTagActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class S7_Test extends AppCompatActivity {


    private String[] hashtags_title;
    private int[] hashtags_index;
    Button parse;
    EditText memo;
    TextView memo_tv;
    String text="#이원철 #연세대학교 #수달 회의하기 싫다 #유포니아 #회의";
    private static JSONObject outputJson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s7__test);

        memo_tv = (TextView) findViewById(R.id.parse_memo_text);
        SpannableString ss = SpannableString.valueOf(text);
        parseMemo(text);
        parseIndex(text, hashtags_title);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(S7_Test.this, A7_SpecificHashTagActivity.class));

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
        };


        /*parse = (Button) findViewById(R.id.parse);
        memo = (EditText) findViewById(R.id.parse_memo);

        parse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseMemo(memo.getText().toString());
            }

        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_s7__test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        for (int i=0; i<2*tags.length; i++){
            hashtags_index[i] = text.indexOf("#"+tags[i]);
            hashtags_index[i+1] = hashtags_index[i] + tags[i].length();
            i++;
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
                    JSONArray results = outputJson.getJSONArray("results");
                    hashtag_id = results.getJSONObject(0).getInt("id");
                    Toast.makeText(mContext, hashtag_title + " : " + hashtag_id, Toast.LENGTH_SHORT).show();
                }catch (JSONException e){

                }
            }
        }
    }


}
