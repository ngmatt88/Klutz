package com.duckwarlocks.klutz;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.duckwarlocks.klutz.Exceptions.StopProcessingException;
import com.duckwarlocks.klutz.constants.CommonConstants;
import com.duckwarlocks.klutz.daos.LocationsDAO;
import com.duckwarlocks.klutz.utilities.GpsCoordinatesHelper;
import com.duckwarlocks.klutz.adapters.LocationAdapter;
import com.duckwarlocks.klutz.utilities.XmlHelper;
import com.duckwarlocks.klutz.vo.LocationVO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SavedLocationsActivity extends Activity {
    private RecyclerView mLocationView;
    private EditText mInputSearch;
    private LocationAdapter adapter;
    private List<LocationVO> mLocationList;
    private LocationsDAO mLocationDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_locations);
        mLocationView = (RecyclerView) findViewById(R.id.recordList);
        registerForContextMenu(mLocationView);


        mLocationView.setLayoutManager(new LinearLayoutManager(this));
        mLocationView.setItemAnimator(new DefaultItemAnimator());

        adapter = new LocationAdapter(getApplicationContext(),R.layout.list_piece);

        mLocationDAO = new LocationsDAO(this);

        try{
            mLocationDAO.open();

            mLocationList = mLocationDAO.getAllVideos();

            mLocationDAO.close();
        }catch (SQLException e){
            //TODO do something later
            Log.e(getClass().getName(),e.toString());
            e.printStackTrace();
        }

        adapter.setLocationVOList(mLocationList);

        mLocationView.setAdapter(adapter);

        mInputSearch = (EditText)findViewById(R.id.inputSearch);
        //Add search functionality to editText
        mInputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.updateList();
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        //Creates long-press context-menu for the saved records
        RecyclerView mLocationItem = (RecyclerView)findViewById(R.id.recordList);
        registerForContextMenu(mLocationItem);
        //set onclick listener
//        mLocationItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            //current lat, current long, target lat, target lang
//                LocationVO currentLoc = getLocation();
//
//                String lat = ((TextView)view.findViewById(R.id.locationListLatitude)).getText().toString();
//                String lon = ((TextView)view.findViewById(R.id.locationListLongitude)).getText().toString();
//
//                lat = lat.replaceAll(CommonConstants.LATITUDE_ABBREV+":","");
//                lon = lon.replaceAll(CommonConstants.LONGITUDE_ABBREV+":","");
//
//                String url =
//                        "http://maps.google.com/maps?saddr="+currentLoc.getmLatitude()+","+currentLoc.getmLongitude()+
//                                "&daddr="+lat+","+lon;
//                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
//                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//                startActivity(intent);
//            }
//        }) ;
    }

    /**
     * Return a locationVO with the data on the current location.
     * To be used for onCLick to map to saved location
     * @return
     */
    private LocationVO getLocation(){
        GpsCoordinatesHelper gps = new GpsCoordinatesHelper(this);
        gps.getmLocation();
        LocationVO currentLoc = new LocationVO();

        currentLoc.setmLatitude(gps.getmLatitude());
        currentLoc.setmLongitude(gps.getmLongitude());
        currentLoc.setmCity(gps.getmCityName());
        currentLoc.setmName("Current Location");

        return currentLoc;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_saved_locations, menu);
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

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v,
//                                    ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.add(0, v.getId(), 0, CommonConstants.DELETE_CONTEXT_MENU);
//    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
//        if (item.getTitle() == CommonConstants.DELETE_CONTEXT_MENU) {
//            adapter.removeItem(adapter.getItem(info.position));
//        }
//        return true;
//    }

}
