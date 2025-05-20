package rasus.lab3.service;

import rasus.lab3.entity.HumidityReading;

public interface MQTTService {
    public void publishReading(HumidityReading reading);
}
