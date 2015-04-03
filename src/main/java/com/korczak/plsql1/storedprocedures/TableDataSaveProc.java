package com.korczak.plsql1.storedprocedures;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class TableDataSaveProc extends StoredProcedure {

    private static final String SAVE_DATA_TO_FILE_PROCEDURE = "hr.pkg_kd_kp_file.save_data_to_file";

    public TableDataSaveProc(DataSource dataSource) {
        super(dataSource, SAVE_DATA_TO_FILE_PROCEDURE);
        declareParameter(new SqlParameter("table_name", Types.VARCHAR));
        declareParameter(new SqlParameter("separator", Types.VARCHAR));
        compile();
    }

    public void execute(String tableName, String separator) {
        Map<String, String> inputParams = new HashMap();
        inputParams.put("table_name", tableName);
        inputParams.put("separator", separator);
        execute(inputParams);
    }
}
