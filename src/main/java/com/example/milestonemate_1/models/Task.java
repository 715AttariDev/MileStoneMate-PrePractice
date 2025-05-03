package com.example.milestonemate_1.models;

import java.time.LocalDate;

public class Task {
    private String project;
    private String title;
    private String description;
    private LocalDate deadline;
    private String assignedTo;
    private String status;
    private String attachedFilePath;  // 🔄 fixed typo here

    public Task(String project, String title, String description, LocalDate deadline, String assignedTo, String status, String attachedFilePath) {
        this.project = project;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.assignedTo = assignedTo;
        this.status = status;
        this.attachedFilePath = attachedFilePath;
    }

    public String toFileString() {
        // Save LocalDate as String (ISO format) + add file path (escape commas if needed)
        return project + "," + title + "," + description + "," + deadline.toString() + "," + assignedTo + "," + status + "," + attachedFilePath;
    }

    public static Task fromFileString(String line) {
        String[] parts = line.split(",", 7);  // ✅ limit = 7 to avoid breaking description with commas
        if (parts.length < 7) {
            throw new IllegalArgumentException("Invalid task data: " + line);
        }
        return new Task(
                parts[0],
                parts[1],
                parts[2],
                LocalDate.parse(parts[3]),
                parts[4],
                parts[5],
                parts[6]
        );
    }

    // Getters
    public String getProject() { return project; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDeadline() { return deadline; }
    public String getAssignedTo() { return assignedTo; }
    public String getStatus() { return status; }
    public String getAttachedFilePath() { return attachedFilePath; }  // ✅ added this getter
}
