package com.tzx.drawerlayoutview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by tanzhenxing
 * Date: 2017/1/6.
 * Description:
 */

public class BaseFragment extends Fragment {
    private Color color;
    private View containView;
    private ViewGroup backGroundView;
    private TextView textView;
    private String[] mPlanetTitles = {"周一", "周二", "周三", "周四"};
    private int[] colors = {Color.RED, Color.GREEN, Color.GREEN, Color.YELLOW};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        containView = inflater.inflate(R.layout.fragment_layout, container, false);
        backGroundView = (ViewGroup) containView.findViewById(R.id.fargment);
        textView = (TextView) containView.findViewById(R.id.textview);
        return containView;
    }

    public void setIndex(int index) {
        containView.setBackgroundColor(colors[index]);
        textView.setText(mPlanetTitles[index]);
    }
}
