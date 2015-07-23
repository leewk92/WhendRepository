package net.whend.soodal.whend.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.Concise_Schedule_Adapter;
import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.model.top.Concise_Schedule;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

// ToolBar 숨기기 Reference : https://mzgreen.github.io/2015/02/15/How-to-hideshow-Toolbar-when-list-is-scroling%28part1%29/
public class F1_Wall extends Fragment {


    private FragmentTabHost mTabHost;
    private View rootview;
    private ListView listview;
    private ArrayList<Concise_Schedule> arrayCSchedule = new ArrayList<Concise_Schedule>();
    private Concise_Schedule_Adapter concise_schedule_adapter;
    private TextView mainactivity_title;
    LinearLayout search_layout, setting_layout;
    ImageView search_btn, back_btn, setting_btn;
    EditText search_text;
    static String nextURL;
    private static JSONObject outputSchedulesJson;

    public F1_Wall() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        arrayCSchedule.clear();
        String url = "http://119.81.176.245/schedules/";
        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(getActivity(), url,"GET");
        a.doExecution();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url = "http://119.81.176.245/schedules/";

        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(getActivity(), url,"GET");
        a.doExecution();


        //Concise_Schedule cs = new Concise_Schedule();
        //arrayCSchedule.add(cs);
        //arrayCSchedule.add(cs);
        //arrayCSchedule.add(cs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 로고 사이즈 조정 및 로고 삽입

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.whend_actionbar);
        Toolbar toolbar = (Toolbar) container.findViewById(R.id.toolbar);

        int actionBarHeight=0;

        TypedValue tv = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }

        int height = (int) (actionBarHeight * 0.8);


        int width = (bmp.getWidth()*height/bmp.getHeight());


        Bitmap resizedbmp = Bitmap.createScaledBitmap(bmp, width, height, true);
        Drawable logo = new BitmapDrawable(getResources(), resizedbmp);

        // 사이즈변경


        ((MainActivity)getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(true); // 로고사용
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false); // 타이틀미사용
        ((MainActivity)getActivity()).getSupportActionBar().setLogo(logo); //  로고박기

        mainactivity_title = (TextView) getActivity().findViewById(R.id.mainactivity_title);
        mainactivity_title.setText("");

        search_layout = (LinearLayout) getActivity().findViewById(R.id.search_layout);
        setting_layout = (LinearLayout) getActivity().findViewById(R.id.setting_layout);

        search_btn = (ImageView) getActivity().findViewById(R.id.search_btn);
        search_text = (EditText) getActivity().findViewById(R.id.search_text);
        back_btn = (ImageView) getActivity().findViewById(R.id.back_btn);
        setting_btn = (ImageView) getActivity().findViewById(R.id.setting_btn);


        back_btn.setVisibility(View.GONE);

        search_layout.setVisibility(View.GONE);
        setting_layout.setVisibility(View.GONE);

        // View 할당
        rootview = inflater.inflate(R.layout.f1_wall_layout, container, false);
        listview = (ListView)rootview.findViewById(R.id.listview_concise_schedule);
        concise_schedule_adapter = new Concise_Schedule_Adapter(getActivity(), R.layout.item_concise_schedule, arrayCSchedule);
        listview.setAdapter(concise_schedule_adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(getActivity(), A3_SpecificScheduleActivity.class);
                intent.putExtra("id",arrayCSchedule.get(position).getId());
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
                        s.setPhoto_dir_fromweb((tmp_ith.getString("photo") == null) ? "" : tmp_ith.getString("photo"));
                        s.setFollow_count((tmp_ith.getInt("count_follow")));
                        s.setLike_count((tmp_ith.getInt("count_like")));

                        Concise_Schedule cs = new Concise_Schedule(s);
                        cs.setIsLike((tmp_ith.getInt("like")==1)?true:false);
                        cs.setIsFollow((tmp_ith.getInt("follow")==1)?true:false);

                        arrayCSchedule.add(cs);
                    }
                    concise_schedule_adapter.notifyDataSetChanged();
                }catch(Exception e){

                }

            }
        }
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

}
