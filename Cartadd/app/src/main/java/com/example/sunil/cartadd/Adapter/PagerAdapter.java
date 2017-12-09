package com.example.sunil.cartadd.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.sunil.cartadd.Fragment.FragElectronics;
import com.example.sunil.cartadd.Fragment.FragGrocery;
import com.example.sunil.cartadd.Fragment.FragSports;

/**
 * Created by Sunil on 12/7/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm,int NumberofTabs) {
        super(fm);
        this.mNoOfTabs=NumberofTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                FragElectronics tab1 = new FragElectronics();
                return tab1;
            case 1:
                FragGrocery tab2 = new FragGrocery();
                return tab2;
            case 2:
                FragSports tab3 = new FragSports();
                return tab3;
            default:
                return null;
        }
    }

        @Override
        public int getCount() {
            return mNoOfTabs;
        }

}
