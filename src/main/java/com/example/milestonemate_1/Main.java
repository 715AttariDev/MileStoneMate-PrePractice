package com.example.milestonemate_1;
import com.example.milestonemate_1.views.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        LoginView loginView = new LoginView();
        Scene loginScene = loginView.getScene(primaryStage); // This will handle login and redirection
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("MileStone Mate");
        // Correct way to load CSS
        loginScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Styles/style.css")).toExternalForm());
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
//
//| Role            | What to Show                                        |
//        | --------------- | --------------------------------------------------- |
//        | **Manager**     | All projects summary, team stats, global pie chart  |
//        | **Team Lead**   | Tasks to review, team progress, task approval rates |
//        | **Team Member** | My tasks, personal progress, upcoming deadlines     |
