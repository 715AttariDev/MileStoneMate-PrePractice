package com.example.milestonemate_1.views;

import com.example.milestonemate_1.FileUtils;
import com.example.milestonemate_1.User;
import com.example.milestonemate_1.models.Team;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreateTeamView implements ViewProvider {

    @Override
    public AnchorPane getView() {
        AnchorPane pane = new AnchorPane();
        pane.getStyleClass().add("main-pane");

        // Adjust the width to accommodate sidebar (215px) and navbar (85px)
//        pane.setPrefWidth(1080);
//        pane.setPrefHeight(400);
//        pane.setMaxWidth(1080);
//        pane.setMaxHeight(400);

        // ====== UI Components ======
        Label titleLabel = new Label("Create Team");
        titleLabel.getStyleClass().add("form-title");

        TextField teamNameField = new TextField();
        teamNameField.setPromptText("Enter Team Name");
        teamNameField.setPrefWidth(300);  // You can adjust this if needed
        teamNameField.getStyleClass().add("team-names");

        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Enter Team Description");
        descriptionField.setPrefWidth(300);
        descriptionField.setPrefRowCount(1);
        descriptionField.getStyleClass().add("team-description");

        ComboBox<String> teamLeadCombo = new ComboBox<>();
        teamLeadCombo.setPromptText("Select Team Lead");
        teamLeadCombo.getStyleClass().add("team-lead");

        Label membersLabel = new Label("Select Members:");
        membersLabel.getStyleClass().add("section-title");

        VBox memberListContainer = new VBox(5);
        memberListContainer.setPadding(new Insets(10));
        memberListContainer.getStyleClass().add("member-list-container");

        ScrollPane scrollPane = new ScrollPane(memberListContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);
        scrollPane.setMaxHeight(200);
        scrollPane.getStyleClass().add("member-scroll");

        // ====== Inline Error Labels ======
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Label createdDateLabel = new Label(); // Will display the created date
        createdDateLabel.setStyle("-fx-text-fill: green;");

        // ====== Load Users ======
        populateUserLists(teamLeadCombo, memberListContainer);

        // ====== Create Button Only ======
        Button createBtn = new Button("Create Team");
        createBtn.getStyleClass().add("create-button");

        // ====== Button Action ======
        createBtn.setOnAction(e -> {
            errorLabel.setText("");
            createdDateLabel.setText("");
            handleCreateTeam(
                    teamNameField, descriptionField, teamLeadCombo,
                    memberListContainer, errorLabel, createdDateLabel
            );
        });

        VBox layout = new VBox(15,
                titleLabel,
                teamNameField,
                descriptionField,
                teamLeadCombo,
                membersLabel,
                scrollPane,
                errorLabel,
                createBtn,
                createdDateLabel
        );

        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_LEFT);
        layout.setFillWidth(true);
        AnchorPane.setTopAnchor(layout, 20.0);
        AnchorPane.setLeftAnchor(layout, 20.0);

        pane.getChildren().add(layout);


        return pane;
    }

    private void populateUserLists(ComboBox<String> teamLeadCombo, VBox memberListContainer) {
        List<User> allUsers = FileUtils.getAllUsers();
        if (allUsers.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "No users found to create a team. Please add users first.").show();
            return;
        }

        for (User user : allUsers) {
            if ("Team Lead".equals(user.getRole())) {
                teamLeadCombo.getItems().add(user.getUsername());
            } else if ("Team Member".equals(user.getRole())) {
                CheckBox checkBox = new CheckBox(user.getUsername());
                memberListContainer.getChildren().add(checkBox);
            }
        }
    }

    private void handleCreateTeam(
            TextField teamNameField,
            TextArea descriptionField,
            ComboBox<String> teamLeadCombo,
            VBox memberListContainer,
            Label errorLabel,
            Label createdDateLabel
    ) {
        String teamName = teamNameField.getText().trim();
        String description = descriptionField.getText().trim();
        String selectedLead = teamLeadCombo.getValue();
        List<String> selectedMembers = new ArrayList<>();

        for (Node node : memberListContainer.getChildren()) {
            if (node instanceof CheckBox checkBox && checkBox.isSelected()) {
                selectedMembers.add(checkBox.getText());
            }
        }

        if (teamName.isEmpty()) {
            errorLabel.setText("Team name is required!");
            return;
        }
        if (description.isEmpty()) {
            errorLabel.setText("Team description is required!");
            return;
        }
        if (selectedLead == null || selectedLead.isEmpty()) {
            errorLabel.setText("Please select a team lead!");
            return;
        }
        if (selectedMembers.isEmpty()) {
            errorLabel.setText("Please select at least one team member!");
            return;
        }

        // Check duplicate team name
        if (FileUtils.isTeamNameExists(teamName)) {
            errorLabel.setText("Team name already exists! Please choose another.");
            return;
        }

        // Save the team
        LocalDate createdDate = LocalDate.now();
        Team newTeam = new Team(teamName, selectedLead, selectedMembers, description);

        boolean saved = FileUtils.saveTeam(newTeam);
        if (saved) {
            errorLabel.setStyle("-fx-text-fill: green;");
            errorLabel.setText("Team created successfully!");
            createdDateLabel.setText("Created on: " + createdDate);

            // Reset fields
            teamNameField.clear();
            descriptionField.clear();
            teamLeadCombo.getSelectionModel().clearSelection();
            for (Node node : memberListContainer.getChildren()) {
                if (node instanceof CheckBox checkBox) {
                    checkBox.setSelected(false);
                }
            }
        } else {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Failed to save team.");
        }
    }
}
