package com.duckwarlocks.klutz.holders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duckwarlocks.klutz.R;
import com.duckwarlocks.klutz.adapters.LocationAdapter;
import com.duckwarlocks.klutz.constants.CommonConstants;
import com.duckwarlocks.klutz.utilities.AlertDialogHelper;
import com.duckwarlocks.klutz.utilities.GpsCoordinatesHelper;
import com.duckwarlocks.klutz.vo.LocationVO;

import org.w3c.dom.Text;

/**
 * Created by ngmat_000 on 6/8/2015.
 */
public class LocationViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener,ItemTouchHelperViewHolder{
    public TextView name;
    public TextView latitude;
    public TextView longitude;
    public TextView cityName;
    public Context context;
    public CardView individualItem;


    public LocationViewHolder(View view){
        super(view);
        this.context = view.getContext().getApplicationContext();

        name = (TextView) view.findViewById(R.id.locationListItem);
        latitude = (TextView) view.findViewById(R.id.locationListLatitude);
        longitude = (TextView) view.findViewById(R.id.locationListLongitude);
        cityName = (TextView) view.findViewById(R.id.locationListCityName);
        individualItem = (CardView)view.findViewById(R.id.pieceView);

        view.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(){
        itemView.setBackgroundColor(Color.LTGRAY);
    }

    @Override
    public void onItemClear(){
        itemView.setBackgroundColor(0);
    }

    @Override
    public void onClick(View v) {
        LocationVO currentLoc = getLocation(v);

        String lat = ((TextView) v.findViewById(R.id.locationListLatitude)).getText().toString();
        String lon = ((TextView) v.findViewById(R.id.locationListLongitude)).getText().toString();

        lat = lat.replaceAll(CommonConstants.LATITUDE_ABBREV + ":", "");
        lon = lon.replaceAll(CommonConstants.LONGITUDE_ABBREV + ":", "");

        String url =
                "http://maps.google.com/maps?saddr=" + currentLoc.getmLatitude() + "," + currentLoc.getmLongitude() +
                        "&daddr=" + lat + "," + lon;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        v.getContext().startActivity(intent);
    }

    /**
     * Return a locationVO with the data on the current location.
     * To be used for onCLick to map to saved location
     * @return
     */
    private LocationVO getLocation(View view){
        GpsCoordinatesHelper gps = new GpsCoordinatesHelper(view.getContext());
        gps.getmLocation();
        LocationVO currentLoc = new LocationVO();

        currentLoc.setmLatitude(gps.getmLatitude());
        currentLoc.setmLongitude(gps.getmLongitude());
        currentLoc.setmCity(gps.getmCityName());
        currentLoc.setmName("Current Location");

        return currentLoc;
    }
}
