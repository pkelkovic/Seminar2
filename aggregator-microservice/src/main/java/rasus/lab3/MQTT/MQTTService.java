package rasus.lab3.MQTT;

import java.nio.charset.StandardCharsets;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import rasus.lab3.entity.HumidityReading;
import rasus.lab3.entity.TemperatureReading;
import rasus.lab3.repository.AggregatorRepo;

@Service
public class MQTTService {
    @Value("${MQTT_BROKER_URI:tcp://mosquitto:1883}")
    private String brokerURI;

    @Value("${MQTT_HUM_TOPIC:sensor/humidity}")
    private String humidityTopic;

    @Value("${MQTT_TEMP_TOPIC:sensor/temperature}")
    private String temperatureTopic;

    @Value("${MQTT_CLIENT_ID:aggregator}")
    private String clientId;

    private MqttClient client;

    @Autowired
    private AggregatorRepo repository;

    ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void connectClient() {
        try {
            this.client = new MqttClient(brokerURI, clientId, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            client.connect(options);  
            client.subscribe(temperatureTopic, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String value = new String(message.getPayload(), StandardCharsets.UTF_8);
                    TemperatureReading tempReading = objectMapper.readValue(value, TemperatureReading.class);
                    repository.addTempReading(tempReading);
                }
            });
    
            client.subscribe(humidityTopic, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String value = new String(message.getPayload(), StandardCharsets.UTF_8);
                    HumidityReading humReading = objectMapper.readValue(value, HumidityReading.class);
                    repository.addHumReading(humReading);
                }
            });      
        } catch (MqttException e) {
            e.printStackTrace(); 
        }

    }
}
