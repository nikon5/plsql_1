package com.korczak.plsql1.spring;

import com.korczak.plsql1.TableCount;
import com.korczak.plsql1.TableDesc;
import com.korczak.plsql1.TableDescSave;
import com.korczak.plsql1.TablesNames;
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
    public TableDesc getTablesDescription() throws SQLException {
        return new TableDesc(dataSource);
    }

    @Bean
    public TableDescSave saveTableDesc() throws SQLException {
        return new TableDescSave(dataSource);
    }

}
