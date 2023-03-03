package com.example.swcamp_p03.customGroup.jpa;

import org.junit.jupiter.api.Test;

public class GroupInsertBatchTest {

    @Test
    void insertBulk() {
//        List<ExcelDataDto> excelDataList = new ArrayList<>();
//
//        String sql = String.format("""
//                INSERT INTO `%s` (username, phonenumber, excel_file_id)
//                VALUES (:username, :phonenumber, :excel_file_id)
//                """, "excel_data");
//        SqlParameterSource[] params = excelDataList.stream().map(BeanPropertySqlParameterSource::new).toArray(SqlParameterSource[]::new);
//        namedParameterJdbcTemplate.batchUpdate(sql, params);
//
//        jdbcTemplate.batchUpdate("insert into excel_data(exce_file_id, username, password) " + "values(?, ?, ?)", new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps, int i) throws SQLException {
//                ps.setObject(1, excelDataList.get(i).getExcelFileId());
//                ps.setString(2, excelDataList.get(i).getUsername());
//                ps.setString(3, excelDataList.get(i).getPhonenumber());
//            }
//
//            @Override
//            public int getBatchSize() {
//                return 1000;
//            }
//        });
    }
}
