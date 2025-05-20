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

import rasus.lab3.entity.HumidityReading;
import rasus.lab3.repository.HumidityRepository;
import rasus.lab3.service.HumidityService;
import rasus.lab3.service.MQTTService;

@Service
public class HumidityServiceImpl implements HumidityService{

    @Autowired
    private HumidityRepository repository;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private MQTTService mqttService;


    HashMap<Integer, ArrayList<String>> data;
    boolean initialized = false;

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

    
    @Override
    public ArrayList<String> getData(int row) {
        return data.get(row);
    }

    public HumidityReading readHumidity() {
        //if csv data isn't already initialized, initialize it
        if (!initialized) {
            initialize();
            initialized = true;
        }
        long currentMinute = Instant.now().atZone(ZoneOffset.UTC).toEpochSecond() / 60;
        int rowNum = (int) (currentMinute % 100) + 1;

        var data = getData(rowNum);
        var hum = Double.parseDouble(data.get(2));

        //creating new instance of HumidityReading
        HumidityReading reading = new HumidityReading();
        reading.setName("Humidity");
        reading.setUnit("%");
        reading.setVal(hum);

        return reading;
    }


    @Override
    @Scheduled(fixedRateString = "${PERIOD:30000}")
    public void readData() {
        // simulate reading humitiy value from outside
        var reading = readHumidity();
        
        //add reading to database
        repository.save(reading);

        // publish reading on MQTT broker
        mqttService.publishReading(reading);

        return;
    }

}
