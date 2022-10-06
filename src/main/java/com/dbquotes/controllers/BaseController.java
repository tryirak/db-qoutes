package com.dbquotes.controllers;

import com.dbquotes.Application;
import com.dbquotes.models.User;
import javafx.fxml.FXML;

public abstract class BaseController {
    protected Application rootApp;
    protected User user;

    @FXML
    protected void initialize() {
        user = User.getInstance();
    }

    public void setAppFX(Application rootApp) {
        this.rootApp = rootApp;
    }
}
