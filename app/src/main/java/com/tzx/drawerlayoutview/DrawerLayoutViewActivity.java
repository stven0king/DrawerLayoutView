package com.tzx.drawerlayoutview;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DrawerLayoutViewActivity extends FragmentActivity {
    private DrawerLayout drawerLayout;
    private ListView listView;
    private String[] mPlanetTitles = {"周一", "周二", "周三", "周四"};
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ActionBar actionBar;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private BaseFragment fragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_view_layout_activity);
        setTitle("原生抽屉");
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(false);
        //actionBar.setHomeButtonEnabled(true);
        //actionBar.setHomeAsUpIndicator(R.drawable.back);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mDrawerTitle = mTitle = getTitle();
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.back,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                actionBar.setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {

            }
        };
        drawerLayout.setDrawerListener(mDrawerToggle);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mPlanetTitles));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object str = adapterView.getItemAtPosition(i);
                if (str instanceof String) {
                    mDrawerTitle = (CharSequence) str;
                    actionBar.setTitle(mDrawerTitle);
                    changedContainerView(i);
                }
            }
        });
        fragment = new BaseFragment();
        fragmentTransaction.replace(R.id.fargmentview, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                //    drawerLayout.closeDrawer(GravityCompat.START);
                //} else {
                //    drawerLayout.openDrawer(GravityCompat.START);
                //}
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changedContainerView(int index) {
        fragment.setIndex(index);
    }
}
