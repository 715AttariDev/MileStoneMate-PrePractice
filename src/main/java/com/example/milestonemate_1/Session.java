package com.example.milestonemate_1;

public class Session {
    private static User currentUser;
    // Getters and Setters
    public static void setUser(User user) {
        currentUser = user;
    }

    public static User getUser() {
        return currentUser;
    }
}

