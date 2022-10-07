package com.dbquotes.controllers;

import com.dbquotes.models.ApplicationUser;
import com.dbquotes.utils.QueryStatus;
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

        boolean loginLengthCondition = login.length() < 256;
        boolean passwordEnterCondition = password.equals(repeatPasswordField.getText());
        boolean notEmpty = login.length() > 0 && password.length() > 0;
        boolean passwordWithoutSpaces = !password.contains(" ") && !login.contains(" ");

        if (!loginLengthCondition)
            messageLabel.setText("Используйте пароль длинной меньше чем 256 символов.");
        else if (!passwordEnterCondition)
            messageLabel.setText("Повторите пароль правильно.");
        else if (!notEmpty)
            messageLabel.setText("Введите логин и пароль.");
        else if (!passwordWithoutSpaces)
            messageLabel.setText("Не используйте в пароли пробелы!");

        if (loginLengthCondition && passwordEnterCondition && notEmpty && passwordWithoutSpaces) {
            QueryStatus queryStatus = ApplicationUser.createNewUser(loginField.getText(), passwordField.getText());
            switch (queryStatus) {
                case DONE -> rootApp.showAuthWindow();
                case DUPLICATE -> messageLabel.setText("Данный логин уже занят! Выберите другой.");
                default -> messageLabel.setText(queryStatus.getText());
            }
        }
    }
}
