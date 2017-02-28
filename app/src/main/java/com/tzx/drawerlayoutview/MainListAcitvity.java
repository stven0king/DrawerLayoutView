package com.tzx.drawerlayoutview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by tanzhenxing
 * Date: 2017/1/9.
 * Description:
 */

public class MainListAcitvity extends Activity{
    private ListView listView;
    private String[] mPlanetTitles = {
            "原生抽屉滑动",
            "旧版QQ列表滑动",
            "新版QQ列表滑动"};

    private String[] classNames = {
            "com.tzx.drawerlayoutview.DrawerLayoutViewActivity",
            "com.tzx.drawerlayoutview.DrawerViewActivity",
            "com.tzx.drawerlayoutview.DrawerScrollViewActivity"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_main_activity);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mPlanetTitles));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String className = classNames[i];
                    Class cls = Class.forName(className);
                    startActivity(new Intent(MainListAcitvity.this, cls));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
