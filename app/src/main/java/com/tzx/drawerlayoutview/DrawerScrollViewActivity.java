package com.tzx.drawerlayoutview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by tanzhenxing
 * Date: 2017/1/10.
 * Description:
 */

public class DrawerScrollViewActivity extends BaseFragmentActivity {
    private ListView listView;
    private String[] titles = {
            "我的好友1",
            "我的好友1",
            "我的好友1",
            "我的好友1",
            "我的好友1",
            "我的好友1",
            "我的好友1",
            "我的好友1",
            "我的好友1",
            "我的好友1",
            "我的好友1",
            "我的好友1",
            "我的好友1",
            "我的好友1",
            "我的好友1",
            "我的好友1"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_scroll_view_activity);
        setTitle("仿QQ消息列表滑动");
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, titles));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(DrawerScrollViewActivity.this, titles[position], Toast.LENGTH_SHORT).show();
            }
        });
    }
}
