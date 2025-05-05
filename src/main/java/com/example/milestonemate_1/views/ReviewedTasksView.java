package com.example.milestonemate_1.views;

import com.example.milestonemate_1.FileUtils;
import com.example.milestonemate_1.models.Task;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewedTasksView implements ViewProvider {
    private final String loggedInUser;

    // Constructor to pass the logged-in user
    public ReviewedTasksView(String loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
    @Override
    public Parent getView() {
        AnchorPane root = new AnchorPane();
        // Load tasks
        List<Task> allTasks = FileUtils.loadAllTasks();
        // Filter tasks by status and assigned user
        List<Task> approvedTasks = allTasks.stream()
                .filter(t -> "Approved".equalsIgnoreCase(t.getStatus()) && t.getAssignedTo().equalsIgnoreCase(loggedInUser))
                .collect(Collectors.toList());

        List<Task> needChangesTasks = allTasks.stream()
                .filter(t -> "Needs Changes".equalsIgnoreCase(t.getStatus()) && t.getAssignedTo().equalsIgnoreCase(loggedInUser))
                .collect(Collectors.toList());


        // Create tables
        TableView<Task> approvedTable = createTaskTable("Approved Tasks", approvedTasks);
        TableView<Task> changesTable = createTaskTable("Needs Changes", needChangesTasks);

//        approvedTable.setPrefWidth(1100);  // Wider since now stacked vertically
//        changesTable.setPrefWidth(1100);

        Label approvedLabel = new Label("Approved Tasks");
        approvedLabel.getStyleClass().add("section-title");

        Label needChanges = new Label("Needs Enhancement or Adjustment");
        needChanges.getStyleClass().add("section-title");

        VBox approvedSection = new VBox(5, approvedLabel, approvedTable);
        VBox changesSection = new VBox(5, needChanges, changesTable);

        approvedSection.getStyleClass().add("task-section");
        changesSection.getStyleClass().add("task-section");

        approvedSection.setAlignment(Pos.TOP_LEFT);
        changesSection.setAlignment(Pos.TOP_LEFT);

        VBox tableColumn = new VBox(20, approvedSection, changesSection);  // stacked vertically
        tableColumn.setAlignment(Pos.TOP_CENTER);
        tableColumn.setPadding(new Insets(10));
        tableColumn.getStyleClass().add("table-column");

        AnchorPane.setTopAnchor(tableColumn, 0.0);
        AnchorPane.setLeftAnchor(tableColumn, 20.0);
        AnchorPane.setRightAnchor(tableColumn, 40.0);
        AnchorPane.setBottomAnchor(tableColumn, 0.0);

        root.getChildren().add(tableColumn);
        return root;
    }

    private TableView<Task> createTaskTable(String title, List<Task> tasks) {
        TableView<Task> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getStyleClass().add("task-table");


        TableColumn<Task, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        titleCol.getStyleClass().add("table-column");

        TableColumn<Task, String> projectCol = new TableColumn<>("Project");
        projectCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProject()));
        projectCol.getStyleClass().add("table-column");

        TableColumn<Task, String> deadlineCol = new TableColumn<>("Deadline");
        deadlineCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDeadline().format(DateTimeFormatter.ofPattern("MMM d, yyyy"))));
        deadlineCol.getStyleClass().add("table-column");

        TableColumn<Task, String> feedbackCol = new TableColumn<>("Feedback");
        feedbackCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFeedback()));
        feedbackCol.getStyleClass().add("table-column");

        // Wrapped feedback cell
        feedbackCol.setCellFactory(column -> new TableCell<Task, String>() {
            private final Text feedbackText = new Text();

            {
                feedbackText.wrappingWidthProperty().bind(column.widthProperty().subtract(10));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    feedbackText.setText(item);

                    // Ensure row height adapts
                    feedbackText.applyCss();
                    double height = feedbackText.getLayoutBounds().getHeight() + 50; // padding
                    if (getTableRow() != null && getTableRow().getPrefHeight() < height) {
                        getTableRow().setPrefHeight(height);
                    }

                    setGraphic(feedbackText);
                }
            }
        });

        table.getColumns().addAll(titleCol, projectCol, deadlineCol, feedbackCol);
        table.getItems().addAll(tasks);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }
}
