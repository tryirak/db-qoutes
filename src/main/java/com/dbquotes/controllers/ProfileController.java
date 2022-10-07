package com.dbquotes.controllers;

import com.dbquotes.utils.QueryStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;

public class ProfileController extends BaseStageController {
    @FXML
    protected TextField loginField;

    @FXML
    protected TextField newPasswordField;

    @FXML
    protected TextField repeatNewPasswordField;

    @FXML
    protected Label messageText;

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

        boolean loginLengthCondition = login.length() < 256;
        boolean passwordEnterCondition = !repeatNewPasswordField.equals(newPasswordField.getText());
        boolean notEmpty = login.length() > 0 && password.length() > 0;
        boolean passwordWithoutSpaces = !password.contains(" ") && !login.contains(" ");

        if (!loginLengthCondition)
            messageText.setText("Please, use login from 6 to 255 symbols.");
        else if (!passwordEnterCondition)
            messageText.setText("Please, repeat password carefully.");
        else if (!notEmpty)
            messageText.setText("Please, fill the fields.");
        else if (!passwordWithoutSpaces)
            messageText.setText("Please, don't use spaces.");

        if (loginLengthCondition && passwordEnterCondition && notEmpty && passwordWithoutSpaces) {
            QueryStatus queryStatus = applicationUser.edit(loginField.getText(), newPasswordField.getText());
            switch (queryStatus) {
                case DONE -> {
                    messageText.setTextFill(Paint.valueOf("GREEN"));
                    messageText.setText("Ваши данные были сохранены успешно!");
                    applicationUser.auth(loginField.getText(), newPasswordField.getText());
                    stage.close();
                    rootApp.getMainWindowController().update();
                }
                case DUPLICATE -> messageText.setText(
                        "User with this login already exist! Please, use another one!.");
                default -> messageText.setText(queryStatus.getText());
            }
        }
    }
}
