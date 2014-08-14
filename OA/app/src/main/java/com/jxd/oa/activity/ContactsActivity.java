package com.jxd.oa.activity;

import android.content.Intent;
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
import com.jxd.oa.fragment.PrivateContactsFragment;
import com.jxd.oa.fragment.PublicContactsFragment;
import com.jxd.oa.fragment.base.AbstractFragment;
import com.jxd.oa.view.TabNavigationView;
import com.yftools.ViewUtil;
import com.yftools.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

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
        List<AbstractFragment> fragmentList = new ArrayList<AbstractFragment>();
        fragmentList.add(new PublicContactsFragment());
        fragmentList.add(new PrivateContactsFragment());
        pagerAdapter = new JxdFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int arg0) {
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageSelected(int position) {
                tabNavigationView.setSelectedPosition(position);
                supportInvalidateOptionsMenu();
            }
        });
    }

    private void initTabBar() {
        tabNavigationView.initView(getResources().getStringArray(R.array.menu_tab_contacts));
        tabNavigationView.setNavigationClick(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sync_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(mContext, ContactsAddActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
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
            AbstractFragment currentFragment = (AbstractFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.mViewPager + ":" + mViewPager.getCurrentItem());
            switch (itemPosition) {
                case 0:
                    if (mViewPager.getCurrentItem() == 0) {
                        ((PublicContactsFragment) currentFragment).fillList();
                    } else {
                        ((PrivateContactsFragment) currentFragment).fillList();
                    }
                    break;
                case 1:
                    if (mViewPager.getCurrentItem() == 0) {
                        ((PublicContactsFragment) currentFragment).fillExpandableList();
                    } else {
                        ((PrivateContactsFragment) currentFragment).fillExpandableList();
                    }
                    break;
            }
            return true;
        }
    }
}
