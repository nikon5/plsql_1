package com.korczak.plsql1;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class TableDescSave extends StoredProcedure {

    private static final String SQL = "hr.write_to_file";

    public TableDescSave(DataSource dataSource) {
        super(dataSource, SQL);
//        declareParameter(new SqlOutParameter("table_names", Types.VARCHAR));
        declareParameter(new SqlParameter("table_name", Types.VARCHAR));
        setFunction(false);//you must set this as it distinguishes it from a sproc
        compile();
    }

    public void execute(String tableName) {
        Map<String, String> inputParams = new HashMap();
        inputParams.put("table_name", tableName);
//        Map<String, Object> outputParams = execute(inputParams);
        execute(inputParams);
        /*if (!outputParams.isEmpty()) {
            Object tables_names = outputParams.get("table_name");
            if (tables_names instanceof String) {
                return (String) tables_names;
            }
        }*/
//        return null;
    }
}
