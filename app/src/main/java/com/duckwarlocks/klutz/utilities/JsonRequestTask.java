package com.duckwarlocks.klutz.utilities;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.duckwarlocks.klutz.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ngmat_000 on 6/16/2015.
 */
public class JsonRequestTask extends AsyncTask<String,String,String>{
    StringBuilder jsonResults = new StringBuilder();
    GoogleMap mMap;
    Context mContext;
    Activity mActivity;
    public JsonRequestTask(Context context,Activity activity,GoogleMap map, String url){
        jsonResults.append(url);
        this.mMap = map;
        this.mContext = context;
        this.mActivity = activity;
    }

    @Override
    protected void onPreExecute(){}

    @Override
    protected String doInBackground(String... urls){
        try{
            URL rqURL = new URL(jsonResults.toString());
            HttpURLConnection conn = (HttpURLConnection) rqURL.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            if(conn!=null){
                conn.disconnect();
            }
            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        }catch(MalformedURLException me){
            //TODO DO SOMETHING
        }catch(IOException io){
            //TODO DO SOMETHING
        }

        return jsonResults.toString();
    }

    @Override
    protected void onPostExecute(String response){
        String latitude;
        String longitude;
        // Create a JSON object hierarchy from the results
        try{
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONObject resultObj = jsonObj.getJSONObject("result");
            latitude= resultObj.getJSONObject("geometry").getJSONObject("location").getString("lat");
            Log.e(JsonRequestTask.class.getName(), "THE LAT/LON IS: LAT : " + resultObj.getJSONObject("geometry").getJSONObject("location"));
            longitude = resultObj.getJSONObject("geometry").getJSONObject("location").getString("lng");
            Toast.makeText(mContext,"The Lat is : " +latitude + " while the Lng is : " + longitude,Toast.LENGTH_LONG);
            final LatLng destLoc = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));

//            GoogleMap map = ((MapFragment)getFragmentManager().findFragmentById(R.id.mapFragment)).getMap();
            if(mMap!=null){
               mActivity.runOnUiThread(new Runnable() {
                   public void run() {
                       Marker mapMarker =
                               mMap.addMarker(new MarkerOptions().position(destLoc).title("IS THIS THE PLACE?".toString()));
                       //move camera to where you are
                       mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destLoc, 15));
                   }
               });
                // Zoom in, animating the camera.
//                mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
            }
        }catch(JSONException je){
            //TODO DO SOMETHING
        }
    }
}
