//package com.example.swcamp_p03.practice;
//
//import com.example.swcamp_p03.customerGroup.entity.CustomerGroup;
//import com.example.swcamp_p03.customerGroup.entity.CustomerProperty;
//import com.example.swcamp_p03.customerGroup.entity.ExcelData;
//import com.example.swcamp_p03.customerGroup.entity.ExcelFile;
//import com.example.swcamp_p03.customerGroup.repository.CustomerGroupRepository;
//import com.example.swcamp_p03.customerGroup.repository.CustomerPropertyRepository;
//import com.example.swcamp_p03.customerGroup.repository.ExcelDataRepository;
//import com.example.swcamp_p03.customerGroup.repository.ExcelFileRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDateTime;
//
//@SpringBootTest
//public class SqlTest {
//    @Autowired
//    private CustomerGroupRepository customerGroupRepository;
//    @Autowired
//    private CustomerPropertyRepository customerPropertyRepository;
//    @Autowired
//    private ExcelDataRepository excelDataRepository;
//    @Autowired
//    private ExcelFileRepository excelFileRepository;
//
//    @Test
//    void before() {
//        for (int i = 1; i <= 2; i++) {
//            initCustomerGroup(i);
//        }
//    }
//
//    private void initCustomerGroup(int i) {
//        CustomerGroup group = CustomerGroup.builder()
//                .customerGroupName("name" + i)
//                .favorite(false)
//                .createdAt(LocalDateTime.now())
//                .build();
//        customerGroupRepository.save(group);
//        for (int j = 1; j <= 3; j++) {
//            initCustomerProperty(group, j);
//        }
//        initExcelFile(i, group);
//    }
//
//    private void initCustomerProperty(CustomerGroup group, int j) {
//        CustomerProperty property = CustomerProperty.builder()
//                .propertyName("속성" + j)
//                .propertyValue("속성값" + j)
//                .customerGroup(group)
//                .build();
//        customerPropertyRepository.save(property);
//    }
//
//    private void initExcelFile(int i, CustomerGroup group) {
//        ExcelFile excelFile = ExcelFile.builder()
//                .excelFileOrgName("고객모수" + i + ".xls")
//                .excelFileSavedName("100000" + i + ".xls")
//                .excelFileSavedPath("/file/엑셀" + i)
//                .excelFileSize("100KB")
//                .createdAt(LocalDateTime.now())
//                .customerGroup(group)
//                .build();
//        excelFileRepository.save(excelFile);
//        for (int j = 1; j < 10; j++) {
//            ExcelData excelData = ExcelData.builder()
//                    .phoneNumber("010-1234-123" + j)
//                    .excelFile(excelFile)
//                    .build();
//            excelDataRepository.save(excelData);
//        }
//    }
//}
