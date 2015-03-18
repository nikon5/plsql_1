package com.korczak.plsql1.controller;

import com.korczak.plsql1.storedprocedures.TableCount;
import com.korczak.plsql1.storedprocedures.TableDescription;
import com.korczak.plsql1.storedprocedures.TableDescriptionSave;
import com.korczak.plsql1.storedprocedures.TablesNames;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.math.BigDecimal;


public class ButtonController extends GenericController {

    @FXML
    private ListView listOfTablesNames;

    @FXML
    private TextArea countText;

    @FXML
    private TableView descTable;

    private final TableColumn nameColumn = new TableColumn("Name");
    private final TableColumn nullableColumn = new TableColumn("Nullable");
    private final TableColumn typeColumn = new TableColumn("Type");
    private int rowCounter = 0;

    private String selectedTableName = null;


    public void onGetTableNamesAction(Event e) {

        TablesNames tableNamesProcedure = applicationContext.getBean(TablesNames.class);

        String allTableNames = tableNamesProcedure.execute();
        String[] splittedTableNames = allTableNames.split(",");

        ObservableList<String> items = FXCollections.observableArrayList(splittedTableNames);
        listOfTablesNames.setItems(items);
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
                for(String row : rows){
                    String[] cells = row.split(":");
                if (cells[1].equals("empty")) {
                    cells[1] = "";
                }
                descriptionTableList.add(new DescriptionRow(cells[0], cells[1], cells[2]));
                if(rowCounter == 0) {
                    addHader();
                }
                rowCounter++;
            }
            descTable.setItems(descriptionTableList);
        }
    }

    private void addHader() {

        descTable.getColumns().addAll(nameColumn, nullableColumn, typeColumn);
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

    public void onTableDescriptionSaveAction(Event e) {

        TableDescriptionSave procedure = applicationContext.getBean(TableDescriptionSave.class);
        procedure.execute(selectedTableName);
    }
}