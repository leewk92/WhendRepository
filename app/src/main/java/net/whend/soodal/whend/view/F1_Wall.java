package net.whend.soodal.whend.view;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.Concise_Schedule_Adapter;
import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.model.top.Concise_Schedule;

import java.util.ArrayList;


public class F1_Wall extends Fragment {


    private FragmentTabHost mTabHost;
    private View rootView;
    private ListView listview;
    private ArrayList<Schedule> arraySchedule = new ArrayList<Schedule>();
    private Concise_Schedule_Adapter concise_schedule_adapter;

    public F1_Wall() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Schedule a = new Schedule();
        arraySchedule.add(a);
        arraySchedule.add(a);
        arraySchedule.add(a);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("WhenD");

        rootView = inflater.inflate(R.layout.f1_wall_layout, container, false);

        listview = (ListView)rootView.findViewById(R.id.listview_concise_schedule);
        listview.setAdapter(new Concise_Schedule_Adapter(getActivity(),R.layout.item_concise_schedule,arraySchedule));

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
