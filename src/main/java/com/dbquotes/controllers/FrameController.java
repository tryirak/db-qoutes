package com.dbquotes.controllers;

import com.dbquotes.Application;
import com.dbquotes.models.ApplicationUser;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public abstract class FrameController {
    protected Stage stage;
    protected Application rootApp;
    protected ApplicationUser applicationUser;

    @FXML
    protected void initialize() {
        applicationUser = ApplicationUser.getInstance();
    }

    public void setAppFX(Application rootApp) {
        this.rootApp = rootApp;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
