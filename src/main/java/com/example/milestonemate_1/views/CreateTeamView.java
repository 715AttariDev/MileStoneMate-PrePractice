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
import java.util.ArrayList;
import java.util.List;

public class CreateTeamView implements ViewProvider {

    @Override
    public AnchorPane getView() {
        AnchorPane pane = new AnchorPane();
        pane.getStyleClass().add("main-pane");

        // ====== UI Components ======
        Label titleLabel = new Label("Create Team");
        titleLabel.getStyleClass().add("form-title");

        TextField teamNameField = new TextField();
        teamNameField.setPromptText("Enter Team Name");
        teamNameField.setPrefWidth(300);
        teamNameField.getStyleClass().add("team-names");

        ComboBox<String> teamLeadCombo = new ComboBox<>();
        teamLeadCombo.setPromptText("Select Team Lead");
        teamLeadCombo.getStyleClass().add("team-lead");

        Label membersLabel = new Label("Select Members:");
        membersLabel.getStyleClass().add("section-title");

        VBox memberListContainer = new VBox(5);
        memberListContainer.setPadding(new Insets(10));
        memberListContainer.getStyleClass().add("member-list-container");

        ScrollPane scrollPane = new ScrollPane(memberListContainer);
        scrollPane.setFitToWidth(true); // Fill horizontally
        scrollPane.setPrefHeight(200); //
        scrollPane.setMaxHeight(200);
        scrollPane.getStyleClass().add("member-scroll");

// ====== Load Users ======
        populateUserLists(teamLeadCombo, memberListContainer);
        // ====== Create Button ======
        Button createBtn = new Button("Create Team");
        createBtn.getStyleClass().add("create-button");
        createBtn.setOnAction(e -> handleCreateTeam(
                teamNameField, teamLeadCombo, memberListContainer
        ));
        VBox layout = new VBox(15, titleLabel, teamNameField, teamLeadCombo, membersLabel, scrollPane, createBtn);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_LEFT);

        pane.getChildren().add(layout);
        AnchorPane.setTopAnchor(layout, 20.0);
        AnchorPane.setLeftAnchor(layout, 20.0);

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

    private void handleCreateTeam(TextField teamNameField, ComboBox<String> teamLeadCombo, VBox memberListContainer) {
        String teamName = teamNameField.getText().trim();
        String selectedLead = teamLeadCombo.getValue();
        List<String> selectedMembers = new ArrayList<>();

        for (Node node : memberListContainer.getChildren()) {
            if (node instanceof CheckBox checkBox && checkBox.isSelected()) {
                selectedMembers.add(checkBox.getText());
            }
        }

        if (teamName.isEmpty() || selectedLead == null || selectedLead.isEmpty() || selectedMembers.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields and select members!").show();
            return;
        }

        // Check duplicate team name
        if (FileUtils.isTeamNameExists(teamName)) {
            new Alert(Alert.AlertType.ERROR, "Team name already exists! Please choose another.").show();
            return;
        }

        // Save the team
        Team newTeam = new Team(teamName, selectedLead, selectedMembers);
        boolean saved = FileUtils.saveTeam(newTeam);
        if (saved) {
            new Alert(Alert.AlertType.INFORMATION, "Team created successfully!").show();
            teamNameField.clear();
            teamLeadCombo.getSelectionModel().clearSelection();

            // Clear the CheckBoxes
            for (Node node : memberListContainer.getChildren()) {
                if (node instanceof CheckBox checkBox) {
                    checkBox.setSelected(false);
                }
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to save team.").show();
        }
    }
}