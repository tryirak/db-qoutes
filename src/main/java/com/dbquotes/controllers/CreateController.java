package com.dbquotes.controllers;

import com.dbquotes.models.*;
import com.dbquotes.utility.QueryStatus;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Date;
import java.time.ZoneId;
import java.util.HashMap;

public class CreateController extends BaseStageController {

    protected Users allUsers;
    protected HashMap<User, Permissions> usersPermissionsHashMap = new HashMap<>();

    @FXML
    protected Label headerLabel;

    @FXML
    protected TextArea quoteField;

    @FXML
    protected TextField teacherField;

    @FXML
    protected TextField subjectField;

    @FXML
    protected DatePicker dateField;

    @FXML
    protected Label ownerLabel;

    @FXML
    protected ListView<String> usersList;

    @FXML
    protected CheckBox readChecker;

    @FXML
    protected CheckBox editChecker;

    @FXML
    protected CheckBox deleteChecker;

    @FXML
    protected Label messageLabel;

    @FXML
    protected void onSetButtonClick() {
        ObservableList<String> selectedItems =  usersList.getSelectionModel().getSelectedItems();
        Permissions p = new Permissions(readChecker.isSelected(), editChecker.isSelected(), deleteChecker.isSelected());
        for (String selected : selectedItems) {
            User user = allUsers.getByName(selected);
            if (user != null)
                usersPermissionsHashMap.put(user, p);
        }
    }

    @Override
    protected void initialize() {
        super.initialize();
        headerLabel.setText("Создать новую запись");
        ownerLabel.setText("");
        allUsers = new Users();
        allUsers.parse(applicationUser);
        for (User user: allUsers)
            usersList.getItems().add(user.name());
        usersList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        messageLabel.setText("");
    }

    @FXML
    protected void onSaveButtonClick() {
        if (!validateFields()) {
            messageLabel.setText("Заполните поля.");
            return;
        }
        String quote = quoteField.getText();
        String teacher = teacherField.getText();
        String subject = subjectField.getText();
        Date date = Date.from(dateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        QueryStatus queryStatus = Quote.createQuote(quote, teacher, subject, date, applicationUser, usersPermissionsHashMap);
        switch (queryStatus) {
            case DONE -> {
                messageLabel.setText("");
                stage.close();
                applicationUser.countUpdate(true);
                rootApp.getMainWindowController().update();
            }
            default -> messageLabel.setText(queryStatus.getText());
        }
    }

    @FXML
    protected void onUserSelected() {
        ObservableList<String> selectedItems =  usersList.getSelectionModel().getSelectedItems();
        for (String selected : selectedItems) {
            User user = allUsers.getByName(selected);
            if (user != null && usersPermissionsHashMap.containsKey(user)) {
                Permissions p = usersPermissionsHashMap.get(user);
                readChecker.setSelected(p.r());
                editChecker.setSelected(p.w());
                deleteChecker.setSelected(p.d());
            } else {
                readChecker.setSelected(true);
                editChecker.setSelected(false);
                deleteChecker.setSelected(false);
            }
        }
    }

    protected boolean validateFields() {
        return !(quoteField.getText().isEmpty() ||
                teacherField.getText().isEmpty() ||
                subjectField.getText().isEmpty() ||
                dateField.getValue() == null);
    }
}
