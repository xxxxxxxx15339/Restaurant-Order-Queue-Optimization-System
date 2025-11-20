package com.restaurant;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // Create a simple label
        Label label = new Label("Restaurant Queue System - Initialized Successfully!");
        label.setStyle("-fx-font-size: 16px; -fx-padding: 20px;");
        
        // Create layout
        StackPane root = new StackPane();
        root.getChildren().add(label);
        
        // Create scene
        Scene scene = new Scene(root, 600, 400);
        
        // Configure stage
        primaryStage.setTitle("Restaurant Queue System");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        System.out.println("JavaFX Application Started!");
    }
    
    public static void main(String[] args) {
        System.out.println("=".repeat(50));
        System.out.println("Restaurant Queue System - Starting...");
        System.out.println("=".repeat(50));
        
        // Launch JavaFX application
        launch(args);
    }
}
