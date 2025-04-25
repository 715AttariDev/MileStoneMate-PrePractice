package com.example.milestonemate_1.views;

import com.example.milestonemate_1.FileUtils;
import com.example.milestonemate_1.Layout;
import com.example.milestonemate_1.User;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterView {
    public Scene getScene(Stage stage) {
        Label title = new Label("📝 Create a New Account");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Choose a username");

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Create a password");

        Label roleLabel = new Label("Select Role:");
        ToggleGroup roleGroup = new ToggleGroup();
        RadioButton manager = new RadioButton("Manager");
        RadioButton lead = new RadioButton("Team Lead");
        RadioButton member = new RadioButton("Team Member");

        manager.setToggleGroup(roleGroup);
        lead.setToggleGroup(roleGroup);
        member.setToggleGroup(roleGroup);

        // Buttons
        Button registerButton = new Button("Register");
        Button backToLoginButton = new Button("Back to Login");

        // Register Logic
        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            RadioButton selectedRole = (RadioButton) roleGroup.getSelectedToggle();

            if (username.isEmpty() || password.isEmpty() || selectedRole == null) {
                new Alert(Alert.AlertType.ERROR, "All fields are required!").show();
                return;
            }

            String role = selectedRole.getText();

            // Create a new User object
            User newUser = new User(username, password, role);
            FileUtils.saveUser(newUser); // Save user to file

            new Alert(Alert.AlertType.INFORMATION, "🎉 User registered successfully! Welcome, " + username + "!").show();

            // 🎯 Navigate to dynamic dashboard layout
            LoginView loginView = new LoginView();
            stage.setScene(loginView.getScene(stage));
        });


        backToLoginButton.setOnAction(e -> {
            LoginView loginView = new LoginView();
            stage.setScene(loginView.getScene(stage));
        });

        VBox layout = new VBox(10, title,
                usernameLabel, usernameField,
                passwordLabel, passwordField,
                roleLabel, manager, lead, member,
                registerButton, backToLoginButton);
        layout.setPadding(new Insets(20));

        return new Scene(layout, 400, 450);
    }
}
