package com.korczak.plsql1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApplicationLauncher extends Application {

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main-window.fxml"));
        primaryStage.setTitle("PL/SQL Viewer");
        primaryStage.setScene(new Scene(root, 600, 360));
        primaryStage.show();
    }
}
