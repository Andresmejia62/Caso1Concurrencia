module com.appregistros.caso1 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.appregistros.caso1 to javafx.fxml;
    opens com.appregistros.caso1.controller to javafx.fxml;
    exports com.appregistros.caso1;
    exports com.appregistros.caso1.controller;
}