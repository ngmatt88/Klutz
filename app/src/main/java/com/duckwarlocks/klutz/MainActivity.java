package com.duckwarlocks.klutz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.duckwarlocks.klutz.Exceptions.StopProcessingException;
import com.duckwarlocks.klutz.utilities.FileHelper;
import com.duckwarlocks.klutz.utilities.GpsCoordinatesHelper;


public class MainActivity extends ActionBarActivity {
    GpsCoordinatesHelper gps;
    String nameTitle;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * Grabs the current GPS coordinates and saves them to external file.
     * @param view
     */
    public void getCoordinates(View view){
        gps = new GpsCoordinatesHelper(this);

        if(gps.isCanGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

//            String nameTitle = promptCoordinateName(MainActivity.this);
            promptCoordinateName(MainActivity.this);

        }else{
            gps.showSettingsAlert();
        }
    }

    private void saveToFile(String name,double latitude, double longitude){
        if(name.equals("")){
            Toast.makeText(
                    getApplicationContext(),"Invalid Name",Toast.LENGTH_LONG).show();
        }else{
//            StringBuilder sb = new StringBuilder();
//            sb.append(name);
//            sb.append(latitude);
//            sb.append(longitude);
            try{
                FileHelper.writeToFile(MainActivity.this,name,latitude,longitude);
                Toast.makeText(
                        getApplicationContext(),"Coordinates Saved",Toast.LENGTH_LONG).show();
            }catch (StopProcessingException e){
                 e.printStackTrace();
            }
        }
    }

    /**
     * Prompt for the name to be given to these coordinates
     * @param context
     * @return
     */
    private void promptCoordinateName(Context context){
        final StringBuilder nameTitle = new StringBuilder("");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.coordinateNameTitle);
        alertDialog.setMessage(R.string.coordinateNameMsg);

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        input.setLayoutParams(layoutParams);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = input.getText().toString();

                if (title != null && !title.equals("")) {
                    nameTitle.append(title);

                    saveToFile(nameTitle.toString(),latitude,longitude);

                    Toast.makeText(getApplicationContext(),"Your location is : Latitude - " + latitude +
                            "and Longitude - " + longitude,Toast.LENGTH_LONG).show();
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
//        return nameTitle.toString();
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
