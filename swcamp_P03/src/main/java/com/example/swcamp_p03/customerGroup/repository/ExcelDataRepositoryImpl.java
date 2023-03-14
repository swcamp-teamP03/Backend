package com.example.swcamp_p03.customerGroup.repository;

import com.example.swcamp_p03.customerGroup.dto.ExcelDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;

//@RequiredArgsConstructor
//public class ExcelDataRepositoryImpl implements ExcelDataRepositoryCustom{
//    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
//    @Override
//    public void bulkInsert(List<ExcelDataDto> excelDataList) {
//        String sql = String.format("""
//                INSERT INTO `%s` (username, phone_number, excel_file_id, created_at)
//                VALUES (:username, :phoneNumber, :excelFileId, :createdAt)
//                """, "excel_data");
//        SqlParameterSource[] params = excelDataList.stream().map(BeanPropertySqlParameterSource::new).toArray(SqlParameterSource[]::new);
//        namedParameterJdbcTemplate.batchUpdate(sql, params);
//    }
//}
