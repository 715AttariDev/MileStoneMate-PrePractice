package com.example.milestonemate_1.views;

import com.example.milestonemate_1.FileUtils;
import com.example.milestonemate_1.models.Task;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

public class MyTasksView implements ViewProvider {
    private final String loggedInUser;

    public MyTasksView(String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public AnchorPane getView() {
        AnchorPane pane = new AnchorPane();
        pane.getStyleClass().add("MyTaskPane");



        // FlowPane for task cards
        FlowPane tasksContainer = new FlowPane();
        tasksContainer.setPadding(new Insets(20));
        tasksContainer.setHgap(20);
        tasksContainer.setVgap(20);
        tasksContainer.setPrefWrapLength(900); // Adjust width as needed
        tasksContainer.getStyleClass().add("tasks-container");

        // Load and filter tasks
        List<Task> myTasks = FileUtils.getAllTasks().stream()
                .filter(task -> task.getAssignedTo().equalsIgnoreCase(loggedInUser))
                .collect(Collectors.toList());

        if (myTasks.isEmpty()) {
            Label noTasksLabel = new Label("🎉 No tasks assigned yet. Enjoy your free time!");
            noTasksLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #777;");
            tasksContainer.getChildren().add(noTasksLabel);
        } else {
            for (Task task : myTasks) {
                VBox taskCard = createTaskCard(task);
                tasksContainer.getChildren().add(taskCard);
            }
        }

        // Anchor FlowPane directly
        AnchorPane.setTopAnchor(tasksContainer, 20.0);
        AnchorPane.setLeftAnchor(tasksContainer, 20.0);
        AnchorPane.setRightAnchor(tasksContainer, 20.0);
        AnchorPane.setBottomAnchor(tasksContainer, 20.0);

        pane.getChildren().addAll(tasksContainer);
        return pane;
    }

    private VBox createTaskCard(Task task) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(15));
        card.getStyleClass().add("task-card");
        card.setMinWidth(250);
        card.setMaxWidth(250);

        Label project = new Label("Project: " + task.getProject());
        Label title = new Label("Task: " + task.getTitle());
        Label description = new Label("Description: " + task.getDescription());
        Label deadline = new Label("Due: " + task.getDeadline());
        Label status = new Label("Status: " + task.getStatus());

        List<Label> labels = List.of(project, title, description, deadline, status);
        for (Label label : labels) {
            label.getStyleClass().add("task-label");
        }

        // File hyperlinks (horizontal layout)
        HBox fileLinksBox = new HBox(10);
        fileLinksBox.setPadding(new Insets(10, 0, 0, 0));
        fileLinksBox.setVisible(false);

        if (task.getAttachedFilePath() != null && !task.getAttachedFilePath().isBlank()) {
            Hyperlink openFileLink = new Hyperlink("Open File");
            openFileLink.setOnAction(e -> openFile(task));

            Hyperlink downloadFileLink = new Hyperlink("Download File");
            downloadFileLink.setOnAction(e -> downloadFile(task));

            fileLinksBox.getChildren().addAll(openFileLink, downloadFileLink);
            fileLinksBox.setVisible(true);
        }

        Button completeBtn = new Button();
        completeBtn.getStyleClass().add("complete-btn");

        if (task.getStatus().equalsIgnoreCase("Completed")) {
            completeBtn.setText("✅ Completed");
            completeBtn.setDisable(true);
        } else {
            completeBtn.setText("✔ Mark as Completed");
            completeBtn.setOnAction(e -> {
                boolean updated = FileUtils.updateTaskStatus(task.getId(), "Completed");
                if (updated) {
                    status.setText("Status: Completed");
                    completeBtn.setText("✅ Completed");
                    completeBtn.setDisable(true);
                    showAlert(Alert.AlertType.INFORMATION, "Task marked as completed!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failed to update task status.");
                }
            });
        }

        card.getChildren().addAll(project, title, description, deadline, status, fileLinksBox, completeBtn);
        return card;
    }

    private void openFile(Task task) {
        File file = new File(task.getAttachedFilePath());
        if (file.exists()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException ex) {
                showAlert(Alert.AlertType.ERROR, "Could not open file: " + ex.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "The attached file no longer exists.");
        }
    }

    private void downloadFile(Task task) {
        File file = new File(task.getAttachedFilePath());
        if (file.exists()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
            fileChooser.setInitialFileName(file.getName());

            File destination = fileChooser.showSaveDialog(null);
            if (destination != null) {
                try {
                    Files.copy(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    showAlert(Alert.AlertType.INFORMATION, "File downloaded successfully!");
                } catch (IOException ex) {
                    showAlert(Alert.AlertType.ERROR, "Error downloading file: " + ex.getMessage());
                }
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "The attached file no longer exists.");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.showAndWait();
    }
}
