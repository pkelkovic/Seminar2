/*package rasus.lab3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import rasus.lab3.service.TemperatureService;

import org.springframework.http.ResponseEntity;

@RestController
public class Controller {

    @Autowired
    private TemperatureService service;

    @GetMapping("/temperature")
    public ResponseEntity<?> getTemperature() {
        service.readData();
        return ResponseEntity.ok("Temperature reading is getting published to MQTT broker!");
    }
}*/
