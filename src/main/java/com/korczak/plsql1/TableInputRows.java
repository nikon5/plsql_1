package com.korczak.plsql1;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class TableInputRows extends StoredProcedure {

    private static final String SQL = "hr.insert_table_rows";

    public TableInputRows(DataSource dataSource) {
        super(dataSource, SQL);
        declareParameter(new SqlParameter("v_numbers", Types.NUMERIC));
        compile();
    }

    public void execute(Integer v_numbers) {
        Map<String, Integer> inputParams = new HashMap();
        inputParams.put("v_numbers", v_numbers);
        execute(inputParams);
        }

}
