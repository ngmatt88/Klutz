package com.duckwarlocks.klutz.utilities;

import android.util.Log;

import com.duckwarlocks.klutz.constants.CommonConstants;
import com.duckwarlocks.klutz.Exceptions.StopProcessingException;
import com.duckwarlocks.klutz.vo.LocationVO;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by ngmat_000 on 6/7/2015.
 */
public class XmlHelper {
    private XmlHelper(){}

    /**
     * Reads from the locations.xml file and returns an arraylist of location VO's
     * @param filePath
     * @return
     * @throws StopProcessingException
     */
    public static ArrayList<LocationVO> parseListOfLocations(String filePath) throws StopProcessingException{
        ArrayList<LocationVO> locations = new ArrayList<LocationVO>();
        try {
            File file = new File(filePath);
            InputStream is = new FileInputStream(file.getPath());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(is));
            doc.getDocumentElement();

            NodeList nodeList = doc.getElementsByTagName(CommonConstants.LOCATION_VO_TAG);

            LocationVO location;
            //loop through every Location aggregate
            for (int aggregate = 0; aggregate < nodeList.getLength(); aggregate++) {
                location = new LocationVO();
                Node item = nodeList.item(aggregate);
                NodeList properties = item.getChildNodes();
                //loop through every element within the Location Aggregate ie name, latitude, etc
                for (int element = 0; element < properties.getLength(); element++) {
                    Node property = properties.item(element);
                    String name = property.getNodeName();
                    if (name.equalsIgnoreCase(CommonConstants.LOCATION_VO_NAME)) {
                        location.setmName(property.getFirstChild().getNodeValue());
                    }
                    if (name.equalsIgnoreCase(CommonConstants.LOCATION_VO_LATITUDE)) {
                        location.setmLatitude(Double.parseDouble(property.getFirstChild().getNodeValue()));
                    }
                    if (name.equalsIgnoreCase(CommonConstants.LOCATION_VO_LONGITUDE)) {
                        location.setmLongitude(Double.parseDouble(property.getFirstChild().getNodeValue()));
                    }
                    if (name.equalsIgnoreCase(CommonConstants.LOCATION_VO_CITYNAME)) {
                        location.setmCity(property.getFirstChild().getNodeValue());
                    }
                }
                locations.add(location);
            }
        }catch (FileNotFoundException e){
            Log.e(XmlHelper.class.getName(),e.toString());
            throw new StopProcessingException(e.toString());
        }catch (IOException io){
            Log.e(XmlHelper.class.getName(),io.toString());
            throw new StopProcessingException(io.toString());
        }catch (SAXException se){
            Log.e(XmlHelper.class.getName(),se.toString());
            throw new StopProcessingException(se.toString());
        }catch (ParserConfigurationException pe){
            Log.e(XmlHelper.class.getName(),pe.toString());
            throw new StopProcessingException(pe.toString());
        }
        return locations;
    }

}
