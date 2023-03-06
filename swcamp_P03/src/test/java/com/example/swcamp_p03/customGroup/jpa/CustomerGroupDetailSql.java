package com.example.swcamp_p03.customGroup.jpa;

import com.example.swcamp_p03.customGroup.dataInit.CustomerGroupDataInit;
import com.example.swcamp_p03.customerGroup.entity.CustomerGroup;
import com.example.swcamp_p03.customerGroup.entity.CustomerProperty;
import com.example.swcamp_p03.customerGroup.entity.ExcelFile;
import com.example.swcamp_p03.customerGroup.repository.ExcelDataRepository;
import com.example.swcamp_p03.customerGroup.repository.ExcelFileRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.swcamp_p03.customerGroup.entity.QCustomerGroup.customerGroup;
import static com.example.swcamp_p03.customerGroup.entity.QCustomerProperty.customerProperty;
import static com.example.swcamp_p03.customerGroup.entity.QExcelData.excelData;
import static com.example.swcamp_p03.customerGroup.entity.QExcelFile.excelFile;

@SpringBootTest
public class CustomerGroupDetailSql extends CustomerGroupDataInit {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Test
    void groupDetail() {
        CustomerGroup result = jpaQueryFactory.select(
                customerGroup)
                .from(customerGroup)
                .join(customerGroup.propertyList, customerProperty).fetchJoin()
                .where(customerGroup.customerGroupId.eq(1L))
                .fetchOne();

        ExcelFileDto excelFileDto = jpaQueryFactory.select(Projections.constructor(ExcelFileDto.class,
                        excelFile.excelFileOrgName,
                        excelFile.excelFileSize,
                        excelFile.createdAt,
                        excelData.count()
                ))
                .from(customerGroup)
                .join(customerGroup.excelFile, excelFile)
                .join(excelData).on(excelFile.eq(excelData.excelFile))
                .where(customerGroup.customerGroupId.eq(1L))
                .fetchOne();
        ResponseDto responseDto = new ResponseDto(result,excelFileDto);
        System.out.println(responseDto);
    }

    @Data
    public static class ResponseDto {
        private String groupName;
        private List<CustomerPropertyDto> customerProperties;
        private ExcelFileDto excelFileDto;


        public ResponseDto(CustomerGroup customerGroup,ExcelFileDto excelFileDto) {
            this.groupName = customerGroup.getCustomerGroupName();
            this.customerProperties = customerGroup.getPropertyList().stream()
                    .map(CustomerPropertyDto::new)
                    .collect(Collectors.toList());
            this.excelFileDto = excelFileDto;
        }
    }

    @Data
    public static class CustomerPropertyDto {
        private String propertyName;
        private String propertyValue;

        public CustomerPropertyDto(CustomerProperty customerProperty) {
            this.propertyName = customerProperty.getPropertyName();
            this.propertyValue = customerProperty.getPropertyValue();
        }
    }

    @Data
    public static class ExcelFileDto {
        private String excelFileName;
        private String excelFileSize;
        private LocalDateTime excelUploadTime;
        private Long customerCnt;

        public ExcelFileDto(String excelFileName, String excelFileSize, LocalDateTime excelUploadTime, Long customerCnt) {
            this.excelFileName = excelFileName;
            this.excelFileSize = excelFileSize;
            this.excelUploadTime = excelUploadTime;
            this.customerCnt = customerCnt;
        }
    }

}
