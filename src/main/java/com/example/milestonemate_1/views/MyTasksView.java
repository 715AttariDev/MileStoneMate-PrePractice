package com.example.milestonemate_1.views;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class MyTasksView implements ViewProvider{
    public AnchorPane getView() {
        AnchorPane pane = new AnchorPane();

        Label welcomeLabel = new Label("👋 Welcome to  My Tasks !");
        welcomeLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        AnchorPane.setTopAnchor(welcomeLabel, 20.0);
        AnchorPane.setLeftAnchor(welcomeLabel, 20.0);
        // Placeholder for stats
        Label statsLabel = new Label("This is boilerplate for developer to start code ...");
        statsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #555;");
        AnchorPane.setTopAnchor(statsLabel, 60.0);
        AnchorPane.setLeftAnchor(statsLabel, 20.0);
        pane.getChildren().addAll(welcomeLabel, statsLabel);
        pane.setStyle("-fx-background-color: #ffffff;-fx-background-radius: 15;");

        return pane;
    }
}

