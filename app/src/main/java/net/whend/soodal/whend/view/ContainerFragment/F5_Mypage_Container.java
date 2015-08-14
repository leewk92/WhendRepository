package net.whend.soodal.whend.view.ContainerFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.whend.soodal.whend.R;
import net.whend.soodal.whend.util.BaseContainerFragment;
import net.whend.soodal.whend.view.F4_Notify;
import net.whend.soodal.whend.view.F5_Mypage;

/**
 * Created by Administrator on 2015-08-13.
 */
public class F5_Mypage_Container extends BaseContainerFragment{

    private boolean mIsViewInited;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("test", "tab 5 oncreateview");
        return inflater.inflate(R.layout.container_fragment, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("test", "tab 5 container on activity created");
        if (!mIsViewInited) {
            mIsViewInited = true;
            initView();
        }
    }

    private void initView() {
        Log.e("test", "tab 5 init view");
        replaceFragment(new F5_Mypage(), true);
    }
}
