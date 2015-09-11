package com.duckwarlocks.klutz.services;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.os.IBinder;
import android.view.View;

import com.duckwarlocks.klutz.R;
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
public class MainService extends Service {

    public static final String PARAM_IN_MSG = "inmsg";
    public static final String PARAM_OUT_MSG = "outmsg";
    public static final String OUT_LAT = "outlat";
    public static final String OUT_LON = "outlon";
    public static final String OUT_CITY = "outcity";
    public boolean isRunning = false;

//    public MainService() {
//        super("MainService");
//    }

    @Override
    public void onCreate(){
        isRunning = true;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId){
        if (intent != null) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
                    final String action = intent.getAction();
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
                        sendBroadcast(broadcastIntent);
                    } else {
                        gps.showSettingsAlert();
                    }
//                }
//            }).start();
            stopSelf();
        }
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0){
        return null;
    }

    @Override
    public void onDestroy(){
        isRunning =false;
    }

//    @Override
//    protected void onHandleIntent(Intent intent) {
//        if (intent != null) {
//            final String action = intent.getAction();
//            GpsCoordinatesHelper gps = new GpsCoordinatesHelper(getApplicationContext());
//
//            if (gps.ismCanGetLocation()) {
//                Double mLatitude = gps.getmLatitude();
//                Double mLongitude = gps.getmLongitude();
//                String mCityName = gps.getmCityName();
//
//
//                MainFragment.mLatitude = mLatitude;
//                MainFragment.mLongitude = mLongitude;
//                MainFragment.mCityName = mCityName;
//
//                Intent broadcastIntent = new Intent();
//                broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
//                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
//                broadcastIntent.putExtra(OUT_LAT, Double.toString(mLatitude));
//                broadcastIntent.putExtra(OUT_LON, Double.toString(mLongitude));
//                broadcastIntent.putExtra(OUT_CITY, mCityName);
//                sendBroadcast(broadcastIntent);
//            } else {
//                gps.showSettingsAlert();
//            }
//        }
//    }

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
