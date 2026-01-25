package com.cargoguard;

public class GuardianService {
    private static final double MAX_DISTANCE_METERS = 30.0;
    private DatabaseService db;

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
        final int R = 6371000; 
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    private void alertDriver(CargoPackage pkg, double distance) {
        System.out.println("\n🛑 [EMERGENCY ALERT] 🛑");
        System.out.println("Package " + pkg.getTrackingId() + " has fallen off!");
        System.out.println("📍 RECOVERY LINK: https://www.google.com/maps?q=" + pkg.getLat() + "," + pkg.getLon());
        db.logIncident(pkg.getTrackingId(), distance, pkg.getLat(), pkg.getLon());
    }
}