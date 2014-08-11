package com.jxd.oa.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jxd.oa.R;

/**
 * *****************************************
 * Description ：导航页签
 * Created by cy on 2014/8/6.
 * *****************************************
 */
public class TabNavigationView extends LinearLayout {

    private NavigationClick navigationClick;
    private int selectedIndex;

    public TabNavigationView(Context context) {
        super(context);
        this.setOrientation(HORIZONTAL);
        this.setBackgroundColor(getResources().getColor(R.color.color_blue_pressed));
    }

    public TabNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(HORIZONTAL);
        this.setBackgroundColor(getResources().getColor(R.color.color_blue_pressed));
    }

    public void initView(String[] menus) {
        if (menus == null) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = 0, len = menus.length; i < len; i++) {
            String menu = menus[i];
            View convertView = inflater.inflate(R.layout.item_navigation, null);
            convertView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            TextView navigationTxt_tv = (TextView) convertView.findViewById(R.id.navigationTxt_tv);
            final View selected_line = convertView.findViewById(R.id.selected_line);
            if (i != 0) {
                selected_line.setVisibility(GONE);
            }
            navigationTxt_tv.setText(menu);
            this.addView(convertView);
            if (i < len - 1) {
                View line = new View(getContext());
                LayoutParams params = new LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT);
                params.setMargins(0, 15, 0, 15);
                line.setLayoutParams(params);
                line.setBackgroundColor(getResources().getColor(R.color.color_line));
                this.addView(line);
            }
            final int finalI = i;
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    getChildAt(selectedIndex * 2).findViewById(R.id.selected_line).setVisibility(GONE);
                    getChildAt(finalI * 2).findViewById(R.id.selected_line).setVisibility(VISIBLE);
                    if (navigationClick != null) {
                        navigationClick.onNavigationClick(finalI);
                    }
                    selectedIndex = finalI;
                }
            });
        }
        invalidate();
    }

    public void setNavigationClick(NavigationClick navigationClick) {
        this.navigationClick = navigationClick;
    }

    public void setSelectedPosition(int position) {
        getChildAt(selectedIndex * 2).findViewById(R.id.selected_line).setVisibility(GONE);
        getChildAt(position * 2).findViewById(R.id.selected_line).setVisibility(VISIBLE);
        selectedIndex = position;
    }

    public interface NavigationClick {
        public void onNavigationClick(int position);
    }
}
