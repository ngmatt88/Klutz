package com.duckwarlocks.klutz.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.duckwarlocks.klutz.R;
import com.duckwarlocks.klutz.constants.CommonConstants;
import com.duckwarlocks.klutz.vo.LocationVO;
import com.duckwarlocks.klutz.vo.LocationViewHolder;

import java.util.ArrayList;

/**
 * Created by ngmat_000 on 6/8/2015.
 */
public class LocationAdapter extends BaseAdapter implements Filterable{

    private LayoutInflater mInflater;
    private ArrayList<LocationVO> mLocationList = new ArrayList<LocationVO>();;
    private ArrayList<LocationVO> mOriginalList;


    public LocationAdapter(Context context,ArrayList<LocationVO> mLocationList){
        mInflater = LayoutInflater.from(context);
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
    public View getView(int position, View convertView,ViewGroup parent){
        LocationViewHolder holder = null;
        if(convertView == null){
            holder = new LocationViewHolder();
            convertView = mInflater.inflate(R.layout.list_piece,null);
            holder.name = (TextView) convertView.findViewById(R.id.locationListItem);
            holder.latitude = (TextView) convertView.findViewById(R.id.locationListLatitude);
            holder.longitude = (TextView) convertView.findViewById(R.id.locationListLongitude);
            holder.cityName = (TextView) convertView.findViewById(R.id.locationListCityName);
            convertView.setTag(holder);
        }else{
            holder = (LocationViewHolder) convertView.getTag();
        }

        holder.name.setText(mLocationList.get(position).getmName());
        holder.latitude.setText(CommonConstants.LATITUDE_ABBREV + ":" + Double.toString(mLocationList.get(position).getmLatitude()));
        holder.longitude.setText(CommonConstants.LONGITUDE_ABBREV + ":" + Double.toString(mLocationList.get(position).getmLongitude()));
        holder.cityName.setText(mLocationList.get(position).getmCity());
        return convertView;
    }

    @Override
    public int getCount(){
        return mLocationList.size();
    }

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
