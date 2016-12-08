package org.debatkusir.rutenia;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import org.debatkusir.rutenia.Fragment.AngkotFragment;
import org.debatkusir.rutenia.Fragment.RouteFragment;

import java.util.ArrayList;

/**
 * Created by Reyhan on 12/5/2016.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private Fragment currentFragment;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragments.add(new RouteFragment());
        this.fragments.add(new AngkotFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}
