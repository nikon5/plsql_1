package com.korczak.plsql1.storedprocedures;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class LoadDataFromFileToBackupTable extends StoredProcedure {

    private static final String LOAD_DATA_FROM_FILE_FUNCTION = "hr.pkg_kd_kp_file.load_data_from_file";

    public LoadDataFromFileToBackupTable(DataSource dataSource) {
        super(dataSource, LOAD_DATA_FROM_FILE_FUNCTION);
        declareParameter(new SqlOutParameter("num_of_insert", Types.NUMERIC));
        declareParameter(new SqlParameter("file_dir", Types.VARCHAR));
        declareParameter(new SqlParameter("file_name", Types.VARCHAR));
        declareParameter(new SqlParameter("table_name", Types.VARCHAR));
        declareParameter(new SqlParameter("separator", Types.VARCHAR));
        setFunction(true);//you must set this as it distinguishes it from a sproc
        compile();
    }

    public BigDecimal execute(String fileDir, String fileName, String tableName, String separator) {
        Map<String, String> inputParams = new HashMap();
        inputParams.put("file_dir", fileDir);
        inputParams.put("file_name", fileName);
        inputParams.put("table_name", tableName);
        inputParams.put("separator", separator);

        Map<String, Object> outputParams = execute(inputParams);
        if (!outputParams.isEmpty()) {
            Object rowCount = outputParams.get("num_of_insert");
            if (rowCount instanceof BigDecimal) {
                return (BigDecimal) rowCount;
            }
        }
        return null;
    }

}
