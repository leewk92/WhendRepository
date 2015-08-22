package net.whend.soodal.whend.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.FacebookFriend_Adapter;
import net.whend.soodal.whend.form.ScheduleFollow_User_Adapter;
import net.whend.soodal.whend.model.base.FacebookFriend;
import net.whend.soodal.whend.model.base.User;
import net.whend.soodal.whend.model.top.ScheduleFollow_User;
import net.whend.soodal.whend.tutorial.T1_welcome;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by wonkyung on 15. 7. 12.
 */
public class A0_4_FacebookFriendActivity extends AppCompatActivity {

    ArrayList <FacebookFriend> User_list= new ArrayList<FacebookFriend>();
    ListView listview;
    JSONObject outputSchedulesJson;
    FacebookFriend_Adapter adapter;
    static String nextURL;
    Context mContext = this;
    Bundle friendJSONData;
    JSONArray facebookfriends;
    String uid[];
    String url = "http://119.81.176.245/rest-auth/social_login/getid/";
    int page=0;
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.abc_popup_enter, R.anim.abc_fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a0_4_facebookfriend_layout);

        Intent intent=new Intent(this.getIntent());


        try{
            facebookfriends = new JSONArray(intent.getStringExtra("facebookfriend"));

        }catch(Exception e){}


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_facebookfriend);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.abc_popup_enter, R.anim.push_right_out);
            }
        });

        adapter = new FacebookFriend_Adapter(this,R.layout.item_facebookfriend,User_list);
        listview = (ListView)findViewById(R.id.listview_facebookfriend);
        listview.setAdapter(adapter);

        uid = new String[facebookfriends.length()];

        for(int i=0; i<facebookfriends.length(); i++){
            try{
                uid[i] = facebookfriends.getJSONObject(i).getString("id");
            }catch (Exception e){}
        }

        //listview.setOnScrollListener(new EndlessScrollListener());
        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(this,url,"POST",uid);
        a.doExecution();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_a0_4__facebookfriend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // 먼저 메모를 파싱한 후 해시태그 이름들을 배열에 담는다.
        // 그담에 배열의 리스트들을 각각 검색하고 검색 결과가 있다면 그 해시태그의 아이디를 가져온다.
        // 만약 검색 결과가 없다면 해시태그를 만드는 요청을 보내고 그 해시태그의 아이디를 가져온다.
        // 이렇게 각각 해시태그에 대한 아이디를 얻어서 배열에 담는다.
        // 이들을 [1,2,55,71] 형식으로 바꾸고 inputBundle.putIntArrayList 한다.

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {

            Toast toast1 = Toast.makeText(mContext, "로그인 성공.", Toast.LENGTH_SHORT);
            toast1.setGravity(0, 0, 100);
            toast1.show();

            Intent intent = new Intent(A0_4_FacebookFriendActivity.this, T1_welcome.class);
            //intent.putExtra("text", String.valueOf("URL"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // 끝없이 로딩 하는거
    public class EndlessScrollListener implements AbsListView.OnScrollListener {

        private int visibleThreshold = 5;
        private int currentPage = 0;
        private int previousTotal = 0;
        private boolean loading = true;

        public EndlessScrollListener() {

        }
        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    currentPage++;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // I load the next page of gigs using a background task,
                // but you can call any function here.
                Log.d("lastItemScrolled", "true");
                try{
                    if(nextURL != "null"){
                        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(mContext,url,"POST",uid);
                        a.doExecution();
                    }
                }catch(Exception e){

                }
                loading = true;
            }
        }
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }


    class HTTPRestfulUtilizerExtender extends HTTPRestfulUtilizer {

        String[] uid_inner;

        public HTTPRestfulUtilizerExtender(Context mContext, String url, String HTTPRestType, String[] uid_inner) {
            this.uid_inner = uid_inner.clone();
            setmContext(mContext);
            setUrl(url);
            setHTTPRestType(HTTPRestType);
            task = new HttpAsyncTaskExtenders();
            Log.d("HTTP Constructor url", url);
            // new HttpAsyncTask().execute(url,HTTPRestType);
        }
        public String POST_hashtag(String url, JSONArray tmp){
            InputStream inputStream = null;
            String result = "";
            try {

                // 1. create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                // 2. make POST request to the given URL
                HttpPost httpPost = new HttpPost(url);

                String json = "";

                json =tmp.toString();
                Log.d("hashjson", json);
                // ** Alternative way to convert Person object to JSON string usin Jackson Lib
                // ObjectMapper mapper = new ObjectMapper();
                // json = mapper.writeValueAsString(person);

                // 5. set json to StringEntity

                StringEntity se = new StringEntity(json,"UTF-8");


                //MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();

                //multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                // multipartEntity.addBinaryBody("someName", file, ContentType.create("image/jpeg"), file.getName());
                //multipartEntity.addPart("content", json);
                //multipartEntity.addTextBody("content",json);
                // 6. set httpPost Entity
                httpPost.setEntity(se);
                //httpPost.setEntity(multipartEntity.build());

                // 7. Set some headers to inform server about the type of the content


                httpPost.setHeader("Accept", "application/json;charset=utf-8");
                httpPost.setHeader("Content-type", "application/json");
                AppPrefs appPrefs = new AppPrefs(getmContext());
                String token = appPrefs.getToken();
                if( token != ""){
                    httpPost.setHeader("Authorization","Token "+token);
                }
                // 8. Execute POST request to the given URL
                HttpResponse httpResponse = httpclient.execute(httpPost);

                // se.consumeContent();

                // 9. receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                // 10. convert inputstream to string
                if(inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                                  Log.d("HTTP POST ResultStream", result);
                }else {
                    result = "Did not work!";
                    Log.d("HTTP POST ResultStream", result);
                }
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }

            // 11. return result
            this.outputString = result;
            try {
                outputJsonObject = new JSONObject(outputString);
            }catch (Exception e){
                outputJsonObject = new JSONObject();
            }

            try {
                outputJsonArray = new JSONArray(outputString);

            }catch (Exception e){
                outputJsonArray = new JSONArray();
            }

            return result;
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

                JSONArray tmp = new JSONArray();

                for(int i=0; i<uid_inner.length; i++){
                    JSONObject tj = new JSONObject();
                    try{
                        tj.put("uid",uid_inner[i]);
                        tmp.put(tj);
                    }catch(Exception e){}
                }

                setOutputString(POST_hashtag(url, tmp));

                return getOutputString();
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                try {

                    // outputSchedulesJson = getOutputJsonObject();
                    //JSONArray results = outputSchedulesJson.getJSONArray("results");
                    JSONArray results = outputJsonArray;
                    JSONObject tmp_ith;
                    //nextURL = outputSchedulesJson.getString("next");
                    for(int i=0; i<results.length() ;i++){
                        tmp_ith = results.getJSONObject(i);
                        FacebookFriend u = new FacebookFriend();
                        u.setUser_id(tmp_ith.getInt("user_id"));
                        u.setWhend_username(tmp_ith.getString("user_name"));
                        u.setFacebook_username(facebookfriends.getJSONObject(i).getString("name"));
                        u.setIsFollow(tmp_ith.getInt("is_follow")==1?true:false);

                        if(facebookfriends.getJSONObject(i).getJSONObject("picture").getJSONObject("data").getString("is_silhouette").contentEquals("false")){
                            u.setFacebook_photo(facebookfriends.getJSONObject(i).getJSONObject("picture").getJSONObject("data").getString("url"));
                        }
                        else{
                            u.setFacebook_photo("null");
                        }
                        //String uid =  facebookfriends.getJSONObject(i).getString("id");

                        //ScheduleFollow_User sfu = new ScheduleFollow_User(u, tmp_ith.getInt("is_follow")==1?true:false);     // 이거 아직 모름
                        User_list.add(u);
                        adapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {

                }

            }
        }

    }
}
