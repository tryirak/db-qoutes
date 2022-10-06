module com.example.greatestquotesofpolytech {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;

    opens com.dbquotes to javafx.fxml;
    exports com.dbquotes;
    exports com.dbquotes.controllers;
    opens com.dbquotes.controllers to javafx.fxml;
    exports com.dbquotes.models;
    opens com.dbquotes.models to javafx.fxml;
}