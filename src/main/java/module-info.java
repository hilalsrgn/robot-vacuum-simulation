module edu.erciyes.robotproje {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.erciyes.robotproje to javafx.fxml;
    exports edu.erciyes.robotproje;
}