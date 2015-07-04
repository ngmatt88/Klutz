package com.duckwarlocks.klutz.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.duckwarlocks.klutz.R;
import com.duckwarlocks.klutz.constants.CommonConstants;
import com.duckwarlocks.klutz.daos.LocationsDAO;
import com.duckwarlocks.klutz.utilities.GpsCoordinatesHelper;
import com.duckwarlocks.klutz.vo.LocationVO;
import com.duckwarlocks.klutz.holders.LocationViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ngmat_000 on 6/8/2015.
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationViewHolder> implements Filterable{

    private LayoutInflater mInflater;
    private static List<LocationVO> mLocationList = new ArrayList<LocationVO>();;
    private static List<LocationVO> mOriginalList;
    private int theLayout;
    private static LocationsDAO locationDAO;
    public ContextMenu.ContextMenuInfo info;

    public LocationAdapter(Context context,int layoutId){
        mInflater = LayoutInflater.from(context);
        theLayout = layoutId;
        locationDAO = new LocationsDAO(context);
    }

    public LocationAdapter(){}

    public void setLocationVOList(List<LocationVO> mLocationList){
        this.mLocationList.addAll(mLocationList);
        this.mOriginalList = mLocationList;
    }


    public LocationVO getItem(int position){
        return mLocationList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    /**
     * Updated the filtered mLocationList with the original list
     * Used to make the onTextChange search bar work for backspaces
     */
    public void updateList(){
        mLocationList = mOriginalList;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(theLayout,viewGroup,false);

        return new LocationViewHolder(view);
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


    @Override
    public void onBindViewHolder(LocationViewHolder viewHolder, int i){
        viewHolder.name.setText(mLocationList.get(i).getmName());
        viewHolder.latitude.setText(Double.toString(mLocationList.get(i).getmLatitude()));
        viewHolder.longitude.setText(Double.toString(mLocationList.get(i).getmLongitude()));
        viewHolder.cityName.setText(mLocationList.get(i).getmCity());
    }


    @Override
    public int getItemCount(){
        return mLocationList == null? 0 : mLocationList.size();
    }
//    @Override
//    public View getView(int position, View convertView,ViewGroup parent){
//        LocationViewHolder holder = null;
//        if(convertView == null){
//            holder = new LocationViewHolder();
//            convertView = mInflater.inflate(R.layout.list_piece,null);
//            holder.name = (TextView) convertView.findViewById(R.id.locationListItem);
//            holder.latitude = (TextView) convertView.findViewById(R.id.locationListLatitude);
//            holder.longitude = (TextView) convertView.findViewById(R.id.locationListLongitude);
//            holder.cityName = (TextView) convertView.findViewById(R.id.locationListCityName);
//            holder.viewItem=(RelativeLayout)convertView.findViewById(R.id.pieceView);
//            convertView.setTag(holder);
//        }else{
//            holder = (LocationViewHolder) convertView.getTag();
//        }
//
//        holder.name.setText(mLocationList.get(position).getmName());
//        holder.latitude.setText(CommonConstants.LATITUDE_ABBREV + ":" + Double.toString(mLocationList.get(position).getmLatitude()));
//        holder.longitude.setText(CommonConstants.LONGITUDE_ABBREV + ":" + Double.toString(mLocationList.get(position).getmLongitude()));
//        holder.cityName.setText(mLocationList.get(position).getmCity());
//
//        View.OnClickListener clickHandler = new View.OnClickListener(){
//            public void onClick(View view){
//                //TODO code to map with google maps API
//            }
//        };
//
//        View.OnLongClickListener longClickHandler = new View.OnLongClickListener(){
//            public boolean onLongClick(View view){
//                //TODO logic to show delete menu
//
//                return true;//return true to not do onClick(the short click) method as well
//            }
//        };
//        holder.name.setOnClickListener(clickHandler);
//        holder.name.setOnLongClickListener(longClickHandler);
//
//        return convertView;
//    }


    public  void removeItemByPosition(int position){
        locationDAO.deleteLocation(mLocationList.get(position));
        mLocationList.remove(position);
        notifyDataSetChanged();
        //don't think the below be too accurate
//                mOriginalList.remove(position);
    }

    public void removeItem(LocationVO locationItem){
        mLocationList.remove(locationItem);
        mOriginalList.remove(locationItem);

        locationDAO.deleteLocation(locationItem);
        notifyDataSetChanged();
//        try {
//            FileHelper.deleteFromFile(locationItem.getmName());
//        } catch (StopProcessingException e) {
//            Log.e(LocationAdapter.class.getName(),e.toString());
//            e.printStackTrace();
//        }
    }

//    @Override
//    public int getCount(){
//        return mLocationList.size();
//    }

    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                FilterResults results = new FilterResults();

                //If there's nothing to filter on, return the original data for your list
                if(charSequence == null || charSequence.length() == 0)
                {
                    results.values = mOriginalList;
                    results.count = mOriginalList.size();
                }
                else
                {
                    ArrayList<LocationVO> filterResultsData = new ArrayList<LocationVO>();

                    for(LocationVO data : mLocationList)
                    {
                        //In this loop, you'll filter through originalData and compare each item to charSequence.
                        //If you find a match, add it to your new ArrayList
                        String name = data.getmName().toUpperCase();
                        String searchString = charSequence.toString().toUpperCase();
                        if(name.contains(searchString))
                        {
                            filterResultsData.add(data);
                        }
                    }
                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                mLocationList = (ArrayList<LocationVO>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
