package net.whend.soodal.whend.view;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
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
import android.widget.ImageView.ScaleType;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.Grid_Search_Adapter;
import net.whend.soodal.whend.model.base.HashTag;
import net.whend.soodal.whend.model.top.Grid_Search_Schedule;
import net.whend.soodal.whend.util.quitview.QuiltView;

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
public class F2_Search extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private FragmentTabHost mTabHost;
    ArrayList<Grid_Search_Schedule> arrayGSchedule = new ArrayList<Grid_Search_Schedule>();
    private Grid_Search_Adapter grid_search_adapter;


    private TextView mainactivity_title;
    private ImageView search_btn, back_btn;
    private GridView search_gridview;
    private EditText search_text;
    private LinearLayout search_grid;
    private FrameLayout search_linear;
    private FrameLayout root_layout;
    private Toolbar toolbar;

    public QuiltView quiltView;


    private View rootView;

        public F2_Search() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HashTag tag1, tag2, tag3, tag4;

        tag1 = new HashTag(0,"가","",0,"가");
        tag2 = new HashTag(0,"나","",0,"가");
        tag3 = new HashTag(0,"다","",0,"가");
        tag4 = new HashTag(0,"라","",0,"가");

        arrayGSchedule.add(new Grid_Search_Schedule(tag1));
        arrayGSchedule.add(new Grid_Search_Schedule(tag2));
        arrayGSchedule.add(new Grid_Search_Schedule(tag3));
        arrayGSchedule.add(new Grid_Search_Schedule(tag4));


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

        quiltView = (QuiltView) rootView.findViewById(R.id.quilt);
        quiltView.setChildPadding(5);


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
/*
        Fragment temp = new F5_Mypage();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.search_linear, temp).commit();
        */

        Grid_Search_Adapter mgrid_search_adapter = new Grid_Search_Adapter(getActivity(), R.layout.item_gridsearch_schedule, arrayGSchedule);

        for(int i=0; i< mgrid_search_adapter.getCount(); i++)
            quiltView.addPatchView(mgrid_search_adapter.getView(i,null,null));

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
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("back_btn_gone", back_btn.getVisibility() == View.GONE);
        super.onSaveInstanceState(outState);
    }

    public void addTestQuilts(int num){
        ArrayList<ImageView> images = new ArrayList<ImageView>();
        for(int i = 0; i < num; i++){
            ImageView image = new ImageView(this.getActivity());
            image.setScaleType(ScaleType.CENTER_CROP);
            if(i % 2 == 0)
                image.setImageResource(R.drawable.mayer);
            else
                image.setImageResource(R.drawable.mayer1);
            images.add(image);
        }
        quiltView.addPatchImages(images);
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
