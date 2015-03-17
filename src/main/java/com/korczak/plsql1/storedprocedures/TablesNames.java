package com.korczak.plsql1.storedprocedures;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.object.StoredProcedure;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class TablesNames extends StoredProcedure {

    private static final String GET_TABLE_NAMES_FUNCTION = "hr.get_tables_names";

    public TablesNames(DataSource dataSource) {
        super(dataSource, GET_TABLE_NAMES_FUNCTION);
        declareParameter(new SqlOutParameter("tables_names", Types.VARCHAR));
        setFunction(true);//you must set this as it distinguishes it from a sproc
        compile();
    }

    public String execute() {
        Map<String, String> inputParams = new HashMap();
        Map<String, Object> outputParams = execute(inputParams);
        if (!outputParams.isEmpty()) {
            Object tables_names = outputParams.get("tables_names");
            if (tables_names instanceof String) {
                return (String) tables_names;
            }
        }
        return null;
    }
}
