package com.jxd.oa.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.adapter.JxdFragmentPagerAdapter;
import com.jxd.oa.view.TabNavigationView;
import com.yftools.ViewUtil;
import com.yftools.view.annotation.ViewInject;

/**
 * *****************************************
 * Description ：通讯录
 * Created by cy on 2014/8/4.
 * *****************************************
 */
public class ContactsActivity extends AbstractActivity implements TabNavigationView.NavigationClick {

    @ViewInject(R.id.tabNavigationView)
    private TabNavigationView tabNavigationView;
    @ViewInject(R.id.mViewPager)
    private ViewPager mViewPager;
    private JxdFragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ViewUtil.inject(this);
        initTopBar();
        initTabBar();
        initPagerAdapter();
    }

    private void initTopBar() {
        ActionBar actionBar = getSupportActionBar();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.menu_contacts));
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ActionBar.OnNavigationListener callback = new MyOnNavigationListener();
        actionBar.setListNavigationCallbacks(adapter, callback);
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
                tabNavigationView.setSelectedPosition(position);
            }
        });
        mViewPager.setCurrentItem(0);
    }

    private void initTabBar() {
        tabNavigationView.initView(getResources().getStringArray(R.array.menu_tab_contacts));
        tabNavigationView.setNavigationClick(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNavigationClick(int position) {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position);
        }
    }

    private class MyOnNavigationListener implements ActionBar.OnNavigationListener {
        @Override
        public boolean onNavigationItemSelected(int itemPosition, long itemId) {
            switch (itemPosition) {
                case 0:
                    break;
                case 1:
                    break;
            }
            return true;
        }
    }
}
