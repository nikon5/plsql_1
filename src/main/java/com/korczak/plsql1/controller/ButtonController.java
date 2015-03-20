package com.korczak.plsql1.controller;

import com.korczak.plsql1.TableInputRows;
import com.korczak.plsql1.storedprocedures.TableCount;
import com.korczak.plsql1.storedprocedures.TableDescription;
import com.korczak.plsql1.storedprocedures.TableDescriptionSave;
import com.korczak.plsql1.storedprocedures.TablesNames;
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
import javafx.scene.layout.Border;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.Collections;


public class ButtonController extends GenericController {

    @FXML
    private ListView listOfTablesNames;

    @FXML
    private TextArea countText;

    @FXML
    private TextField howManyRows;

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

    public void onInsertRows(Event e) {
        setRed(howManyRows);

        if (howManyRows.getText().length()== 0 ) {
            return;
        }
        final Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);


        Button okButton = new Button("Ok");
        okButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent arg0) {
                dialog.close();
            }

        });
        Button noButton = new Button("No");
        noButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent arg0) {
                dialog.close();
            }

        });
        Button yesButton = new Button("Yes");
        yesButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent arg0) {
                TableInputRows procedure = applicationContext.getBean(TableInputRows.class);
                int howMany;
                try {
                    howMany = Integer.parseInt(howManyRows.getText());
                } catch (NumberFormatException ex){
                    howMany = 0;
                }
                if (howMany>0){
                    procedure.execute(howMany);
                }
                dialog.close();
            }

        });
        int howMany;
        try {
            howMany = Integer.parseInt(howManyRows.getText());
            Scene dialogScene = new Scene(VBoxBuilder.create()
                    .children(new Text("Do you want to commit your changes?"), noButton, yesButton)
                    .alignment(Pos.CENTER)
                    .padding(new Insets(10))
                    .build());
            dialog.setScene(dialogScene);
            dialog.show();
        } catch (NumberFormatException ex){
            howMany = 0;
            Scene dialogScene = new Scene(VBoxBuilder.create()
                    .children(new Text("Provide number"), okButton)
                    .alignment(Pos.CENTER)
                    .padding(new Insets(10))
                    .build());
            dialog.setScene(dialogScene);
            dialog.show();
        }
    }

    private void setRed(TextField tf) {
        ObservableList<String> styleClass = tf.getStyleClass();

        if(!styleClass.contains("tferror")) {
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
