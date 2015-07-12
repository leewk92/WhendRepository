package net.whend.soodal.whend.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.Concise_Schedule_Adapter;
import net.whend.soodal.whend.model.top.Concise_Schedule;

import java.util.ArrayList;

// ToolBar 숨기기 Reference : https://mzgreen.github.io/2015/02/15/How-to-hideshow-Toolbar-when-list-is-scroling%28part1%29/
public class F1_Wall extends Fragment {


    private FragmentTabHost mTabHost;
    private View rootview;
    private ListView listview;
    private ArrayList<Concise_Schedule> arrayCSchedule = new ArrayList<Concise_Schedule>();
    private Concise_Schedule_Adapter concise_schedule_adapter;

    private TextView mainactivity_title;
    ImageView search_btn, back_btn;
    EditText search_text;

    public F1_Wall() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Concise_Schedule a = new Concise_Schedule();
        arrayCSchedule.add(a);
        arrayCSchedule.add(a);
        arrayCSchedule.add(a);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 로고 사이즈 조정 및 로고 삽입

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.whend_actionbar);
        Toolbar toolbar = (Toolbar) container.findViewById(R.id.toolbar);

        int actionBarHeight=0;

        TypedValue tv = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }

        int height = (int) (actionBarHeight * 0.8);


        int width = (bmp.getWidth()*height/bmp.getHeight());


        Bitmap resizedbmp = Bitmap.createScaledBitmap(bmp, width, height, true);
        Drawable logo = new BitmapDrawable(getResources(), resizedbmp);

        // 사이즈변경


        ((MainActivity)getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(true); // 로고사용
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false); // 타이틀미사용
        ((MainActivity)getActivity()).getSupportActionBar().setLogo(logo); //  로고박기

        mainactivity_title = (TextView) getActivity().findViewById(R.id.mainactivity_title);
        mainactivity_title.setText("");

        search_btn = (ImageView) getActivity().findViewById(R.id.search_btn);
        search_text = (EditText) getActivity().findViewById(R.id.search_text);
        back_btn = (ImageView) getActivity().findViewById(R.id.back_btn);

        back_btn.setVisibility(View.GONE);

        search_btn.setVisibility(View.INVISIBLE);
        search_text.setVisibility(View.INVISIBLE);

        // View 할당
        rootview = inflater.inflate(R.layout.f1_wall_layout, container, false);
        listview = (ListView)rootview.findViewById(R.id.listview_concise_schedule);

        listview.setAdapter(new Concise_Schedule_Adapter(getActivity(), R.layout.item_concise_schedule, arrayCSchedule));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(getActivity(), A3_SpecificScheduleActivity.class);
                startActivity(intent);
            }
        });
        return rootview;
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
