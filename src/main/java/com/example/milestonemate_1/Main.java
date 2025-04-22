package com.example.milestonemate_1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    private static BorderPane root;

    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();
        root.setCenter(Layout.getView());
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setTitle("MileStone Mate ");
        primaryStage.setScene(scene);

        // Correct way to load CSS
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}