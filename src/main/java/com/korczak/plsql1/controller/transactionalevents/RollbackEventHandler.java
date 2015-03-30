package com.korczak.plsql1.controller.transactionalevents;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class RollbackEventHandler implements EventHandler<ActionEvent> {

    private Stage dialog;

    public RollbackEventHandler(Stage dialog) {
        this.dialog = dialog;
    }

    @Override
    public void handle(ActionEvent arg0) {
        dialog.close();
    }
}
