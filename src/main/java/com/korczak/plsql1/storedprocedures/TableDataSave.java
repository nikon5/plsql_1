package com.korczak.plsql1.storedprocedures;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class TableDataSave extends StoredProcedure {

    private static final String TABLE_DATA_SAVE_FUNCTION = "hr.write_table_data_to_file";

    public TableDataSave(DataSource dataSource) {
        super(dataSource, TABLE_DATA_SAVE_FUNCTION);
        declareParameter(new SqlOutParameter("time_elapsed", Types.NUMERIC));
        declareParameter(new SqlParameter("table_name", Types.VARCHAR));
        declareParameter(new SqlParameter("separator", Types.VARCHAR));
        setFunction(true);//you must set this as it distinguishes it from a sproc
        compile();
    }

    public BigDecimal execute(String tableName, String separator) {
        Map<String, String> inputParams = new HashMap();
        inputParams.put("table_name", tableName);
        inputParams.put("separator", separator);

        Map<String, Object> outputParams = execute(inputParams);
        if (!outputParams.isEmpty()) {
            Object time_elapsed = outputParams.get("time_elapsed");
            if (time_elapsed instanceof BigDecimal) {
                return (BigDecimal) time_elapsed;
            }
        }
        return null;
    }

}
