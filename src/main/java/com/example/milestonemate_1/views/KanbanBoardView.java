package com.example.milestonemate_1.views;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import com.example.milestonemate_1.models.Task;
import com.example.milestonemate_1.FileUtils;

import java.util.List;
import java.util.stream.Collectors;

public class KanbanBoardView implements ViewProvider {
    private final String role;
    private final String username;
    private List<Task> allTasks;
    private HBox kanbanColumns;

    public KanbanBoardView(String role, String username) {
        this.role = role;
        this.username = username;
        this.allTasks = FileUtils.loadAllTasks();
    }

    @Override
    public AnchorPane getView() {
        AnchorPane pane = new AnchorPane();
        pane.getStyleClass().add("kanban-root");

        kanbanColumns = new HBox(20);
        kanbanColumns.getStyleClass().add("kanban-columns");

        populateColumns();

        ScrollPane scrollPane = new ScrollPane(kanbanColumns);
        scrollPane.getStyleClass().add("kanban-scroll");
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 20.0);
        AnchorPane.setRightAnchor(scrollPane, 20.0);
        AnchorPane.setBottomAnchor(scrollPane, 20.0);

        pane.getChildren().addAll(scrollPane);
        return pane;
    }

    private void populateColumns() {
        kanbanColumns.getChildren().clear();

        if ("Manager".equals(role) || "Team Lead".equals(role) || "Team Member".equals(role)) {
            kanbanColumns.getChildren().addAll(
                    buildColumn("Pending"),
                    buildColumn("Completed"),
                    buildColumn("Approved"),
                    buildColumn("Needs Changes")
            );
        } else {
            kanbanColumns.getChildren().add(new VBox(new Label("Unknown role: " + role)));
        }

    }


    private VBox buildColumn(String title) {
        String normalizedTitle = title.replace("My ", "").trim();
        VBox column = new VBox(10);
        column.getStyleClass().add("kanban-column");
        column.getStyleClass().add(getColumnStyleClass(normalizedTitle)); // Add status-based style

        Label header = new Label(title);
        header.getStyleClass().add("column-header");
        column.getChildren().add(header);

        column.setOnDragOver(event -> {
            if (event.getGestureSource() != column && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        column.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String taskId = db.getString();
                Task draggedTask = findTaskById(taskId);

                if (draggedTask != null && isMoveAllowed(draggedTask, normalizedTitle)) {
                    draggedTask.setStatus(normalizedTitle);
                    updateTaskInList(draggedTask);
                    refreshSingleColumn(normalizedTitle);
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        List<Task> tasks = getTasksForColumn(title);

        for (Task task : tasks) {
            TaskCard card = new TaskCard(task,role);

            card.setOnDragDetected(event -> {
                Dragboard db = card.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(task.getId());
                db.setContent(content);
                event.consume();
            });

            if ("Team Member".equals(role) && "Pending".equalsIgnoreCase(task.getStatus().trim())) {
                Button completeButton = new Button("Mark Completed");
                completeButton.getStyleClass().add("create-button");
                completeButton.setOnAction(e -> {
                    task.setStatus("Completed");
                    updateTaskInList(task);
                    refreshSingleColumn("Completed");
                });
                VBox cardWithButton = new VBox(5, card, completeButton);
                column.getChildren().add(cardWithButton);
            } else {
                column.getChildren().add(card);
            }
        }

        VBox.setVgrow(column, Priority.ALWAYS);
        HBox.setHgrow(column, Priority.ALWAYS);
        return column;
    }

    private String getColumnStyleClass(String status) {
        return switch (status.toLowerCase()) {
            case "pending" -> "column-pending";
            case "completed" -> "column-completed";
            case "approved" -> "column-approved";
            case "needs changes" -> "column-needs-changes";
            default -> "column-default";
        };
    }


    private List<Task> getTasksForColumn(String columnTitle) {
        String normalizedStatus = columnTitle.trim().toLowerCase();

        if ("Team Member".equals(role)) {
            return allTasks.stream()
                    .filter(task -> username.equals(task.getAssignedTo()) &&
                            normalizedStatus.equals(task.getStatus().trim().toLowerCase()))
                    .collect(Collectors.toList());
        } else {
            return allTasks.stream()
                    .filter(task -> normalizedStatus.equals(task.getStatus().trim().toLowerCase()))
                    .collect(Collectors.toList());
        }
    }


    private Task findTaskById(String id) {
        return allTasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private boolean isMoveAllowed(Task task, String targetStatus) {
        // Add role-based or status-based rules here
        if ("Team Lead".equals(role) && "Approved".equalsIgnoreCase(targetStatus)) {
            return "Completed".equalsIgnoreCase(task.getStatus());
        }
        if ("Team Member".equals(role)) {
            return false;  // Members can’t move tasks directly between columns
        }
        return true; // Managers can do all
    }

    private void updateTaskInList(Task updatedTask) {
        for (int i = 0; i < allTasks.size(); i++) {
            if (allTasks.get(i).getId().equals(updatedTask.getId())) {
                allTasks.set(i, updatedTask);
                break;
            }
        }
        FileUtils.saveAllTasks(allTasks); // Save to file in real time
    }

    private void refreshSingleColumn(String columnTitle) {
        this.allTasks = FileUtils.loadAllTasks();
        populateColumns(); // simple approach: repopulate all for now (later can optimize further)
        kanbanColumns.requestLayout();
    }
}
