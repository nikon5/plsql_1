package com.korczak.plsql1.storedprocedures;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class TableDescription extends StoredProcedure {

    private static final String GET_TABLE_DESCRIPTION_FUNCTION = "hr.get_table_description";

    public TableDescription(DataSource dataSource) {
        super(dataSource, GET_TABLE_DESCRIPTION_FUNCTION);
        declareParameter(new SqlOutParameter("table_desc", Types.VARCHAR));
        declareParameter(new SqlParameter("table_name", Types.VARCHAR));
        setFunction(true);//you must set this as it distinguishes it from a sproc
        compile();
    }

    public String execute(String tableName) {
        Map<String, String> inputParams = new HashMap();
        inputParams.put("table_name", tableName);

        Map<String, Object> outputParams = execute(inputParams);
        if (!outputParams.isEmpty()) {
            Object tableDesc = outputParams.get("table_desc");
            if (tableDesc instanceof String) {
                return (String) tableDesc;
            }
        }
        return null;
    }
}
