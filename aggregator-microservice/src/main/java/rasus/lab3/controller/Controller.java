package rasus.lab3.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import rasus.lab3.repository.AggregatorRepo;


import org.springframework.http.ResponseEntity;

@RestController
public class Controller {
    
    @Autowired
    private AggregatorRepo repository;

    @GetMapping("/reading")
    public ResponseEntity<?> getReadings() {
        var temp = repository.getLastTempReading();
        var hum = repository.getLastHumReading();
        var response = new ArrayList<>();
        response.add(hum);
        response.add(temp);
        return ResponseEntity.ok(response);
    }
}
