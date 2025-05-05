package com.example.milestonemate_1.models;

import java.time.LocalDate;
import java.util.UUID;

public class Task {
    private String project;
    private String title;
    private String description;
    private LocalDate deadline;
    private String assignedTo;
    private String status;
    private String attachedFilePath;
    private String feedback; // ✅ New field for feedback
    private final String id;

    // Constructor for new tasks
    public Task(String project, String title, String description, LocalDate deadline, String assignedTo, String status, String attachedFilePath) {
        this.project = project;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.assignedTo = assignedTo;
        this.status = status;
        this.attachedFilePath = attachedFilePath;
        this.feedback = ""; // default empty
        this.id = UUID.randomUUID().toString();
    }

    // Constructor for loading existing tasks (includes feedback)
    public Task(String id, String project, String title, String description, LocalDate deadline, String assignedTo, String status, String attachedFilePath, String feedback) {
        this.id = id;
        this.project = project;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.assignedTo = assignedTo;
        this.status = status;
        this.attachedFilePath = attachedFilePath;
        this.feedback = feedback;
    }

    public String toFileString() {
        return id + "," + project + "," + title + "," + description + "," +
                deadline.toString() + "," + assignedTo + "," + status + "," +
                attachedFilePath + "," + feedback.replace("\n", "\\n");  // preserve newlines
    }

    public static Task fromFileString(String line) {
        String[] parts = line.split(",", 9);  // Now expecting 9 parts
        if (parts.length < 9) {
            throw new IllegalArgumentException("Invalid task data: " + line);
        }
        return new Task(
                parts[0],  // id
                parts[1],  // project
                parts[2],  // title
                parts[3],  // description
                LocalDate.parse(parts[4]),
                parts[5],  // assignedTo
                parts[6],  // status
                parts[7],  // attachedFilePath
                parts[8].replace("\\n", "\n")  // restore newlines in feedback
        );
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", project='" + project + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", assignedTo='" + assignedTo + '\'' +
                ", status='" + status + '\'' +
                ", attachedFilePath='" + attachedFilePath + '\'' +
                ", feedback='" + feedback + '\'' +
                '}';
    }

    // Getters and setters
    public String getId() { return id; }
    public String getProject() { return project; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDeadline() { return deadline; }
    public String getAssignedTo() { return assignedTo; }
    public String getStatus() { return status; }
    public String getAttachedFilePath() { return attachedFilePath; }
    public String getFeedback() { return feedback; }

    public void setStatus(String newStatus) {
        this.status = newStatus;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
