package rasus.lab3.repository;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;
import lombok.Data;
import rasus.lab3.entity.HumidityReading;
import rasus.lab3.entity.TemperatureReading;

@Data
@Repository
public class AggregatorRepo {
    private ArrayList<TemperatureReading> lastTempReading;
    private ArrayList<HumidityReading> lastHumReading;

    public AggregatorRepo() {
        this.lastHumReading = new ArrayList<HumidityReading>();
        this.lastTempReading = new ArrayList<TemperatureReading>();
    }

    public void addHumReading(HumidityReading hum) {
        if (lastHumReading.size() > 10) lastHumReading.clear();
        lastHumReading.add(hum);
    }

    public void addTempReading(TemperatureReading temp) {
        if (lastTempReading.size() > 10) lastTempReading.clear();
        lastTempReading.add(temp);
    }
}
