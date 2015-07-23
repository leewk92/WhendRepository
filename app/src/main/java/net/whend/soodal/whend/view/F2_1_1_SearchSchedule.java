package net.whend.soodal.whend.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.Concise_Schedule_Adapter;
import net.whend.soodal.whend.form.SearchSchedule_Adapter;
import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.model.top.Concise_Schedule;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wonkyung on 2015-07-12.
 */
public class F2_1_1_SearchSchedule extends Fragment {

    private View rootview;
    private ListView listview;
    private EditText search_text;
    private ArrayList<Concise_Schedule> arrayCSchedule = new ArrayList<Concise_Schedule>();
    private SearchSchedule_Adapter searchSchedule_adapter;
    private static JSONObject outputSchedulesJson;
    static String nextURL;
    public F2_1_1_SearchSchedule() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String url = "http://119.81.176.245/schedules/all/?search=";

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
                HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(getActivity(),url+search_text.getText(),"GET");
                a.doExecution();
                Log.d("changed.",search_text.getText().toString());
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
        rootview = inflater.inflate(R.layout.f2_1_1_searchschedule_layout, container, false);
        listview = (ListView)rootview.findViewById(R.id.listview_searchschedule);
        searchSchedule_adapter = new SearchSchedule_Adapter(getActivity(), R.layout.item_searchschedule, arrayCSchedule);
        listview.setAdapter(searchSchedule_adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(getActivity(), A3_SpecificScheduleActivity.class);
                intent.putExtra("id", arrayCSchedule.get(position).getId());
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
                arrayCSchedule.clear();
                try{
                    outputSchedulesJson = getOutputJsonObject();
                    JSONArray results = outputSchedulesJson.getJSONArray("results");
                    JSONObject tmp_ith;
                    nextURL = outputSchedulesJson.getString("next");
                    for(int i=0; i<outputSchedulesJson.getInt("count") ;i++){
                        Schedule s = new Schedule();
                        tmp_ith = results.getJSONObject(i);
                        s.setId(tmp_ith.getInt("id"));
                        s.setTitle(tmp_ith.getString("title"));
                        s.setStarttime(tmp_ith.getString("start_time"));
                        s.setEndtime(tmp_ith.getString("end_time"));
                        s.setMemo(tmp_ith.getString("memo"));
                        s.setUploaded_username(tmp_ith.getString("user_name"));
                        s.setUploaded_user_id(tmp_ith.getInt("user_id"));
                        s.setPhoto_dir_fromweb(tmp_ith.getString("photo"));
                        Concise_Schedule cs = new Concise_Schedule(s);
                        arrayCSchedule.add(cs);
                    }
                    searchSchedule_adapter.notifyDataSetChanged();
                }catch(Exception e){

                }

            }
        }
    }
}
