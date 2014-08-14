package com.jxd.oa.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.jxd.oa.fragment.base.AbstractFragment;

import java.util.List;

/**
 * *****************************************
 * Description ： FragmentStatePagerAdapter为了解决缓存问题。。。。
 * Created by cy on 2014/8/4.
 * *****************************************
 */
public class JxdFragmentPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager fm;
    private List<AbstractFragment> mFragments;

    public JxdFragmentPagerAdapter(FragmentManager fm, List<AbstractFragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public int getCount() {
        return mFragments != null ? mFragments.size() : 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments != null ? mFragments.get(position) : null;
    }

}

