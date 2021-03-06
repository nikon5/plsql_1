package com.korczak.plsql1.storedprocedures;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class TableCount extends StoredProcedure {

    private static final String TABLE_COUNT_FUNCTION = "hr.table_row_count";

    public TableCount(DataSource dataSource) {
        super(dataSource, TABLE_COUNT_FUNCTION);
        declareParameter(new SqlOutParameter("table_count", Types.NUMERIC));
        declareParameter(new SqlParameter("table_name", Types.VARCHAR));
        setFunction(true);//you must set this as it distinguishes it from a sproc
        compile();
    }

    public BigDecimal execute(String tableName) {
        Map<String, String> inputParams = new HashMap();
        inputParams.put("table_name", tableName);

        Map<String, Object> outputParams = execute(inputParams);
        if (!outputParams.isEmpty()) {
            Object rowCount = outputParams.get("table_count");
            if (rowCount instanceof BigDecimal) {
                return (BigDecimal) rowCount;
            }
        }
        return null;
    }

}
