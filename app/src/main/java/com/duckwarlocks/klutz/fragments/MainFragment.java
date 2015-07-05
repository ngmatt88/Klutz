package com.duckwarlocks.klutz.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duckwarlocks.klutz.Exceptions.StopProcessingException;
import com.duckwarlocks.klutz.MainActivity;
import com.duckwarlocks.klutz.R;
import com.duckwarlocks.klutz.constants.CommonConstants;
import com.duckwarlocks.klutz.daos.LocationsDAO;
import com.duckwarlocks.klutz.utilities.AlertDialogHelper;
import com.duckwarlocks.klutz.utilities.FileHelper;
import com.duckwarlocks.klutz.utilities.GpsCoordinatesHelper;
import com.duckwarlocks.klutz.vo.LocationVO;

import java.sql.SQLException;


/**
 */
public class MainFragment extends Fragment implements View.OnClickListener{
    private GpsCoordinatesHelper gps;
    private String mCityName;
    private double mLatitude;
    private double mLongitude;
    private View mRootView;
    private Button mGetCoordinates;
    private Button mSaveCoordinates;
    private TextView mCurCoordinateTxtView;
    private LocationsDAO mLocationDAO;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater,
                                ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRootView = inflater.inflate(R.layout.fragment_main,container,false);
        mContext = mRootView.getContext().getApplicationContext();

        mGetCoordinates = (Button)mRootView.findViewById(R.id.grabCoordinatesBtn);
        mSaveCoordinates = (Button)mRootView.findViewById(R.id.saveCoordinatesBtn);
        mCurCoordinateTxtView = (TextView)mRootView.findViewById(R.id.currentCoordinates);

        mGetCoordinates.setOnClickListener(this);
        mSaveCoordinates.setOnClickListener(this);

        mLocationDAO = new LocationsDAO(mContext);
//        return inflater.inflate(R.layout.fragment_main, container, false);
        return mRootView;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.grabCoordinatesBtn: getCoordinates(v);
                break;
            case R.id.saveCoordinatesBtn: promptCoordinateName(v);
                break;

        }
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
            AlertDialogHelper.buildWarningAlert(this.getActivity(),
                    "Current Location Not Found",
                    "Please Get Current Location Then Try Again",false,"OK").show();
        }else{
            String alertTitle = getResources().getString(R.string.coordinateNameTitle);
            String msg = getResources().getString(R.string.coordinateNameMsg);

            AlertDialog.Builder theAlert = AlertDialogHelper.buildAlert(this.getActivity(),alertTitle,msg,false);

            final EditText input = new EditText(mContext);
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

//                        saveToFile(createLocationVO(title));
                        saveToDB(createLocationVO(title));

                        Toast.makeText(mContext, "Your location is - Latitude : " + mLatitude +
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

    private LocationVO createLocationVO(String nameTitle){
        LocationVO locationVO = new LocationVO();
        locationVO.setmName(nameTitle);
        locationVO.setmLatitude(mLatitude);
        locationVO.setmLongitude(mLongitude);
        locationVO.setmCity(mCityName);

        return locationVO;
    }

    private void saveToDB(LocationVO location){
        try{
            mLocationDAO.open();

            mLocationDAO.createLocationVO(
                    location.getmName(), Double.toString(location.getmLatitude()),
                    Double.toString(location.getmLongitude()), location.getmCity());
            mLocationDAO.close();
        }catch(SQLException e){
            e.printStackTrace();
            System.exit(1);
        }
        Toast saveToast = Toast.makeText(mContext, "Your Searches Have Been Saved", Toast.LENGTH_LONG);
        saveToast.setGravity(Gravity.CENTER, 0, 0);
        saveToast.show();
    }

    /**
     * Grabs the current GPS coordinates and saves them to external file.
     * @param view
     */
    public void getCoordinates(View view){
        gps = new GpsCoordinatesHelper(view.getContext().getApplicationContext());

        if(gps.ismCanGetLocation()){
            mLatitude = gps.getmLatitude();
            mLongitude = gps.getmLongitude();
            mCityName = gps.getmCityName();

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
}
