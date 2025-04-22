package com.example.milestonemate_1;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class DashBoardView {

    // Custom method to create an animated sidebar button
    private static StackPane createAnimatedButton(String buttonText) {
        // Create the main button
        Button button = new Button(buttonText);
        button.getStyleClass().add("sidebar-button"); // Apply the sidebar-button CSS style

        // Create the underline rectangle (initially width = 0)
        javafx.scene.shape.Rectangle underline = new javafx.scene.shape.Rectangle(0, 2);
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

        // Create the navigation bar
        HBox navBar = new HBox();
        navBar.setPadding(new Insets(10)); // Padding around the navbar
        navBar.setSpacing(10); // Spacing between elements
        navBar.getStyleClass().add("navbar"); // Apply navbar CSS
        navBar.setAlignment(Pos.CENTER); // Align content center vertically

        // Create Sign Up button
        Button signUpButton = new Button("Sign Up");
        signUpButton.getStyleClass().add("nav-button"); // Apply nav-button CSS

        // Create a spacer to push elements to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Create profile image (circular)
        ImageView profileImageView = new ImageView();
        try {
            Image profileImage = new Image(DashBoardView.class.getResource("/images/MAbdulRahimImage.png").toExternalForm());
            profileImageView.setImage(profileImage);
            profileImageView.setFitWidth(30); // Set image size
            profileImageView.setFitHeight(30);
            profileImageView.setPreserveRatio(true);

        } catch (Exception e) {
            System.out.println("⚠️ Profile image not found. Check path: /images/MAbdulRahimImage.png");
        }

// Apply the CSS class for circular styling
        profileImageView.getStyleClass().add("profile-image");

// Add hover effect
        profileImageView.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), profileImageView);
            scale.setToX(1.1);
            scale.setToY(1.1);
            scale.play();
        });

        profileImageView.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), profileImageView);
            scale.setToX(1);
            scale.setToY(1);
            scale.play();
        });

// Apply the CSS class for circular styling
        profileImageView.getStyleClass().add("profile-image");

// Add hover effect
        profileImageView.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), profileImageView);
            scale.setToX(1.1);
            scale.setToY(1.1);
            scale.play();
        });

        profileImageView.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), profileImageView);
            scale.setToX(1);
            scale.setToY(1);
            scale.play();
        });


        // Add spacer, Sign Up button, and profile image to navbar
        navBar.getChildren().addAll(spacer, signUpButton, profileImageView);

        // Add navbar and sidebar to the main container
        dashAnchorPane.getChildren().addAll(navBar, sideBar);

        // Anchor the navbar to the top of the window
        AnchorPane.setTopAnchor(navBar, 0.0);
        AnchorPane.setLeftAnchor(navBar, 200.0); // Position navbar to the right of the sidebar
        AnchorPane.setRightAnchor(navBar, 0.0); // Align navbar to the right edge of the window

        return dashAnchorPane;
    }
}