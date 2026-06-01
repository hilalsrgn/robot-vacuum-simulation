module com.robotvacuum.vacuum_cleaner_simulation {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.robotvacuum.vacuum_cleaner_simulation.controller to javafx.fxml;
    exports com.robotvacuum.vacuum_cleaner_simulation;
}