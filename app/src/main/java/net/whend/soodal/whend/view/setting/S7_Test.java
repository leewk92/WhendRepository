package net.whend.soodal.whend.view.setting;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class S7_Test extends AppCompatActivity {


    private String[] hashtags_title;
    Button parse;
    EditText memo;
    private static JSONObject outputJson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s7__test);

        parse = (Button) findViewById(R.id.parse);
        memo = (EditText) findViewById(R.id.parse_memo);

        parse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseMemo(memo.getText().toString());
            }

        });

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

            for (int i = 1; i < tmpArray.length; i++) {
                hashtags_title[i - 1] = tmpArray[i].split(" ")[0];

                String url = "http://119.81.176.245/hashtags/all/exact/?search="+hashtags_title[i-1];

                HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(this, url,"GET",hashtags_title[i-1]);
                a.doExecution();

                Log.d("hashtags_title_array", hashtags_title[i - 1]);
            }
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
