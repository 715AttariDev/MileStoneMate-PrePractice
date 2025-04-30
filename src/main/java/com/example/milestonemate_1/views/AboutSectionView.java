package com.example.milestonemate_1.views;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class AboutSectionView implements ViewProvider {
    @Override
    public AnchorPane getView() {
        AnchorPane pane = new AnchorPane();

        VBox content = new VBox(10); // 10px spacing between items
        content.setAlignment(Pos.CENTER);
        content.getStyleClass().add("about-content");

        Label titleLabel = new Label("📚 About MilestoneMate");
        titleLabel.getStyleClass().add("about-title");

        Label versionLabel = new Label("Version: 1.0.0");
        versionLabel.getStyleClass().add("about-text");

        Label devsLabel = new Label("Developed by: Abdul Rahim & Muhammad Bilal");
        devsLabel.getStyleClass().add("about-text");

        Label uniLabel = new Label("University of Engineering and Technology, Taxila");
        uniLabel.getStyleClass().add("about-text");

        Label purposeLabel = new Label("Helping teams manage projects efficiently and easily.");
        purposeLabel.getStyleClass().add("about-purpose");

        content.getChildren().addAll(titleLabel, versionLabel, devsLabel, uniLabel, purposeLabel);

        AnchorPane.setTopAnchor(content, 50.0);
        AnchorPane.setLeftAnchor(content, 30.0);
        AnchorPane.setRightAnchor(content, 30.0);

        pane.getChildren().add(content);
        pane.getStyleClass().add("about-pane");

        return pane;
    }
}
