package com.dbquotes.controllers;

import com.dbquotes.Application;
import com.dbquotes.models.ApplicationUser;
import com.dbquotes.models.HandlerDB;
import com.dbquotes.models.Quote;
import com.dbquotes.utils.QueryStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class RecordEditableController extends RecordController {
    protected Quote quote;
    protected VBox recordContainer;
    protected ApplicationUser applicationUser;
    protected HBox record;
    protected Label messageLabel;
    protected Application rootApp;

    @FXML
    protected Button editRecordButton;

    @FXML
    protected Button deleteRecordButton;

    @FXML
    protected void onEditRecordButtonClick() {
        try {
            // connection validation
            HandlerDB.getConnection();
            HandlerDB.closeConnection();

            messageLabel.setText("");
            rootApp.showEditWindow(quote);
        } catch (SQLException e) {
            QueryStatus queryStatus = e.getSQLState().equals("08S01") ? QueryStatus.NO_CONNECTION : QueryStatus.UNKNOWN;
            messageLabel.setText(queryStatus.getText());
        }
    }

    @FXML
    protected void onDeleteRecordButtonClick() {
        QueryStatus queryStatus = quote.deleteQuote(applicationUser);
        switch (queryStatus) {
            case DONE -> {
                messageLabel.setText("");
                recordContainer.getChildren().remove(record);
                if (quote.owner().equals(applicationUser.getLogin()))
                    rootApp.getMainWindowController().deleteQuoteEvent(quote);
            }
            case NO_PERMISSIONS -> {
                messageLabel.setText("У тебя нет прав для этой операции.");
                rootApp.getMainWindowController().update();
            }
            default -> messageLabel.setText(queryStatus.getText());
        }
    }

    public void setRecordContainer(VBox recordContainer) {
        this.recordContainer = recordContainer;
    }

    public void setUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public void setQuote(Quote q) {
        quote = q;
        setQuoteLabel(String.format("«%s»", q.quote()));
        setTeacherLabel(q.teacher());
        setSubjectLabel(q.subject());
        setDateLabel(q.date());
        setOwnerLabel(q.owner());
        if (q.permissions().w())
            editRecordButton.setVisible(true);
        if (q.permissions().d())
            deleteRecordButton.setVisible(true);
    }

    public void setRecord(HBox record) {
        this.record = record;
    }

    public void setMessageLabel(Label messageLabel) {
        this.messageLabel = messageLabel;
    }

    public void setRootApp(Application rootApp) {
        this.rootApp = rootApp;
    }
}
