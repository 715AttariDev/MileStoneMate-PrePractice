package com.example.milestonemate_1.views;

import com.example.milestonemate_1.models.Project;
import com.example.milestonemate_1.models.Task;
import com.example.milestonemate_1.FileUtils;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.*;

public class DashBoardView implements ViewProvider {

    private final String role;
    private final String username;
    private final List<Task> tasks;

    public DashBoardView(String role, String username, List<Task> tasks) {
        this.role = role;
        this.username = username;
        this.tasks = tasks;
    }

    @Override
    public Pane getView() {
        // Root AnchorPane
        AnchorPane root = new AnchorPane();
        root.setPrefSize(1000, 640);

        // Header HBox
        HBox headerBox = new HBox();
        headerBox.setLayoutX(17);
        headerBox.setLayoutY(21);
        headerBox.setPrefSize(868, 93);
        headerBox.getStyleClass().add("dash-header-box");

        VBox headerLabels = new VBox();
        headerLabels.setPrefSize(243, 80);

        Label welcomeLabel = new Label("Welcome to Manager");
        welcomeLabel.setPrefSize(243, 53);
        welcomeLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        welcomeLabel.getStyleClass().add("label-header");

        Label dashboardLabel = new Label("Manager Dashboard");
        dashboardLabel.setPrefSize(243, 32);
        dashboardLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        dashboardLabel.getStyleClass().add("label-header");

        headerLabels.getChildren().addAll(welcomeLabel, dashboardLabel);
        headerBox.getChildren().add(headerLabels);

        // inside getView()
        HBox projectsBox = new HBox();
        projectsBox.setLayoutX(17);
        projectsBox.setLayoutY(128);
        projectsBox.setPrefSize(868, 158);
        projectsBox.getStyleClass().add("dash-header-box");
        projectsBox.setSpacing(10);

// Load projects from file
        List<Project> projects = FileUtils.getAllProjects();

// Show up to 4 projects on dashboard
        int displayCount = Math.min(projects.size(), 4);

        for (int i = 0; i < displayCount; i++) {
            Project proj = projects.get(i);

            VBox projectBox = new VBox();
            projectBox.setPrefSize(200, 158);
            projectBox.getStyleClass().add("dash-vbox");

            Label nameLabel = new Label(proj.getProjectName());
            nameLabel.setFont(new Font(14));
            nameLabel.getStyleClass().add("label-project");

            Label descLabel = new Label(proj.getDescription());
            descLabel.setWrapText(true);
            descLabel.setMaxWidth(180);

            Label teamLabel = new Label("Team: " + proj.getTeamName());

            Label dueDateLabel = new Label("Due: " + proj.getDueDate().toString());

            projectBox.getChildren().addAll(nameLabel, descLabel, teamLabel, dueDateLabel);
            projectsBox.getChildren().add(projectBox);
        }
        // Tasks FlowPane
        FlowPane tasksPane = new FlowPane();
        tasksPane.setLayoutX(14);
        tasksPane.setLayoutY(300);
        tasksPane.setPrefSize(874, 329);
        tasksPane.setHgap(10);

        // Tasks Status VBox
        VBox tasksStatusBox = new VBox();
        tasksStatusBox.setPrefSize(504, 319);
        tasksStatusBox.getStyleClass().add("dash-vbox");

        Label tasksStatusLabel = new Label("Tasks Status");
        tasksStatusLabel.setPrefSize(194, 29);

        HBox tasksChartBox = new HBox();
        tasksChartBox.setPrefSize(462, 275);
        tasksChartBox.getStyleClass().add("dash-vbox");
        tasksChartBox.setSpacing(10);

        PieChart pieChart = new PieChart();
        pieChart.setPrefSize(194, 267);
        pieChart.setTitle("Pie Chart");

        ProgressIndicator progress1 = new ProgressIndicator(0.75);
        progress1.setPrefSize(150, 150);
        progress1.setStyle("-fx-progress-color: green;");


        ProgressIndicator progress2 = new ProgressIndicator(0.69);
        progress2.setPrefSize(113, 108);
        progress2.setStyle("-fx-progress-color: #2196F3;");

        tasksChartBox.getChildren().addAll(pieChart, progress1, progress2);
        tasksStatusBox.getChildren().addAll(tasksStatusLabel, tasksChartBox);

        // Tasks Deadline BarChart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setPrefSize(356, 208);
        barChart.setTitle("Tasks Deadline");

        tasksPane.getChildren().addAll(tasksStatusBox, barChart);

        // Add all sections to root
        root.getChildren().addAll(headerBox, projectsBox, tasksPane);

        return root;
    }
}