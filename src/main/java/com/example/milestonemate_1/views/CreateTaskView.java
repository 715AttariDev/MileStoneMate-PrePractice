package com.example.milestonemate_1.views;

import com.example.milestonemate_1.FileUtils;
import com.example.milestonemate_1.User;
import com.example.milestonemate_1.models.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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
        descArea.setPrefRowCount(4);
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

        List<User> users = FileUtils.getAllUsers();
        for (User user : users) {
            if ("Team Member".equals(user.getRole())) {
                assignComboBox.getItems().add(user.getUsername());
            }
        }

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

            Task task = new Task(selectedProject, taskName, description, deadline, assignee, "Pending");
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
