package com.duckwarlocks.klutz.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duckwarlocks.klutz.R;
import com.duckwarlocks.klutz.constants.CommonConstants;
import com.duckwarlocks.klutz.daos.LocationsDAO;
import com.duckwarlocks.klutz.utilities.AlertDialogHelper;
import com.duckwarlocks.klutz.utilities.GpsCoordinatesHelper;
import com.duckwarlocks.klutz.views.PrettyButtonView;
import com.duckwarlocks.klutz.vo.LocationVO;
import com.easyandroidanimations.library.BounceAnimation;

import java.sql.SQLException;


/**
 */
public class MainFragment extends Fragment implements View.OnClickListener{
    private GpsCoordinatesHelper gps;
    private String mCityName;
    private double mLatitude;
    private double mLongitude;
    private View mRootView;
    private TextView mCurCoordinateTxtView;
    private LocationsDAO mLocationDAO;
    private Context mContext;

    //==============Update the buttons you want added below======================
    private String[] BUTTON_NAMES = {"Grab Coordinates","Save Location"};
    private int[] BUTTON_RES = {R.id.grabCoordinatesBtn,R.id.saveCoordinatesBtn};
    private int[] BUTTON_IMAGES= {R.drawable.pineapple,R.drawable.car_keys_icon};
    //============================================================================


    @Override
    public View onCreateView(LayoutInflater inflater,
                                ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRootView = inflater.inflate(R.layout.fragment_main, container, false);
        mContext = mRootView.getContext().getApplicationContext();

        mCurCoordinateTxtView = (TextView)mRootView.findViewById(R.id.currentCoordinates);

        mLocationDAO = new LocationsDAO(mContext);

        setUpButtons();

        return mRootView;
    }

    /**
     * Sets up buttons to be used in the fragment.  Adds animation, onclicklistener, speech bubble
     * text, and image.
     */
    private void setUpButtons(){
        for(int i = 0 ; i < BUTTON_RES.length;i++){
            PrettyButtonView prettyBtn = (PrettyButtonView)mRootView.findViewById(BUTTON_RES[i]);
            prettyBtn.setSpeechTxt(BUTTON_NAMES[i]);
            prettyBtn.setmBtnImage(BUTTON_IMAGES[i]);
            prettyBtn.setOnClickListener(this);
            new BounceAnimation(prettyBtn).setBounceDistance(25).setNumOfBounces(3).setDuration(1000).animate();
        }
    }

    /**
     * MUST Add the appropriate method/action to be called when adding a new button to the screen
     * @param v
     */
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
    public void promptCoordinateName(final View view){
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
            input.setTextColor(Color.BLACK);
            theAlert.setView(input);

            theAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String title = input.getText().toString();

                    if (title != null && !title.equals("")) {

                        saveToDB(createLocationVO(title));

                        Toast.makeText(mContext, "Your location is - Latitude : " + mLatitude +
                                "and Longitude : " + mLongitude, Toast.LENGTH_LONG).show();
                    }
                }
            });

            theAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(input.getWindowToken(), 0);

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
