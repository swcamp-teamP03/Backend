package com.example.swcamp_p03.customGroup.jpa.bulkInsertTest;

import com.example.swcamp_p03.common.DatabaseCleanup;
import com.example.swcamp_p03.customGroup.jpa.testEntity.ATableRepository;
import com.example.swcamp_p03.customGroup.jpa.testEntity.AaaTable;
import com.example.swcamp_p03.customGroup.jpa.testEntity.BTableRepository;
import com.example.swcamp_p03.customGroup.jpa.testEntity.BbbTable;
import lombok.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class GroupInsertBatchTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ATableRepository aTableRepository;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private BTableRepository bTableRepository;
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void before() {
        AaaTable aTable = new AaaTable("aaa");
        aTableRepository.save(aTable);
    }

    @AfterEach
    void after() {
        databaseCleanup.execute();
    }

    @Test
    @DisplayName("bulkInsert")
    void insertBulk() {
        String name = "aaa";
        AaaTable findATable = aTableRepository.findByName(name);

        List<BTableDto> bTableList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            bTableList.add(new BTableDto("aaa" + 1,findATable.getAaaTableId()));
        }
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        jdbcTemplate.batchUpdate("insert into btable(content, a_table_id) values (?,?)", new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps, int i) throws SQLException {
//                ps.setString(1,bTableList.get(i).getContent());
//                ps.setString(2, findATable.getBTableId());
//            }
//
//            @Override
//            public int getBatchSize() {
//                return 1000;
//            }
//        });
//        stopWatch.stop();
//        System.out.println("쿼리 시간 : " + stopWatch.getTotalTimeSeconds());

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String sql = String.format("""
                INSERT INTO `%s` (content, aaa_table_id)
                VALUES (:content, :aaaTableId)
                """, "bbb_table");
        SqlParameterSource[] params = bTableList.stream().map(BeanPropertySqlParameterSource::new).toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
        stopWatch.stop();
        System.out.println("bulk insert 쿼리 시간 : " + stopWatch.getTotalTimeSeconds());
    }

    @Test
    @DisplayName("saveAll")
    void saveAll() {
        String name = "aaa";
        AaaTable findATable = aTableRepository.findByName(name);

//        List<BTableDto> bTableList = new ArrayList<>();
        List<BbbTable> bTableList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            bTableList.add(new BbbTable("aaa" + 1, findATable));
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        bTableRepository.saveAll(bTableList);
        stopWatch.stop();
        System.out.println("saveAll 쿼리 시간 : " + stopWatch.getTotalTimeSeconds());

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
