package rasus.lab3.service;
import java.util.ArrayList;

import rasus.lab3.entity.HumidityReading;


// Interface
public interface HumidityService {
    //initialize data from readings.csv
    public void initialize();

    //function that returns row no. "row" from data
    public ArrayList<String> getData(int row);

    // simulates reading humitiy value and turning it into HumitiyReading object
    public HumidityReading readHumidity();

    //function for reading humidity value, publishing it to MQTT broker and saving it to database
    public void readData();
}