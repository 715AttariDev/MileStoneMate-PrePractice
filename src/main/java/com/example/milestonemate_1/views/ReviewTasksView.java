package com.example.milestonemate_1.views;
import com.example.milestonemate_1.FileUtils;
import com.example.milestonemate_1.models.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewTasksView implements ViewProvider {

    public AnchorPane getView() {
        AnchorPane pane = new AnchorPane();

        TableView<Task> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getStyleClass().add("task-table");

        TableColumn<Task, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        titleCol.getStyleClass().add("table-column");

        TableColumn<Task, String> projectCol = new TableColumn<>("Project");
        projectCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getProject()));
        projectCol.getStyleClass().add("table-column");

        TableColumn<Task, String> assignedCol = new TableColumn<>("Assigned To");
        assignedCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAssignedTo()));
        assignedCol.getStyleClass().add("table-column");

        TableColumn<Task, String> dueDateCol = new TableColumn<>("Due Date");
        dueDateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDeadline().format(DateTimeFormatter.ofPattern("MMM d, yyyy"))));
        dueDateCol.getStyleClass().add("table-column");

        TableColumn<Task, Void> actionCol = new TableColumn<>("Review");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button reviewBtn = new Button("🔍 Review");

            {
                reviewBtn.getStyleClass().add("review-button");
                reviewBtn.setOnAction(e -> {
                    Task task = getTableView().getItems().get(getIndex());
                    showReviewPopup(task);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(reviewBtn);
                }
            }
        });
        actionCol.getStyleClass().add("table-column");

        table.getColumns().addAll(titleCol, projectCol, assignedCol, dueDateCol, actionCol);

        List<Task> tasksToReview = FileUtils.getAllTasks().stream()
                .filter(task -> task.getStatus().equalsIgnoreCase("Completed"))
                .collect(Collectors.toList());

        table.getItems().addAll(tasksToReview);

        VBox wrapper = new VBox(10, table);
        wrapper.setPadding(new Insets(20));
        wrapper.getStyleClass().add("table-wrapper");
        AnchorPane.setTopAnchor(wrapper, 0.0);
        AnchorPane.setLeftAnchor(wrapper, 0.0);
        AnchorPane.setRightAnchor(wrapper, 0.0);
        AnchorPane.setBottomAnchor(wrapper, 0.0);

        pane.getChildren().add(wrapper);
        return pane;
    }

    private void showReviewPopup(Task task) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Review Task");

        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.getStyleClass().add("popup-card");

        TextFlow project = createStyledText("Project : ", task.getProject());
        TextFlow title = createStyledText("Task : ", task.getTitle());
        TextFlow description = createStyledText("Description : ", task.getDescription());
        TextFlow deadline = createStyledText("Due : ", task.getDeadline().format(DateTimeFormatter.ofPattern("MMM d, yyyy")));
        TextFlow assignedTo = createStyledText("Assigned To : ", task.getAssignedTo());

        VBox content = new VBox(5, project, title, description, deadline, assignedTo);
        content.getStyleClass().add("popup-content");

        if (task.getAttachedFilePath() != null && !task.getAttachedFilePath().isBlank()) {
            Hyperlink openFileLink = new Hyperlink("Open File");
            openFileLink.getStyleClass().add("file-link");
            openFileLink.setOnAction(e -> {
                File file = new File(task.getAttachedFilePath());
                if (file.exists()) {
                    try {
                        java.awt.Desktop.getDesktop().open(file);
                    } catch (Exception ex) {
                        showAlert(Alert.AlertType.ERROR, "Unable to open file: " + ex.getMessage());
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "File not found.");
                }
            });
            content.getChildren().add(openFileLink);
        }

        TextArea feedbackArea = new TextArea();
        feedbackArea.setPromptText("Enter feedback");
        feedbackArea.setWrapText(true);
        feedbackArea.setPrefRowCount(2);
        feedbackArea.getStyleClass().add("team-description");

        Button approveBtn = new Button("✅ Approve");
        Button requestChangesBtn = new Button("❌ Request Changes");
        approveBtn.getStyleClass().add("approve-button");
        requestChangesBtn.getStyleClass().add("remove-button");

        approveBtn.setOnAction(e -> {
            boolean updated = FileUtils.updateTaskStatus(task.getId(), "Approved");
            if (!feedbackArea.getText().isBlank()) {
                FileUtils.updateTaskFeedback(task.getId(), feedbackArea.getText().trim());
            }
            if (updated) {
                showAlert(Alert.AlertType.INFORMATION, "Task approved successfully!");
                popup.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to approve task.");
            }
        });

        requestChangesBtn.setOnAction(e -> {
            String feedback = feedbackArea.getText().trim();
            if (feedback.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Feedback is required when requesting changes.");
                return;
            }
            boolean updated = FileUtils.updateTaskStatus(task.getId(), "Needs Changes");
            FileUtils.updateTaskFeedback(task.getId(), feedback);
            if (updated) {
                showAlert(Alert.AlertType.INFORMATION, "Changes requested with feedback.");
                popup.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to update task.");
            }
        });

        HBox buttons = new HBox(10, approveBtn, requestChangesBtn);
        buttons.setPadding(new Insets(10, 0, 0, 0));
        buttons.getStyleClass().add("popup-buttons");

        card.getChildren().addAll(content, feedbackArea, buttons);
        Scene scene = new Scene(card, 400, 400);

        scene.getStylesheets().add(getClass().getResource("/Styles/style.css").toExternalForm());  // <-- Add this line
        popup.setScene(scene);
        popup.showAndWait();
    }

    private TextFlow createStyledText(String boldLabel, String normalText) {
        Text bold = new Text(boldLabel);
        bold.getStyleClass().add("text-bold");

        Text normal = new Text(normalText);
        normal.getStyleClass().add("text-normal");

        return new TextFlow(bold, normal);
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.showAndWait();
    }
}
