package com.dbquotes.controllers;

import com.dbquotes.models.Permissions;
import com.dbquotes.models.Quote;
import com.dbquotes.utility.QueryStatus;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class EditController extends CreateController {
    protected Quote q;

    @FXML
    protected AnchorPane ownerPanel;

    @Override
    protected void initialize() {
        super.initialize();
        headerLabel.setText("Редактирование");
        setQuoteData();
        if (!applicationUser.getLogin().equals(q.owner()))
            ownerPanel.setVisible(false);
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
        QueryStatus queryStatus = q.editQuote(quote, teacher, subject, date, applicationUser, usersPermissionsHashMap);
        if (queryStatus == QueryStatus.DONE) {
            messageLabel.setText("");
            q = new Quote(q.id(), quote, teacher, subject, date, q.owner(), q.permissions());
            rootApp.getMainWindowController().editQuoteEvent(q);
            stage.close();
        } else {
            messageLabel.setText(queryStatus.getText());
        }
    }

    public void setQuote(Quote quote) {
        this.q = quote;
        usersPermissionsHashMap = Permissions.getPermissionsByRecordID(quote.id());
    }

    protected void setQuoteData() {
        quoteField.setText(q.quote());
        teacherField.setText(q.teacher());
        subjectField.setText(q.subject());
        ownerLabel.setText(q.owner());
        LocalDate ld = LocalDate.of(1900+q.date().getYear(), q.date().getMonth()+1, q.date().getDate());
        dateField.setValue(ld);
    }

}
