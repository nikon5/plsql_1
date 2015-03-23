package com.korczak.plsql1.storedprocedures;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class TableInputRows extends StoredProcedure {

    private static final String INSERT_INTO_JOBS_TABLE_FUNCTION = "hr.insert_jobs_rows";

    public TableInputRows(DataSource dataSource) {
        super(dataSource, INSERT_INTO_JOBS_TABLE_FUNCTION);
        declareParameter(new SqlOutParameter("time_elapsed", Types.NUMERIC));
        declareParameter(new SqlParameter("rows_number", Types.NUMERIC));
        setFunction(true);
        compile();
    }

    public BigDecimal execute(Integer rows_number) {
        Map<String, Integer> inputParams = new HashMap();
        inputParams.put("rows_number", rows_number);
        Map<String, Object> outputParams = execute(inputParams);
        if (!outputParams.isEmpty()) {
            Object timeElapsed = outputParams.get("time_elapsed");
            if (timeElapsed instanceof BigDecimal) {
                return (BigDecimal) timeElapsed;
            }
        }
        return null;
        }
}
