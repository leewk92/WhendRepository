package net.whend.soodal.whend.view;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.whend.soodal.whend.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
<<<<<<< HEAD:app/src/main/java/net/whend/soodal/whend/view/F2_Search.java
 * Use the {@link F2_Search#newInstance} factory method to
=======

>>>>>>> origin/JB:app/src/main/java/net/whend/soodal/whend/view/Fragment2.java
 * create an instance of this fragment.
 */
public class F2_Search extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private FragmentTabHost mTabHost;
    private View rootView;

        public F2_Search() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("검색");

        rootView = inflater.inflate(R.layout.f2_search_layout, container, false);

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
