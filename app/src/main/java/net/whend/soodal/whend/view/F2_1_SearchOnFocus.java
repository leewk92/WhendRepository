package net.whend.soodal.whend.view;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.whend.soodal.whend.R;

/**
 * Created by wonkyung on 2015-07-12.
 */
public class F2_1_SearchOnFocus extends Fragment {

    private FragmentTabHost mTabHost;

    TextView mainactivity_title;
    ImageView search_btn, back_btn;
    EditText search_text;

    private View rootView;

    public F2_1_SearchOnFocus() {
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

        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("searchschedule").setIndicator("일정"),
                F2_1_1_SearchSchedule.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("searchhashtag").setIndicator("태그"),
                F2_1_2_SearchHashtag.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("searchuser").setIndicator("유저"),
                F2_1_3_SearchUser.class, null);

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
}
