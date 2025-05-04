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
    private final String id;  // 🔥 unique ID field

    // Constructor for creating a new task (auto-generates ID)
    public Task(String project, String title, String description, LocalDate deadline, String assignedTo, String status, String attachedFilePath) {
        this.project = project;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.assignedTo = assignedTo;
        this.status = status;
        this.attachedFilePath = attachedFilePath;
        this.id = UUID.randomUUID().toString();  // 💥 auto-generate ID
    }

    // Constructor for loading an existing task (uses existing ID)
    public Task(String id, String project, String title, String description, LocalDate deadline, String assignedTo, String status, String attachedFilePath) {
        this.id = id;
        this.project = project;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.assignedTo = assignedTo;
        this.status = status;
        this.attachedFilePath = attachedFilePath;
    }

    public String toFileString() {
        // ✅ now saving ID too (first field)
        return id + "," + project + "," + title + "," + description + "," + deadline.toString() + "," + assignedTo + "," + status + "," + attachedFilePath;
    }

    public static Task fromFileString(String line) {
        String[] parts = line.split(",", 8);  // ✅ limit 8 because now we have 8 fields (including ID)
        if (parts.length < 8) {
            throw new IllegalArgumentException("Invalid task data: " + line);
        }
        return new Task(
                parts[0],  // ID
                parts[1],  // project
                parts[2],  // title
                parts[3],  // description
                LocalDate.parse(parts[4]),
                parts[5],  // assignedTo
                parts[6],  // status
                parts[7]   // attachedFilePath
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
                '}';
    }

    // Getters
    public String getId() { return id; }
    public String getProject() { return project; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDeadline() { return deadline; }
    public String getAssignedTo() { return assignedTo; }
    public String getStatus() { return status; }
    public String getAttachedFilePath() { return attachedFilePath; }

    // ✅ Working setStatus method
    public void setStatus(String newStatus) {
        this.status = newStatus;
    }
}
