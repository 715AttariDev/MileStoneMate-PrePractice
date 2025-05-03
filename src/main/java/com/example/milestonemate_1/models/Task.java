package com.example.milestonemate_1.models;

import java.time.LocalDate;

public class Task {
    private String project;
    private String title;
    private String description;
    private LocalDate deadline;  // ⬅️ Now it's a LocalDate
    private String assignedTo;
    private String status;

    public Task(String project, String title, String description, LocalDate deadline, String assignedTo, String status) {
        this.project = project;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.assignedTo = assignedTo;
        this.status = status;
    }

    public String toFileString() {
        // Save LocalDate as String (ISO format)
        return project + "," + title + "," + description + "," + deadline.toString() + "," + assignedTo + "," + status;
    }

    public static Task fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length < 6) {
            throw new IllegalArgumentException("Invalid task data: " + line);
        }
        return new Task(
                parts[0],
                parts[1],
                parts[2],
                LocalDate.parse(parts[3]),  // ⬅️ Parse back into LocalDate
                parts[4],
                parts[5]
        );
    }

    // Getters
    public String getProject() { return project; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDeadline() { return deadline; }  // Notice LocalDate here
    public String getAssignedTo() { return assignedTo; }
    public String getStatus() { return status; }
}
