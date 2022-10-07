package com.dbquotes;

import com.dbquotes.controllers.*;
import com.dbquotes.models.HandlerDB;
import com.dbquotes.models.Quote;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    private Stage primaryStage;
    private MainController mainWindowController;

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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setTitle("Main Window");
            primaryStage.setScene(scene);
            MainController controller = fxmlLoader.getController();
            controller.setAppFX(this);
            controller.update();
            mainWindowController = controller;
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showProfileWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("profile.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage extraStage = new Stage();
            extraStage.setResizable(false);
            extraStage.setTitle("Profile");
            extraStage.setScene(scene);
            BaseStageController controller = fxmlLoader.getController();
            controller.setAppFX(this);
            controller.setStage(extraStage);
            extraStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCreateWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("write.fxml"));
            CreateController controller = new CreateController();
            Stage extraStage = new Stage();
            extraStage.setResizable(false);
            extraStage.setTitle("Create");
            controller.setAppFX(this);
            controller.setStage(extraStage);
            fxmlLoader.setController(controller);
            Scene scene = new Scene(fxmlLoader.load());
            extraStage.setScene(scene);
            extraStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showEditWindow(Quote quote) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("write.fxml"));
            Stage extraStage = new Stage();
            extraStage.setResizable(false);
            EditController controller = new EditController();
            controller.setAppFX(this);
            controller.setQuote(quote);
            controller.setStage(extraStage);
            extraStage.setTitle("Edit");
            fxmlLoader.setController(controller);
            Scene scene = new Scene(fxmlLoader.load());
            extraStage.setScene(scene);
            extraStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MainController getMainWindowController() {
        return mainWindowController;
    }

    public static void main(String[] args) {
        launch();
    }
}