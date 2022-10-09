package com.dbquotes.controllers;

import com.dbquotes.utility.QueryStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;

public class ProfileController extends FrameController {
    @FXML
    protected TextField loginField;

    @FXML
    protected TextField newPasswordField;

    @FXML
    protected TextField repeatNewPasswordField;

    @FXML
    protected Label messageLabel;

    @FXML
    protected Label roleLabel;

    @Override
    public void initialize() {
        super.initialize();

        loginField.setText(applicationUser.getLogin());
        roleLabel.setText(applicationUser.getRole().toString());
    }

    @FXML
    protected void onSaveButtonClick() {
        String login = loginField.getText();
        String password = newPasswordField.getText();
        String repeatedPassword = repeatNewPasswordField.getText();

        if (applicationUser.validateUpdatedUserData(login, password, repeatedPassword, messageLabel)) {
            QueryStatus queryStatus = applicationUser.edit(loginField.getText(), newPasswordField.getText());
            switch (queryStatus) {
                case DONE -> {
                    messageLabel.setTextFill(Paint.valueOf("GREEN"));
                    messageLabel.setText("Ваши данные были сохранены успешно!");
                    applicationUser.auth(loginField.getText(), newPasswordField.getText());
                    stage.close();
                    rootApp.getMainWindowController().update();
                }
                case DUPLICATE -> messageLabel.setText("Данный логин уже занят! Выберите другой.");
                default -> messageLabel.setText(queryStatus.getText());
            }
        }
    }
}
