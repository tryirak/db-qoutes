package com.dbquotes.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordController extends HBox {

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @FXML
    protected Label quoteLabel;

    @FXML
    protected Label dateLabel;

    @FXML
    protected Label subjectLabel;

    @FXML
    protected Label teacherLabel;

    @FXML
    protected Label ownerLabel;

    // TODO При значениях нуль в дате выдает ошибку, исправить
    public void setQuoteLabel(String quote) {
        quoteLabel.setText(quote);
    }

    public void setDateLabel(Date date) {
        dateLabel.setText(format.format(date));
    }

    public void setSubjectLabel(String subject) {
        subjectLabel.setText(subject);
    }

    public void setTeacherLabel(String teacher) {
        teacherLabel.setText(teacher);
    }

    public void setOwnerLabel(String user) {
        ownerLabel.setText(user);
    }
}
