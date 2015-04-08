package com.korczak.plsql1.storedprocedures;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class TableDataLoadFromBckp extends StoredProcedure {

    private static final String TABLE_DATA_SAVE_FUNCTION = "hr.pkg_kd_kp_file.load_data";

    public TableDataLoadFromBckp(DataSource dataSource) {
        super(dataSource, TABLE_DATA_SAVE_FUNCTION);
        declareParameter(new SqlParameter("table_name", Types.VARCHAR));
        declareParameter(new SqlParameter("commit_occurence", Types.NUMERIC));
        declareParameter(new SqlOutParameter("total_inserted", Types.NUMERIC));
        compile();
    }

    public BigDecimal execute(String tableName, String commit_occurence) {
        Map<String, String> inputParams = new HashMap();
        inputParams.put("table_name", tableName);
        inputParams.put("commit_occurence", commit_occurence);

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
