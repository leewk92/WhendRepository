package net.whend.soodal.whend.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import net.whend.soodal.whend.R;
import net.whend.soodal.whend.view.F1_Wall;
import net.whend.soodal.whend.view.F2_Search;
import net.whend.soodal.whend.view.F3_Upload;
import net.whend.soodal.whend.view.F4_Notify;
import net.whend.soodal.whend.view.F5_Mypage;


public class BaseContainerFragment extends Fragment {

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        Log.d("nullcheck",fragment==null?"null":"notnull");
        transaction.replace(R.id.container_framelayout, fragment);
        transaction.commit();
        getChildFragmentManager().executePendingTransactions();
    }

    public void replaceFragmentByIndex(int fragmentIndex, boolean addToBackStack) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();


        Fragment fragment = null;

       switch (fragmentIndex){
           case 0: fragment = new F1_Wall(); break;
           case 1: fragment = new F2_Search(); break;
           case 2: fragment = new F3_Upload(); break;
           case 3: fragment = new F4_Notify(); break;
           case 4: fragment = new F5_Mypage(); break;
       }

        transaction.replace(R.id.container_framelayout, fragment);
        Log.d("addto",""+addToBackStack);
        if (addToBackStack) {

            transaction.addToBackStack(null);
        }
        transaction.commit();
        getChildFragmentManager().executePendingTransactions();
    }

    public boolean popFragment() {
        Log.e("test", "pop fragment: " + getChildFragmentManager().getBackStackEntryCount());
        boolean isPop = false;
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            isPop = true;
            getChildFragmentManager().popBackStack();
        }
        return isPop;
    }

}