package com.korczak.plsql1.controller;

import com.korczak.plsql1.controller.transactionalevents.CommitEventHandler;
import com.korczak.plsql1.controller.transactionalevents.RollbackEventHandler;
import com.korczak.plsql1.spring.DatabaseConfiguration;
import com.korczak.plsql1.storedprocedures.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;


public class ButtonController extends GenericController {

    @FXML
    private ListView listOfTablesNames;

    @FXML
    private TextArea countText;

    @FXML
    private TextField howManyRows;

    @FXML
    private TextField insertTime;

    @FXML
    private TextField saveTime;

    @FXML
    private TableView tableDescription;

    private final TableColumn nameColumn = new TableColumn("Name");
    private final TableColumn nullableColumn = new TableColumn("Nullable");
    private final TableColumn typeColumn = new TableColumn("Type");
    private int rowCounter = 0;

    private String selectedTableName = null;
    private final static String DEFAULT_SEPARATOR = ";"; //TODO: MUST FIX IT


    public void onGetTableNamesAction(Event e) {

        TablesNames tableNamesProcedure = applicationContext.getBean(TablesNames.class);

        String allTableNames = tableNamesProcedure.execute();
        String[] splittedTableNames = allTableNames.split(",");

        ObservableList<String> items = FXCollections.observableArrayList(splittedTableNames);
        listOfTablesNames.setItems(items);
    }

    public void onInsertRows(Event e) {
        if (howManyRows.getText().length() == 0) {
            setRed(howManyRows);
            return;
        } else {
            removeRed(howManyRows);
        }
        final Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);


        Button okButton = new Button("Ok");
        okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                dialog.close();
            }

        });

        DataSource dataSource = null;
        try {
            dataSource = applicationContext.getBean(DatabaseConfiguration.class).getDataSource();
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            Button commitButton = new Button("Commit");
            commitButton.setOnAction(new CommitEventHandler(connection, dialog));
            Button rollbackButton = new Button("Rollback");
            rollbackButton.setOnAction(new RollbackEventHandler(connection, dialog));

            int howMany;
            try {
                howMany = Integer.parseInt(howManyRows.getText());
                TableInsertRows tableInsertRowsProcedure = applicationContext.getBean(TableInsertRows.class);
                if (howMany > 0) {
                    BigDecimal insertTimeValue = tableInsertRowsProcedure.execute(howMany);
                    insertTime.setText(insertTimeValue.toString() + " [ms]");
                }
                commitRollbackWindow(dialog, commitButton, rollbackButton);

            } catch (NumberFormatException ex) {
                howMany = 0;
                Scene dialogScene = new Scene(VBoxBuilder.create()
                        .children(new Text("Provide number"), okButton)
                        .alignment(Pos.CENTER)
                        .padding(new Insets(10))
                        .build());
                dialog.setScene(dialogScene);
                dialog.show();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void onSaveData2(Event e) {
        TableDataSave procedure = applicationContext.getBean(TableDataSave.class);
        final Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        TextField separatorTextField = new TextField();
        separatorTextField.setText(DEFAULT_SEPARATOR);
        Button okButton = new Button("Ok");
        okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                if (separatorTextField.getText().length() > 0) {

                    BigDecimal timeElapsed = procedure.execute(selectedTableName, separatorTextField.getText().toString());  //TODO: SEPARATOR MUST BE REAL PARAMETER - READ FROM USER
                    String result = Float.parseFloat(timeElapsed.toString()) / 10 + " [ms]";
                    System.out.println("Persisting " + selectedTableName + " data to file takes: " + Float.parseFloat(timeElapsed.toString()) / 10 + " [ms]");
                    saveTime.setText(result);
                } else {
                    BigDecimal timeElapsed = procedure.execute(selectedTableName, DEFAULT_SEPARATOR);  //TODO: SEPARATOR MUST BE REAL PARAMETER - READ FROM USER
                    String result = Float.parseFloat(timeElapsed.toString()) / 10 + " [ms]";
                    System.out.println("Persisting " + selectedTableName + " data to file takes: " + Float.parseFloat(timeElapsed.toString()) / 10 + " [ms]");
                    saveTime.setText(result);
                }
                dialog.close();
            }

        });
        Button closeButton = new Button("Close");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                dialog.close();
            }

        });
        Scene dialogScene = new Scene(VBoxBuilder.create()
                .children(new Text("Provide separator (Default ;)"), separatorTextField, okButton, closeButton)
                .padding(new Insets(10))
                .build());
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void commitRollbackWindow(Stage dialog, Button commitButton, Button rollbackButton) {
        Scene dialogScene = new Scene(VBoxBuilder.create()
                .children(new Text("Do you want to commit your changes?"), rollbackButton, commitButton)
                .alignment(Pos.CENTER)
                .padding(new Insets(10))
                .build());
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void setRed(TextField textField) {
        ObservableList<String> styleClass = textField.getStyleClass();

        if (!styleClass.contains("tferror")) {
            styleClass.add("tferror");
        }
    }

    private void removeRed(TextField tf) {
        ObservableList<String> styleClass = tf.getStyleClass();
        styleClass.removeAll(Collections.singleton("tferror"));
    }

    public void handleMouseClick(MouseEvent arg0) {

        selectedTableName = (String) listOfTablesNames.getSelectionModel().getSelectedItem();
        if (selectedTableName != null) {
            TableCount tableCountProcedure = applicationContext.getBean(TableCount.class);
            BigDecimal tableCount = tableCountProcedure.execute(selectedTableName);
            countText.setText(tableCount.toString());

            TableDescription tableDescriptionProcedure = applicationContext.getBean(TableDescription.class);
            String tableDesc = tableDescriptionProcedure.execute(selectedTableName);

            prepareTableColumns();

            String[] rows = tableDesc.split(",");

            ObservableList<DescriptionRow> descriptionTableList = FXCollections.observableArrayList();
            for (String row : rows) {
                String[] cells = row.split(":");
                if (cells[1].equals("empty")) {
                    cells[1] = "";
                }
                descriptionTableList.add(new DescriptionRow(cells[0], cells[1], cells[2]));
                if (rowCounter == 0) {
                    addHader();
                }
                rowCounter++;
            }
            tableDescription.setItems(descriptionTableList);
        }
    }

    public void onTableDescriptionSaveAction(Event e) {

        TableDescriptionSave procedure = applicationContext.getBean(TableDescriptionSave.class);
        procedure.execute(selectedTableName);
    }

    public void onTableDataSaveAction(Event e) {
        TableDataSave procedure = applicationContext.getBean(TableDataSave.class);
        final Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        TextField separatorTextField = new TextField();
        separatorTextField.setText(DEFAULT_SEPARATOR);
        Button okButton = new Button("Ok");
        okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                if (separatorTextField.getText().length() > 0) {

                    BigDecimal timeElapsed = procedure.execute(selectedTableName, separatorTextField.getText().toString());  //TODO: SEPARATOR MUST BE REAL PARAMETER - READ FROM USER
                    String result = Float.parseFloat(timeElapsed.toString()) / 10 + " [ms]";
                    System.out.println("Persisting " + selectedTableName + " data to file takes: " + Float.parseFloat(timeElapsed.toString()) / 10 + " [ms]");
                    saveTime.setText(result);
                } else {
                    BigDecimal timeElapsed = procedure.execute(selectedTableName, DEFAULT_SEPARATOR);  //TODO: SEPARATOR MUST BE REAL PARAMETER - READ FROM USER
                    String result = Float.parseFloat(timeElapsed.toString()) / 10 + " [ms]";
                    System.out.println("Persisting " + selectedTableName + " data to file takes: " + Float.parseFloat(timeElapsed.toString()) / 10 + " [ms]");
                    saveTime.setText(result);
                }
                dialog.close();
            }

        });
        Button closeButton = new Button("Close");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                dialog.close();
            }

        });
        Scene dialogScene = new Scene(VBoxBuilder.create()
                .children(new Text("Provide separator (Default ;)"), separatorTextField, okButton, closeButton)
                .padding(new Insets(10))
                .build());
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void addHader() {

        tableDescription.getColumns().addAll(nameColumn, nullableColumn, typeColumn);
    }

    private void prepareTableColumns() {

        setCellValueFactories();
        setFixedWidthOnColumns();
    }

    private void setCellValueFactories() {

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nullableColumn.setCellValueFactory(new PropertyValueFactory<>("isNull"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    private void setFixedWidthOnColumns() {

        nameColumn.setPrefWidth(124);
        nullableColumn.setPrefWidth(124);
        typeColumn.setPrefWidth(124);
    }
}
