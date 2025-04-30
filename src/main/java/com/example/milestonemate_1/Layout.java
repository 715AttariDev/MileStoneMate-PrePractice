package com.example.milestonemate_1;

import com.example.milestonemate_1.views.LoginView;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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
        button.getStyleClass().addAll("sidebar-button");

        Rectangle underline = new Rectangle(0, 2);
        underline.getStyleClass().add("sidebar-button-underline");
        underline.setTranslateY(10);

        StackPane buttonStack = new StackPane(button, underline);

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
        dashboardLeftLabel.getStyleClass().add("nav-label");

        StackPane centerContent = new StackPane();
        centerContent.getStyleClass().add("content-background");

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
            addSidebarButton(sideBar, centerContent, dashboardLeftLabel, "About", new com.example.milestonemate_1.views.AboutSectionView());
        } else if (role.equals("Team Lead")) {
            addSidebarButton(sideBar, centerContent, dashboardLeftLabel, "Review Tasks", new com.example.milestonemate_1.views.ReviewTasksView());
            addSidebarButton(sideBar, centerContent, dashboardLeftLabel, "Kanban Board", new com.example.milestonemate_1.views.KanbanBoardView());
            addSidebarButton(sideBar, centerContent, dashboardLeftLabel, "About", new com.example.milestonemate_1.views.AboutSectionView());

        } else if (role.equals("Team Member")) {
            addSidebarButton(sideBar, centerContent, dashboardLeftLabel, "My Tasks", new com.example.milestonemate_1.views.MyTasksView());
            addSidebarButton(sideBar, centerContent, dashboardLeftLabel, "Reviewed Tasks", new com.example.milestonemate_1.views.ReviewedTasksView());
            addSidebarButton(sideBar, centerContent, dashboardLeftLabel, "Kanban Board", new com.example.milestonemate_1.views.KanbanBoardView());
            addSidebarButton(sideBar, centerContent, dashboardLeftLabel, "About", new com.example.milestonemate_1.views.AboutSectionView());

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



        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("LogOut_button");
        logoutButton.setOnAction(e -> {
            Session.setUser(null);
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new LoginView().getScene(stage));
        });

        Label dashboardRightLabel = new Label(role);
        dashboardRightLabel.getStyleClass().add("nav-label");

        Label dateLabel = new Label();
        dateLabel.getStyleClass().add("date-label");
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy");
        dateLabel.setText(currentDate.format(formatter));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        StackPane profileImageContainer = new StackPane();
        profileImageContainer.getStyleClass().add("profile-image-container");

        Circle avatarCircle = new Circle(25);
        avatarCircle.getStyleClass().add("avatar-circle");
        String[] colors = {"#FF6B6B", "#6BCB77", "#4D96FF", "#FF6F91", "#845EC2", "#00C9A7"};
        int randomIndex = (int)(Math.random() * colors.length);
        avatarCircle.setStyle("-fx-fill:" + colors[randomIndex] + ";");

        Label firstLetterLabel = new Label(role.substring(0, 1).toUpperCase());
        firstLetterLabel.getStyleClass().add("initial-label");  // Ensure this applies the .initial-label class

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
                logoutButton
        );

        dashAnchorPane.getChildren().addAll(navBar, sideBar);

        AnchorPane.setTopAnchor(navBar, 0.0);
        AnchorPane.setLeftAnchor(navBar, 200.0);
        AnchorPane.setRightAnchor(navBar, 0.0);

        centerContent.setPadding(new Insets(20));

        centerContent.getChildren().setAll(new com.example.milestonemate_1.views.DashBoardView().getView());

        AnchorPane.setTopAnchor(centerContent, 85.0);
        AnchorPane.setLeftAnchor(centerContent, 215.0);
        AnchorPane.setRightAnchor(centerContent, 0.0);
        AnchorPane.setBottomAnchor(centerContent, 0.0);

        dashAnchorPane.getChildren().add(centerContent);

        return dashAnchorPane;
    }

    private static void addSidebarButton(VBox sideBar, StackPane centerContent, Label navLabel, String labelText, Object viewInstance) {
        Button[] btnRef = new Button[1];
        StackPane btn = createAnimatedButton(labelText, btnRef);
        btnRef[0].setOnAction(e -> {
            if (viewInstance instanceof com.example.milestonemate_1.views.ViewProvider) {
                com.example.milestonemate_1.views.ViewProvider viewProvider = (com.example.milestonemate_1.views.ViewProvider) viewInstance;
                centerContent.getChildren().setAll(viewProvider.getView());
                animateNavTitleChange(navLabel, labelText);
            }
        });
        sideBar.getChildren().add(btn);
    }
}