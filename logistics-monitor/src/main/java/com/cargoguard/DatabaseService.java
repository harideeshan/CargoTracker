package com.cargoguard;

import java.sql.*;

public class DatabaseService {
    private String url = "jdbc:h2:./cargoguard_db;AUTO_SERVER=TRUE";
    private String user = "sa";
    private String password = "";

    public DatabaseService() {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS incidents (" +
                         "id INT AUTO_INCREMENT PRIMARY KEY, " +
                         "package_id VARCHAR(50), " +
                         "distance_dropped DOUBLE, " +
                         "latitude DOUBLE, " +
                         "longitude DOUBLE, " +
                         "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            stmt.execute(sql);
        } catch (Exception e) {
            System.out.println("❌ Database Init Error: " + e.getMessage());
        }
    }

    public void logIncident(String pkgId, double dist, double lat, double lon) {
        String query = "INSERT INTO incidents (package_id, distance_dropped, latitude, longitude) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, pkgId);
            pstmt.setDouble(2, dist);
            pstmt.setDouble(3, lat);
            pstmt.setDouble(4, lon);
            pstmt.executeUpdate();
            System.out.println("✅ Incident saved to Database.");
        } catch (Exception e) {
            System.out.println("❌ Database Log Error: " + e.getMessage());
        }
    }

    public void printIncidentReport() {
        System.out.println("\n--- 📋 RECOVERY LOG REPORT ---");
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM incidents")) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " | Pkg: " + rs.getString("package_id") + 
                                   " | Lat: " + rs.getDouble("latitude") + " | Lon: " + rs.getDouble("longitude"));
            }
        } catch (Exception e) {
            System.out.println("❌ Report Error: " + e.getMessage());
        }
    }
}