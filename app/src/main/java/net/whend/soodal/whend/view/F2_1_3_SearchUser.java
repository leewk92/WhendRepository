package net.whend.soodal.whend.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.SearchHashTag_Adapter;
import net.whend.soodal.whend.form.SearchUser_Adapter;
import net.whend.soodal.whend.model.base.HashTag;
import net.whend.soodal.whend.model.base.User;
import net.whend.soodal.whend.model.top.Search_HashTag;
import net.whend.soodal.whend.model.top.Search_User;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wonkyung on 2015-07-12.
 */
public class F2_1_3_SearchUser extends Fragment {

    private View rootview;
    private ListView listview;
    private EditText search_text;
    private ArrayList<Search_User> SUser_list = new ArrayList<Search_User>();
    private SearchUser_Adapter searchUser_adapter;
    private static JSONObject outputSchedulesJson;
    static String nextURL;

    public F2_1_3_SearchUser() {
        // Required empty public constructor

    }

    @Override
    public void onResume() {
        super.onResume();
        SUser_list.clear();
        String url =  "http://119.81.176.245/userinfos/all/?search=";
        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(getActivity(), url,"GET");
        a.doExecution();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SUser_list.clear();
        final String url = "http://119.81.176.245/userinfos/all/?search=";
        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(getActivity(),url,"GET");
        a.doExecution();

        search_text = (EditText)(getParentFragment().getParentFragment().getActivity().findViewById(R.id.search_text));
        // search_text 검색시 이벤트

        search_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Toast.makeText(getActivity(), search_text.getText().toString(), Toast.LENGTH_SHORT).show();
                    return true;
                } else {

                }
                return false;
            }
        });
        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(getActivity(), url + search_text.getText(), "GET");
                a.doExecution();
                Log.d("changed.", search_text.getText().toString());
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // View 할당
        rootview = inflater.inflate(R.layout.f2_1_3_searchuser_layout, container, false);
        listview = (ListView)rootview.findViewById(R.id.listview_searchuser);
        searchUser_adapter =  new SearchUser_Adapter(getActivity(), R.layout.item_searchuser, SUser_list);
        listview.setAdapter(searchUser_adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(getActivity(), A2_UserProfileActivity.class);
                intent.putExtra("id", SUser_list.get(position).getUser().getId());
                startActivity(intent);
            }
        });
        listview.setOnScrollListener(new EndlessScrollListener());
        return rootview;
    }



    // 끝없이 로딩 하는거
    public class EndlessScrollListener implements AbsListView.OnScrollListener {

        private int visibleThreshold = 2;
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
                    HTTPRestfulUtilizerExtender_loadmore b = new HTTPRestfulUtilizerExtender_loadmore(getActivity(),nextURL,"POST");
                    b.doExecution();
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

        // Constructor for GET
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
            task.execute(getUrl(),getHTTPRestType());
        }
        class HttpAsyncTaskExtenders extends HTTPRestfulUtilizer.HttpAsyncTask{
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
                SUser_list.clear();
                try{
                    outputSchedulesJson = getOutputJsonObject();
                    JSONArray results = outputSchedulesJson.getJSONArray("results");
                    JSONObject tmp_ith;
                    nextURL = outputSchedulesJson.getString("next");
                    for(int i=0; i<results.length() ;i++){
                        User u = new User();
                        tmp_ith = results.getJSONObject(i);
                        u.setId(tmp_ith.getInt("user_id"));
                        u.setUsername(tmp_ith.getString("user_name"));
                        u.setUser_photo((tmp_ith.getString("photo") == "null") ? "" : tmp_ith.getString("photo").substring(0, tmp_ith.getString("photo").length() - 4) + ".200x200.jpg");
                        u.setCount_following_user(tmp_ith.getInt("count_following_user"));
                        u.setCount_follower(tmp_ith.getInt("count_follower"));
                        u.setCount_uploaded_schedule(tmp_ith.getInt("count_uploaded_schedule"));
                        u.setCount_following_hashtag(tmp_ith.getInt("count_following_hashtag"));
                        Search_User su = new Search_User(u);
                        su.setIsFollow(tmp_ith.getInt("is_follow")==1?true:false);
                        SUser_list.add(su);
                    }
                    searchUser_adapter.notifyDataSetChanged();
                }catch(Exception e){

                }

            }
        }
    }


    class HTTPRestfulUtilizerExtender_loadmore extends HTTPRestfulUtilizer {

        // Constructor for GET
        public HTTPRestfulUtilizerExtender_loadmore(Context mContext, String url, String HTTPRestType) {
            setmContext(mContext);
            setUrl(url);
            setHTTPRestType(HTTPRestType);
            task = new HttpAsyncTaskExtenders();
            Log.d("HTTP Constructor url", url);
            // new HttpAsyncTask().execute(url,HTTPRestType);
        }

        @Override
        public void doExecution(){
            task.execute(getUrl(),getHTTPRestType());
        }
        class HttpAsyncTaskExtenders extends HTTPRestfulUtilizer.HttpAsyncTask{
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

                try{
                    outputSchedulesJson = getOutputJsonObject();
                    JSONArray results = outputSchedulesJson.getJSONArray("results");
                    JSONObject tmp_ith;
                    nextURL = outputSchedulesJson.getString("next");
                    for(int i=0; i<results.length() ;i++){
                        User u = new User();
                        tmp_ith = results.getJSONObject(i);
                        u.setId(tmp_ith.getInt("user_id"));
                        u.setUsername(tmp_ith.getString("user_name"));
                        u.setUser_photo((tmp_ith.getString("photo") == "null") ? "" : tmp_ith.getString("photo").substring(0, tmp_ith.getString("photo").length() - 4) + ".200x200.jpg");
                        u.setCount_following_user(tmp_ith.getInt("count_following_user"));
                        u.setCount_follower(tmp_ith.getInt("count_follower"));
                        u.setCount_uploaded_schedule(tmp_ith.getInt("count_uploaded_schedule"));
                        u.setCount_following_hashtag(tmp_ith.getInt("count_following_hashtag"));
                        Search_User su = new Search_User(u);
                        su.setIsFollow(tmp_ith.getInt("is_follow")==1?true:false);
                        SUser_list.add(su);
                    }
                    searchUser_adapter.notifyDataSetChanged();
                }catch(Exception e){

                }

            }
        }
    }

}
