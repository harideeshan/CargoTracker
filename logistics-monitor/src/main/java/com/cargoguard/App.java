package com.cargoguard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allows your React frontend to connect
public class App {

    private DatabaseService db = new DatabaseService();
    private GuardianService guardian = new GuardianService(db);
    private CargoPackage amazonBox = new CargoPackage("AMZ-667-BLR", 11.0168, 76.9558);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @PostMapping("/update-location")
    public Map<String, Object> updateLocation(@RequestBody Map<String, Double> location) {
        double truckLat = location.get("lat");
        double truckLon = location.get("lon");

        guardian.monitorSecurity(amazonBox, truckLat, truckLon);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "Location Processed");
        response.put("packageId", amazonBox.getTrackingId());
        response.put("isDropped", amazonBox.isDropped());
        
        if (amazonBox.isDropped()) {
            response.put("recoveryLink", "https://www.google.com/maps?q=" + amazonBox.getLat() + "," + amazonBox.getLon());
        }

        return response;
    }
}