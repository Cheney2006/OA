package com.jxd.oa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.adapter.JxdFragmentPagerAdapter;
import com.jxd.oa.fragment.PrivateContactFragment;
import com.jxd.oa.fragment.PublicContactFragment;
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
public class ContactActivity extends AbstractActivity implements TabNavigationView.NavigationClick {

    @ViewInject(R.id.tabNavigationView)
    private TabNavigationView tabNavigationView;
    @ViewInject(R.id.mViewPager)
    private ViewPager mViewPager;
    private JxdFragmentPagerAdapter pagerAdapter;
    private Status status = Status.PUBLIC_CONTACT;

    private enum Status {
        PUBLIC_CONTACT, PRIVATE_CONTACT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ViewUtil.inject(this);
        initTopBar();
        initTabBar();
        initPagerAdapter();
    }

    private void initTopBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.txt_cloud));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.menu_contact));
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ActionBar.OnNavigationListener callback = new MyOnNavigationListener();
        actionBar.setListNavigationCallbacks(adapter, callback);
    }

    private void initPagerAdapter() {
        List<AbstractFragment> fragmentList = new ArrayList<AbstractFragment>();
        fragmentList.add(new PublicContactFragment());
        fragmentList.add(new PrivateContactFragment());
        pagerAdapter = new JxdFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int arg0) {
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageSelected(int position) {
                tabNavigationView.setSelectedPosition(position);
                if (position == 0) {
                    status = Status.PUBLIC_CONTACT;
                } else {
                    status = Status.PRIVATE_CONTACT;
                }
                supportInvalidateOptionsMenu();
                refreshFragment(getSupportActionBar().getSelectedNavigationIndex());
            }
        });
    }

    private void initTabBar() {
        tabNavigationView.initView(getResources().getStringArray(R.array.menu_tab_contact));
        tabNavigationView.setNavigationClick(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(status==Status.PRIVATE_CONTACT){
            getMenuInflater().inflate(R.menu.menu_add, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(mContext, ContactAddActivity.class));
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
            refreshFragment(itemPosition);
            return true;
        }
    }

    private void refreshFragment(int itemPosition) {
        AbstractFragment currentFragment = (AbstractFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.mViewPager + ":" + mViewPager.getCurrentItem());
        switch (itemPosition) {
            case 0:
                if (mViewPager.getCurrentItem() == 0) {
                    ((PublicContactFragment) currentFragment).fillList();
                } else {
                    ((PrivateContactFragment) currentFragment).fillList();
                }
                break;
            case 1:
                if (mViewPager.getCurrentItem() == 0) {
                    ((PublicContactFragment) currentFragment).fillExpandableList();
                } else {
                    ((PrivateContactFragment) currentFragment).fillExpandableList();
                }
                break;
        }
    }
}
