package com.example.milestonemate_1.models;

import java.time.LocalDate;

public class Project {
    private String projectName;
    private String description;
    private String teamName;
    private LocalDate dueDate;

    public Project(String projectName, String description, String teamName, LocalDate dueDate) {
        this.projectName = projectName;
        this.description = description;
        this.teamName = teamName;
        this.dueDate = dueDate;
    }

    // Getters and Setters here
    public String getProjectName() { return projectName; }
    public String getDescription() { return description; }
    public String getTeamName() { return teamName; }
    public LocalDate getDueDate() { return dueDate; }

}

