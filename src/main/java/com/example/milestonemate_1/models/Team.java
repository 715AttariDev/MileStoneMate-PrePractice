package com.example.milestonemate_1.models;

import java.util.List;

public class Team {
    private String teamName;
    private String teamLead;
    private List<String> teamMembers;

    public Team(String teamName, String teamLead, List<String> teamMembers) {
        this.teamName = teamName;
        this.teamLead = teamLead;
        this.teamMembers = teamMembers;
    }

    // Getters
    public String getTeamName() {
        return teamName;
    }

    public String getTeamLead() {
        return teamLead;
    }

    public List<String> getTeamMembers() {
        return teamMembers;
    }

    // Optionally toString (for saving as text)
    @Override
    public String toString() {
        return teamName + ";" + teamLead + ";" + String.join(",", teamMembers);
    }

    // Factory method to parse from a line
    public static Team fromString(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Input line cannot be null or empty");
        }

        String[] parts = line.split(";");

        // Ensure that there are at least three parts: teamName, teamLead, and members
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid team data: " + line);
        }

        String name = parts[0].trim();
        String lead = parts[1].trim();

        // Handle case where there may be no members or a malformed members string
        List<String> members;
        if (parts[2].trim().isEmpty()) {
            members = List.of(); // If no members, return an empty list
        } else {
            members = List.of(parts[2].split(","));
        }

        // Ensure the team name and lead are not empty
        if (name.isEmpty() || lead.isEmpty()) {
            throw new IllegalArgumentException("Team name and lead cannot be empty");
        }

        return new Team(name, lead, members);
    }
}