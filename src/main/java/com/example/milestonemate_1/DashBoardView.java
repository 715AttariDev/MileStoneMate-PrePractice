package com.example.milestonemate_1;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.geometry.Insets;

public class DashBoardView {

    public static AnchorPane getView() {
        // Main layout
        AnchorPane dashAnchorPane = new AnchorPane();

        // Sidebar VBox
        VBox sideBar = new VBox(20); // spacing of 20
        sideBar.setPadding(new Insets(30));
        sideBar.getStyleClass().add("sidebar"); // CSS class

        // App Logo
        ImageView logoView = new ImageView();
        try {
            Image logo = new Image(DashBoardView.class.getResource("/images/Blue Logo.png").toExternalForm());
            logoView.setImage(logo);
            logoView.setFitWidth(150);
            logoView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("⚠️ Logo image not found. Check path: /images/Blue Logo.png");
        }

        // Sidebar Buttons
        Button dashboardBtn = new Button("Dashboard");
        Button tasksBtn = new Button("Tasks");
        Button teamBtn = new Button("Team");

        // Style buttons
        dashboardBtn.getStyleClass().add("sidebar-button");
        tasksBtn.getStyleClass().add("sidebar-button");
        teamBtn.getStyleClass().add("sidebar-button");

        // Add all elements to the sidebar
        sideBar.getChildren().addAll(logoView, dashboardBtn, tasksBtn, teamBtn);

        // Anchor the sidebar to the left
        AnchorPane.setTopAnchor(sideBar, 0.0);
        AnchorPane.setBottomAnchor(sideBar, 0.0);
        AnchorPane.setLeftAnchor(sideBar, 0.0);
        sideBar.setPrefWidth(200);

        // Add sidebar to main layout
        dashAnchorPane.getChildren().add(sideBar);

        return dashAnchorPane;
    }
}
