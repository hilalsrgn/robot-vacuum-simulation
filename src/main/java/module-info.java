module edu.erciyes.robotproje {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens edu.erciyes.robotproje to javafx.fxml;
    exports edu.erciyes.robotproje;
}