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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.Notify_Schedule_Adapter;
import net.whend.soodal.whend.model.top.Notify_Schedule;

import java.util.ArrayList;


public class F4_Notify extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private FragmentTabHost mTabHost;

    TextView mainactivity_title;
    LinearLayout search_layout, setting_layout;
    ImageView search_btn, back_btn, setting_btn;
    EditText search_text;
    ListView notify_listview;

    private ArrayList<Notify_Schedule> arrayNTchedule = new ArrayList<Notify_Schedule>();

    private View rootView;

    public F4_Notify() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Notify_Schedule temp1 = new Notify_Schedule();
        Notify_Schedule temp2 = new Notify_Schedule();
        Notify_Schedule temp3 = new Notify_Schedule();

        arrayNTchedule.add(temp1);
        arrayNTchedule.add(temp2);
        arrayNTchedule.add(temp3);

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

        Notify_Schedule_Adapter notify_schedule_adapter = new Notify_Schedule_Adapter(getActivity(), R.layout.item_concise_schedule, arrayNTchedule);
        notify_listview.setAdapter(notify_schedule_adapter);


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

}
