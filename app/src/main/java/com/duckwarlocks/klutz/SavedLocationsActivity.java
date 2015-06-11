package com.duckwarlocks.klutz;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
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
import android.widget.Toast;

import com.duckwarlocks.klutz.Exceptions.StopProcessingException;
import com.duckwarlocks.klutz.constants.CommonConstants;
import com.duckwarlocks.klutz.utilities.FileHelper;
import com.duckwarlocks.klutz.utilities.GpsCoordinatesHelper;
import com.duckwarlocks.klutz.utilities.LocationAdapter;
import com.duckwarlocks.klutz.utilities.XmlHelper;
import com.duckwarlocks.klutz.vo.LocationVO;

import java.util.ArrayList;


public class SavedLocationsActivity extends Activity {
    private ListView mLocationView;
    private EditText mInputSearch;
    private LocationAdapter adapter;
    private ArrayList<LocationVO> mLocationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_locations);

        adapter = new LocationAdapter(getApplicationContext());

        StringBuilder fileLoc =
                new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath());
        fileLoc.append("/"+CommonConstants.KLUTZ.toLowerCase()+"/");
        fileLoc.append(CommonConstants.FILE_LOCATION);

        try{
           mLocationList =  XmlHelper.parseListOfLocations(fileLoc.toString());
        }catch(StopProcessingException e){
            Log.e(SavedLocationsActivity.class.getName(),e.toString());
            //TODO force it to crash?
        }


        adapter.setLocationVOList(mLocationList);

        mLocationView = (ListView) findViewById(R.id.recordList);
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

        mLocationView.setAdapter(adapter);

        //Creates long-press context-menu for the saved records
        ListView mLocationItem = (ListView)findViewById(R.id.recordList);
        registerForContextMenu(mLocationItem);
        //set onclick listener
        mLocationItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //current lat, current long, target lat, target lang
                LocationVO currentLoc = getLocation();

                String lat = ((TextView)view.findViewById(R.id.locationListLatitude)).getText().toString();
                String lon = ((TextView)view.findViewById(R.id.locationListLongitude)).getText().toString();

                lat = lat.replaceAll(CommonConstants.LATITUDE_ABBREV+":","");
                lon = lon.replaceAll(CommonConstants.LONGITUDE_ABBREV+":","");

                String url =
                        "http://maps.google.com/maps?saddr="+currentLoc.getmLatitude()+","+currentLoc.getmLongitude()+
                                "&daddr="+lat+","+lon;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        }) ;
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.setHeaderTitle("Context Menu");
        menu.add(0, v.getId(), 0, CommonConstants.DELETE_CONTEXT_MENU);
//        menu.add(0, v.getId(), 0, "Action 2");
//        menu.add(0, v.getId(), 0, "Action 3");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        if (item.getTitle() == CommonConstants.DELETE_CONTEXT_MENU) {
            //TODO write your own removal method then it should work!!!
//            adapter.remove(adapter.getItem(info.position));
            adapter.removeItem(adapter.getItem(info.position));
            adapter.notifyDataSetChanged();
        }
//        else if (item.getTitle() == "Action 2") {
//            Toast.makeText(this, "Action 2 invoked", Toast.LENGTH_SHORT).show();
//        } else if (item.getTitle() == "Action 3") {
//            Toast.makeText(this, "Action 3 invoked", Toast.LENGTH_SHORT).show();
//        } else {
//            return false;
//        }
        return true;
    }

}
