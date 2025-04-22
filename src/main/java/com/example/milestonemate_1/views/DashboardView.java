package com.example.milestonemate_1.views;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardView {
    public VBox getView() {
        VBox box = new VBox(10);
        Label title = new Label("Welcome to the Dashboard!");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #2c3e50;");
        box.getChildren().add(title);
        return box;
    }
}
