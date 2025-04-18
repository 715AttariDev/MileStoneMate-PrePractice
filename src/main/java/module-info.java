module com.example.milestonemate_1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.desktop;

    opens com.example.milestonemate_1 to javafx.fxml;
    exports com.example.milestonemate_1;
}