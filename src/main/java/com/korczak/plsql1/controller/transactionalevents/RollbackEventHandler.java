package com.korczak.plsql1.controller.transactionalevents;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class RollbackEventHandler implements EventHandler<ActionEvent> {

    private Stage dialog;
    private Connection connection;

    public RollbackEventHandler(Connection connection, Stage dialog) {
        this.connection = connection;
        this.dialog = dialog;
    }

    @Override
    public void handle(ActionEvent arg0) {
        try {
            connection.rollback();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            dialog.close();
        }
    }
}
