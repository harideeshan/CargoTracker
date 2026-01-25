# 📦 CargoGuard: Real-Time Logistics Integrity System

**CargoGuard** is a full-stack Java and React application designed to solve the "Cargo Leakage" problem in the logistics industry. Inspired by a real-world observation of a package falling off a delivery vehicle, this system uses geospatial logic to detect asset loss in real-time.



## 🚀 The Problem
In last-mile delivery, items can fall from vehicles unnoticed, leading to financial loss, insurance claims, and customer dissatisfaction. Standard tracking only identifies "Last Known Location," but doesn't alert drivers the moment a package is left behind.

## 🛠️ Tech Stack
- **Backend:** Java 25, Spring Boot, Maven
- **Database:** H2 (SQL) for persistent incident logging
- **Frontend:** React.js, Lucide-React (Icons)
- **Algorithms:** Haversine Formula for geospatial distance calculation

## 🧠 Key Features
- **Real-Time Monitoring:** Calculates the distance between the vehicle and cargo every 2 seconds.
- **Geospatial Intelligence:** Triggers an emergency alert if the distance exceeds a 30-meter threshold.
- **Incident Persistence:** Automatically logs drop-off coordinates, timestamps, and package IDs to an SQL database.
- **One-Click Recovery:** Generates a dynamic Google Maps navigation link to the exact coordinates of the lost item.

## 🏗️ System Architecture
The system follows a Service-Oriented Architecture (SOA):
1. **The Logic Engine:** A Java service implementing spherical geometry to monitor proximity.
2. **REST API:** A Spring Boot controller providing endpoints for telemetry data.
3. **The Dashboard:** A reactive UI that simulates vehicle movement and displays live telemetry.

## 🏁 Getting Started

### Prerequisites
- JDK 17 or higher
- Node.js & npm
- Maven

### Installation
1. **Backend:**
   ```bash
   cd logistics-monitor
   mvn spring-boot:run