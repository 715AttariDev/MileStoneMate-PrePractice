package com.example.milestonemate_1.views;

import com.example.milestonemate_1.FileUtils;
import com.example.milestonemate_1.Layout;
import com.example.milestonemate_1.Session;
import com.example.milestonemate_1.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class LoginView {
    private String selectedRole;

    public String getSelectedRole() {
        return selectedRole;
    }

    public Scene getScene(Stage stage) {
        // Title
        Label title = createStyledLabel("🔐 Login to Your Account", "18px", "bold");
        title.getStyleClass().add("large");

        // Username Section
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.getStyleClass().add("text-field");

        VBox usernameBox = new VBox(5, usernameLabel, usernameField);
        usernameBox.setAlignment(Pos.TOP_LEFT);

        // Password Section
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.getStyleClass().add("password-field");

        VBox passwordBox = new VBox(5, passwordLabel, passwordField);
        passwordBox.setAlignment(Pos.TOP_LEFT);

        // Buttons
        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("button");

        Button goToRegisterButton = new Button("Go to Register");
        goToRegisterButton.getStyleClass().addAll("button", "secondary");

        // Button Actions
        loginButton.setOnAction(e -> handleLogin(stage, usernameField.getText().trim(), passwordField.getText().trim()));
        goToRegisterButton.setOnAction(e -> stage.setScene(new RegisterView().getScene(stage)));

        // Layout
        VBox layout = new VBox(12,
                title,
                usernameBox,
                passwordBox,
                loginButton,
                goToRegisterButton
        );
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("vbox");

        Scene scene = new Scene(layout, 400, 430);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles/style.css")).toExternalForm());

        return scene;
    }

    private void handleLogin(Stage stage, String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill in all fields");
            return;
        }

        User user = FileUtils.getUser(username);

        if (user != null && user.getPassword().equals(password)) {
            Session.setUser(user);
            navigateToDashboard(user.getRole(), stage);
        } else {
            showAlert(Alert.AlertType.ERROR, "❌ Invalid username or password");
        }
    }

    private void navigateToDashboard(String role, Stage stage) {
        AnchorPane dashboardView = Layout.getView(role);
        Scene dashboardScene = new Scene(dashboardView, 1280, 720);
        dashboardScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles/style.css")).toExternalForm());
        stage.setScene(dashboardScene);
    }

    private void showAlert(Alert.AlertType type, String message) {
        new Alert(type, message).show();
    }

    private Label createStyledLabel(String text, String fontSize, String fontWeight) {
        Label label = new Label(text);
        label.setStyle(String.format("-fx-font-size: %s; -fx-font-weight: %s;", fontSize, fontWeight));
        return label;
    }
}
