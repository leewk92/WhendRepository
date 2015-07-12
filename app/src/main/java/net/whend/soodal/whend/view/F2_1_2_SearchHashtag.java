package net.whend.soodal.whend.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.form.SearchHashTag_Adapter;
import net.whend.soodal.whend.form.SearchSchedule_Adapter;
import net.whend.soodal.whend.model.top.Concise_Schedule;
import net.whend.soodal.whend.model.top.Search_HashTag;

import java.util.ArrayList;

/**
 * Created by wonkyung on 2015-07-12.
 */
public class F2_1_2_SearchHashtag extends Fragment {

    private View rootview;
    private ListView listview;
    private ArrayList<Search_HashTag> SHashtag_list = new ArrayList<Search_HashTag>();
    private SearchHashTag_Adapter searchHashTag_adapter;

    public F2_1_2_SearchHashtag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Search_HashTag a = new Search_HashTag();
        SHashtag_list.add(a);
        SHashtag_list.add(a);
        SHashtag_list.add(a);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // View 할당
        rootview = inflater.inflate(R.layout.f2_1_2_searchhashtag_layout, container, false);
        listview = (ListView)rootview.findViewById(R.id.listview_searchhashtag);

        listview.setAdapter(new SearchHashTag_Adapter(getActivity(), R.layout.item_searchhashtag, SHashtag_list));
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

}
