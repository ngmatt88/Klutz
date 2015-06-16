package com.duckwarlocks.klutz.utilities;

import com.duckwarlocks.klutz.vo.GooglePlacesResponseVO;
import com.duckwarlocks.klutz.vo.LocationVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by ngmat_000 on 6/16/2015.
 */
public class JSONHelper {
    JSONObject mRawJsonObj;
    ArrayList<GooglePlacesResponseVO> mLocationList = new ArrayList<GooglePlacesResponseVO>();

    public JSONHelper(String jsonResp) throws JSONException{
        mRawJsonObj = new JSONObject(jsonResp);
    }

    public void parseRawJSON() throws JSONException{
        JSONArray parsedJsonArray = mRawJsonObj.getJSONArray("predictions");

        for(int i = 0; i<parsedJsonArray.length();i++){
            JSONObject oneObj = parsedJsonArray.getJSONObject(i);

            String description =  oneObj.getString("description");
            String[] splitArray = description.split(",");

            if(splitArray.length > 3){
                GooglePlacesResponseVO location = new GooglePlacesResponseVO();
                location.setRawDescription(description);
                location.setName(splitArray[0]);
                location.setAddr(splitArray[1]);
                location.setCity(splitArray[2]);
                location.setCountry(splitArray[3]);

                String placeID = oneObj.getString("place_id");
                location.setPlaceID(placeID);

                mLocationList.add(location);
            }
        }
    }
    public ArrayList<GooglePlacesResponseVO> getLocationList(){
        return mLocationList;
    }

}
