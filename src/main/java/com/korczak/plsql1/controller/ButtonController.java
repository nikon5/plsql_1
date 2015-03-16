package com.korczak.plsql1.controller;

import com.korczak.plsql1.TableCount;
import com.korczak.plsql1.TableDesc;
import com.korczak.plsql1.TablesNames;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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

    TableColumn firstNameCol = new TableColumn("Name");
    TableColumn lastNameCol = new TableColumn("Type");
    TableColumn emailCol = new TableColumn("IsNull");
    int temp = 0;

    public void onEmployeesAction(Event e) {
        TableCount myProcedure = applicationContext.getBean(TableCount.class);
        System.out.println(myProcedure.execute("employees"));
    }

    public void onCountriesAction(Event e) {

        TablesNames procedure = applicationContext.getBean(TablesNames.class);

        String allTableNames = procedure.execute();
        System.out.println(allTableNames);
        String[] splitedTableNames = allTableNames.split(",");
        List<String> listOfTables = Arrays.asList(splitedTableNames);

        ObservableList<String> items = FXCollections.observableArrayList(Arrays.asList(splitedTableNames));
        listOfTablesNames.setItems(items);
    }
    public void handleMouseClick(MouseEvent arg0) {
        if (listOfTablesNames.getSelectionModel().getSelectedItem() != null) {
            TableCount myProcedure = applicationContext.getBean(TableCount.class);
            Object tableCount = myProcedure.execute(listOfTablesNames.getSelectionModel().getSelectedItem()).get("table_count");
            countText.setText(tableCount.toString());
            TableDesc description = applicationContext.getBean(TableDesc.class);
            String tableDesc = description.execute(listOfTablesNames.getSelectionModel().getSelectedItem()).get("table_count").toString();
            System.out.println(tableDesc);
            firstNameCol.setCellValueFactory(
                    new PropertyValueFactory<>("name")
            );
            lastNameCol.setCellValueFactory(
                    new PropertyValueFactory<>("type")
            );
            emailCol.setCellValueFactory(
                    new PropertyValueFactory<>("isNull")
            );
             ObservableList<Desc> data = FXCollections.observableArrayList();
            String[] row = tableDesc.split(",");
            for (int i = 0; i<row.length ; i++){
                String[] cells = new String[]{"","",""};
                cells = row[i].split(":");
                if (cells[2].equals("empty")){
                    cells[2]="";
                }
                data.add(new Desc (cells[0], cells[1], cells[2]));
            }
            descTable.setItems(data);
            if(temp==0) {
                descTable.getColumns().addAll(firstNameCol, lastNameCol, emailCol);
            }
            temp++;
        }
    }

    public void onDepartmentsAction(Event e) {
        TableCount myProcedure = applicationContext.getBean(TableCount.class);
        System.out.println(myProcedure.execute("departments"));
    }

    public void onJobHistoryAction(Event e) {
        TableCount myProcedure = applicationContext.getBean(TableCount.class);
        System.out.println(myProcedure.execute("job_history"));
    }

    public void onJobsAction(Event e) {
        TableCount myProcedure = applicationContext.getBean(TableCount.class);
        System.out.println(myProcedure.execute("jobs"));
    }

    public void onLocationsAction(Event e) {
        TableCount myProcedure = applicationContext.getBean(TableCount.class);
        System.out.println(myProcedure.execute("locations"));
    }

    public void onRegionsAction(Event e) {
        TableCount myProcedure = applicationContext.getBean(TableCount.class);
        System.out.println(myProcedure.execute("regions"));
    }
    public static class Desc {

        private final SimpleStringProperty name;
        private final SimpleStringProperty type;
        private final SimpleStringProperty isNull;

        private Desc(String name, String type, String isNull) {
            this.name = new SimpleStringProperty(name);
            this.type = new SimpleStringProperty(type);
            this.isNull = new SimpleStringProperty(isNull);
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
