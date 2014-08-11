package com.jxd.oa.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.adapter.JxdFragmentPagerAdapter;
import com.yftools.ViewUtil;
import com.yftools.view.annotation.ViewInject;

/**
 * *****************************************
 * Description ：企业云
 * Created by cy on 2014/8/6.
 * *****************************************
 */
public class CloudActivity extends AbstractActivity {

    @ViewInject(R.id.mViewPager)
    private ViewPager mViewPager;
    private JxdFragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        ViewUtil.inject(this);
        initTabBar();
        initPagerAdapter();
    }

    private void initPagerAdapter() {
        pagerAdapter = new JxdFragmentPagerAdapter(getSupportFragmentManager(), null);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int arg0) {
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageSelected(int position) {
                getSupportActionBar().selectTab(getSupportActionBar().getTabAt(position));
            }
        });
        mViewPager.setCurrentItem(0);
    }

    private void initTabBar() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for (String s : getResources().getStringArray(R.array.menu_tab_cloud)) {
            ActionBar.Tab tab = getSupportActionBar().newTab().setText(s).setTabListener(new MyTabListener());
            getSupportActionBar().addTab(tab);
        }
    }


    private class MyTabListener implements ActionBar.TabListener {

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mViewPager != null) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }
    }

}
