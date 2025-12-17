package com.restaurant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Loading main-view.fxml...");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main-view.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1200, 720);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        primaryStage.setTitle("Restaurant Queue System");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(640);
        primaryStage.show();

        System.out.println("Restaurant Queue System UI loaded successfully.");
    }

    public static void main(String[] args) {
        System.out.println("=".repeat(50));
        System.out.println("Restaurant Queue System - Starting...");
        System.out.println("=".repeat(50));

        launch(args);
    }
}
