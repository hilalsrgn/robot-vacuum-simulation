# 🤖 BZ214 Visual Programming Robot Vacuum Cleaning Simulation

##  About the Project

Robot Vacuum Cleaning Simulation is a robot vacuum cleaner simulation developed using JavaFX. The project aims to simulate a robot vacuum cleaner moving within a room, cleaning dirt, avoiding obstacles, and cleaning the environment using different cleaning algorithms.

The application allows users to customize the room environment, add dirt and furniture, select different cleaning strategies, and observe the robot's behavior in real time.

---

##  Project Objective

This project was developed to simulate the behavior of an autonomous robot vacuum cleaner in a home environment. The goal is for the robot to navigate using different cleaning algorithms, avoid obstacles, clean dirt, and manage its battery level efficiently.

---

##  Features

### Robot Vacuum System

* Robot navigation within the room
* Real-time position tracking
* Direction display
* Battery status monitoring
* Adjustable speed control
* Charging station support

### 🧹 Dirt Management

* Ability to add dirt to the map
* Different dirt types:

  * Dust
  * Liquid
  * Stain

### 🛋️ Furniture and Obstacle System

* Ability to place furniture on the map
* Furniture acts as obstacles
* User-customizable room layouts

### 🗺️ Room Simulation

* Grid-based room structure
* Cell-based positioning system
* Visual map representation

###  Cleaning Algorithms

* Random Cleaning
* Spiral Cleaning
* Wall Following

###  User Interface

* Cleaning algorithm selection
* Speed control
* Dirt placement
* Furniture placement
* Battery indicator
* Cleaning information display

---

##  Cleaning Algorithms

### Random Algorithm

The robot moves randomly within the room while performing cleaning tasks.

### Spiral Algorithm

The robot follows a spiral path to clean the environment systematically and cover as much area as possible.

### Wall Following Algorithm

The robot follows walls and obstacles while cleaning the surrounding environment.

---

##  Architecture

The project follows the MVC (Model-View-Controller) architecture.

### Model Layer

* Room
* Robot
* Position
* Cell
* Furniture
* FurnitureType
* Dirt
* Obstacle
* ChargingStation

### View Layer

* SimulationCanvas
* ControlPanel
* SoundEffect

### Controller Layer

* BatteryManager
* RobotController
* PathFinder

---

## 💻 Technologies Used

* Java
* JavaFX
* Maven
* IntelliJ IDEA
* GitHub

---

## ⚙️ System Requirements

The following software is required to run the project:

* Java 21
* JavaFX 21
* Maven 3.9+
* IntelliJ IDEA (Recommended)

---

##  Installation and Setup

### 1. Clone the Repository

```bash
git clone https://github.com/hilalsrgn/robot-vacuum-cleaning-simulation.git
```

### 2. Open the Project in IntelliJ IDEA

* Launch IntelliJ IDEA.
* Open the cloned project.
* Wait for Maven dependencies to be downloaded and configured.

---


##  Usage Scenario

1. Launch the application.
2. Select a cleaning algorithm.
3. Add dirt to the environment.
4. Place furniture on the map.
5. Start the robot.
6. Observe the robot's movement and cleaning process.
7. Monitor battery and cleaning information.

---

## ⚠️ Challenges Encountered

During the development process, the following challenges were addressed:

* Designing robot movement algorithms
* Managing obstacles and furniture
* Implementing cleaning strategies
* Developing a visual simulation using JavaFX
* Battery management
* Handling user interactions

---

##  Future Improvements

* Detection of inaccessible areas
* Multiple room layouts
* Cleaning animations

---

## 👥 Team Members

* Yasemin Akdeniz
* Hilal Sargın
* Bilge Akpınar

---

## 📚 Course Information

**BZ 214 - Visual Programming**

Erciyes University
Department of Computer Engineering

2025 - 2026

---

## 🙏 Acknowledgements

This project was developed as part of the **BZ 214 - Visual Programming** course offered by the Department of Computer Engineering at Erciyes University.

We would like to thank our course instructor and all team members who contributed to the development of this project.

---

