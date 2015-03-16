package com.korczak.plsql1.controller;

import com.korczak.plsql1.TableCount;
import com.korczak.plsql1.TablesNames;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ButtonController extends GenericController {

    @FXML
    private ListView listOfTablesNames;

    public void onEmployeesAction(Event e) {
        TableCount myProcedure = applicationContext.getBean(TableCount.class);
        System.out.println(myProcedure.execute("employees"));
    }

    public void onCountriesAction(Event e) {

        TablesNames procedure = applicationContext.getBean(TablesNames.class);

        String allTableNames = procedure.execute();
        String[] splitedTableNames = allTableNames.split(",");
        List<String> listOfTables = Arrays.asList(splitedTableNames);

        ObservableList<String> items = FXCollections.observableArrayList(Arrays.asList(splitedTableNames));
        listOfTablesNames.setItems(items);
    }
//    public void onCountriesAction(Event e){
//        TableCount myProcedure = applicationContext.getBean(TableCount.class);
//        System.out.println(myProcedure.execute("countries"));
//    }

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
