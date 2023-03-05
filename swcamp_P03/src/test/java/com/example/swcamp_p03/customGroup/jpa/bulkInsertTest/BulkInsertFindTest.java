package com.example.swcamp_p03.customGroup.jpa.bulkInsertTest;

import com.example.swcamp_p03.customGroup.jpa.testEntity.ATableRepository;
import com.example.swcamp_p03.customGroup.jpa.testEntity.AaaTable;
import com.example.swcamp_p03.customGroup.jpa.testEntity.BTableRepository;
import com.example.swcamp_p03.customGroup.jpa.testEntity.BbbTable;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class BulkInsertFindTest {
    @Autowired
    private ATableRepository aTableRepository;
    @Autowired
    private BTableRepository bTableRepository;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    void before() {
        AaaTable aTable = new AaaTable("aaa");
        aTableRepository.save(aTable);
        List<BTableDto> bTableList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            bTableList.add(new BTableDto("aaa" + 1,aTable.getAaaTableId()));
        }
        String sql = String.format("""
                INSERT INTO `%s` (content, aaa_table_id)
                VALUES (:content, :aaaTableId)
                """, "bbb_table");
        SqlParameterSource[] params = bTableList.stream().map(BeanPropertySqlParameterSource::new).toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }

    @Test
    void test() {
        String name = "aaa";
        AaaTable findA = aTableRepository.findByName(name);
        List<BbbTable> allByAaaTable = bTableRepository.findAllByAaaTable(findA);
        for (BbbTable bbbTable : allByAaaTable) {
            System.out.println("bbbTable = " + bbbTable);
        }
    }

    @Data
    public static class BTableDto {
        private String content;
        private Long aaaTableId;

        public BTableDto(String content, Long aaaTableId) {
            this.content = content;
            this.aaaTableId = aaaTableId;
        }
    }
}
