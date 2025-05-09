package com.example.milestonemate_1.views;

import com.example.milestonemate_1.models.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TaskCard extends VBox {

    public TaskCard(Task task, String userRole) {
        Label projectLabel = new Label("📁  " + task.getProject());
        projectLabel.getStyleClass().add("task-project");

        Label taskLabel = new Label("📝  " + task.getTitle());
        taskLabel.getStyleClass().add("task-title");

        Label priorityLabel = new Label("Priority :  " + task.getPriority());
        priorityLabel.getStyleClass().add("task-priority");

        String priorityClass = getPriorityCssClass(task.getPriority());
        priorityLabel.getStyleClass().add(priorityClass);

        Label deadlineLabel = new Label("⏰ Due: " + task.getDeadline());
        deadlineLabel.getStyleClass().add("task-deadline");

        this.setSpacing(5);
        this.setPadding(new Insets(10));
        this.getStyleClass().add("kanban-card");

// 💡 Always add these first
        this.getChildren().addAll(
                projectLabel,
                taskLabel
        );

// 💡 Conditionally add "Assigned to" right after taskLabel
        if ("Manager".equalsIgnoreCase(userRole) || "Team Lead".equalsIgnoreCase(userRole)) {
            Label assignedToLabel = new Label("👤 Assigned to: " + task.getAssignedTo());
            assignedToLabel.getStyleClass().add("task-assigned-to");
            this.getChildren().add(assignedToLabel);
        }

// 💡 Add the remaining labels
        this.getChildren().addAll(
                priorityLabel,
                deadlineLabel
        );

        // If user is Manager or Team Lead, show "Assigned to"
    }

    private String getPriorityCssClass(String priority) {
        return switch (priority.toLowerCase()) {
            case "high" -> "priority-high";
            case "medium" -> "priority-medium";
            case "low" -> "priority-low";
            default -> "priority-default";
        };
    }
}
