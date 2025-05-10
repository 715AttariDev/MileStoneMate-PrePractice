package com.example.milestonemate_1.views;

import com.example.milestonemate_1.models.Project;
import com.example.milestonemate_1.models.Task;
import com.example.milestonemate_1.FileUtils;
import javafx.scene.paint.Color;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Arc;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
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
        AnchorPane root = new AnchorPane();
        root.setPrefSize(1080, 640);
        Map<String, Integer> statusCounts = getStatusCounts();

        HBox headerBox = new HBox();
        headerBox.setLayoutX(0);
        headerBox.setLayoutY(0);
        headerBox.setPrefSize(1000, 87);
        headerBox.getStyleClass().add("dash-header-box");

        VBox headerLabels = new VBox();
        headerLabels.setPrefSize(243, 75);

        Label welcomeLabel = new Label("Welcome's " + username);
        welcomeLabel.getStyleClass().add("DashWelcomeLabel");

        Label dashboardLabel = new Label(role+ "'s Dashboard");
        dashboardLabel.getStyleClass().add("DashRoleLabel");

        headerLabels.getChildren().addAll(welcomeLabel, dashboardLabel);
        headerBox.getChildren().add(headerLabels);

        HBox projectsBox = new HBox(10);
        projectsBox.setLayoutX(0);
        projectsBox.setLayoutY(100);
        projectsBox.setPrefSize(1000, 158);
        projectsBox.getStyleClass().add("dash-project-box");
        projectsBox.setSpacing(10);

        List<Project> projects = FileUtils.getAllProjects();
        int displayCount = Math.min(projects.size(), 4);

        for (int i = 0; i < displayCount; i++) {
            Project proj = projects.get(i);

            VBox projectBox = new VBox(5);
            projectBox.setPrefSize(220, 160);
            projectBox.getStyleClass().add("dash-vbox");
            projectBox.setPadding(new Insets(10));

            String projectName = (proj.getProjectName() != null && !proj.getProjectName().isEmpty()) ? proj.getProjectName() : "Unnamed Project";
            String description = (proj.getDescription() != null && !proj.getDescription().isEmpty()) ? proj.getDescription() : "No description provided.";
            String teamName = (proj.getTeamName() != null && !proj.getTeamName().isEmpty()) ? proj.getTeamName() : "No team assigned.";
            String dueDateStr = (proj.getDueDate() != null) ? proj.getDueDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy")) : "No due date";

            TextFlow nameFlow = createStyledText("Project : ", projectName);
            TextFlow descFlow = createStyledText("Description : ", description);
            TextFlow teamFlow = createStyledText("Team : ", teamName);
            TextFlow dueDateFlow = createStyledText("Due : ", dueDateStr);

            projectBox.getChildren().addAll(nameFlow, descFlow, teamFlow, dueDateFlow);
            projectsBox.getChildren().add(projectBox);
        }

        FlowPane tasksPane = new FlowPane(10, 10);
        tasksPane.setLayoutX(0);
        tasksPane.setLayoutY(290);
        tasksPane.setPrefSize(874, 329);

        VBox tasksStatusBox = new VBox(10);
        tasksStatusBox.setPrefSize(504, 319);
        tasksStatusBox.getStyleClass().add("dash-vbox");

        Label tasksStatusLabel = new Label("Task Progress");
        tasksStatusLabel.getStyleClass().add("dashBoardStatusLabel");


        HBox tasksChartBox = new HBox(10);
        tasksChartBox.setPrefSize(500, 275);
//        tasksChartBox.getStyleClass().add("dash-vbox");

        PieChart chart = new PieChart(FXCollections.observableArrayList(
                new PieChart.Data("Completed", statusCounts.getOrDefault("Completed", 0)),
                new PieChart.Data("Needs Changes", statusCounts.getOrDefault("Needs Changes", 0)),
                new PieChart.Data("Pending", statusCounts.getOrDefault("Pending", 0)),
                new PieChart.Data("Approved", statusCounts.getOrDefault("Approved", 0))
        ));
        chart.setPrefSize(300, 300);
        chart.setLegendVisible(true);
        chart.setLegendSide(Side.BOTTOM);

        applyColors(chart, Map.of(
                "Completed", "#00bbff",
                "Needs Changes", "#1dc2ae",
                "Pending", "#e67e22",
                "Approved", "#001587"
        ));

        int totalTasks = tasks.size();
        int pendingCount = statusCounts.getOrDefault("Pending", 0);
        int approvedCount = statusCounts.getOrDefault("Approved", 0);

        double pendingPercent = (totalTasks == 0) ? 0 : ((double) pendingCount / totalTasks) * 100;
        double approvedPercent = (totalTasks == 0) ? 0 : ((double) approvedCount / totalTasks) * 100;

        DecimalFormat df = new DecimalFormat("#.##");

        StackPane loaderPane1 = createLoaderPane(Color.DODGERBLUE, "Pending Tasks", df.format(pendingPercent) + "%");
        StackPane loaderPane2 = createLoaderPane(Color.DODGERBLUE, "Approved Tasks", df.format(approvedPercent) + "%");

        tasksChartBox.getChildren().addAll(chart, loaderPane1, loaderPane2);

        tasksStatusBox.getChildren().addAll(tasksStatusLabel, tasksChartBox);

        // Bar chart setup
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Deadlines");  // 🠔 Label under the x-axis

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Tasks");  // 🠔 Label next to the y-axis

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setPrefSize(356, 208);
        barChart.setTitle("Tasks Deadline");
        barChart.getStyleClass().add("custom-bar-chart");

// Filter deadlines based on role
        Map<String, Integer> deadlineCounts = new HashMap<>();
        for (Task task : tasks) {
            if (role.equalsIgnoreCase("Team Member") && !task.getAssignedTo().equalsIgnoreCase(username)) {
                continue;
            }
            String deadline = task.getDeadline().toString();  // format as needed
            deadlineCounts.put(deadline, deadlineCounts.getOrDefault(deadline, 0) + 1);
        }

// Populate bar chart
        XYChart.Series<String, Number> deadlineSeries = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : deadlineCounts.entrySet()) {
            deadlineSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            deadlineSeries.setName("Task Count by Deadline");
        }
        barChart.getData().clear();
        barChart.getData().add(deadlineSeries);

        tasksPane.getChildren().addAll(tasksStatusBox, barChart);

        root.getChildren().addAll(headerBox, projectsBox, tasksPane);

        return root;
    }


    private StackPane createLoaderPane(Color strokeColor, String labelText, String percentageText) {
        Arc arc = new Arc(50, 50, 40, 40, 90, 270);
        arc.setFill(Color.TRANSPARENT);
        arc.setStrokeWidth(10);

        // Define gradient stops
        Stop[] stops = new Stop[] {
                new Stop(0, Color.web("#0091ff")),  // Light blue
                new Stop(1, Color.web("#000ba1"))   // Dark blue
        };

        // Create a linear gradient
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1,  // StartX, StartY, EndX, EndY (relative)
                true,        // proportional
                CycleMethod.NO_CYCLE, stops
        );

        arc.setStroke(gradient);  // Apply the gradient to the stroke

        RotateTransition rotate = new RotateTransition(Duration.seconds(2), arc);
        rotate.setByAngle(360);
        rotate.setCycleCount(Animation.INDEFINITE);
        rotate.play();

        // Percentage label (center of arc)
        Label percentageLabel = new Label(percentageText);
        percentageLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        percentageLabel.setTextFill(Color.BLACK);
        percentageLabel.setAlignment(Pos.CENTER);

        StackPane arcWithPercentage = new StackPane(arc, percentageLabel);
        arcWithPercentage.setPrefSize(100, 100);

        // Label below arc
        Label belowLabel = new Label(labelText);
        belowLabel.setFont(Font.font("System", FontWeight.NORMAL, 12));
        belowLabel.setTextFill(Color.BLACK);
        belowLabel.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(arcWithPercentage, belowLabel);

        StackPane loaderPane = new StackPane(vbox);
        loaderPane.setPrefSize(150, 150);

        return loaderPane;
    }



    private void applyColors(PieChart chart, Map<String, String> colorMap) {
        for (PieChart.Data data : chart.getData()) {
            data.getNode().setStyle("-fx-pie-color: " + colorMap.getOrDefault(data.getName(), "#bdc3c7") + ";");
        }
    }

    private Map<String, Integer> getStatusCounts() {
        Map<String, Integer> counts = new HashMap<>();
        for (Task task : tasks) {
            String status = (task.getStatus() != null) ? task.getStatus() : "Unknown";
            counts.put(status, counts.getOrDefault(status, 0) + 1);

        }
        return counts;
    }

    private TextFlow createStyledText(String label, String value) {
        Text labelText = new Text(label);
        labelText.setStyle("-fx-font-weight: bold; -fx-fill: #2c3e50;-fx-font-size: 15px;-fx-font-style:  italic;");

        Text valueText = new Text((value != null && !value.isEmpty()) ? value : "N/A");
        valueText.setStyle("-fx-fill: #34495e;-fx-font-size: 15px;-fx-font-style:  italic;");

        return new TextFlow(labelText, valueText);
    }




}
