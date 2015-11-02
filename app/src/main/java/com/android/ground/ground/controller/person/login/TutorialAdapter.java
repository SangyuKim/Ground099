package com.android.ground.ground.controller.person.login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by dongja94 on 2015-10-08.
 */
public class TutorialAdapter extends FragmentStatePagerAdapter {
    public TutorialAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return SubTutorialFragment.newInstance("title : " + position, "");
    }

    @Override
    public float getPageWidth(int position) {
        return 1;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
