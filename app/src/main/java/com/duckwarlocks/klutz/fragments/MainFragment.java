package com.duckwarlocks.klutz.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.duckwarlocks.klutz.services.MainIntentService;
import com.duckwarlocks.klutz.utilities.AlertDialogHelper;
import com.duckwarlocks.klutz.utilities.GpsCoordinatesHelper;
import com.duckwarlocks.klutz.views.PrettyButtonView;
import com.duckwarlocks.klutz.vo.LocationVO;

import java.io.IOException;
import java.io.IOException;
import java.sql.SQLException;


/**
 */
public class MainFragment extends Fragment implements TextureView.SurfaceTextureListener, View.OnClickListener{

    private GpsCoordinatesHelper gps;
    public static  String mCityName;
    public static double mLatitude;
    public static double mLongitude;
    private View mRootView;
    public static TextView mCurCoordinateTxtView;
    private LocationsDAO mLocationDAO;
    private Context mContext;
    public static final String STATE_LAT = "current_lat";
    public static final String STATE_LONG = "current_long";

    private MediaPlayer mMediaPlayer;
    private TextureView mTextureView;
    private final String FILE_NAME = "video_bg3.mp4";
    //==============Update the buttons you want added below======================
//    private String[] BUTTON_NAMES = {"Grab Coordinates","Save Location"};
    private int[] BUTTON_RES = {R.id.grabCoordinatesBtn,R.id.saveCoordinatesBtn};
//    private int[] BUTTON_IMAGES= {R.drawable.pineapple,R.drawable.car_keys_icon};
    //============================================================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRootView = inflater.inflate(R.layout.fragment_main, container, false);
        mContext = mRootView.getContext().getApplicationContext();

        mCurCoordinateTxtView = (TextView) mRootView.findViewById(R.id.currentCoordinates);

        mTextureView = (TextureView) mRootView.findViewById(R.id.surface);
        mTextureView.setSurfaceTextureListener(this);

        mLocationDAO = new LocationsDAO(mContext);

        setUpButtons();

        return mRootView;
    }
    @Override
    public void onViewStateRestored (Bundle savedInstanceState){
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            String lat = savedInstanceState.getString(STATE_LAT);
            String longitud = savedInstanceState.getString(STATE_LONG);
            mCurCoordinateTxtView.setText(CommonConstants.LATITUDE_ABBREV + " : " + lat + " " + CommonConstants.LONGITUDE_ABBREV + " : " + longitud);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            String lat = savedInstanceState.getString(STATE_LAT);
            String longitud = savedInstanceState.getString(STATE_LONG);
            mCurCoordinateTxtView.setText(CommonConstants.LATITUDE_ABBREV + " : " + lat + " " + CommonConstants.LONGITUDE_ABBREV + " : " + longitud);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_LAT, String.valueOf(mLatitude));
        outState.putString(STATE_LONG, String.valueOf(mLongitude));
    }
    /**
     * Sets up buttons to be used in the fragment.  Adds animation, onclicklistener, speech bubble
     * text, and image.
     */
    private void setUpButtons(){
        for(int i = 0 ; i < BUTTON_RES.length;i++){
            Button prettyBtn = (Button)mRootView.findViewById(BUTTON_RES[i]);
            prettyBtn.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Bold.ttf"));
            prettyBtn.setOnClickListener(this);
        }
    }

    /**
     * MUST Add the appropriate method/action to be called when adding a new button to the screen
     * @param v
     */
    @Override
    public void onClick(View v){
        Button theBtn = (Button)v;
        theBtn.setEnabled(false);
        switch (v.getId()){
            case R.id.grabCoordinatesBtn:
                getCoordinates(v);
                getActivity().findViewById(R.id.step1Set).setVisibility(View.INVISIBLE);
                (getActivity().findViewById(R.id.step2Set)).setVisibility(View.VISIBLE);
                break;
            case R.id.saveCoordinatesBtn:
                promptCoordinateName(v);
                getActivity().findViewById(R.id.step2Set).setVisibility(View.INVISIBLE);
                (getActivity().findViewById(R.id.step1Set)).setVisibility(View.VISIBLE);
                break;
        }
        theBtn.setEnabled(true);
    }

    private void setUpImageAnimations(int viewId, int imageId){
        try{
            ImageView theImg = ((PrettyButtonView)getActivity().findViewById(viewId)).getmBtnImage();
            Animation animation;
            if(theImg != null){
                switch (imageId){
                    case R.drawable.pineapple:
                        animation = AnimationUtils.loadAnimation(
                                getActivity().getApplicationContext(), R.anim.spin);
                        theImg.startAnimation(animation);
                        break;
                    case R.drawable.car_keys_icon:
                        break;
                    default:
                        break;
                }
            }
        }catch(NullPointerException e){

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

                        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
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
        Intent getCoordinatesIntent = new Intent(getActivity(), MainIntentService.class);
        getCoordinatesIntent.putExtra(MainIntentService.PARAM_IN_MSG,"");
        getActivity().startService(getCoordinatesIntent);
//        gps = new GpsCoordinatesHelper(view.getContext().getApplicationContext());
//
//        if(gps.ismCanGetLocation()){
//            mLatitude = gps.getmLatitude();
//            mLongitude = gps.getmLongitude();
//            mCityName = gps.getmCityName();
//
//            displayCurrentCoordinates();
//        }else{
//            gps.showSettingsAlert();
//        }
    }

    /**
     * Displays Coordinates in the sub-title textview
     */
    private void displayCurrentCoordinates(){
        mCurCoordinateTxtView.setText(CommonConstants.LATITUDE_ABBREV + " : " + mLatitude + " " + CommonConstants.LONGITUDE_ABBREV + " : " + mLongitude);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

        Surface s = new Surface(surface);

        try {
            AssetFileDescriptor descriptor = getResources().getAssets().openFd(FILE_NAME);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            mMediaPlayer.setSurface(s);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setVolume(0, 0);

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
