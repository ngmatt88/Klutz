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
import com.duckwarlocks.klutz.vo.LocationVO;
import com.duckwarlocks.klutz.vo.LocationViewHolder;

import java.util.ArrayList;

/**
 * Created by ngmat_000 on 6/8/2015.
 */
public class LocationAdapter extends BaseAdapter implements Filterable{

    private LayoutInflater inflater;
    private ArrayList<LocationVO> locationList = new ArrayList<LocationVO>();;
    private ArrayList<LocationVO> originalList;


    public LocationAdapter(Context context,ArrayList<LocationVO> locationList){
        inflater = LayoutInflater.from(context);
//        this.locationList = locationList;
        this.locationList.addAll(locationList);
        this.originalList = locationList;
    }

    public LocationVO getItem(int position){
        return locationList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent){
        LocationViewHolder holder = null;
        if(convertView == null){
            holder = new LocationViewHolder();
            convertView = inflater.inflate(R.layout.list_piece,null);
            holder.name = (TextView) convertView.findViewById(R.id.locationListItem);
            holder.latitude = (TextView) convertView.findViewById(R.id.locationListLatitude);
            holder.longitude = (TextView) convertView.findViewById(R.id.locationListLongitude);
            convertView.setTag(holder);
        }else{
            holder = (LocationViewHolder) convertView.getTag();
        }
        holder.name.setText(locationList.get(position).getName());
        holder.latitude.setText(Double.toString(locationList.get(position).getLatitude()));
        holder.longitude.setText(Double.toString(locationList.get(position).getLongitude()));
        return convertView;
    }

    @Override
    public int getCount(){
        return locationList.size();
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
//                    results.values = locationList;
//                    results.count = locationList.size();
                    results.values = originalList;
                    results.count = originalList.size();
                }
                else
                {
                    ArrayList<LocationVO> filterResultsData = new ArrayList<LocationVO>();

                    for(LocationVO data : locationList)
                    {
                        //In this loop, you'll filter through originalData and compare each item to charSequence.
                        //If you find a match, add it to your new ArrayList
                        String name = data.getName().toUpperCase();
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
                if(filterResults.count == 0){
                    notifyDataSetInvalidated();
                }else{
                    locationList = (ArrayList<LocationVO>)filterResults.values;
                    notifyDataSetChanged();
                }
            }
        };
    }
}
