package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class LocationsDaoGenerator {

    public static void main(String args[]) throws Exception{
        Schema schema = new Schema(1,"com.example.model");

        Entity location = schema.addEntity("SavedLocations");
        location.addIdProperty();
        location.addStringProperty("title");
        location.addStringProperty("latitude");
        location.addStringProperty("longitude");
        location.addStringProperty("city");

        new DaoGenerator().generateAll(schema,"../app/src/main/java");
    }
}
