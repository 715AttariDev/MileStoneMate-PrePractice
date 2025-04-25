package com.example.milestonemate_1.views;

import com.example.milestonemate_1.FileUtils;
import com.example.milestonemate_1.Layout;
import com.example.milestonemate_1.Session;
import com.example.milestonemate_1.User;
import javafx.geometry.Insets;
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
        // UI Elements
        Label title = createStyledLabel("🔐 Login to Your Account", "18px", "bold");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        Button loginButton = new Button("Login");
        Button goToRegisterButton = new Button("Go to Register");

        // Actions
        goToRegisterButton.setOnAction(e -> {
            stage.setScene(new RegisterView().getScene(stage));
        });

        loginButton.setOnAction(e -> handleLogin(stage, usernameField.getText().trim(), passwordField.getText().trim()));

        VBox layout = new VBox(10, title,
                new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                loginButton, goToRegisterButton
        );
        layout.setPadding(new Insets(20));

        return new Scene(layout, 400, 300);
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
