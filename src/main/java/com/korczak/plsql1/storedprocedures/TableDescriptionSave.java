package com.korczak.plsql1.storedprocedures;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class TableDescriptionSave extends StoredProcedure {

    private static final String WRITE_TO_FILE_PROCEDURE = "hr.write_description_to_file";

    public TableDescriptionSave(DataSource dataSource) {
        super(dataSource, WRITE_TO_FILE_PROCEDURE);
        declareParameter(new SqlParameter("table_name", Types.VARCHAR));
        compile();
    }

    public void execute(String tableName) {
        Map<String, String> inputParams = new HashMap();
        inputParams.put("table_name", tableName);
        execute(inputParams);
    }
}
