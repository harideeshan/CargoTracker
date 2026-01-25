package com.cargoguard;

public class CargoPackage {
    private String trackingId;
    private double lastKnownLat;
    private double lastKnownLon;
    private boolean isDropped;

    public CargoPackage(String id, double lat, double lon) {
        this.trackingId = id;
        this.lastKnownLat = lat;
        this.lastKnownLon = lon;
        this.isDropped = false;
    }

    public String getTrackingId() { return trackingId; }
    public double getLat() { return lastKnownLat; }
    public double getLon() { return lastKnownLon; }
    public boolean isDropped() { return isDropped; }
    public void setDropped(boolean dropped) { isDropped = dropped; }
}