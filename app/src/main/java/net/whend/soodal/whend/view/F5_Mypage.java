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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.model.base.User;
import net.whend.soodal.whend.model.top.Concise_Schedule;
import net.whend.soodal.whend.util.HTTPRestfulUtilizer;

import org.json.JSONArray;
import org.json.JSONObject;


public class F5_Mypage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private FragmentTabHost mTabHost;

    TextView mainactivity_title;
    ImageView search_btn, back_btn;
    EditText search_text;
    JSONObject outputSchedulesJson;
    private View rootView;
    User u = new User();
    public F5_Mypage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(false);

        rootView = inflater.inflate(R.layout.f5_mypage_layout, container, false);
        mainactivity_title = (TextView) getActivity().findViewById(R.id.mainactivity_title);
        mainactivity_title.setText("마이 페이지");

        search_btn = (ImageView) getActivity().findViewById(R.id.search_btn);
        search_text = (EditText) getActivity().findViewById(R.id.search_text);
        back_btn = (ImageView) getActivity().findViewById(R.id.back_btn);

        search_btn.setVisibility(View.INVISIBLE);
        search_text.setVisibility(View.INVISIBLE);
        back_btn.setVisibility(View.GONE);

        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setTop(120);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("fragmentb").setIndicator("MY"),
                F5_1_MyTimeline.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("fragmentc").setIndicator("관심"),
                F5_2_MyLikeSchedules.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("fragmentd").setIndicator("Fragment D"),
                F4_Notify.class, null);


        String url = "http://119.81.176.245/userinfos/";

        HTTPRestfulUtilizerExtender a = new HTTPRestfulUtilizerExtender(getActivity(),rootView,url,"GET");
        a.doExecution();

        View follower_count_clickablelayout = rootView.findViewById(R.id.follower_count_clickablelayout);
        View following_count_clickablelayout = rootView.findViewById(R.id.following_count_clickablelayout);
        FollowerClickListener(follower_count_clickablelayout);

        return rootView;
    }

    // 유저 이름 누를 때 리스너
    public void FollowerClickListener(View follower_count_clickablelayout){
        follower_count_clickablelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), A5_WhoFollowsScheduleActivity.class);
                intent.putExtra("url", "http://119.81.176.245/userinfos/"+u.getId()+"/followers/");
                startActivity(intent);
            }
        });

    }


    class HTTPRestfulUtilizerExtender extends HTTPRestfulUtilizer {
        private View v;

        // Constructor for GET
        public HTTPRestfulUtilizerExtender(Context mContext,View rootView, String url, String HTTPRestType) {
            this.v = rootView;
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
                    JSONObject tmp_ith = outputSchedulesJson;


                    tmp_ith = outputSchedulesJson;
                    u.setId(tmp_ith.getInt("user_id"));
                    u.setUsername(tmp_ith.getString("user_name"));
                    u.setPhoto(tmp_ith.getString("photo")==null?"":tmp_ith.getString("photo"));
                    u.setCount_following_user(tmp_ith.getInt("count_following_user"));
                    u.setCount_following_hashtag(tmp_ith.getInt("count_following_hashtag"));
                    u.setCount_follower(tmp_ith.getInt("count_follower"));

                    JSONArray tmpjsonarray = tmp_ith.getJSONArray("following_user");
                    if(tmpjsonarray!=null) {
                        int[] following_user = new int[tmpjsonarray.length()];
                        // Extract numbers from JSON array.
                        for (int i = 0; i < tmpjsonarray.length(); ++i) {
                            following_user[i] = tmpjsonarray.optInt(i);
                        }
                        u.setFollowing_user(following_user);
                    }

                    tmpjsonarray = tmp_ith.getJSONArray("following_hashtag");

                    if(tmpjsonarray!=null) {
                        int[] following_hashtag = new int[tmpjsonarray.length()];
                        // Extract numbers from JSON array.
                        for (int i = 0; i < tmpjsonarray.length(); ++i) {
                            following_hashtag[i] = tmpjsonarray.optInt(i);
                        }
                        u.setFollowing_hashtag(following_hashtag);
                    }

                    tmpjsonarray = tmp_ith.getJSONArray("like_schedule");

                    if(tmpjsonarray!=null) {
                        int[] like_schedule = new int[tmpjsonarray.length()];
                        // Extract numbers from JSON array.
                        for (int i = 0; i < tmpjsonarray.length(); ++i) {
                            like_schedule[i] = tmpjsonarray.optInt(i);
                        }
                        u.setLike_schedule(like_schedule);
                    }

                    tmpjsonarray = tmp_ith.getJSONArray("following_schedule");
                    if(tmpjsonarray!=null) {
                        int[] following_schedule = new int[tmpjsonarray.length()];
                        // Extract numbers from JSON array.
                        for (int i = 0; i < tmpjsonarray.length(); ++i) {
                            following_schedule[i] = tmpjsonarray.optInt(i);
                        }
                        u.setFollowing_schedule(following_schedule);
                    }
                }catch(Exception e){

                }
                ((TextView)v.findViewById(R.id.username)).setText(u.getUsername());
                ((TextView)v.findViewById(R.id.follower_count)).setText(u.getCount_follower() + "");
                ((TextView)v.findViewById(R.id.schedule_count)).setText(u.getSchedule_count() + "");        // not yet
                ((TextView)v.findViewById(R.id.following_count)).setText(String.valueOf(u.getCount_following_hashtag() + u.getCount_following_user()));

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
