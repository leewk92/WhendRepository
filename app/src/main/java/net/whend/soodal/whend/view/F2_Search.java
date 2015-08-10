package net.whend.soodal.whend.view;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.Grid_Search_Adapter;
import net.whend.soodal.whend.model.base.HashTag;
import net.whend.soodal.whend.model.top.Grid_Search_Schedule;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;
import net.whend.soodal.whend.util.observablescrollview.ObservableScrollView;
import net.whend.soodal.whend.util.observablescrollview.ScrollViewListener;
import net.whend.soodal.whend.util.quitview.QuiltView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

// Nested Fragment Ref : http://developer.android.com/about/versions/android-4.2.html#NestedFragments
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.


>>>>>>> origin/JB:app/src/main/java/net/whend/soodal/whend/view/Fragment2.java
 * create an instance of this fragment.
 */
public class F2_Search extends Fragment implements ScrollViewListener {
    // TODO: Rename parameter arguments, choose names that match
    public static GoogleAnalytics analytics;
    public static Tracker mTracker;
    private FragmentTabHost mTabHost;
    ArrayList<Grid_Search_Schedule> arrayGSchedule = new ArrayList<Grid_Search_Schedule>();
    private Grid_Search_Adapter grid_search_adapter;


    private TextView mainactivity_title;
    LinearLayout search_layout, setting_layout;
    private ImageView search_btn, back_btn, setting_btn;
    private GridView search_gridview;
    private EditText search_text;
    private LinearLayout search_grid;
    private FrameLayout search_linear;
    private FrameLayout root_layout;
    private Toolbar toolbar;
    static String nextURL;
    public QuiltView quiltView;
    private JSONObject outputSchedulesJson;
//    private JSONArray outputSchedulesJson;
    Grid_Search_Adapter mgrid_search_adapter;
    boolean loading=true;
    int threshold=250;
    int page=0;
    private View rootView;

        public F2_Search() {
        // Required empty public constructor
    }
/*
    @Override
    public void onPause() {
        super.onPause();
        arrayGSchedule.clear();
        // quiltView.removeAllViews();
        try{
            quiltView.refresh();
        }catch(Exception e){}
        quiltView = (QuiltView) rootView.findViewById(R.id.quilt);
        quiltView.setChildPadding(-3);

        String url = "http://119.81.176.245/hashtags/";
        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(getActivity(),url,"GET");
        a.doExecution();
    }
*/
    @Override
    public void onResume(){
        super.onResume();
        loading=true;
        page=0;
        threshold=250;
        //threshold

        arrayGSchedule.clear();
        mgrid_search_adapter.notifyDataSetChanged();


        for(int i=0; i< mgrid_search_adapter.getCount(); i++)
            quiltView.addPatchView(mgrid_search_adapter.getView(i,null,null));
        //첫번째 뜨는거 지우기 -> 실패
        try{
            if(quiltView.views.get(0) != null)
                quiltView.removeQuilt(quiltView.views.get(0));
        }catch(Exception e){}

        //quiltView.removeViewAt(0);
        Log.d("count",mgrid_search_adapter.getCount()+"");
        //quiltView.removeAllViews();
        try{
            quiltView.refresh();
        }catch(Exception e){}


        quiltView = (QuiltView) rootView.findViewById(R.id.quilt);
        quiltView.setChildPadding(-3);

        String url = "http://119.81.176.245/hashtags/";
        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(getActivity(),url,"GET");
        a.doExecution();

   //     mTracker.setScreenName("Image~" + "F2_Search");
  //      mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
   //     AnalyticsApplication application = (AnalyticsApplication)getActivity().getApplication();
  //      mTracker= application.getDefaultTracker();
  }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // search_linear 에 F2_1_SearchOnFocus 프래그먼트 심기
        final Fragment temp = new F2_1_SearchOnFocus();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        try {
            transaction.replace(R.id.search_linear, temp);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        }catch (Exception e) {
            transaction.show(temp).commit();
        }

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(false);


        rootView = inflater.inflate(R.layout.f2_search_layout, container, false);

        search_grid = (LinearLayout) rootView.findViewById(R.id.search_grid);
        search_linear = (FrameLayout) rootView.findViewById(R.id.search_linear);
        root_layout = (FrameLayout) rootView.findViewById(R.id.search_rootlayout);

      //  quiltView = (QuiltView) rootView.findViewById(R.id.quilt);
      //  quiltView.setChildPadding(-3);



        // 툴바 커스터마이징

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mainactivity_title = (TextView) getActivity().findViewById(R.id.mainactivity_title);
        mainactivity_title.setText("");

        search_btn = (ImageView) getActivity().findViewById(R.id.search_btn);
        search_text = (EditText) getActivity().findViewById(R.id.search_text);
        back_btn = (ImageView) getActivity().findViewById(R.id.back_btn);
        setting_btn = (ImageView) getActivity().findViewById(R.id.setting_btn);


        search_layout = (LinearLayout) getActivity().findViewById(R.id.search_layout);
        setting_layout = (LinearLayout) getActivity().findViewById(R.id.setting_layout);

        search_layout.setVisibility(View.VISIBLE);
        search_btn.setVisibility(View.VISIBLE);
        search_text.setVisibility(View.VISIBLE);
        setting_layout.setVisibility(View.GONE);


        // 기본 frame layout 설정
        search_grid.setVisibility(View.VISIBLE);
        search_linear.setVisibility(View.INVISIBLE);
/*
        Fragment temp = new F5_Mypage();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.search_linear, temp).commit();
        */

        mgrid_search_adapter = new Grid_Search_Adapter(getActivity(), R.layout.item_gridsearch_schedule, arrayGSchedule);

//        for(int i=0; i< mgrid_search_adapter.getCount(); i++)
//            quiltView.addPatchView(mgrid_search_adapter.getView(i,null,null));

        // search_text 검색시 이벤트
        search_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Toast.makeText(getActivity(), search_text.getText().toString(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        // 포커스 받았을 시, 클릭됐을 시

        search_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    search_grid.setVisibility(View.INVISIBLE);
                    search_linear.setVisibility(View.VISIBLE);
                    back_btn.setVisibility(View.VISIBLE);
                    // temp 가 탭호스트, 프래그먼트 안에 탭호스트 하는 법


                }

            }
        });

        search_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_grid.setVisibility(View.INVISIBLE);
                search_linear.setVisibility(View.VISIBLE);
                back_btn.setVisibility(View.VISIBLE);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                search_linear.setVisibility(View.INVISIBLE);
                search_grid.setVisibility(View.VISIBLE);
                search_grid.requestFocus();
                back_btn.setVisibility(View.GONE);

                //hide keyboard
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        });




        return rootView;
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {

        Log.d("scroll_y",y+" " + oldy);
        Log.d("scroll_position",scrollView.getVerticalScrollbarPosition()+"");
        Log.d("scroll_Y",scrollView.getY()+"");
        Log.d("scroll_getHeight",scrollView.getHeight()+"");
        if(y > threshold && loading){

            loading = false;
            String url = "http://119.81.176.245/hashtags/";
            HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(getActivity(),nextURL,"GET");
            a.doExecution();
            threshold = threshold +270*3;
            Log.d("scroll_threshold",threshold +"");
        }
        //Toast.makeText(getActivity(),"scrolled",Toast.LENGTH_SHORT);
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
                    outputSchedulesJson = getOutputJsonObject();
                    JSONArray results = outputSchedulesJson.getJSONArray("results");
                    JSONObject tmp_ith;
                    nextURL = outputSchedulesJson.getString("next");
                    for(int i=0; i<results.length() ;i++){
                        HashTag h = new HashTag();
                        tmp_ith = results.getJSONObject(i);

                        h.setId(tmp_ith.getInt("id"));
                        h.setTitle(tmp_ith.getString("title"));
                        h.setFollower_count(tmp_ith.getInt("count_follower"));
                        h.setPhoto((tmp_ith.getString("photo") == "null") ? "" : tmp_ith.getString("photo").substring(0, tmp_ith.getString("photo").length() - 4) + ".800x200.jpg");
                        h.setContent(tmp_ith.getString("content"));
                        h.setCount_schedule(tmp_ith.getInt("count_schedule"));
                        h.setCount_upcoming_schedule(tmp_ith.getInt("count_upcoming_schedule"));
                        h.setIs_Follow(tmp_ith.getInt("is_follow")==1?true:false);


                        Grid_Search_Schedule gs = new Grid_Search_Schedule(h);
                        arrayGSchedule.add(gs);
                        mgrid_search_adapter.notifyDataSetChanged();
                    }

            /*        outputSchedulesJson = getOutputJsonArray();
             //       JSONArray results = outputSchedulesJson.getJSONArray("results");
                    JSONObject tmp_ith;
             //       nextURL = outputSchedulesJson.getString("next");
                    for(int i=0; i<outputSchedulesJson.length() ;i++){
                        HashTag h = new HashTag();
                        tmp_ith = outputSchedulesJson.getJSONObject(i);

                        h.setId(tmp_ith.getInt("id"));
                        h.setTitle(tmp_ith.getString("title"));
                        h.setFollower_count(tmp_ith.getInt("count_follower"));
                        h.setPhoto((tmp_ith.getString("photo") == "null") ? "" : tmp_ith.getString("photo").substring(0, tmp_ith.getString("photo").length() - 4) + ".800x200.jpg");
                        h.setContent(tmp_ith.getString("content"));
                        h.setCount_schedule(tmp_ith.getInt("count_schedule"));
                        h.setCount_upcoming_schedule(tmp_ith.getInt("count_upcoming_schedule"));
                        h.setIs_Follow(tmp_ith.getInt("is_follow")==1?true:false);


                        Grid_Search_Schedule gs = new Grid_Search_Schedule(h);
                        arrayGSchedule.add(gs);
                        mgrid_search_adapter.notifyDataSetChanged();
                    }*/

                    for(int i=10*page; i< mgrid_search_adapter.getCount(); i++) {
                        quiltView.addPatchView(mgrid_search_adapter.getView(i, null, null));
                    }
                }catch(Exception e){

                }finally{

                    ObservableScrollView scrollView = (ObservableScrollView) rootView.findViewById(R.id.quilt_scroll);
                    scrollView.setScrollViewListener(F2_Search.this);

                }
                loading = true;
                page++;
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("back_btn_gone", back_btn.getVisibility() == View.GONE);
        super.onSaveInstanceState(outState);
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
