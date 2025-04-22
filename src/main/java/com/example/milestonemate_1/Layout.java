package com.example.milestonemate_1;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Layout {

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
            Image logo = new Image(Layout.class.getResource("/images/Blue Logo.png").toExternalForm());
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
        navBar.setSpacing(25); // Spacing between elements
        navBar.getStyleClass().add("navbar"); // Apply navbar CSS
        navBar.setAlignment(Pos.CENTER); // Align content center vertically

        // Create left Dashboard label
        Label dashboardLeftLabel = new Label("Dashboard");
        dashboardLeftLabel.setPadding(new Insets(21));
        dashboardLeftLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Create Sign Up button
        Button signUpButton = new Button("Sign Up");
        signUpButton.getStyleClass().add("nav-button"); // Apply nav-button CSS
        signUpButton.setOnAction(e -> {
            // Create labels and input fields
            Label usernameLabel = new Label("Username:");
            TextField usernameField = new TextField();
            usernameField.setPromptText("Enter username");

            Label emailLabel = new Label("Email:");
            TextField emailField = new TextField();
            emailField.setPromptText("Enter email");

            Label passwordLabel = new Label("Password:");
            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText("Enter password");

            VBox dialogContent = new VBox(10); // Vertical box with spacing
            dialogContent.getChildren().addAll(usernameLabel, usernameField,
                    emailLabel, emailField,
                    passwordLabel, passwordField);
            dialogContent.setPadding(new Insets(10));

            // Create the alert dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sign Up");
            alert.setHeaderText("Create your account");
            alert.getDialogPane().setContent(dialogContent);

            // Custom button text
            alert.getButtonTypes().setAll(
                    new javafx.scene.control.ButtonType("Register", javafx.scene.control.ButtonBar.ButtonData.OK_DONE),
                    new javafx.scene.control.ButtonType("Cancel", javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE)
            );

            // Show the dialog and handle result
            alert.showAndWait().ifPresent(result -> {
                if (result.getButtonData() == javafx.scene.control.ButtonBar.ButtonData.OK_DONE) {
                    // You can add basic validation here
                    String username = usernameField.getText();
                    String email = emailField.getText();
                    String password = passwordField.getText();

                    System.out.println("✅ Registered User: " + username + ", " + email);
                    // Show confirmation or store info
                    Alert confirm = new Alert(Alert.AlertType.INFORMATION, "User " + username + " registered successfully!");
                    confirm.show();
                }
            });
        });

        // Create right Dashboard label with date
        Label dashboardRightLabel = new Label("Manager");
        dashboardRightLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Add current date display
        Label dateLabel = new Label();
        dateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy");
        dateLabel.setText(currentDate.format(formatter));

        // Create a spacer to push elements to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Create profile image container (circular)
        StackPane profileImageContainer = new StackPane();
        profileImageContainer.getStyleClass().add("profile-image-container");

        // Create profile image
        ImageView profileImageView = new ImageView();
        try {
            Image profileImage = new Image(Layout.class.getResource("/images/MAbdulRahimImage.png").toExternalForm());
            profileImageView.setImage(profileImage);
            profileImageView.setFitWidth(60); // Set image size
            profileImageView.setFitHeight(60);
            profileImageView.setPreserveRatio(true);

            // Clip the image into a circle
            Circle clip = new Circle(30, 30, 30); // (centerX, centerY, radius)
            profileImageView.setClip(clip);

        } catch (Exception e) {
            System.out.println("⚠️ Profile image not found. Check path: /images/MAbdulRahimImage.png");
        }

        profileImageContainer.getChildren().add(profileImageView);

        // Add space between profile image and other elements
        HBox.setMargin(profileImageContainer, new Insets(0, 15, 0, 0));

        // Add hover effect to the container
        profileImageContainer.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), profileImageContainer);
            scale.setToX(1.1);
            scale.setToY(1.1);
            scale.play();
        });

        profileImageContainer.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), profileImageContainer);
            scale.setToX(1);
            scale.setToY(1);
            scale.play();
        });

        // Add elements to navbar
        navBar.getChildren().addAll(
                dashboardLeftLabel, // Left Dashboard label
                spacer,
                dashboardRightLabel, // Right Dashboard label
                dateLabel,          // Date display
                signUpButton,
                profileImageContainer
        );

        // Add navbar and sidebar to the main container
        dashAnchorPane.getChildren().addAll(navBar, sideBar);

        // Anchor the navbar to the top of the window
        AnchorPane.setTopAnchor(navBar, 0.0);
        AnchorPane.setLeftAnchor(navBar, 200.0); // Position navbar to the right of the sidebar
        AnchorPane.setRightAnchor(navBar, 0.0); // Align navbar to the right edge of the window

        return dashAnchorPane;
    }
}