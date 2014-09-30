package com.jxd.oa.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.adapter.JxdFragmentPagerAdapter;
import com.jxd.oa.bean.Cloud;
import com.jxd.oa.fragment.CloudDownloadFragment;
import com.jxd.oa.fragment.CloudFragment;
import com.jxd.oa.fragment.base.AbstractFragment;
import com.yftools.ViewUtil;
import com.yftools.ui.DatePickUtil;
import com.yftools.view.annotation.ContentView;
import com.yftools.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * *****************************************
 * Description ：企业云
 * Created by cy on 2014/8/6.
 * *****************************************
 */
@ContentView(R.layout.activity_view_pager)
public class CloudActivity extends AbstractActivity {

    @ViewInject(R.id.mViewPager)
    private ViewPager mViewPager;
    private JxdFragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.inject(this);
        initTabBar();
        initPagerAdapter();
    }

    private void initPagerAdapter() {
        List<AbstractFragment> fragmentList = new ArrayList<AbstractFragment>();
        fragmentList.add(new CloudFragment());
        fragmentList.add(new CloudDownloadFragment());
        pagerAdapter = new JxdFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
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
        getSupportActionBar().setTitle(getString(R.string.txt_cloud));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sync, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sync:
                new DatePickUtil(mContext, "请选择开始时间", new DatePickUtil.DateSetFinished() {
                    @Override
                    public void onDateSetFinished(String pickYear, String pickMonth, String pickDay) {
                        syncData(Cloud.class, pickYear + "-" + pickMonth + "-" + pickDay);
                    }
                }).showDateDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setPageIndex(int index) {
        mViewPager.setCurrentItem(index);
    }

    @Override
    protected void refreshData() {
        super.refreshData();
    }
}
