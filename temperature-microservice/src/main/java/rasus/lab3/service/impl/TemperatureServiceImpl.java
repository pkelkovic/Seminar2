package rasus.lab3.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;

import rasus.lab3.entity.TemperatureReading;
import rasus.lab3.repository.TemperatureRepository;
import rasus.lab3.service.MQTTService;
import rasus.lab3.service.TemperatureService;

@Service
public class TemperatureServiceImpl implements TemperatureService{

    @Autowired
    private TemperatureRepository repository;

    @Autowired
    private MQTTService mqttService;

    HashMap<Integer, ArrayList<String>> data;
    boolean initialized = false;

    @Autowired
    private ResourceLoader resourceLoader;

    //initialize data from readings.csv
    @Override
    public void initialize() {
        data = new HashMap<Integer, ArrayList<String>>();
        try {
            Resource resource = resourceLoader.getResource("classpath:readings.csv");
            InputStream inputStream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String redak = reader.readLine();
            int i = 0;
            redak = reader.readLine();
            while (redak != null) {
                var list = redak.split(",");
                var help = new ArrayList<String>();
                for(var s: list ) {
                    help.add(s);
                }
                data.put(i, help);
                i ++;
                redak = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //function that returns row no. "row" from data
    @Override
    public ArrayList<String> getData(int row) {
        return data.get(row);
    }

    // function that simulates reading temperature and puting data into TemperatureReading object
    public TemperatureReading getReading() {
        //if csv data isn't already initialized, initialize it
        if (!initialized) {
            initialize();
            initialized = true;
        }
        long currentMinute = Instant.now().atZone(ZoneOffset.UTC).toEpochSecond() / 60;
        int rowNum = (int) (currentMinute % 100) + 1;

        var data = getData(rowNum);
        var temp = Double.parseDouble(data.get(0));

        //creating new instance of TemperatureReading
        TemperatureReading reading = new TemperatureReading();
        reading.setName("Temperature");
        reading.setUnit("C");
        reading.setVal(temp);

        return reading;
    }

    //function that is executed every PERIOD seconds, generates reading, sends it to MQTT topic and savs to database
    @Override
    @Scheduled(fixedRateString = "${PERIOD:30000}")
    public void readData() {
        // "read" temperature from outside
        var reading = getReading();

        // add reading to database
        repository.save(reading);

        // publish reading to topic sensor/temperature
        mqttService.publishReading(reading);
        
        return;
    }

}
