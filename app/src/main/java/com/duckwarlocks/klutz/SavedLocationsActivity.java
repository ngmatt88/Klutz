package com.duckwarlocks.klutz;

import android.app.Activity;
import android.os.Environment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.duckwarlocks.klutz.Exceptions.StopProcessingException;
import com.duckwarlocks.klutz.constants.CommonConstants;
import com.duckwarlocks.klutz.utilities.LocationAdapter;
import com.duckwarlocks.klutz.utilities.XmlHelper;
import com.duckwarlocks.klutz.vo.LocationVO;

import java.util.ArrayList;


public class SavedLocationsActivity extends Activity {
    private ListView mLocationView;
    private EditText mInputSearch;
    private ArrayList<LocationVO> mLocationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_locations);

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

        final LocationAdapter adapter = new LocationAdapter(this, mLocationList);

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
}
