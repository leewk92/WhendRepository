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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.SearchHashTag_Adapter;
import net.whend.soodal.whend.form.SearchSchedule_Adapter;
import net.whend.soodal.whend.model.base.HashTag;
import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.model.top.Concise_Schedule;
import net.whend.soodal.whend.model.top.Search_HashTag;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wonkyung on 2015-07-12.
 */
public class F2_1_2_SearchHashtag extends Fragment {

    private View rootview;
    private ListView listview;
    private EditText search_text;
    private ArrayList<Search_HashTag> SHashtag_list = new ArrayList<Search_HashTag>();
    private SearchHashTag_Adapter searchHashTag_adapter;
    private static JSONObject outputSchedulesJson;
    static String nextURL;


    public F2_1_2_SearchHashtag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String url = "http://119.81.176.245/hashtags/all/?search=";
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
        rootview = inflater.inflate(R.layout.f2_1_2_searchhashtag_layout, container, false);
        listview = (ListView)rootview.findViewById(R.id.listview_searchhashtag);
        searchHashTag_adapter = new SearchHashTag_Adapter(getActivity(), R.layout.item_searchhashtag, SHashtag_list);
        listview.setAdapter(searchHashTag_adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(getActivity(), A7_SpecificHashTagActivity.class);
                intent.putExtra("id",SHashtag_list.get(position).getHashTag().getId());
                intent.putExtra("title",SHashtag_list.get(position).getHashTag().getTitle());
                intent.putExtra("follower_count",SHashtag_list.get(position).getHashTag().getFollower_count());

                startActivity(intent);
            }
        });
        return rootview;
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
                SHashtag_list.clear();
                try{
                    outputSchedulesJson = getOutputJsonObject();
                    JSONArray results = outputSchedulesJson.getJSONArray("results");
                    JSONObject tmp_ith;
                    nextURL = outputSchedulesJson.getString("next");
                    for(int i=0; i<outputSchedulesJson.getInt("count") ;i++){
                        HashTag h = new HashTag();

                        tmp_ith = results.getJSONObject(i);
                        h.setId(tmp_ith.getInt("id"));
                        h.setTitle(tmp_ith.getString("title"));
                        h.setFollower_count(tmp_ith.getInt("follower_count"));
                        h.setPhoto((tmp_ith.getString("photo") == null) ? "" : tmp_ith.getString("photo"));
                        h.setContent(tmp_ith.getString("content"));
                        h.setCount_schedule(tmp_ith.getInt("count_schedule"));
                        h.setCount_upcoming_schedule(tmp_ith.getInt("count_upcoming_schedule"));
                        h.setIs_Follow(tmp_ith.getInt("is_follow")==1?true:false);
                        Search_HashTag sh = new Search_HashTag(h);
                        SHashtag_list.add(sh);
                    }
                    searchHashTag_adapter.notifyDataSetChanged();
                }catch(Exception e){

                }

            }
        }
    }


}
