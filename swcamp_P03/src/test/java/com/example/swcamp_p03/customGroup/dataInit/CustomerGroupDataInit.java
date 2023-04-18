package com.example.swcamp_p03.customGroup.dataInit;

import com.example.swcamp_p03.common.DatabaseCleanup;
import com.example.swcamp_p03.customerGroup.entity.CustomerGroup;
import com.example.swcamp_p03.customerGroup.entity.CustomerProperty;
import com.example.swcamp_p03.customerGroup.entity.ExcelData;
import com.example.swcamp_p03.customerGroup.entity.ExcelFile;
import com.example.swcamp_p03.customerGroup.repository.CustomerGroupRepository;
import com.example.swcamp_p03.customerGroup.repository.CustomerPropertyRepository;
import com.example.swcamp_p03.customerGroup.repository.ExcelDataRepository;
import com.example.swcamp_p03.customerGroup.repository.ExcelFileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CustomerGroupDataInit {
    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private CustomerGroupRepository customerGroupRepository;
    @Autowired
    private CustomerPropertyRepository customerPropertyRepository;
    @Autowired
    private ExcelDataRepository excelDataRepository;
    @Autowired
    private ExcelFileRepository excelFileRepository;

    @BeforeEach
    void before() {
        for (int i = 1; i <= 20; i++) {
            initCustomerGroup(i);
        }
    }

    private void initCustomerGroup(int i) {
        ExcelFile excelFile = null;
        if (i <= 7) {
            excelFile = initExcelFile(i);
        }
        CustomerGroup group = CustomerGroup.register()
                .customerGroupName("name" + i)
                .favorite(false)
                .excelFile(excelFile)
                .build();
        customerGroupRepository.save(group);
        for (int j = 1; j <= 3; j++) {
            initCustomerProperty(group, j);
        }
    }

    private void initCustomerProperty(CustomerGroup group, int j) {
        CustomerProperty property = CustomerProperty.register()
                .customerGroup(group)
                .build();
        customerPropertyRepository.save(property);
    }

    private ExcelFile initExcelFile(int i) {
        ExcelFile excelFile = ExcelFile.register()
                .excelFileOrgName("고객모수" + i + ".xls")
                .excelFileSavedName("100000" + i + ".xls")
                .excelFileSavedPath("/file/엑셀" + i)
                .excelFileSize("100KB")
                .build();
        excelFileRepository.save(excelFile);

        List<ExcelData> excelDataList = new ArrayList<>();
        for (int j = 1; j < 30; j++) {
            ExcelData excelData = ExcelData.testInsert()
                    .username("이름"+j)
                    .phoneNumber("010-1234-123" + j)
                    .excelFile(excelFile)
                    .build();
            excelDataList.add(excelData);
        }
        excelDataRepository.saveAll(excelDataList);
        return excelFile;
    }

//    @AfterEach
//    void after() {
//        databaseCleanup.execute();
//    }

}
