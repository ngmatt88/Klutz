package com.duckwarlocks.klutz.utilities;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.duckwarlocks.klutz.constants.CommonConstants;
import com.duckwarlocks.klutz.Exceptions.StopProcessingException;
import com.duckwarlocks.klutz.vo.LocationVO;

import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ngmat_000 on 6/7/2015.
 */
public class FileHelper {
    private static boolean standAlone = true;
    private static LocationVO newLocation = new LocationVO();

    public static void writeToFile(Context context,String name, double latitude, double longitude) throws StopProcessingException{
        newLocation.setName(name);
        newLocation.setLatitude(latitude);
        newLocation.setLongitude(longitude);
        try{
            File folder = new File(
                    Environment.getExternalStorageDirectory().toString() +
                            File.separator +CommonConstants.KLUTZ.toLowerCase());

            if(!folder.exists()){
                folder.mkdir();
            }

            File root = new File(folder,CommonConstants.FILE_LOCATION);

            if(!root.exists()){
                root.createNewFile();
                Toast.makeText(context,root.getAbsolutePath(),Toast.LENGTH_LONG).show();
            }

//            Toast.makeText(context,root.getAbsolutePath(),Toast.LENGTH_LONG).show();
            XmlSerializer serializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            serializer.setOutput(writer);

            XmlHelper xmlHelper = new XmlHelper();
            List<LocationVO> locationVOList = (root.length() > 0) ?  xmlHelper.parseListOfLocations(root.getAbsolutePath()) : new ArrayList<LocationVO>();
            locationVOList.add(newLocation);

            //delete contents of file first after reading it!
            FileOutputStream fos = new FileOutputStream(root,false);
            fos.close();
            fos = new FileOutputStream(root, true);

            //Loop through and create the aggregates
            serializer.startDocument("UTF-8", true);
                serializer.startTag(null, CommonConstants.LOCATION_VO_DOC);
                for(LocationVO record : locationVOList) {
                    serializer.startTag(null, CommonConstants.LOCATION_VO_TAG);
                        serializer.startTag(null, CommonConstants.LOCATION_VO_NAME);
                            serializer.text(record.getName());
                        serializer.endTag(null, CommonConstants.LOCATION_VO_NAME);
                        serializer.startTag(null, CommonConstants.LOCATION_VO_LATITUDE);
                            serializer.text(Double.toString(record.getLatitude()));
                        serializer.endTag(null, CommonConstants.LOCATION_VO_LATITUDE);
                        serializer.startTag(null, CommonConstants.LOCATION_VO_LONGITUDE);
                            serializer.text(Double.toString(record.getLongitude()));
                        serializer.endTag(null, CommonConstants.LOCATION_VO_LONGITUDE);
                    serializer.endTag(null, CommonConstants.LOCATION_VO_TAG);
                }
                serializer.endTag(null,CommonConstants.LOCATION_VO_DOC);
            serializer.endDocument();
            serializer.flush();

            String dataWrite = writer.toString();

            fos.write(dataWrite.getBytes());
            fos.close();
        }catch(FileNotFoundException e){
            Log.e(FileHelper.class.getName(),"Location file not found!");
            throw new StopProcessingException(e.toString());
        }catch(IOException io){
            Log.e(FileHelper.class.getName(),"Error reading location file");
            throw new StopProcessingException(io.toString());
        }
    }


    public static String readFile(Context context) throws StopProcessingException{
        String resultSet = "";

        try{
            InputStream inputStream = context.openFileInput(CommonConstants.FILE_LOCATION);
            if(inputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String record;
                StringBuilder sb = new StringBuilder();

                while((record = bufferedReader.readLine()) != null){
                    sb.append(record);
                }
                inputStream.close();
                inputStreamReader.close();
                bufferedReader.close();
                resultSet = sb.toString();
            }
        }catch (FileNotFoundException e){
            Log.e(FileHelper.class.getName(),"Location file not found!");
            throw new StopProcessingException(e.toString());
        }catch(IOException io){
            Log.e(FileHelper.class.getName(),"Error reading location file");
            throw new StopProcessingException(io.toString());
        }
        return resultSet;
    }
}
