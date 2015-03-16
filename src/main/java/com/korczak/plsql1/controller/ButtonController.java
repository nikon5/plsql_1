package com.korczak.plsql1.controller;

import com.korczak.plsql1.TableCount;
import com.korczak.plsql1.TableDesc;
import com.korczak.plsql1.TablesNames;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ButtonController extends GenericController {

    @FXML
    private ListView listOfTablesNames;

    @FXML
    private TextArea countText;

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
//    public void onCountriesAction(Event e){
//        TableCount myProcedure = applicationContext.getBean(TableCount.class);
//        System.out.println(myProcedure.execute("countries"));
//    }
    public void handleMouseClick(MouseEvent arg0) {
        if (listOfTablesNames.getSelectionModel().getSelectedItem() != null) {
            TableCount myProcedure = applicationContext.getBean(TableCount.class);
            Object tableCount = myProcedure.execute(listOfTablesNames.getSelectionModel().getSelectedItem()).get("table_count");
            countText.setText(tableCount.toString());
           // TableDesc description = applicationContext.getBean(TableDesc.class);
            //Object tableDesc = description.execute(listOfTablesNames.getSelectionModel().getSelectedItem()).get("table_count");
           // System.out.println(description.execute(listOfTablesNames.getSelectionModel().getSelectedItem()));
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
}
