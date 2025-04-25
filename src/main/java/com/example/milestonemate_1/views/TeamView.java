package com.example.milestonemate_1.views;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class TeamView {

    public AnchorPane getView() {
        AnchorPane pane = new AnchorPane();

        Label label = new Label("👥 This is the Team View");
        label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        AnchorPane.setTopAnchor(label, 20.0);
        AnchorPane.setLeftAnchor(label, 20.0);

        pane.getChildren().add(label);
        pane.setStyle("-fx-background-color: #ffffff;-fx-background-radius: 15;");
        return pane;
    }
}
