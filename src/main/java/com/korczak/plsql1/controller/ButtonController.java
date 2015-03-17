package com.korczak.plsql1.controller;

import com.korczak.plsql1.TableCount;
import com.korczak.plsql1.TableDesc;
import com.korczak.plsql1.TableDescSave;
import com.korczak.plsql1.TablesNames;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ButtonController extends GenericController {

    @FXML
    private ListView listOfTablesNames;

    @FXML
    private TextArea countText;

    @FXML
    private TableView descTable;

    private final TableColumn nameCol = new TableColumn("Name");
    private final TableColumn nullCol = new TableColumn("Null");
    private final TableColumn typeCol = new TableColumn("Type");
    private int temp = 0;

    private Object selectedTableName = null;

    public void onGetTableNamesAction(Event e) {

        TablesNames tableNamesProcedure = applicationContext.getBean(TablesNames.class);

        String allTableNames = tableNamesProcedure.execute();
        String[] splittedTableNames = allTableNames.split(",");

        ObservableList<String> items = FXCollections.observableArrayList(splittedTableNames);
        listOfTablesNames.setItems(items);
    }

    public void handleMouseClick(MouseEvent arg0) {
        selectedTableName = listOfTablesNames.getSelectionModel().getSelectedItem();
        if (selectedTableName != null) {
            TableCount myProcedure = applicationContext.getBean(TableCount.class);
            Object tableCount = myProcedure.execute(selectedTableName).get("table_count");
            countText.setText(tableCount.toString());
            TableDesc description = applicationContext.getBean(TableDesc.class);
            String tableDesc = description.execute(selectedTableName).get("table_count").toString();
            nameCol.setCellValueFactory(
                    new PropertyValueFactory<>("name")
            );
            nullCol.setCellValueFactory(
                    new PropertyValueFactory<>("isNull")
            );
            typeCol.setCellValueFactory(
                    new PropertyValueFactory<>("type")
            );
            nameCol.setPrefWidth(124);
            nullCol.setPrefWidth(124);
            typeCol.setPrefWidth(124);
            ObservableList<Desc> data = FXCollections.observableArrayList();
            String[] row = tableDesc.split(",");
            for (int i = 0; i < row.length; i++) {
                String[] cells = new String[]{"", "", ""};
                cells = row[i].split(":");
                if (cells[1].equals("empty")) {
                    cells[1] = "";
                }
                data.add(new Desc(cells[0], cells[1], cells[2]));
            }
            descTable.setItems(data);
            if (temp == 0) {
                descTable.getColumns().addAll(nameCol, nullCol, typeCol);
            }
            temp++;
        }
    }

    public void onTableDescSaveAction(Event e) {
        TableDescSave procedure = applicationContext.getBean(TableDescSave.class);
        procedure.execute(selectedTableName);
    }

    public static class Desc {

        private final SimpleStringProperty name;
        private final SimpleStringProperty type;
        private final SimpleStringProperty isNull;

        private Desc(String name, String isNull, String type) {
            this.name = new SimpleStringProperty(name);
            this.isNull = new SimpleStringProperty(isNull);
            this.type = new SimpleStringProperty(type);
        }

        public String gettName() {
            return name.get();
        }

        public String getType() {
            return type.get();
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public SimpleStringProperty typeProperty() {
            return type;
        }

        public String getIsNull() {
            return isNull.get();
        }

        public SimpleStringProperty isNullProperty() {
            return isNull;
        }

        public void setType(String type) {
            this.type.set(type);
        }

        public void setIsNull(String isNull) {
            this.isNull.set(isNull);
        }

        public void setName(String name) {
            this.name.set(name);
        }
    }
}

