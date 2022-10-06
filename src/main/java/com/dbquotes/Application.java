package com.dbquotes;

import com.dbquotes.controllers.*;
import com.dbquotes.models.HandlerDB;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    private Stage primaryStage;

    @Override
    public void start(Stage stage){
        primaryStage = stage;
        primaryStage.setResizable(false);
        showAuthWindow();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        HandlerDB.closeConnection();
    }

    public void showAuthWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("auth.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setTitle("Authentication");
            primaryStage.setScene(scene);
            BaseController controller = fxmlLoader.getController();
            controller.setAppFX(this);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showRegistrationWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registration.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setTitle("Registration");
            primaryStage.setScene(scene);
            BaseController controller = fxmlLoader.getController();
            controller.setAppFX(this);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMainWindow() {
        System.out.println("Show main window");
    }

    public void showProfileWindow() {
        System.out.println("Show profile window");
    }

    public void showCreateWindow() {
        System.out.println("Show create window");
    }

    public static void main(String[] args) {
        launch();
    }
}