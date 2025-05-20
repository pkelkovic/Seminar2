package rasus.lab3.service;

import rasus.lab3.entity.TemperatureReading;

public interface MQTTService {
    public void publishReading(TemperatureReading reading);
}
