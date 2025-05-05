package com.example.milestonemate_1.views;

import com.example.milestonemate_1.FileUtils;
import com.example.milestonemate_1.models.Task;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class MyTasksView implements ViewProvider {
    private final String loggedInUser;

    public MyTasksView(String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public AnchorPane getView() {
        AnchorPane pane = new AnchorPane();
        pane.getStyleClass().add("root");

        FlowPane tasksContainer = new FlowPane();
        tasksContainer.setPadding(new Insets(20));
        tasksContainer.setHgap(20);
        tasksContainer.setVgap(20);
        tasksContainer.setPrefWrapLength(900);
        tasksContainer.getStyleClass().add("tasks-container");

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

        AnchorPane.setTopAnchor(tasksContainer, 0.0);
        AnchorPane.setLeftAnchor(tasksContainer, 20.0);
        AnchorPane.setRightAnchor(tasksContainer, 0.0);
        AnchorPane.setBottomAnchor(tasksContainer, 0.0);

        pane.getChildren().addAll(tasksContainer);
        return pane;
    }

    private VBox createTaskCard(Task task) {
        VBox card = new VBox();
        card.setPadding(new Insets(15));
        card.getStyleClass().add("task-card");
        card.setMinWidth(280);
        card.setMaxWidth(280);
        card.setPrefHeight(280); // Or however tall you want the card to be

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");

        TextFlow project = createStyledText("Project : ", task.getProject());
        TextFlow title = createStyledText("Task : ", task.getTitle());
        TextFlow description = createStyledText("Description : ", task.getDescription());
        TextFlow deadline = createStyledText("Due : ", task.getDeadline().format(formatter));
        TextFlow status = createStyledText("Status : ", task.getStatus());

        VBox contentBox = new VBox(5, project, title, description, deadline, status);

        // Add hyperlinks if file is attached
        if (task.getAttachedFilePath() != null && !task.getAttachedFilePath().isBlank()) {
            Hyperlink openFileLink = new Hyperlink("Open File");
            openFileLink.setOnAction(e -> openFile(task));

            Hyperlink downloadFileLink = new Hyperlink("Download File");
            downloadFileLink.setOnAction(e -> downloadFile(task));

            HBox fileLinksBox = new HBox(10, openFileLink, downloadFileLink);
            fileLinksBox.setPadding(new Insets(2, 0, 0, 0));
            contentBox.getChildren().add(fileLinksBox);
        }

        VBox.setVgrow(contentBox, Priority.ALWAYS); // Take up available space

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
                    status.getChildren().set(1, new Text("Completed"));
                    completeBtn.setText("✅ Completed");
                    completeBtn.setDisable(true);
                    showAlert(Alert.AlertType.INFORMATION, "Task marked as completed!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failed to update task status.");
                }
            });
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS); // Push button to bottom

        card.getChildren().addAll(contentBox, spacer, completeBtn);
        return card;
    }


    private TextFlow createStyledText(String boldLabel, String normalText) {
        Text bold = new Text(boldLabel);
        bold.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 17));

        Text normal = new Text(normalText);
        normal.setFont(Font.font("Arial", FontWeight.LIGHT,FontPosture.ITALIC, 15));

        return new TextFlow(bold, normal);
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
