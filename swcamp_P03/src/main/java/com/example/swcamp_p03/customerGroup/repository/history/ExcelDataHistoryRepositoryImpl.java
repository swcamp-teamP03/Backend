package com.example.swcamp_p03.customerGroup.repository.history;

import com.example.swcamp_p03.customerGroup.dto.ExcelDataHistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;

@RequiredArgsConstructor
public class ExcelDataHistoryRepositoryImpl implements ExcelDataHistoryRepositoryCustom {


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void bulkInsert(List<ExcelDataHistoryDto> excelDataList) {
        String sql = String.format("""
                INSERT INTO `%s` (username, phone_number, excel_file_history_id, created_at)
                VALUES (:username, :phoneNumber, :excelFileHistoryId, :createdAt)
                """, "excel_data_history");
        SqlParameterSource[] params = excelDataList.stream().map(BeanPropertySqlParameterSource::new).toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }
}
