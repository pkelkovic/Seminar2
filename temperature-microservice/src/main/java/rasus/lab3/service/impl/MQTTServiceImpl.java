package rasus.lab3.service.impl;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;


import rasus.lab3.entity.TemperatureReading;
import rasus.lab3.service.MQTTService;

@Service
public class MQTTServiceImpl implements MQTTService{
    @Value("${MQTT_BROKER_URI:tcp://mosquitto:1883}")
    private String brokerURI;

    @Value("${MQTT_CLIENT_ID:temperature_sensor}")
    private String clientId;

    @Value("${MQTT_TOPIC:sensor/temperature}")
    private String topic;

    private MqttClient client;

    @Autowired
    private ObjectMapper objectMapper; // for converting reading to JSON format


    @PostConstruct
    public void connectClient() {
        try {
            this.client = new MqttClient(brokerURI, clientId, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            client.connect(options);        
        } catch (MqttException e) {
            e.printStackTrace(); 
        }
    }


    @Override
    public void publishReading(TemperatureReading reading) {
        try {
            MqttMessage message = new MqttMessage(toJson(reading).getBytes());
            message.setQos(1); // message should be delvered at least once
            client.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    } 
    

    public String toJson(TemperatureReading reading) {
        try {
            return objectMapper.writeValueAsString(reading);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }

}
