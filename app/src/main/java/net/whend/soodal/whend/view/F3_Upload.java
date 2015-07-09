package net.whend.soodal.whend.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.whend.soodal.whend.R;


public class F3_Upload extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private FragmentTabHost mTabHost;
    private View rootView;

    public F3_Upload() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("업로드");

        rootView = inflater.inflate(R.layout.f3_upload_layout, container, false);

        Intent intent = new Intent(getActivity(),A1_UploadActivity.class);
        startActivityForResult(intent, 1);
        getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);

        return rootView;

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setCurrentTab(0);
    }


    public void setCurrentTab(int tab_index){
        mTabHost = (FragmentTabHost)getActivity().findViewById(android.R.id.tabhost);
        mTabHost.setCurrentTab(tab_index);
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
