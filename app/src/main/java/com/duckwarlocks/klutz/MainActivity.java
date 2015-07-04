package com.duckwarlocks.klutz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.duckwarlocks.klutz.Exceptions.StopProcessingException;
import com.duckwarlocks.klutz.adapter.DrawerAdapter;
import com.duckwarlocks.klutz.utilities.FileHelper;
import com.duckwarlocks.klutz.utilities.GpsCoordinatesHelper;
import com.duckwarlocks.klutz.vo.LocationVO;


public class MainActivity extends ActionBarActivity {

    private GpsCoordinatesHelper gps;
    private String mCityName;
    private double mLatitude;
    private double mLongitude;
    private Toolbar toolbar;

    String TITLES[] = {"Favorites","Recents"};
    int ICONS[] = {R.drawable.ic_fav24dp,R.drawable.ic_star24dp};

    String NAME = "User";
    String EMAIL = "email";
    int PROFILE = R.drawable.user;
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new DrawerAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE, this);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }



        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State


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

            promptCoordinateName(MainActivity.this);

        }else{
            gps.showSettingsAlert();
        }
    }

    /**
     *
     * @param locationVO
     */
    private void saveToFile(LocationVO locationVO){
        if(locationVO.getmName().equals("")){
            Toast.makeText(getApplicationContext(),"Invalid Name",Toast.LENGTH_LONG).show();
        }else{
            try{
                FileHelper.writeNewRecordToFile(locationVO);
                Toast.makeText(getApplicationContext(),"Coordinates Saved",Toast.LENGTH_LONG).show();
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
     * @param context
     * @return
     */
    private void promptCoordinateName(Context context){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.coordinateNameTitle);
        alertDialog.setMessage(R.string.coordinateNameMsg);

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(layoutParams);
        alertDialog.setView(input);

        //"SAVE" done in here because NOT Synchronous
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = input.getText().toString();

                if (title != null && !title.equals("")) {

                    saveToFile(createLocationVO(title));

                    Toast.makeText(getApplicationContext(), "Your location is - Latitude : " + mLatitude + "and Longitude : " + mLongitude, Toast.LENGTH_LONG).show();
                }
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
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
        if (id == R.id.action_example) {
            Toast.makeText(this, "Agregame Google+.", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_settings) {
                Toast.makeText(this, "Example settings.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
