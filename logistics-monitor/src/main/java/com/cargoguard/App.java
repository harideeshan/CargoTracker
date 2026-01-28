package com.cargoguard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") 
public class App {

    @Autowired
    private GuardianService guardian;

    private CargoPackage amazonBox = new CargoPackage("AMZ-667-BLR", 11.0168, 76.9558);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @PostMapping("/update-location")
    public Map<String, Object> updateLocation(@RequestBody Map<String, Double> location) {
        double truckLat = location.getOrDefault("lat", 0.0);
        double truckLon = location.getOrDefault("lon", 0.0);

        guardian.monitorSecurity(amazonBox, truckLat, truckLon);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "Location Processed");
        response.put("packageId", amazonBox.getTrackingId());
        response.put("isDropped", amazonBox.isDropped());
        
        // CORRECTION: Send full alert data in the REST response as a fallback
        if (amazonBox.isDropped()) {
            response.put("type", "DROPPED");
            response.put("pkgId", amazonBox.getTrackingId());
            response.put("lat", amazonBox.getLat());
            response.put("lon", amazonBox.getLon());
            response.put("recoveryLink", "https://www.google.com/maps?q=" + amazonBox.getLat() + "," + amazonBox.getLon());
        }

        return response;
    }
}