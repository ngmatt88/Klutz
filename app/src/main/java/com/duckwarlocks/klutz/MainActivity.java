package com.duckwarlocks.klutz;


import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.duckwarlocks.klutz.constants.CommonConstants;
import com.duckwarlocks.klutz.fragments.NavigationDrawerFragment;
import com.duckwarlocks.klutz.fragments.MainFragment;
import com.duckwarlocks.klutz.receivers.ResponseReceiver;


public class MainActivity extends ActionBarActivity {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private TextView txt;
    static Fragment mCurrentFragment = new MainFragment();
    private  ResponseReceiver receiver;
    private IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);


        if(savedInstanceState!=null){
            txt = MainFragment.mCurCoordinateTxtView;
            mCurrentFragment = getSupportFragmentManager().getFragment(savedInstanceState,"mCurrentFragment");
        }
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.myDrawerLayout));

        filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        state.putString(MainFragment.STATE_LAT, String.valueOf(MainFragment.mLatitude));
        state.putString(MainFragment.STATE_LONG, String.valueOf(MainFragment.mLongitude));
        getSupportFragmentManager().putFragment(state, "mCurrentFragment", mCurrentFragment);
    }

    @Override
    public void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

            txt = (TextView) findViewById(R.id.currentCoordinates);
            txt = MainFragment.mCurCoordinateTxtView;
            String lat = state.getString(MainFragment.STATE_LAT);
            String longitud = state.getString(MainFragment.STATE_LONG);
            txt.setText(CommonConstants.LATITUDE_ABBREV + " : " + lat + " " + CommonConstants.LONGITUDE_ABBREV + " : " + longitud);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mNavigationDrawerFragment.mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mNavigationDrawerFragment.mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    @Override
    public void onStart(){
        super.onStart();
        registerReceiver(receiver, filter);
        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
        fragTransaction.replace(R.id.emptyFrameForFragment, mCurrentFragment);
        fragTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public static void  setmCurrentFragment(Fragment newFrag){
        mCurrentFragment = newFrag;
    }
}
