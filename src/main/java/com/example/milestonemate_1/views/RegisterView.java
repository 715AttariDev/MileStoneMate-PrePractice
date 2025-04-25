package com.example.milestonemate_1.views;

import com.example.milestonemate_1.FileUtils;
import com.example.milestonemate_1.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterView {
    public Scene getScene(Stage stage) {
        // Title
        Label title = new Label("📝 Create a New Account");
        title.getStyleClass().addAll("label", "large");

        // Username Section
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Choose a username");
        usernameField.getStyleClass().add("text-field");

        VBox usernameBox = new VBox(5, usernameLabel, usernameField);
        usernameBox.setAlignment(Pos.TOP_LEFT);

        // Password Section
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Create a password");
        passwordField.getStyleClass().add("password-field");

        VBox passwordBox = new VBox(5, passwordLabel, passwordField);
        passwordBox.setAlignment(Pos.TOP_LEFT);

        // Role Section
        Label roleLabel = new Label("Select Role:");
        ToggleGroup roleGroup = new ToggleGroup();
        RadioButton manager = new RadioButton("Manager");
        RadioButton lead = new RadioButton("Team Lead");
        RadioButton member = new RadioButton("Team Member");

        manager.setToggleGroup(roleGroup);
        lead.setToggleGroup(roleGroup);
        member.setToggleGroup(roleGroup);

        VBox roleBox = new VBox(5, manager, lead, member);
        roleBox.setAlignment(Pos.TOP_LEFT);

        VBox roleSection = new VBox(5, roleLabel, roleBox);
        roleSection.setAlignment(Pos.TOP_LEFT);

        // Buttons
        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("button");

        Button backToLoginButton = new Button("Back to Login");
        backToLoginButton.getStyleClass().addAll("button", "secondary");

        // Button Actions
        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            RadioButton selectedRole = (RadioButton) roleGroup.getSelectedToggle();

            if (username.isEmpty() || password.isEmpty() || selectedRole == null) {
                new Alert(Alert.AlertType.ERROR, "All fields are required!").show();
                return;
            }

            String role = selectedRole.getText();
            User newUser = new User(username, password, role);
            FileUtils.saveUser(newUser);

            new Alert(Alert.AlertType.INFORMATION, "🎉 User registered successfully! Welcome, " + username + "!").show();
            stage.setScene(new LoginView().getScene(stage));
        });

        backToLoginButton.setOnAction(e -> stage.setScene(new LoginView().getScene(stage)));

        // Main Layout
        VBox layout = new VBox(12,
                title,
                usernameBox,
                passwordBox,
                roleSection,
                registerButton,
                backToLoginButton
        );
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.getStyleClass().add("vbox");

        Scene scene = new Scene(layout, 400, 600);
        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

        return scene;
    }
}
