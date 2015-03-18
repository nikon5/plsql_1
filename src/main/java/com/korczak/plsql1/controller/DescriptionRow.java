package com.korczak.plsql1.controller;

import javafx.beans.property.SimpleStringProperty;

public class DescriptionRow {

    private final SimpleStringProperty name;
    private final SimpleStringProperty isNull;
    private final SimpleStringProperty type;

    DescriptionRow(String name, String isNull, String type) {
        this.name = new SimpleStringProperty(name);
        this.isNull = new SimpleStringProperty(isNull);
        this.type = new SimpleStringProperty(type);
    }

    public String getName() {
        return name.get();
    }

    public String getIsNull() {
        return isNull.get();
    }

    public String getType() {
        return type.get();
    }
}