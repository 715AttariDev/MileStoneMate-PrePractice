package com.example.milestonemate_1.models;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Team {
    private String teamName;
    private String teamLead;
    private List<String> teamMembers;
    private String description;

    public Team(String teamName, String teamLead, List<String> teamMembers, String description) {
        this.teamName = teamName;
        this.teamLead = teamLead;
        this.teamMembers = new ArrayList<>(teamMembers); // Ensure it's mutable
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    // toString (for saving as text)
    @Override
    public String toString() {
        return escape(teamName) + ";" +
                escape(teamLead) + ";" +
                String.join(",", escapeList(teamMembers)) + ";" +
                escape(description);
    }

    // Factory method to parse from a line
    public static Team fromString(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Input line cannot be null or empty");
        }

        String[] parts = line.split(";", -1); // Keep empty parts

        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid team data: " + line);
        }

        String name = unescape(parts[0].trim());
        String lead = unescape(parts[1].trim());
        List<String> members = parts[2].trim().isEmpty() ? new ArrayList<>() :
                new ArrayList<>(Arrays.asList(parts[2].split(",")));
        members.replaceAll(Team::unescape);  // Unescape each member
        String desc = unescape(parts[3].trim());

        if (name.isEmpty() || lead.isEmpty()) {
            throw new IllegalArgumentException("Team name and lead cannot be empty");
        }

        return new Team(name, lead, members, desc);
    }

    // Escaping to handle special characters
    private static String escape(String input) {
        return input.replace(";", "\\;").replace(",", "\\,");
    }

    private static List<String> escapeList(List<String> list) {
        List<String> escaped = new ArrayList<>();
        for (String item : list) {
            escaped.add(escape(item));
        }
        return escaped;
    }

    private static String unescape(String input) {
        return input.replace("\\;", ";").replace("\\,", ",");
    }
}
