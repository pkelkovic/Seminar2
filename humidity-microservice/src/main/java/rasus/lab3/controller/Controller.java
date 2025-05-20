/*package rasus.lab3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import rasus.lab3.service.HumidityService;

import org.springframework.http.ResponseEntity;

@RestController
public class Controller {

    @Autowired
    private HumidityService service;

    @GetMapping("/humidity")
    public ResponseEntity<?> getHumidity() {
        service.readData();
        return ResponseEntity.ok("Humidity reading is getting published to MQTT broker!");
    }
}*/
