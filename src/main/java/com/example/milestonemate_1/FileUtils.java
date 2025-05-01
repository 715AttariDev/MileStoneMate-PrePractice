package com.example.milestonemate_1;

import com.example.milestonemate_1.models.Team;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class FileUtils {
    private static final String USER_FILE = "users.txt";
    private static final String TEAMS_FILE = "teams.txt";

    public static void saveUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            if (user.getUsername().isEmpty() || user.getPassword().isEmpty() || user.getRole().isEmpty()) {
                throw new IllegalArgumentException("User data cannot be empty");
            }
            writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getRole());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
        return false;
    }

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
            e.printStackTrace();
        }
        return null;
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 3) {
                    users.add(new User(userData[0], userData[1], userData[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static boolean isTeamNameExists(String teamName) {
        File file = new File(TEAMS_FILE);
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines
                Team team = Team.fromString(line);
                if (team.getTeamName().equalsIgnoreCase(teamName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean saveTeam(Team team) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEAMS_FILE, true))) {
            if (team.getTeamName().isEmpty() || team.getTeamLead().isEmpty() || team.getTeamMembers().isEmpty()) {
                throw new IllegalArgumentException("Team data cannot be empty");
            }
            writer.write(team.toString());
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
