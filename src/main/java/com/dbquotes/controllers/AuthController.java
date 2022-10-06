package com.dbquotes.controllers;

import com.dbquotes.utils.QueryStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AuthController extends BaseController {

    @FXML
    protected TextField loginField;

    @FXML
    protected PasswordField passwordField;

    @FXML
    protected Label messageLabel;

    @Override
    protected void initialize() {
        super.initialize();
        messageLabel.setText("");
    }

    @FXML
    protected void onGuestButtonClick() {
        user.reset();
        QueryStatus queryStatus = user.authAsGuest();
        if (queryStatus == QueryStatus.DONE)
            rootApp.showMainWindow();
        else
            messageLabel.setText(queryStatus.getText());
    }

    @FXML
    protected void onSignUpButtonClick() {
        rootApp.showRegistrationWindow();
    }

    @FXML
    protected void onSignInButtonClick() {
        String login = loginField.getText();
        String password = passwordField.getText();

        if (login.length() == 0 || password.length() == 0)
            messageLabel.setText("Please, fill the fields.");

        QueryStatus queryStatus = user.auth(login, password);
        switch (queryStatus) {
            case DONE -> {
                user.parseCount();
                rootApp.showMainWindow();
            }
            case NO_ENTRY -> messageLabel.setText("Wrong login or password!");
            default -> messageLabel.setText(queryStatus.getText());
        }
    }
}