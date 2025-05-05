package com.example.milestonemate_1.models;

import java.time.LocalDate;
import java.util.UUID;

public class Task {
    private final String id;
    private String project;
    private String title;
    private String description;
    private LocalDate deadline;
    private String assignedTo;
    private String status;
    private String attachedFilePath;
    private String feedback; // ✅ feedback field
    private String priority; // ✅ priority field

    // Constructor for creating a NEW task
    public Task(String project, String title, String description, LocalDate deadline,
                String assignedTo, String status, String attachedFilePath, String priority) {
        this.id = UUID.randomUUID().toString();
        this.project = project;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.assignedTo = assignedTo;
        this.status = status;
        this.attachedFilePath = attachedFilePath;
        this.feedback = ""; // default empty
        this.priority = priority;
    }

    // Constructor for loading EXISTING task (with ID and feedback)
    public Task(String id, String project, String title, String description, LocalDate deadline,
                String assignedTo, String status, String attachedFilePath, String feedback, String priority) {
        this.id = id;
        this.project = project;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.assignedTo = assignedTo;
        this.status = status;
        this.attachedFilePath = attachedFilePath;
        this.feedback = feedback;
        this.priority = priority;
    }

    // Serialize to file string (all fields)
    public String toFileString() {
        return String.join("|",
                id,
                project,
                title,
                description,
                deadline.toString(),
                assignedTo,
                status,
                attachedFilePath == null ? "null" : attachedFilePath,
                feedback.replace("\n", "\\n"),
                priority
        );
    }

    // Deserialize from file string (all fields)
    public static Task fromFileString(String line) {
        String[] parts = line.split("\\|", -1);  // expecting 10 parts
        if (parts.length < 10) {
            throw new IllegalArgumentException("Invalid task data: " + line);
        }

        String id = parts[0];
        String project = parts[1];
        String title = parts[2];
        String description = parts[3];
        LocalDate deadline = LocalDate.parse(parts[4]);
        String assignedTo = parts[5];
        String status = parts[6];
        String attachedFilePath = "null".equals(parts[7]) ? null : parts[7];
        String feedback = parts[8].replace("\\n", "\n");
        String priority = parts[9];

        return new Task(id, project, title, description, deadline, assignedTo, status, attachedFilePath, feedback, priority);
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
                ", priority='" + priority + '\'' +
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
    public String getPriority() { return priority; }

    public void setStatus(String newStatus) { this.status = newStatus; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    public void setPriority(String priority) { this.priority = priority; }
}
