package com.duckwarlocks.klutz.utilities;

import android.location.Location;
import android.os.Environment;
import android.renderscript.Element;
import android.util.Log;

import com.duckwarlocks.klutz.CommonConstants;
import com.duckwarlocks.klutz.Exceptions.StopProcessingException;
import com.duckwarlocks.klutz.vo.LocationVO;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by ngmat_000 on 6/7/2015.
 */
public class XmlHelper {
    private List<LocationVO> locations;
    private LocationVO location;
    private String value;

    public XmlHelper(){
        locations = new ArrayList<LocationVO>();
    }

    public List<LocationVO> getAllLocations(){
        return locations;
    }

    public List<LocationVO> parseListOfLocations(String filePath) throws StopProcessingException{
        try {
            File file = new File(filePath);
            InputStream is = new FileInputStream(file.getPath());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(is));
            doc.getDocumentElement();

            NodeList nodeList = doc.getElementsByTagName(CommonConstants.LOCATION_VO_TAG);

            LocationVO location = new LocationVO();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node item = nodeList.item(i);
                NodeList properties = item.getChildNodes();
                for (int j = 0; j < properties.getLength(); j++) {
                    Node property = properties.item(j);
                    String name = property.getNodeName();
                    if (name.equalsIgnoreCase(CommonConstants.LOCATION_VO_NAME)) {
                        // Store it where you want
                        location.setName(property.getFirstChild().getNodeValue());
                    }
                    if (name.equalsIgnoreCase(CommonConstants.LOCATION_VO_LATITUDE)) {
                        location.setLatitude(Double.parseDouble(property.getFirstChild().getNodeValue()));
                    }
                    if (name.equalsIgnoreCase(CommonConstants.LOCATION_VO_LONGITUDE)) {
                        location.setLongitude(Double.parseDouble(property.getFirstChild().getNodeValue()));
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

//    public List<LocationVO> parseListOfLocations(InputStream is) throws StopProcessingException{
//        XmlPullParserFactory  factory = null;
//        XmlPullParser parser = null;
//        try{
//            factory = XmlPullParserFactory.newInstance();
//            factory.setNamespaceAware(true);
//
//            parser = factory.newPullParser();
//
//            parser.setInput(is,null);
//
//            int eventType = parser.getEventType();
//            while(eventType != XmlPullParser.END_DOCUMENT){
//                String tagname = parser.getName();
//
//                switch(eventType){
//                    case XmlPullParser.START_TAG :
//                        if(tagname.equals(CommonConstants.LOCATION_VO_DOC)){
//                            //make a new location
//                            location = new LocationVO();
//                        }
//                        break;
//
//                    case XmlPullParser.TEXT:
//                        value = parser.getText();
//                        break;
//
//                    case XmlPullParser.END_TAG:
//                        if(tagname.equals(CommonConstants.LOCATION_VO_TAG)){
//                            locations.add(location);
//                        }else if(tagname.equals(CommonConstants.LOCATION_VO_NAME)){
//                            location.setName(value);
//                        }else if(tagname.equals(CommonConstants.LOCATION_VO_LATITUDE)){
//                            location.setLatitude(Double.parseDouble((value)));
//                        }else if(tagname.equals(CommonConstants.LOCATION_VO_LONGITUDE)){
//                            location.setLongitude(Double.parseDouble(value));
//                        }
//                        break;
//                    default: break;
//                }
//            }
//        }catch(XmlPullParserException e){
//            Log.e(XmlHelper.class.getName(),e.toString());
//            throw new StopProcessingException(e.toString());
//        }
//        return locations;
//    }
}
