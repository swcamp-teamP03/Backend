package com.example.swcamp_p03.customGroup.dataInit;

import com.example.swcamp_p03.common.DatabaseCleanup;
import com.example.swcamp_p03.customerGroup.repository.CustomerGroupRepository;
import com.example.swcamp_p03.customerGroup.repository.ExcelDataRepository;
import com.example.swcamp_p03.customerGroup.repository.ExcelFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CustomerGroupDataInit {
    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private CustomerGroupRepository customerGroupRepository;
    @Autowired
    private ExcelDataRepository excelDataRepository;
    @Autowired
    private ExcelFileRepository excelFileRepository;

    @BeforeEach
    void before() {
    }


//    @AfterEach
//    void after() {
//        databaseCleanup.execute();
//    }

}
