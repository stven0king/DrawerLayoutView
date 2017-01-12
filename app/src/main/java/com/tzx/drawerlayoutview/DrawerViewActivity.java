package com.tzx.drawerlayoutview;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by tanzhenxing
 * Date: 2017/1/9.
 * Description:
 */

public class DrawerViewActivity extends BaseFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_view_activity);
        setTitle("放旧版QQ类似抽屉滑动效果");
    }
}
