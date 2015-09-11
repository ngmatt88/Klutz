package com.duckwarlocks.klutz;


import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;

import com.duckwarlocks.klutz.fragments.MainFragment;
import com.duckwarlocks.klutz.receivers.ResponseReceiver;
import com.hartsolution.bedrock.AbstractBaseActivity;


public class MainActivity extends AbstractBaseActivity {

    private MainFragment mainf = new MainFragment();
    private  ResponseReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null){
//            txt = mainf.mCurCoordinateTxtView;
        }

        receiver = new ResponseReceiver();
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        state.putString(mainf.STATE_LAT, String.valueOf(mainf.mLatitude));
        state.putString(mainf.STATE_LONG, String.valueOf(mainf.mLongitude));
    }

    @Override
    public void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        if (state != null) {
//            txt = (TextView) findViewById(R.id.currentCoordinates);
//            txt = mainf.mCurCoordinateTxtView;
            String lat = state.getString(mainf.STATE_LAT);
            String longitud = state.getString(mainf.STATE_LONG);
//            txt.setText(CommonConstants.LATITUDE_ABBREV + " : " + lat + " " + CommonConstants.LONGITUDE_ABBREV + " : " + longitud);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onStart(){
        super.onStart();
        KlutzApplication.MAIN_SCREEN.execute(this,null);

        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onStop(){
        super.onStop();
        unregisterReceiver(receiver);
    }

    //required for bedrock
    protected ContentViewIds getContentViewId(){
        ContentViewIds contentViewIds = new ContentViewIds();
        return contentViewIds;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
