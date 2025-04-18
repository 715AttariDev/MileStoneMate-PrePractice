package com.example.milestonemate_1;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class DashBoardView {

    // Custom method to create an animated sidebar button
    private static StackPane createAnimatedButton(String buttonText) {
        // Create the main button
        Button button = new Button(buttonText);
        button.getStyleClass().add("sidebar-button"); // Apply the sidebar-button CSS style

        // Create the underline rectangle (initially width = 0)
        Rectangle underline = new Rectangle(0, 2);
        underline.setStyle("-fx-fill: #00aaff;"); // Blue color for underline
        underline.setTranslateY(10); // Slightly move the underline below the button text

        // StackPane to overlay button and underline together
        StackPane buttonStack = new StackPane(button, underline);

        // When mouse enters (hover starts), animate underline expanding left to right
        button.setOnMouseEntered(e -> {
            ScaleTransition expand = new ScaleTransition(Duration.millis(300), underline);
            expand.setFromX(0);
            expand.setToX(1);
            expand.setCycleCount(1);
            expand.setAutoReverse(false);
            underline.setScaleX(0); // Reset scale before animation
            expand.play();
        });

        // When mouse exits (hover ends), animate underline shrinking back
        button.setOnMouseExited(e -> {
            ScaleTransition shrink = new ScaleTransition(Duration.millis(200), underline);
            shrink.setFromX(1);
            shrink.setToX(0);
            shrink.setCycleCount(1);
            shrink.setAutoReverse(false);
            shrink.play();
        });

        return buttonStack;
    }

    public static AnchorPane getView() {
        // Create the main container layout
        AnchorPane dashAnchorPane = new AnchorPane();

        // Create the sidebar with vertical spacing of 20 between items
        VBox sideBar = new VBox(20);
        sideBar.setPadding(new Insets(30)); // Padding around the sidebar
        sideBar.getStyleClass().add("sidebar"); // Apply sidebar background CSS

        // Load and display the app logo
        ImageView logoView = new ImageView();
        try {
            Image logo = new Image(DashBoardView.class.getResource("/images/Blue Logo.png").toExternalForm());
            logoView.setImage(logo);
            logoView.setFitWidth(150); // Set logo width
            logoView.setPreserveRatio(true); // Keep logo's aspect ratio
        } catch (Exception e) {
            System.out.println("⚠️ Logo image not found. Check path: /images/Blue Logo.png");
        }

        // Create animated sidebar buttons
        StackPane dashboardBtn = createAnimatedButton("Dashboard");
        StackPane tasksBtn = createAnimatedButton("Tasks");
        StackPane teamBtn = createAnimatedButton("Team");

        // Add logo and buttons to the sidebar
        sideBar.getChildren().addAll(logoView, dashboardBtn, tasksBtn, teamBtn);

        // Anchor the sidebar to the left side of the window
        AnchorPane.setTopAnchor(sideBar, 0.0);
        AnchorPane.setBottomAnchor(sideBar, 0.0);
        AnchorPane.setLeftAnchor(sideBar, 0.0);
        sideBar.setPrefWidth(200); // Fixed sidebar width

        // Add sidebar to the main layout
        dashAnchorPane.getChildren().add(sideBar);

        return dashAnchorPane;
    }
}
