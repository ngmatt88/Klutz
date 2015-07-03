package com.duckwarlocks.klutz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duckwarlocks.klutz.Exceptions.StopProcessingException;
import com.duckwarlocks.klutz.constants.CommonConstants;
import com.duckwarlocks.klutz.utilities.AlertDialogHelper;
import com.duckwarlocks.klutz.utilities.FileHelper;
import com.duckwarlocks.klutz.utilities.GpsCoordinatesHelper;
import com.duckwarlocks.klutz.vo.LocationVO;

import org.w3c.dom.Text;

import java.io.File;


public class MainActivity extends Activity {
    private GpsCoordinatesHelper gps;
    private String mCityName;
    private double mLatitude;
    private double mLongitude;
    private TextView mCurCoordinateTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCurCoordinateTxtView = (TextView)findViewById(R.id.currentCoordinates);

        try {
            //create the directory if it doesn't exist.
            FileHelper.createDir();
        } catch (StopProcessingException e) {
            e.printStackTrace();
            //TODO should plan out some sort of fail-safe
        }
    }

    public void sendToSavedLocations(View view){
        Intent intent = new Intent(this,SavedLocationsActivity.class);
        startActivity(intent);
    }


    /**
     * Grabs the current GPS coordinates and saves them to external file.
     * @param view
     */
    public void getCoordinates(View view){
        gps = new GpsCoordinatesHelper(this);

        if(gps.ismCanGetLocation()){
            mLatitude = gps.getmLatitude();
            mLongitude = gps.getmLongitude();
            mCityName = gps.getmCityName();

//            promptCoordinateName(MainActivity.this);
            displayCurrentCoordinates();
        }else{
            gps.showSettingsAlert();
        }
    }

    /**
     * Displays Coordinates in the sub-title textview
     */
    private void displayCurrentCoordinates(){
        mCurCoordinateTxtView.setText(CommonConstants.LATITUDE_ABBREV + " : " + mLatitude + " " + CommonConstants.LONGITUDE_ABBREV + " : " + mLongitude);
    }



    /**
     *
     * @param locationVO
     */
    private void saveToFile(LocationVO locationVO){
        if(locationVO.getmName().equals("")){
            Toast.makeText(
                    getApplicationContext(),"Invalid Name",Toast.LENGTH_LONG).show();
        }else{
            try{
                FileHelper.writeNewRecordToFile(locationVO);
                Toast.makeText(
                        getApplicationContext(),"Coordinates Saved",Toast.LENGTH_LONG).show();
            }catch (StopProcessingException e){
                 e.printStackTrace();
            }
        }
    }

    private LocationVO createLocationVO(String nameTitle){
        LocationVO locationVO = new LocationVO();
        locationVO.setmName(nameTitle);
        locationVO.setmLatitude(mLatitude);
        locationVO.setmLongitude(mLongitude);
        locationVO.setmCity(mCityName);

        return locationVO;
    }

    /**
     * Prompt for the name to be given to these coordinates
     * @param view
     * @return
     */
    public void promptCoordinateName(View view){
        String defaultStr = mCurCoordinateTxtView.getText().toString();
        String subTitleStr = getResources().getString(R.string.subTitle);
        if(defaultStr.equals(subTitleStr)){
            AlertDialogHelper.buildWarningAlert(this,
                    "Current Location Not Found",
                    "Please Get Current Location Then Try Again",false,"OK").show();
        }else{
            String alertTitle = getResources().getString(R.string.coordinateNameTitle);
            String msg = getResources().getString(R.string.coordinateNameMsg);

            AlertDialog.Builder theAlert = AlertDialogHelper.buildAlert(this,alertTitle,msg,false);

            final EditText input = new EditText(MainActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            input.setLayoutParams(layoutParams);
            theAlert.setView(input);

            theAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String title = input.getText().toString();

                    if (title != null && !title.equals("")) {

                        saveToFile(createLocationVO(title));

                        Toast.makeText(getApplicationContext(), "Your location is - Latitude : " + mLatitude +
                                "and Longitude : " + mLongitude, Toast.LENGTH_LONG).show();
                    }
                }
            });

            theAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            theAlert.show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
