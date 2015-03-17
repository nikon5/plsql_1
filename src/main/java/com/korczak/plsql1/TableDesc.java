package com.korczak.plsql1;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class TableDesc extends StoredProcedure {

    private static final String SQL = "hr.get_table_description";

    public TableDesc(DataSource dataSource) {
        super(dataSource, SQL);
        declareParameter(new SqlOutParameter("table_count", Types.VARCHAR));
        declareParameter(new SqlParameter("table_name", Types.VARCHAR));
        setFunction(true);//you must set this as it distinguishes it from a sproc
        compile();
    }

    public String execute(String tableName) {
        Map<String, String> inputParams = new HashMap();
        inputParams.put("table_name", tableName);

        Map<String, Object> outputParams = execute(inputParams);
        if (!outputParams.isEmpty()) {
            Object rowCount = outputParams.get("table_count");
            if (rowCount instanceof BigDecimal) {
                return (String) rowCount;
            }
        }
        return null;
    }

}
