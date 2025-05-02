package com.example.milestonemate_1.views;

import com.example.milestonemate_1.FileUtils;
import com.example.milestonemate_1.models.Project;
import com.example.milestonemate_1.models.Team;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;

public class CreateProjectView implements ViewProvider {

    @Override
    public AnchorPane getView() {
        AnchorPane pane = new AnchorPane();
        pane.getStyleClass().add("main-pane");

        // ====== UI Components ======
        Label titleLabel = new Label("Create Project");
        titleLabel.getStyleClass().add("form-title");

        TextField projectNameField = new TextField();
        projectNameField.setPromptText("Enter Project Name");
        projectNameField.setPrefWidth(300);
        projectNameField.getStyleClass().add("team-names");

        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Enter Project Description");
        descriptionField.setPrefWidth(300);
        descriptionField.setPrefRowCount(1);
        descriptionField.getStyleClass().add("team-description");

        ComboBox<String> teamComboBox = new ComboBox<>();
        teamComboBox.setPromptText("Select Team");
        teamComboBox.getStyleClass().add("team-lead");

        DatePicker dueDatePicker = new DatePicker();
        dueDatePicker.setPromptText("Select Due Date");
        dueDatePicker.getStyleClass().add("due-date");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        // ====== Load Teams ======
        populateTeams(teamComboBox);

        // ====== Create Button ======
        Button createBtn = new Button("Create Project");
        createBtn.getStyleClass().add("create-button");

        createBtn.setOnAction(e -> {
            errorLabel.setText("");
            handleCreateProject(
                    projectNameField,
                    descriptionField,
                    teamComboBox,
                    dueDatePicker,
                    errorLabel
            );
        });

        VBox layout = new VBox(15,
                titleLabel,
                projectNameField,
                descriptionField,
                teamComboBox,
                dueDatePicker,
                errorLabel,
                createBtn
        );

        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_LEFT);
        layout.setFillWidth(true);
        AnchorPane.setTopAnchor(layout, 20.0);
        AnchorPane.setLeftAnchor(layout, 20.0);

        pane.getChildren().add(layout);

        return pane;
    }

    private void populateTeams(ComboBox<String> teamComboBox) {
        List<Team> allTeams = FileUtils.getAllTeams();
        if (allTeams.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "No teams found. Please create a team first.").show();
            return;
        }

        for (Team team : allTeams) {
            teamComboBox.getItems().add(team.getTeamName());
        }
    }

    private void handleCreateProject(
            TextField projectNameField,
            TextArea descriptionField,
            ComboBox<String> teamComboBox,
            DatePicker dueDatePicker,
            Label errorLabel
    ) {
        String projectName = projectNameField.getText().trim();
        String description = descriptionField.getText().trim();
        String selectedTeam = teamComboBox.getValue();
        LocalDate dueDate = dueDatePicker.getValue();

        if (projectName.isEmpty()) {
            errorLabel.setText("Project name is required!");
            return;
        }
        if (description.isEmpty()) {
            errorLabel.setText("Project description is required!");
            return;
        }
        if (selectedTeam == null || selectedTeam.isEmpty()) {
            errorLabel.setText("Please select a team!");
            return;
        }
        if (dueDate == null) {
            errorLabel.setText("Please select a due date!");
            return;
        }

        // Check duplicate project name
        if (FileUtils.isProjectNameExists(projectName)) {
            errorLabel.setText("Project name already exists! Please choose another.");
            return;
        }

        // Save the project
        Project newProject = new Project(projectName, description, selectedTeam, dueDate);
        boolean saved = FileUtils.saveProject(newProject);

        if (saved) {
            errorLabel.setStyle("-fx-text-fill: green;");
            errorLabel.setText("Project created successfully!");

            // Reset fields
            projectNameField.clear();
            descriptionField.clear();
            teamComboBox.getSelectionModel().clearSelection();
            dueDatePicker.setValue(null);
        } else {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Failed to save project.");
        }
    }
}
