package net.whend.soodal.whend.view;


import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.Grid_Search_Adapter;
import net.whend.soodal.whend.model.top.Grid_Search_Schedule;

import java.util.ArrayList;


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
    ArrayList<Grid_Search_Schedule> arrayGSchedule = new ArrayList<Grid_Search_Schedule>();
    private Grid_Search_Adapter grid_search_adapter;


    private TextView mainactivity_title;
    private ImageView search_btn, back_btn;
    private GridView search_gridview;
    private EditText search_text;
    private LinearLayout search_grid, search_linear;
    private FrameLayout root_layout;
    private Toolbar toolbar;

    private View rootView;

        public F2_Search() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Grid_Search_Schedule a = new Grid_Search_Schedule();
        arrayGSchedule.add(a);
        arrayGSchedule.add(a);
        arrayGSchedule.add(a);
        arrayGSchedule.add(a);
        arrayGSchedule.add(a);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(false);


        rootView = inflater.inflate(R.layout.f2_search_layout, container, false);

        search_grid = (LinearLayout) rootView.findViewById(R.id.search_grid);
        search_linear = (LinearLayout) rootView.findViewById(R.id.search_linear);
        root_layout = (FrameLayout) rootView.findViewById(R.id.search_rootlayout);
        search_gridview = (GridView) rootView.findViewById(R.id.search_gridview);


        // 툴바 커스터마이징

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mainactivity_title = (TextView) getActivity().findViewById(R.id.mainactivity_title);
        mainactivity_title.setText("");

        search_btn = (ImageView) getActivity().findViewById(R.id.search_btn);
        search_text = (EditText) getActivity().findViewById(R.id.search_text);
        back_btn = (ImageView) getActivity().findViewById(R.id.back_btn);


        search_btn.setVisibility(View.VISIBLE);
        search_text.setVisibility(View.VISIBLE);



        // 기본 frame layout 설정
        search_grid.setVisibility(View.VISIBLE);
        search_linear.setVisibility(View.INVISIBLE);


        //GridLayout 설정



        search_gridview.setAdapter(new Grid_Search_Adapter(getActivity(), R.layout.item_gridsearch_schedule, arrayGSchedule));


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

                search_text.clearFocus();

                search_grid.setVisibility(View.VISIBLE);
                search_linear.setVisibility(View.INVISIBLE);
                back_btn.setVisibility(View.GONE);

                //hide keyboard
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });


        return rootView;
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
