package com.example.milestonemate_1;

import com.example.milestonemate_1.models.Project;
import com.example.milestonemate_1.models.Task;
import com.example.milestonemate_1.models.Team;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
                line = line.trim(); // Remove any leading/trailing spaces

                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }
                String[] userData = line.split(",");
                // Trim each part to avoid spaces messing things up
                for (int i = 0; i < userData.length; i++) {
                    userData[i] = userData[i].trim();
                }
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

    public static List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(TASKS_FILE);
        if (!file.exists()) return tasks;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines
                try {
                    Task task = Task.fromFileString(line);
                    tasks.add(task);
                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping invalid task entry: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading tasks: " + e.getMessage());
            e.printStackTrace();
        }
        return tasks;
    }

    public static String copyFileToTaskFolder(File file, String taskName) {
        if(file == null || !file.exists()){
            throw new IllegalArgumentException("File does not exist");
        }
        if (taskName == null || taskName.trim().isEmpty()) {
            throw new IllegalArgumentException("Task name cannot be empty");
        }

        // Define the folder where we want to copy the file
        File taskFolder = new File("task_files", taskName);
        if (!taskFolder.exists()) {
            boolean created = taskFolder.mkdirs(); // create directories if not exist
            if (!created) {
                throw new RuntimeException("Failed to create task folder: " + taskFolder.getAbsolutePath());
            }
        }

        // Create a destination file in the task folder
        File destFile = new File(taskFolder, file.getName());

        try (InputStream in = new FileInputStream(file);
             OutputStream out = new FileOutputStream(destFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (IOException e) {
            System.err.println("Error copying file: " + e.getMessage());
            e.printStackTrace();
            return null; // or throw exception if you want
        }

        return destFile.getAbsolutePath(); // returning the new file path
    }
    public static List<String> getTeamMembersForProject(String projectName) {
        List<String> members = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("projects.txt"))) {
            String projectLine;
            String teamName = null;

            // First: Find the team name for the given project
            while ((projectLine = br.readLine()) != null) {
                String[] parts = projectLine.split(",");
                if (parts.length >= 3 && parts[0].equalsIgnoreCase(projectName)) {
                    teamName = parts[2].trim();
                    break;
                }
            }

            if (teamName != null) {
                // Now: Find the team and its members from the teams.txt file
                try (BufferedReader teamReader = new BufferedReader(new FileReader("teams.txt"))) {
                    String teamLine;
                    while ((teamLine = teamReader.readLine()) != null) {
                        if (teamLine.trim().isEmpty()) continue;
                        Team team = Team.fromString(teamLine);
                        if (team.getTeamName().equalsIgnoreCase(teamName)) {
                            members.addAll(team.getTeamMembers());
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error fetching team members for project: " + e.getMessage());
            e.printStackTrace();
        }
        return members;
    }
    public static boolean updateTaskStatus(String taskId, String newStatus) {
        List<Task> tasks = getAllTasks();
        boolean updated = false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE))) {
            for (Task task : tasks) {
                if (task.getId().equals(taskId)) {
                    task.setStatus(newStatus);  // 🔄 update status
                    updated = true;
                }
                writer.write(task.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating task status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return updated;
    }


    public static boolean copyFile(File sourceFile, File destinationFile) throws FileNotFoundException {
        if (sourceFile == null || destinationFile == null) {
            throw new IllegalArgumentException("Source or destination file cannot be null");
        }

        if (!sourceFile.exists()) {
            throw new FileNotFoundException("Source file does not exist");
        }

        // If the destination file's parent directory does not exist, create it
        File parentDir = destinationFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                throw new RuntimeException("Failed to create directory: " + parentDir.getAbsolutePath());
            }
        }

        // Proceed to copy the file
        try (InputStream in = new FileInputStream(sourceFile);
             OutputStream out = new FileOutputStream(destinationFile)) {

            byte[] buffer = new byte[1024];  // Buffer size for reading and writing
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            return true; // Successfully copied
        } catch (IOException e) {
            System.err.println("Error copying file: " + e.getMessage());
            e.printStackTrace();
            return false; // Failure in copying the file
        }
    }

    public static void updateTaskFeedback(String taskId, String feedback) {
        List<Task> tasks = getAllTasks();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE))) {
            for (Task task : tasks) {
                if (task.getId().equals(taskId)) {
                    task.setFeedback(feedback);
                }
                writer.write(task.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating task feedback: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<Task> loadAllTasks() {
        List<Task> tasks = new ArrayList<>();
        List<String> lines = readLinesFromFile("tasks.txt");  // Your task file path

        for (String line : lines) {
            try {
                Task task = Task.fromFileString(line);
                tasks.add(task);
            } catch (Exception e) {
                System.out.println("Skipping invalid task line: " + line);
            }
        }
        return tasks;
    }

    public static List<String> readLinesFromFile(String fileName) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) { // skip empty lines
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + fileName);
            e.printStackTrace();
        }
        return lines;
    }
    public static void saveAllTasks(List<Task> tasks) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(TASKS_FILE))) {
            for (Task task : tasks) {
                writer.write(task.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("❌ Error writing tasks file: " + e.getMessage());
        }
    }

}
