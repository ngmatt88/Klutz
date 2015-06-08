package com.duckwarlocks.klutz;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.duckwarlocks.klutz.Exceptions.StopProcessingException;
import com.duckwarlocks.klutz.constants.CommonConstants;
import com.duckwarlocks.klutz.utilities.XmlHelper;
import com.duckwarlocks.klutz.vo.LocationVO;

import java.util.ArrayList;


public class SavedLocationsActivity extends ActionBarActivity {
    private ListView locationView;
    private ArrayAdapter<String> adapter;
    private EditText inputSearch;
    private ArrayList<LocationVO> locationList;//TODO should be changed to acceptvo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_locations);

        XmlHelper xmlHelper = new XmlHelper();
        StringBuilder fileLoc =
                new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath());
        fileLoc.append("/"+CommonConstants.KLUTZ.toLowerCase()+"/");
        fileLoc.append(CommonConstants.FILE_LOCATION);

        try{
           locationList =  xmlHelper.parseListOfLocations(fileLoc.toString());
        }catch(StopProcessingException e){
            Log.e(SavedLocationsActivity.class.getName(),e.toString());
            //TODO force it to crash?
        }
        locationView = (ListView) findViewById(R.id.recordList);
        inputSearch = (EditText)findViewById(R.id.inputSearch);

        //Add search functionality to editText
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // When user changed the Text
                SavedLocationsActivity.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //TODO try to abstract this part
        String[] locationArray = new String[locationList.size()];
        for(int i = 0; i<locationList.size();i++){
            locationArray[i] = locationList.get(i).getName();
            //TODO expand on the info and show coordinates/city?
        }

        //add items to list now
        adapter = new ArrayAdapter<String>
                (this,R.layout.list_piece,R.id.locationListItem,locationArray);
        locationView.setAdapter(adapter);
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
