package com.dbquotes.controllers;

import com.dbquotes.models.ApplicationUser;
import com.dbquotes.utility.QueryStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RegistrationController extends BaseController {
    @FXML
    protected TextField loginField;

    @FXML
    protected TextField passwordField;

    @FXML
    protected TextField repeatPasswordField;

    @FXML
    protected Label messageLabel;

    @Override
    protected void initialize() {
        super.initialize();
        messageLabel.setText("");
    }

    @FXML
    protected void onBackButtonClick() {
        rootApp.showAuthWindow();
    }

    @FXML
    protected void onSignUpButtonClick() {
        String login = loginField.getText();
        String password = passwordField.getText();
        String repeatedPassword = repeatPasswordField.getText();

        if (applicationUser.validateUpdatedUserData(login, password, repeatedPassword, messageLabel)){
            QueryStatus queryStatus = ApplicationUser.createNewUser(loginField.getText(), passwordField.getText());
            switch (queryStatus) {
                case DONE -> rootApp.showAuthWindow();
                case DUPLICATE -> messageLabel.setText("Данный логин уже занят! Выберите другой.");
                default -> messageLabel.setText(queryStatus.getText());
            }
        }
    }
}
