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
import net.whend.soodal.whend.form.SearchUser_Adapter;
import net.whend.soodal.whend.model.top.Search_User;

import java.util.ArrayList;

/**
 * Created by wonkyung on 2015-07-12.
 */
public class F2_1_3_SearchUser extends Fragment {

    private View rootview;
    private ListView listview;
    private ArrayList<Search_User> SUser_list = new ArrayList<Search_User>();
    private SearchUser_Adapter searchUser_adapter;

    public F2_1_3_SearchUser() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Search_User a = new Search_User();
        SUser_list.add(a);
        SUser_list.add(a);
        SUser_list.add(a);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // View 할당
        rootview = inflater.inflate(R.layout.f2_1_3_searchuser_layout, container, false);
        listview = (ListView)rootview.findViewById(R.id.listview_searchuser);

        listview.setAdapter(new SearchUser_Adapter(getActivity(), R.layout.item_searchuser, SUser_list));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(getActivity(), A2_UserProfileActivity.class);
                startActivity(intent);
            }
        });
        return rootview;
    }

}
