package com.dbquotes.controllers;

import com.dbquotes.Application;
import com.dbquotes.models.ApplicationUser;
import javafx.fxml.FXML;

public abstract class BaseController {
    protected Application rootApp;
    protected ApplicationUser applicationUser;

    @FXML
    protected void initialize() {
        applicationUser = ApplicationUser.getInstance();
    }

    public void setAppFX(Application rootApp) {
        this.rootApp = rootApp;
    }
}
