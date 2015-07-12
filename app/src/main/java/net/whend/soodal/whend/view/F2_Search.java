package net.whend.soodal.whend.view;


import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.whend.soodal.whend.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.


>>>>>>> origin/JB:app/src/main/java/net/whend/soodal/whend/view/Fragment2.java
 * create an instance of this fragment.
 */
public class F2_Search extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private FragmentTabHost mTabHost;
    TextView mainactivity_title;
    ImageView search_btn;
    EditText search_text;
    GridLayout search_grid;
    LinearLayout search_linear;
    FrameLayout root_layout;
    Toolbar toolbar;

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


        rootView = inflater.inflate(R.layout.f2_search_layout, container, false);

        search_grid = (GridLayout) rootView.findViewById(R.id.search_grid);
        search_linear = (LinearLayout) rootView.findViewById(R.id.search_linear);
        root_layout = (FrameLayout) rootView.findViewById(R.id.search_rootlayout);

        search_grid.setVisibility(View.VISIBLE);
        search_linear.setVisibility(View.INVISIBLE);


        // 툴바 커스터마이징

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mainactivity_title = (TextView) getActivity().findViewById(R.id.mainactivity_title);
        mainactivity_title.setText("");

        search_btn = (ImageView) getActivity().findViewById(R.id.search_btn);
        search_text = (EditText) getActivity().findViewById(R.id.search_text);

        search_btn.setVisibility(View.VISIBLE);
        search_text.setVisibility(View.VISIBLE);

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

        search_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                search_grid.setVisibility(View.INVISIBLE);
                search_linear.setVisibility(View.VISIBLE);
                toolbar.setNavigationIcon(R.drawable.cancel);

                if (hasFocus) {

                } else {
                    search_grid.setVisibility(View.VISIBLE);
                    search_linear.setVisibility(View.INVISIBLE);


                }
            }
        });

        root_layout.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    search_grid.setVisibility(View.VISIBLE);
                    search_linear.setVisibility(View.INVISIBLE);
                    ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    return true;
                }
                return false;
            }
        });



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
