package com.duckwarlocks.klutz.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.duckwarlocks.klutz.constants.CommonConstants;
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
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.duckwarlocks.klutz.services.action.FOO";
    private static final String ACTION_BAZ = "com.duckwarlocks.klutz.services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.duckwarlocks.klutz.services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.duckwarlocks.klutz.services.extra.PARAM2";

    public static final String PARAM_IN_MSG = "inmsg";
    public static final String PARAM_OUT_MSG = "outmsg";
    public static final String OUT_LAT = "outlat";
    public static final String OUT_LON = "outlon";
    public static final String OUT_CITY = "outcity";

    public MainIntentService() {
        super("MainIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            GpsCoordinatesHelper gps = new GpsCoordinatesHelper(getApplicationContext());

            if (gps.ismCanGetLocation()) {
                String mLatitude = Double.toString(gps.getmLatitude());
                String mLongitude = Double.toString(gps.getmLongitude());
                String mCityName = gps.getmCityName();

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                broadcastIntent.putExtra(OUT_LAT, mLatitude);
                broadcastIntent.putExtra(OUT_LON, mLongitude);
                broadcastIntent.putExtra(OUT_CITY, mCityName);
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
