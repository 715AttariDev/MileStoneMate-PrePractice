package com.example.milestonemate_1.views;

import com.example.milestonemate_1.FileUtils;
import com.example.milestonemate_1.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class CreateTeamView implements ViewProvider {

    public AnchorPane getView() {
        AnchorPane pane = new AnchorPane();
        pane.getStyleClass().add("main-pane");

        // TEAM NAME
        TextField teamNameField = new TextField();
        teamNameField.setPromptText("Enter Team Name");
        teamNameField.setPrefWidth(300);

        // TEAM LEAD (ComboBox)
        ComboBox<String> teamLeadCombo = new ComboBox<>();
        teamLeadCombo.setPromptText("Select Team Lead");

        // TEAM MEMBERS (CheckBox List)
        VBox memberListContainer = new VBox(5);
        memberListContainer.setPadding(new Insets(10));
        memberListContainer.getStyleClass().add("member-list-container");

        // Load Users from user.txt and populate
        List<User> allUsers = FileUtils.getAllUsers();
        for (User user : allUsers) {
            if (user.getRole().equals("Team Lead")) {
                teamLeadCombo.getItems().add(user.getUsername());
            } else if (user.getRole().equals("Team Member")) {
                CheckBox checkBox = new CheckBox(user.getUsername());
                memberListContainer.getChildren().add(checkBox);
            }
        }

        // CREATE BUTTON
        Button createBtn = new Button("Create Team");
        createBtn.setOnAction(e -> {
            String teamName = teamNameField.getText().trim();
            String selectedLead = teamLeadCombo.getValue();
            List<String> selectedMembers = new ArrayList<>();

            for (javafx.scene.Node node : memberListContainer.getChildren()) {
                if (node instanceof CheckBox checkBox && checkBox.isSelected()) {
                    selectedMembers.add(checkBox.getText());
                }
            }

            if (teamName.isEmpty() || selectedLead == null || selectedMembers.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please fill all fields and select members!").show();
            } else {
                // TODO: Save to team.txt
                System.out.println("Team Created: " + teamName + " | Lead: " + selectedLead + " | Members: " + selectedMembers);
            }
        });

        VBox layout = new VBox(15, new Label("Create Team"), teamNameField, teamLeadCombo, new Label("Select Members:"), memberListContainer, createBtn);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_LEFT);

        pane.getChildren().add(layout);
        AnchorPane.setTopAnchor(layout, 20.0);
        AnchorPane.setLeftAnchor(layout, 20.0);

        return pane;
    }
}
