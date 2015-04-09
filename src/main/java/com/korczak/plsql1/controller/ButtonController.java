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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.sql.DataSource;
import javax.xml.transform.sax.SAXSource;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;

import static javafx.stage.FileChooser.*;


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
    private final static String DEFAULT_SEPARATOR = ";";
    private File loadDataToFileName;


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

        commitRollbackWindow(dialog);
    }

    private void commitRollbackWindow(Stage dialog) {

        Button commitButton = new Button("Commit");
        commitButton.setOnAction(new CommitEventHandler(applicationContext, dialog, howManyRows, insertTime));
        Button rollbackButton = new Button("Rollback");
        rollbackButton.setOnAction(new RollbackEventHandler(dialog));

        Scene dialogScene = new Scene(VBoxBuilder.create()
                .children(new Text("Do you want to commit your changes?"), rollbackButton, commitButton)
                .alignment(Pos.CENTER)
                .padding(new Insets(10))
                .build());
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void onSaveDataToFileProc(Event e) {

        TableDataSaveProc procedure = applicationContext.getBean(TableDataSaveProc.class);
        final Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        TextField separatorTextField = new TextField();
        separatorTextField.setText(DEFAULT_SEPARATOR);
        Button okButton = new Button("Ok");
        okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                if (!separatorTextField.getText().isEmpty()) {

                    procedure.execute(selectedTableName, separatorTextField.getText());
                } else {
                    procedure.execute(selectedTableName, DEFAULT_SEPARATOR);
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
                .children(new Text("Provide a separator (default is ; )"), separatorTextField, okButton, closeButton)
                .padding(new Insets(10))
                .build());
        dialog.setScene(dialogScene);
        dialog.show();
    }

        public void onLoadDataFromBackupTable(Event e) {
            TableDataLoadFromBckp procedure = applicationContext.getBean(TableDataLoadFromBckp.class);
            final Stage dialog = new Stage();
            dialog.initModality(Modality.WINDOW_MODAL);
            TextField commitField = new TextField();
            commitField.setText("20");

            Button closeButton = new Button("Close");
            closeButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent arg0) {
                    dialog.close();
                }

            });
            Button okButton = new Button("Ok");
            okButton.setOnAction(new EventHandler<ActionEvent>() {

                public static final String FILE_DIR = "OUTPUT_DIR";

                @Override
                public void handle(ActionEvent arg0) {

                    if(selectedTableName.toUpperCase().contains("_BCKP")){
                        dialog.close();
                        Scene dialogScene = new Scene(VBoxBuilder.create()
                                .children(new Text("Rodzynku,\n" +
                                        "Wybrałeś tabelę backup'ową (powinieneś 'oryginalną', bo do niej chcesz wczytać dane)."), closeButton)
                                .padding(new Insets(14))
                                .build());
                        dialog.setScene(dialogScene);
                        dialog.show();
                        return;
                    }

                    BigDecimal numberOfInserts = BigDecimal.ZERO;
                    if(commitField.getText().length() > 0){
                        try {
                           numberOfInserts =  procedure.execute(selectedTableName, Integer.parseInt(commitField.getText()));
                        } catch (NumberFormatException ex) {
                            Scene dialogScene = new Scene(VBoxBuilder.create()
                                    .children(new Text("Rodzynku,\n" +
                                            "Wpisałeś wartość, która nie jest liczbą."), closeButton)
                                    .padding(new Insets(14))
                                    .build());
                            dialog.setScene(dialogScene);
                            dialog.show();
                        }
                    }
                    System.out.println(numberOfInserts);
                    dialog.close();
                }

            });

            Scene dialogScene = new Scene(VBoxBuilder.create()
                    .children(new Text("Provide commit occurence"), commitField, okButton, closeButton)
                    .padding(new Insets(14))
                    .build());
            dialog.setScene(dialogScene);
            dialog.show();
        }

    public void onLoadDataFromFileToBackupTable(Event e) {
        LoadDataFromFileToBackupTable procedure = applicationContext.getBean(LoadDataFromFileToBackupTable.class);
        loadDataToFileName = null;
        final Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        TextField separatorField = new TextField();
        separatorField.setText(DEFAULT_SEPARATOR);

        Button fileChooserButton = new Button("Choose file...");
        fileChooserButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {

                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
                fileChooser.getExtensionFilters().add(txtFilter);
                fileChooser.getExtensionFilters().add(csvFilter);
                fileChooser.setInitialDirectory(new File("C:\\oracle_save_file"));

                loadDataToFileName = fileChooser.showOpenDialog(null);
            }

        });
        Button closeButton = new Button("Close");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                dialog.close();
            }

        });
        Button okButton = new Button("Ok");
        okButton.setOnAction(new EventHandler<ActionEvent>() {

            public static final String FILE_DIR = "OUTPUT_DIR";

            @Override
            public void handle(ActionEvent arg0) {

                String trimmedFileName = loadDataToFileName.getName().split("\\.")[0];
                if (loadDataToFileName != null && !selectedTableName.toUpperCase().equals(trimmedFileName.toUpperCase() + "_BCKP")) {
                    dialog.close();
                    Scene dialogScene = new Scene(VBoxBuilder.create()
                            .children(new Text("Rodzynku,\nSkasowałeś plik z danymi, tudzież nie wybrałeś żadnego, " +
                                    "\n ewentualnie wybrałeś tabelę oryginalną (nie backup'ową)."), closeButton)
                            .padding(new Insets(14))
                            .build());
                    dialog.setScene(dialogScene);
                    dialog.show();
                    return;
                }

                BigDecimal numberOfInserts = BigDecimal.ZERO;
                    String separator = separatorField.getText();
                    if(separator.isEmpty()){
                        separator = DEFAULT_SEPARATOR;
                    }
                    numberOfInserts = procedure.execute(FILE_DIR, loadDataToFileName.getName(), selectedTableName, separator);
                    System.out.println(numberOfInserts);
                dialog.close();
            }

        });

        Scene dialogScene = new Scene(VBoxBuilder.create()
                .children(/*new Text("Size of commit"), sizeOfCommit,*/ new Text("Separator"), separatorField, okButton, fileChooserButton, closeButton)
                .padding(new Insets(14))
                .build());
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /*private void commitRollbackWindow(Stage dialog, Button commitButton, Button rollbackButton) {
        Scene dialogScene = new Scene(VBoxBuilder.create()
                .children(new Text("Do you want to commit your changes?"), rollbackButton, commitButton)
                .alignment(Pos.CENTER)
                .padding(new Insets(10))
                .build());
        dialog.setScene(dialogScene);
        dialog.show();
    }*/

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

                    BigDecimal timeElapsed = procedure.execute(selectedTableName, separatorTextField.getText().toString());
                    String result = Float.parseFloat(timeElapsed.toString()) / 10 + " [ms]";
                    System.out.println("Persisting " + selectedTableName + " data to file takes: " + Float.parseFloat(timeElapsed.toString()) / 10 + " [ms]");
                    saveTime.setText(result);
                } else {
                    BigDecimal timeElapsed = procedure.execute(selectedTableName, DEFAULT_SEPARATOR);
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
                .children(new Text("Provide separator (default is ;)"), separatorTextField, okButton, closeButton)
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
