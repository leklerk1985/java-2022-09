module app.gamespiders {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens app.gamespiders to javafx.fxml;
    exports app.gamespiders;
}