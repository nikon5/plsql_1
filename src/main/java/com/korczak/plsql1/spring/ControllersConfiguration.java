package com.korczak.plsql1.spring;

import com.korczak.plsql1.storedprocedures.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class ControllersConfiguration {

    @Autowired
    private DataSource dataSource;

    @Bean
    public TableCount getTableCount() throws SQLException {
        return new TableCount(dataSource);
    }

    @Bean
    public TablesNames getTablesNames() throws SQLException {
        return new TablesNames(dataSource);
    }

    @Bean
    public TableDescription getTablesDescription() throws SQLException {
        return new TableDescription(dataSource);
    }

    @Bean
    public TableDescriptionSave saveTableDesc() throws SQLException {
        return new TableDescriptionSave(dataSource);
    }

    @Bean
    public TableInsertRows getTablesRows() throws SQLException {
        return new TableInsertRows(dataSource);
    }

    @Bean
    public TableDataSave saveTableData() throws SQLException {
        return new TableDataSave(dataSource);
    }
}
