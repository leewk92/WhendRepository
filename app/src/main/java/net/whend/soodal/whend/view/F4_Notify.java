package net.whend.soodal.whend.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.Notify_Schedule_Adapter;
import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.model.top.Concise_Schedule;
import net.whend.soodal.whend.model.top.Notify_Schedule;
import net.whend.soodal.whend.util.AppPrefs;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class F4_Notify extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private FragmentTabHost mTabHost;

    TextView mainactivity_title;
    LinearLayout search_layout, setting_layout;
    ImageView search_btn, back_btn, setting_btn;
    EditText search_text;
    ListView notify_listview;
    private static JSONObject outputSchedulesJson;
    private ArrayList<Notify_Schedule> arrayNTchedule = new ArrayList<Notify_Schedule>();

    private View rootView;
    Notify_Schedule_Adapter notify_schedule_adapter;
    static String nextURL;

    public F4_Notify() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        arrayNTchedule.clear();
        notify_schedule_adapter.notifyDataSetChanged();
        String url = "http://119.81.176.245/notifications/";
        nextURL = null;
        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(getActivity(), url,"GET");
        a.doExecution();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*arrayNTchedule.clear();
        String url = "http://119.81.176.245/notifications/";
        nextURL = null;
        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(getActivity(), url,"GET");
        a.doExecution();*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ((MainActivity)getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(false);

        rootView = inflater.inflate(R.layout.f4_notify_layout, container, false);

        mainactivity_title = (TextView) getActivity().findViewById(R.id.mainactivity_title);
        mainactivity_title.setText("알림");

        search_btn = (ImageView) getActivity().findViewById(R.id.search_btn);
        search_text = (EditText) getActivity().findViewById(R.id.search_text);
        back_btn = (ImageView) getActivity().findViewById(R.id.back_btn);
        setting_btn = (ImageView) getActivity().findViewById(R.id.setting_btn);

        search_layout = (LinearLayout) getActivity().findViewById(R.id.search_layout);
        setting_layout = (LinearLayout) getActivity().findViewById(R.id.setting_layout);

        notify_listview = (ListView) rootView.findViewById(R.id.listview_notify_schedule);

        notify_schedule_adapter = new Notify_Schedule_Adapter(getActivity(), R.layout.item_concise_schedule, arrayNTchedule);
        notify_listview.setAdapter(notify_schedule_adapter);


        notify_listview.setOnScrollListener(new EndlessScrollListener());


        back_btn.setVisibility(View.GONE);
        search_layout.setVisibility(View.GONE);
        setting_layout.setVisibility(View.GONE);


        return rootView;

    }

    //
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
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
                arrayNTchedule.clear();
                try{
                    outputSchedulesJson = getOutputJsonObject();

                    JSONArray results = outputSchedulesJson.getJSONArray("results");
                    JSONObject tmp_ith;
                    nextURL = outputSchedulesJson.getString("next");

                    for(int i=0; i<results.length() ;i++){
                        Notify_Schedule ns = new Notify_Schedule();
                        tmp_ith = results.getJSONObject(i);


                        ns.setUser_id(tmp_ith.getInt("actor_id"));
                        ns.setActor_type(tmp_ith.getString("actor_type"));
                        if(ns.getActor_type().equals("hash tag")){
                            ns.setActor_name(tmp_ith.getString("actor_name2"));
                        }else{
                            ns.setActor_name(tmp_ith.getString("actor_name"));
                        }

                        ns.setVerb(tmp_ith.getString("verb"));
                        ns.setDescription(tmp_ith.getString("description"));
                        ns.setTimestamp(tmp_ith.getString("timestamp"));
                        ns.setTarget_type(tmp_ith.getString("target_type"));
                        ns.setTarget_id((tmp_ith.getString("target_id")) == "null" ? -1 : tmp_ith.getInt("target_id"));


                        ns.setUnread(tmp_ith.getBoolean("unread"));

                        AppPrefs appPrefs = new AppPrefs(getActivity());

                        if(ns.getActor_name().equals(appPrefs.getUsername())){
                            notify_schedule_adapter.notifyDataSetChanged();

                        }else{
                            arrayNTchedule.add(ns);
                            notify_schedule_adapter.notifyDataSetChanged();
                        }
                    }


                    // 내용 클릭 리스너

                    notify_listview.setClickable(true);
                    notify_listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                            // type이 schedule이면
                            if(arrayNTchedule.get(position).getTarget_type().equals("schedule")){
                                Intent intent = new Intent(getActivity(), A3_SpecificScheduleActivity.class);
                                intent.putExtra("id", arrayNTchedule.get(position).getTarget_id());
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.abc_popup_exit);

                            // 없으면 -1을 넣고 사용자정보로 보냄냄
                           } else if (arrayNTchedule.get(position).getTarget_id() == -1 || arrayNTchedule.get(position).getTarget_type().equals("user info") ){
                                Intent intent = new Intent(getActivity(), A2_UserProfileActivity.class);
                                intent.putExtra("id", arrayNTchedule.get(position).getUser_id());
                                startActivity(intent);

                                getActivity().overridePendingTransition(R.anim.abc_popup_enter, R.anim.abc_popup_exit);
                            }

                        }
                    });

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
            task.execute(getUrl(), getHTTPRestType());
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
                    Log.d("loadmore","loadmore called");
                    outputSchedulesJson = getOutputJsonObject();

                    JSONArray results = outputSchedulesJson.getJSONArray("results");
                    JSONObject tmp_ith;
                    nextURL = outputSchedulesJson.getString("next");
                    for(int i=0; i<results.length() ;i++){
                        Notify_Schedule ns = new Notify_Schedule();
                        tmp_ith = results.getJSONObject(i);

                        ns.setUser_id(tmp_ith.getInt("actor_id"));
                        ns.setActor_type(tmp_ith.getString("actor_type"));
                        if(ns.getActor_type().equals("hash tag")){
                            ns.setActor_name(tmp_ith.getString("actor_name2"));
                        }else{
                            ns.setActor_name(tmp_ith.getString("actor_name"));
                        }
                        ns.setVerb(tmp_ith.getString("verb"));
                        ns.setDescription(tmp_ith.getString("description"));
                        ns.setTimestamp(tmp_ith.getString("timestamp"));
                        ns.setTarget_type(tmp_ith.getString("target_type"));
                        ns.setTarget_id((tmp_ith.getString("target_id"))=="null" ? -1 : tmp_ith.getInt("target_id"));
                        ns.setUnread(tmp_ith.getBoolean("unread"));


                        AppPrefs appPrefs = new AppPrefs(getActivity());


                        if(ns.getActor_name().equals(appPrefs.getUsername())){
                            notify_schedule_adapter.notifyDataSetChanged();

                        }else{
                            arrayNTchedule.add(ns);
                            notify_schedule_adapter.notifyDataSetChanged();
                        }

                    }

                    notify_listview.setClickable(true);
                    notify_listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            if(arrayNTchedule.get(position).getTarget_type().equals("schedule")){
                                Intent intent = new Intent(getActivity(), A3_SpecificScheduleActivity.class);
                                intent.putExtra("id", arrayNTchedule.get(position).getTarget_id());
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.abc_popup_exit);


                            } else if (arrayNTchedule.get(position).getTarget_id() == -1 || arrayNTchedule.get(position).getTarget_type().equals("user info") ){
                                Intent intent = new Intent(getActivity(), A2_UserProfileActivity.class);
                                intent.putExtra("id", arrayNTchedule.get(position).getUser_id());
                                startActivity(intent);

                                getActivity().overridePendingTransition(R.anim.abc_popup_enter, R.anim.abc_popup_exit);
                            }

                        }
                    });

                }catch(Exception e){

                }

            }
        }
    }

}
