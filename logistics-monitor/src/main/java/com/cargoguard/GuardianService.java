package com.cargoguard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class GuardianService {
    private static final double MAX_DISTANCE_METERS = 30.0;
    private final DatabaseService db;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public GuardianService(DatabaseService dbService) {
        this.db = dbService;
    }

    public void monitorSecurity(CargoPackage pkg, double truckLat, double truckLon) {
        double distance = calculateDistance(pkg.getLat(), pkg.getLon(), truckLat, truckLon);
        
        if (distance > MAX_DISTANCE_METERS && !pkg.isDropped()) {
            pkg.setDropped(true);
            alertDriver(pkg, distance);
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Earth radius in meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    private void alertDriver(CargoPackage pkg, double distance) {
        // 1. Console Logging
        System.out.println("\n🛑 [EMERGENCY ALERT] 🛑");
        System.out.println("Package " + pkg.getTrackingId() + " has fallen off!");

        // 2. Database Persistence
        db.logIncident(pkg.getTrackingId(), distance, pkg.getLat(), pkg.getLon());

        // 3. Professional WebSocket Push
        // This sends a real-time JSON object to the React frontend
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "DROPPED");
        payload.put("pkgId", pkg.getTrackingId());
        payload.put("lat", pkg.getLat());
        payload.put("lon", pkg.getLon());
        payload.put("distance", String.format("%.2f", distance));
        payload.put("recoveryLink", "https://www.google.com/maps?q=" + pkg.getLat() + "," + pkg.getLon());

        messagingTemplate.convertAndSend("/topic/alerts", payload);
    }
}