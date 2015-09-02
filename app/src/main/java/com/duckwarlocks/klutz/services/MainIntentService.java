package com.duckwarlocks.klutz.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;

import com.duckwarlocks.klutz.constants.CommonConstants;
import com.duckwarlocks.klutz.fragments.MainFragment;
import com.duckwarlocks.klutz.receivers.ResponseReceiver;
import com.duckwarlocks.klutz.utilities.GpsCoordinatesHelper;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MainIntentService extends IntentService {

    public static final String PARAM_IN_MSG = "inmsg";
    public static final String PARAM_IN_TRIP = "intrip";
    public static final String PARAM_OUT_MSG = "outmsg";
    public static final String OUT_LAT = "outlat";
    public static final String OUT_LON = "outlon";
    public static final String OUT_CITY = "outcity";
    public static  final String OUT_TRIP = "outtrip";

    public MainIntentService() {
        super("MainIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            Bundle extras = intent.getExtras();
            Boolean trip  = extras.getBoolean(MainIntentService.PARAM_IN_TRIP);

            GpsCoordinatesHelper gps = new GpsCoordinatesHelper(getApplicationContext());
            if (gps.ismCanGetLocation()) {
                Double mLatitude = gps.getmLatitude();
                Double mLongitude = gps.getmLongitude();
                String mCityName = gps.getmCityName();

                MainFragment.mLatitude = mLatitude;
                MainFragment.mLongitude = mLongitude;
                MainFragment.mCityName = mCityName;

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                broadcastIntent.putExtra(OUT_LAT, Double.toString(mLatitude));
                broadcastIntent.putExtra(OUT_LON, Double.toString(mLongitude));
                broadcastIntent.putExtra(OUT_CITY, mCityName);
                broadcastIntent.putExtra(OUT_TRIP,trip);

                sendBroadcast(broadcastIntent);
            } else {
                gps.showSettingsAlert();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
