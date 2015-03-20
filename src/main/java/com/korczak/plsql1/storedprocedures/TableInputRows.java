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

    private static final String SQL = "hr.insert_jobs_rows";

    public TableInputRows(DataSource dataSource) {
        super(dataSource, SQL);
        declareParameter(new SqlOutParameter("v_time", Types.NUMERIC));
        declareParameter(new SqlParameter("v_numbers", Types.NUMERIC));
        setFunction(true);
        compile();
    }

    public BigDecimal execute(Integer v_numbers) {
        Map<String, Integer> inputParams = new HashMap();
        inputParams.put("v_numbers", v_numbers);
       // execute(inputParams);
        Map<String, Object> outputParams = execute(inputParams);
        if (!outputParams.isEmpty()) {
            Object rowCount = outputParams.get("v_time");
            if (rowCount instanceof BigDecimal) {
                return (BigDecimal) rowCount;
            }
        }
        return null;
        }

}
