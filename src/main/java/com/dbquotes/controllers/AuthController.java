package com.dbquotes.controllers;

import com.dbquotes.utility.QueryStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AuthController extends FrameController {

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
        applicationUser.reset();
        QueryStatus queryStatus = applicationUser.authAsGuest();
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
            messageLabel.setText("Пожалуйста, заполните поля.");

        QueryStatus queryStatus = applicationUser.auth(login, password);
        switch (queryStatus) {
            case DONE -> {
                applicationUser.parseCount();
                rootApp.showMainWindow();
            }
            case EMPTY -> messageLabel.setText("Неверный логин или пароль!");
            default -> messageLabel.setText(queryStatus.getText());
        }
    }
}