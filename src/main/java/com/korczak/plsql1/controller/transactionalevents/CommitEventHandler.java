package com.korczak.plsql1.controller.transactionalevents;

import com.korczak.plsql1.storedprocedures.TableInsertRows;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class CommitEventHandler implements EventHandler<ActionEvent> {

    private ApplicationContext applicationContext;
    private Stage dialog;
    private TextField rowsAmount;
    private TextField insertTime;

    public CommitEventHandler(ApplicationContext applicationContext, Stage dialog, TextField rowsAmount, TextField insertTime) {
        this.applicationContext = applicationContext;
        this.dialog = dialog;
        this.rowsAmount = rowsAmount;
        this.insertTime = insertTime;
    }

    @Override
    public void handle(ActionEvent arg0) {

        try {
                int howMany = Integer.parseInt(rowsAmount.getText());
                TableInsertRows tableInsertRowsProcedure = applicationContext.getBean(TableInsertRows.class);
                if (howMany > 0) {
                    BigDecimal insertTimeValue = tableInsertRowsProcedure.execute(howMany);
                    insertTime.setText(insertTimeValue.toString() + " [ms]");
                }

        } catch (NumberFormatException ex) {
            Scene dialogScene = new Scene(VBoxBuilder.create()
                        .children(new Text("Provide number"), buildOKButton())
                        .alignment(Pos.CENTER)
                        .padding(new Insets(10))
                        .build());
            dialog.setScene(dialogScene);
            }
        dialog.close();
    }

    private Button buildOKButton() {
        Button okButton = new Button("Ok");
        okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                dialog.close();
            }

        });
        return okButton;
    }
}
