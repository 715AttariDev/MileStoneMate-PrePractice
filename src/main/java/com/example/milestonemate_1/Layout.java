package com.example.milestonemate_1;

import com.example.milestonemate_1.views.LoginView;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


public class Layout {

    private static void animateNavTitleChange(Label label, String newTitle) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), label);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> {
            label.setText(newTitle);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(250), label);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }

    private static StackPane createAnimatedButton(String buttonText, Button[] buttonRef) {
        Button button = new Button(buttonText);
        button.getStyleClass().add("sidebar-button");

        javafx.scene.shape.Rectangle underline = new javafx.scene.shape.Rectangle(0, 2);
        underline.setStyle("-fx-fill: #00aaff;");
        underline.setTranslateY(10);

        StackPane buttonStack = new StackPane(button, underline);
        buttonStack.setPickOnBounds(false);

        buttonRef[0] = button;

        button.setOnMouseEntered(e -> {
            ScaleTransition expand = new ScaleTransition(Duration.millis(300), underline);
            expand.setFromX(0);
            expand.setToX(1);
            underline.setScaleX(0);
            expand.play();
        });

        button.setOnMouseExited(e -> {
            ScaleTransition shrink = new ScaleTransition(Duration.millis(200), underline);
            shrink.setFromX(1);
            shrink.setToX(0);
            shrink.play();
        });

        return buttonStack;
    }

    public static AnchorPane getView(String role) {
        AnchorPane dashAnchorPane = new AnchorPane();
        Label dashboardLeftLabel = new Label("Dashboard");

        StackPane centerContent = new StackPane();

        VBox sideBar = new VBox(20);
        sideBar.setPadding(new Insets(30));
        sideBar.getStyleClass().add("sidebar");

        ImageView logoView = new ImageView();
        try {
            Image logo = new Image(Objects.requireNonNull(Layout.class.getResource("/images/Blue Logo.png")).toExternalForm());
            logoView.setImage(logo);
            logoView.setFitWidth(150);
            logoView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("⚠️ Logo image not found. Check path: /images/Blue Logo.png");
        }

        sideBar.getChildren().add(logoView);

        Button[] dashboardBtnRef = new Button[1];
        StackPane dashboardBtn = createAnimatedButton("Dashboard", dashboardBtnRef);
        dashboardBtnRef[0].setOnAction(e -> {
            centerContent.getChildren().setAll(new com.example.milestonemate_1.views.DashBoardView().getView());
            animateNavTitleChange(dashboardLeftLabel, "Dashboard");
        });
        sideBar.getChildren().add(dashboardBtn);

        if (role.equals("Manager")) {
            addSidebarButton(sideBar, centerContent, dashboardLeftLabel, "Create Team", new com.example.milestonemate_1.views.CreateTeamView());
            addSidebarButton(sideBar, centerContent, dashboardLeftLabel, "Create Project", new com.example.milestonemate_1.views.CreateProjectView());
            addSidebarButton(sideBar, centerContent, dashboardLeftLabel, "Kanban Board", new com.example.milestonemate_1.views.KanbanBoardView());
        } else if (role.equals("Team Lead")) {
            addSidebarButton(sideBar, centerContent, dashboardLeftLabel, "Review Tasks", new com.example.milestonemate_1.views.ReviewTasksView());
            addSidebarButton(sideBar, centerContent, dashboardLeftLabel, "Kanban Board", new com.example.milestonemate_1.views.KanbanBoardView());
        } else if (role.equals("Team Member")) {
            addSidebarButton(sideBar, centerContent, dashboardLeftLabel, "My Tasks", new com.example.milestonemate_1.views.MyTasksView());
            addSidebarButton(sideBar, centerContent, dashboardLeftLabel, "Reviewed Tasks", new com.example.milestonemate_1.views.ReviewedTasksView());
            addSidebarButton(sideBar, centerContent, dashboardLeftLabel, "Kanban Board", new com.example.milestonemate_1.views.KanbanBoardView());
        } else {
            System.out.println("⚠️ Unknown role: " + role);
        }

        AnchorPane.setTopAnchor(sideBar, 0.0);
        AnchorPane.setBottomAnchor(sideBar, 0.0);
        AnchorPane.setLeftAnchor(sideBar, 0.0);
        sideBar.setPrefWidth(200);

        HBox navBar = new HBox();
        navBar.setPadding(new Insets(10));
        navBar.setSpacing(25);
        navBar.getStyleClass().add("navbar");
        navBar.setAlignment(Pos.CENTER);

        // Create Logout Button
        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("LogOut_button");
        logoutButton.setOnAction(e -> {
            Session.setUser(null); // Clear session
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new LoginView().getScene(stage)); // Back to login
        });


        dashboardLeftLabel.setPadding(new Insets(21));
        dashboardLeftLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Label dashboardRightLabel = new Label(role);
        dashboardRightLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Label dateLabel = new Label();
        dateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy");
        dateLabel.setText(currentDate.format(formatter));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        StackPane profileImageContainer = new StackPane();
        profileImageContainer.getStyleClass().add("profile-image-container");

        // Create a Circle for profile avatar
        Circle avatarCircle = new Circle(25); // radius 30 for 60x60 size

// Random background color (choose some soft nice ones manually)
        String[] colors = {"#FF6B6B", "#6BCB77", "#4D96FF", "#FF6F91", "#845EC2", "#00C9A7"};
        int randomIndex = (int) (Math.random() * colors.length);
        avatarCircle.setStyle("-fx-fill: " + colors[randomIndex] + ";");

// Create a Label for first letter
        Label firstLetterLabel = new Label(role.substring(0, 1).toUpperCase()); // Taking first letter of role (you can use username too)
        firstLetterLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20 px; -fx-font-weight: bold;");

        profileImageContainer.getChildren().addAll(avatarCircle, firstLetterLabel);
        profileImageContainer.setAlignment(Pos.CENTER);


        profileImageContainer.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), profileImageContainer);
            scale.setToX(1.1);
            scale.setToY(1.1);
            scale.play();
        });

        profileImageContainer.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), profileImageContainer);
            scale.setToX(1);
            scale.setToY(1);
            scale.play();
        });

        navBar.getChildren().addAll(
                dashboardLeftLabel,
                spacer,
                dashboardRightLabel,
                dateLabel,
                profileImageContainer,
                logoutButton // 👈 Add here to show it on the right
        );


        dashAnchorPane.getChildren().addAll(navBar, sideBar);

        AnchorPane.setTopAnchor(navBar, 0.0);
        AnchorPane.setLeftAnchor(navBar, 200.0);
        AnchorPane.setRightAnchor(navBar, 0.0);

        centerContent.setPadding(new Insets(20));
        centerContent.setStyle("-fx-background-color: #f4f4f4; ");

        centerContent.getChildren().setAll(new com.example.milestonemate_1.views.DashBoardView().getView());

        AnchorPane.setTopAnchor(centerContent, 85.0);
        AnchorPane.setLeftAnchor(centerContent, 215.0);
        AnchorPane.setRightAnchor(centerContent, 0.0);
        AnchorPane.setBottomAnchor(centerContent, 0.0);

        dashAnchorPane.getChildren().add(centerContent);

        return dashAnchorPane;
    }

    // 🔄 Updated for Java 8-15 compatibility (classic instanceof + cast)
    private static void addSidebarButton(VBox sideBar, StackPane centerContent, Label navLabel, String labelText, Object viewInstance) {
        Button[] btnRef = new Button[1];
        StackPane btn = createAnimatedButton(labelText, btnRef);
        btnRef[0].setOnAction(e -> {
            if (viewInstance instanceof com.example.milestonemate_1.views.ViewProvider viewProvider) {
                centerContent.getChildren().setAll(viewProvider.getView());
                animateNavTitleChange(navLabel, labelText);
            }
        });
        sideBar.getChildren().add(btn);
    }
}
