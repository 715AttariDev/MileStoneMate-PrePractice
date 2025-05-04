package com.example.milestonemate_1.views;

import com.example.milestonemate_1.FileUtils;
import com.example.milestonemate_1.models.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class CreateTaskView implements ViewProvider {

    @Override
    public Parent getView() {
        AnchorPane pane = new AnchorPane();
        pane.getStyleClass().add("main-pane");
        // ====== UI Components ======
        Label titleLabel = new Label("Create New Task");
        titleLabel.getStyleClass().add("form-title");

        Label projectLabel = new Label("Select Project:");
        projectLabel.getStyleClass().add("team-label");

        ComboBox<String> projectComboBox = new ComboBox<>();
        projectComboBox.setPromptText("Choose a project");
        projectComboBox.getStyleClass().add("team-lead");

        List<String> projectNames = FileUtils.getAllProjectNames();
        projectComboBox.getItems().addAll(projectNames);

        Label nameLabel = new Label("Task Name:");
        nameLabel.getStyleClass().add("team-label");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter task name");
        nameField.getStyleClass().add("team-names");

        Label descLabel = new Label("Description:");
        descLabel.getStyleClass().add("team-label");

        TextArea descArea = new TextArea();
        descArea.setPromptText("Enter task description");
        descArea.setPrefRowCount(2);
        descArea.getStyleClass().add("team-description");

        Label deadlineLabel = new Label("Deadline:");
        deadlineLabel.getStyleClass().add("team-label");

        DatePicker deadlinePicker = new DatePicker();
        deadlinePicker.getStyleClass().add("team-lead");

        Label assignLabel = new Label("Assign To:");
        assignLabel.getStyleClass().add("team-label");

        ComboBox<String> assignComboBox = new ComboBox<>();
        assignComboBox.setPromptText("Select team member");
        assignComboBox.getStyleClass().add("team-lead");

        assignComboBox.setDisable(true);

        projectComboBox.setOnAction(event -> {
            String selectedProject = projectComboBox.getValue();
            if (selectedProject != null) {
                assignComboBox.setDisable(false);
                assignComboBox.getItems().clear();
                List<String> teamMembers = FileUtils.getTeamMembersForProject(selectedProject);
                assignComboBox.getItems().addAll(teamMembers);
            }
        });


        // ===== File Attachment =====
        Label attachLabel = new Label("Attach File (optional):");
        attachLabel.getStyleClass().add("team-label");

        Button attachButton = new Button("Choose File");
        attachButton.getStyleClass().add("create-button");

        Label fileNameLabel = new Label("No file selected");
        fileNameLabel.setStyle("-fx-font-style: italic;");

        final File[] selectedFile = {null};

        attachButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a File to Attach");
            File file = fileChooser.showOpenDialog(pane.getScene().getWindow());
            if (file != null) {
                selectedFile[0] = file;
                fileNameLabel.setText(file.getName());
            }
        });

        // ====== Error Label ======
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        // ====== Submit Button ======
        Button submitButton = new Button("Create Task");
        submitButton.getStyleClass().add("create-button");

        submitButton.setOnAction(e -> {
            String selectedProject = projectComboBox.getValue();
            String taskName = nameField.getText().trim();
            String description = descArea.getText().trim();
            LocalDate deadline = deadlinePicker.getValue();
            String assignee = assignComboBox.getValue();

            if (selectedProject == null || taskName.isEmpty() || description.isEmpty() || deadline == null || assignee == null) {
                errorLabel.setText("Please fill in all fields.");
                errorLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            String filePath = null;
            if (selectedFile[0] != null) {
                // save the file to attached_files folder
                filePath = FileUtils.copyFileToTaskFolder(selectedFile[0], taskName);
                if (filePath == null) {
                    errorLabel.setStyle("-fx-text-fill: red;");
                    errorLabel.setText("Failed to attach file.");
                    return;
                }
            }

            // updated Task with filePath
            Task task = new Task(selectedProject, taskName, description, deadline, assignee, "Pending", filePath);
            boolean success = FileUtils.saveTask(task);

            if (success) {
                errorLabel.setStyle("-fx-text-fill: green;");
                errorLabel.setText("Task created successfully!");

                // Clear form
                projectComboBox.getSelectionModel().clearSelection();
                nameField.clear();
                descArea.clear();
                deadlinePicker.setValue(null);
                assignComboBox.getSelectionModel().clearSelection();
                fileNameLabel.setText("No file selected");
                selectedFile[0] = null;
            } else {
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setText("Failed to create task. Please try again.");
            }
        });

        // ====== Layout ======
        VBox layout = new VBox(5,
                titleLabel,
                projectLabel, projectComboBox,
                nameLabel, nameField,
                descLabel, descArea,
                deadlineLabel, deadlinePicker,
                assignLabel, assignComboBox,
                attachLabel, attachButton, fileNameLabel,
                errorLabel,
                submitButton
        );

        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_LEFT);
        layout.setFillWidth(true);

        AnchorPane.setTopAnchor(layout, 20.0);
        AnchorPane.setLeftAnchor(layout, 20.0);
        pane.getChildren().add(layout);

        return pane;
    }
}
