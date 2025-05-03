package com.example.milestonemate_1;

import com.example.milestonemate_1.models.Project;
import com.example.milestonemate_1.models.Task;
import com.example.milestonemate_1.models.Team;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static final String USER_FILE = "users.txt";
    private static final String TEAMS_FILE = "teams.txt";
    private static final String PROJECTS_FILE = "projects.txt";
    private static final String TASKS_FILE = "tasks.txt";

    // Save a user
    public static void saveUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User object cannot be null");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            if (user.getUsername().isEmpty() || user.getPassword().isEmpty() || user.getRole().isEmpty()) {
                throw new IllegalArgumentException("User data cannot be empty");
            }
            writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getRole());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Validate user
    public static boolean validateUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 3 && userData[0].equals(username) && userData[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error validating user: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Get a user by username
    public static User getUser(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 3 && userData[0].equals(username)) {
                    return new User(userData[0], userData[1], userData[2]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error fetching user: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Get all users
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 3) {
                    users.add(new User(userData[0], userData[1], userData[2]));
                } else {
                    System.err.println("Skipping invalid user entry: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading users: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    // Check if team name exists
    public static boolean isTeamNameExists(String teamName) {
        File file = new File(TEAMS_FILE);
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines
                try {
                    Team team = Team.fromString(line);
                    if (team.getTeamName().equalsIgnoreCase(teamName)) {
                        return true;
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping invalid team entry: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking team name: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Save a team
    public static boolean saveTeam(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("Team object cannot be null");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEAMS_FILE, true))) {
            if (team.getTeamName().isEmpty() || team.getTeamLead().isEmpty() || team.getTeamMembers().isEmpty()) {
                throw new IllegalArgumentException("Team data cannot be empty");
            }
            writer.write(team.toString());
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error saving team: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Save a project
    public static boolean saveProject(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Project object cannot be null");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PROJECTS_FILE, true))) {
            if (project.getProjectName().isEmpty() || project.getDescription().isEmpty() || project.getTeamName().isEmpty() || project.getDueDate() == null) {
                throw new IllegalArgumentException("Project data cannot be empty");
            }
            writer.write(project.getProjectName() + "," +
                    project.getDescription() + "," +
                    project.getTeamName() + "," +
                    project.getDueDate());
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error saving project: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Get all projects
    public static List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        File file = new File(PROJECTS_FILE);
        if (!file.exists()) return projects;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] projectData = line.split(",");
                if (projectData.length == 4) {
                    try {
                        Project project = new Project(
                                projectData[0],
                                projectData[1],
                                projectData[2],
                                LocalDate.parse(projectData[3])
                        );
                        projects.add(project);
                    } catch (Exception e) {
                        System.err.println("Skipping invalid project entry: " + line);
                    }
                } else {
                    System.err.println("Skipping invalid project entry: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading projects: " + e.getMessage());
            e.printStackTrace();
        }
        return projects;
    }

    // Check if a project name already exists
    public static boolean isProjectNameExists(String projectName) {
        File file = new File(PROJECTS_FILE);
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines
                String[] projectData = line.split(",");
                if (projectData.length >= 1 && projectData[0].equalsIgnoreCase(projectName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking project name: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Get all teams from the file
    public static List<Team> getAllTeams() {
        List<Team> teams = new ArrayList<>();
        File file = new File(TEAMS_FILE);
        if (!file.exists()) return teams;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines
                try {
                    Team team = Team.fromString(line);
                    teams.add(team);
                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping invalid team entry: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading teams: " + e.getMessage());
            e.printStackTrace();
        }
        return teams;
    }


    public static boolean saveTask(Task task) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE, true))) {
            writer.write(task.toFileString());
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getAllProjectNames() {
        List<String> projectNames = new ArrayList<>();
        File file = new File("projects.txt");  // should point to projects.txt, not teams.txt
        if (!file.exists()) return projectNames;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) projectNames.add(parts[0]);  // project name
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projectNames;
    }


    public static List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        File file = new File("users.txt");
        if (!file.exists()) return usernames;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) usernames.add(parts[0]);  // username
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usernames;
    }


}
