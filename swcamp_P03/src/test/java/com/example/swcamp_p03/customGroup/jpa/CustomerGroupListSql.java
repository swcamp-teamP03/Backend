package com.example.swcamp_p03.customGroup.jpa;

import com.example.swcamp_p03.customGroup.dataInit.CustomerGroupDataInit;
import com.example.swcamp_p03.customerGroup.entity.CustomerGroup;
import com.example.swcamp_p03.customerGroup.repository.CustomerGroupRepository;
import com.example.swcamp_p03.customerGroup.repository.ExcelFileRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.swcamp_p03.customerGroup.entity.QCustomerGroup.customerGroup;
import static com.example.swcamp_p03.customerGroup.entity.QExcelData.excelData;
import static com.example.swcamp_p03.customerGroup.entity.QExcelFile.excelFile;

@SpringBootTest
public class CustomerGroupListSql extends CustomerGroupDataInit {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

//    @Test
    void groupList() {
        int page = 1;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        List<GroupList> result = jpaQueryFactory.select(Projections.constructor(GroupList.class,
                        customerGroup,
                        excelData.excelDataId.count()
                ))
                .from(customerGroup)
                .leftJoin(customerGroup.excelFile, excelFile).fetchJoin()
                .leftJoin(excelData).on(excelFile.eq(excelData.excelFile))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(customerGroup.customerGroupId)
                .orderBy(customerGroup.createdAt.desc())
                .fetch();
        Long count = jpaQueryFactory.select(
                        customerGroup.count()
                )
                .from(customerGroup)
                .fetchOne();
        System.out.println("===================================================");
        PageImpl<GroupList> groupLists = new PageImpl<>(result, pageable, count);
        for (GroupList groupList : groupLists) {
            System.out.println("groupList = " + groupList);
        }
        System.out.println("groupLists = " + groupLists.getTotalPages());
        System.out.println("groupLists = " + groupLists.getNumber());
//        ResponseDto responseDto = new ResponseDto(count,result);
//        System.out.println("responseDto = " + responseDto);
//        List<GroupList> groupListList = responseDto.groupListList;
//        for (GroupList groupList : groupListList) {
//            System.out.println("groupList = " + groupList);
//        }
        System.out.println("===================================================");
    }

    @Data
    public static class ResponseDto {
        private Long totalGroup;
        private List<GroupList> groupListList;

        public ResponseDto(Long count, List<GroupList> groupListList) {
            this.totalGroup = count;
            this.groupListList = groupListList;
        }
    }

    @Data
    public static class GroupList {
        private Long customerGroupId;
        private String groupName;
        private Long customerCnt;
        private Boolean favorite;
        private Boolean excelUploadCheck;
        private LocalDateTime date;

        public GroupList(CustomerGroup customerGroup, Long customerCnt) {
            this.customerGroupId = customerGroup.getCustomerGroupId();
            this.groupName = customerGroup.getCustomerGroupName();
            this.customerCnt = customerCnt;
            this.favorite = customerGroup.getFavorite();
            this.date = customerGroup.getCreatedAt();
        }
    }
}
