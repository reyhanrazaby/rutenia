package org.debatkusir.rutenia;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.debatkusir.rutenia.Fragment.AngkotFragment;
import org.debatkusir.rutenia.Fragment.RouteFragment;

/**
 * Created by Reyhan on 12/5/2016.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int numberOfTabs;

    public PagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new RouteFragment();
            case 1:
                return new AngkotFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
