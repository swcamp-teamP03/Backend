package com.example.swcamp_p03.customGroup.jpa;

import com.example.swcamp_p03.customGroup.dataInit.CustomerGroupDataInit;
import com.example.swcamp_p03.customerGroup.entity.CustomerGroup;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.swcamp_p03.customerGroup.entity.QCustomerGroup.customerGroup;
import static com.example.swcamp_p03.customerGroup.entity.QExcelData.excelData;
import static com.example.swcamp_p03.customerGroup.entity.QExcelFile.excelFile;

@SpringBootTest
public class SearchGroupSql extends CustomerGroupDataInit {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

//    @Test
    void search() {
        int page = 0;
        int size = 5;
        String keyword = "name2";
        List<GroupList> result = jpaQueryFactory.select(Projections.constructor(GroupList.class,
                        customerGroup,
                        excelData.excelDataId.count()
                ))
                .from(customerGroup)
                .leftJoin(customerGroup.excelFile, excelFile).fetchJoin()
                .leftJoin(excelData).on(excelFile.eq(excelData.excelFile))
                .where(customerGroup.customerGroupName.eq(keyword))
                .offset(page)
                .limit(size)
                .groupBy(customerGroup.customerGroupId)
                .orderBy(customerGroup.createdAt.desc())
                .fetch();
        Long count = jpaQueryFactory.select(
                        customerGroup.count()
                )
                .from(customerGroup)
                .fetchOne();
        ResponseDto responseDto = new ResponseDto(count,result);
        System.out.println("responseDto = " + responseDto);
        List<GroupList> groupListList = responseDto.getGroupList();
        for (GroupList groupList : groupListList) {
            System.out.println("groupList = " + groupList);
        }
    }

    @Data
    public static class ResponseDto {
        private Long totalGroup;
        private List<GroupList> groupList;

        public ResponseDto(Long count, List<GroupList> groupList) {
            this.totalGroup = count;
            this.groupList = groupList;
        }
    }

    @Data
    public static class GroupList {
        private Long customerGroupId;
        private String groupName;
        private Long customerCnt;
        private Boolean favorite;
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
