package com.example.swcamp_p03.campaign;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.swcamp_p03.campaign.entity.QCampaign.campaign;
import static com.example.swcamp_p03.campaign.entity.QCampaignMessage.campaignMessage;
import static com.example.swcamp_p03.campaign.entity.QSendMessages.sendMessages;
import static com.example.swcamp_p03.copyGroup.entity.QCopyGroup.copyGroup;
import static com.example.swcamp_p03.customerGroup.entity.QCustomerGroup.customerGroup;
import static com.example.swcamp_p03.customerGroup.entity.QCustomerProperty.customerProperty;
import static com.example.swcamp_p03.customerGroup.entity.QExcelFile.excelFile;

@SpringBootTest
public class CampaignDetail {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Test
    void test() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        CampaignDetailDto response = jpaQueryFactory.select(Projections.constructor(CampaignDetailDto.class,
                        campaign.createdAt,
                        campaign.sendingDate,
                        sendMessages.campaign.count(),
                        campaign.messageType,
                        customerGroup.customerGroupName,
                        excelFile.excelFileOrgName,
                        copyGroup.coupGroupName,
                        campaign.comment
                ))
                .from(campaign)
                .join(sendMessages).on(campaign.eq(sendMessages.campaign))
                .join(campaign.customerGroup, customerGroup)
                .join(campaign.copyGroup, copyGroup)
                .join(customerGroup.excelFile, excelFile)
                .where(campaign.campaignId.eq(1L))
                .fetchOne();

        List<CustomerPropertyDto> propertyDtos = jpaQueryFactory.select(Projections.constructor(CustomerPropertyDto.class,
                        customerProperty.propertyValue
                ))
                .from(campaign)
                .join(campaign.customerGroup, customerGroup)
                .join(customerGroup.propertyList, customerProperty)
                .where(campaign.campaignId.eq(1L))
                .fetch();

        List<CopyWriteABDto> copyWriteABDtos = jpaQueryFactory.select(Projections.constructor(CopyWriteABDto.class,
                        campaignMessage.messageSection,
                        campaignMessage.message))
                .from(campaign)
                .join(campaignMessage).on(campaign.eq(campaignMessage.campaign))
                .where(campaign.campaignId.eq(1L))
                .fetch();

//        MessageADto messageADto = jpaQueryFactory.select(Projections.constructor(MessageADto.class,
//                        sendMessages.count(),
////                        sendMessages.visitedTime.count(),
//                        JPAExpressions.select(sendMessages.visitedTime.count(), sendMessages.count())
//                                .from(campaign)
//                                .join(sendMessages).on(campaign.eq(sendMessages.campaign))
//                                .join(campaignMessage).on(campaign.eq(campaignMessage.campaign))
//                                .where(campaign.campaignId.eq(1L), campaignMessage.messageSection.eq("A"), sendMessages.campaignMessage.eq(campaignMessage), sendMessages.sendState.eq("발송성공"))
//                ))
//                .from(campaign)
//                .join(sendMessages).on(campaign.eq(sendMessages.campaign))
//                .join(campaignMessage).on(campaign.eq(campaignMessage.campaign))
//                .where(campaign.campaignId.eq(1L), campaignMessage.messageSection.eq("A"), sendMessages.campaignMessage.eq(campaignMessage))
//                .fetchOne();

        MessageADto messageADto = jpaQueryFactory.select(Projections.constructor(MessageADto.class,
                        JPAExpressions.select(sendMessages.count())
                                .from(campaign)
                                .join(sendMessages).on(campaign.eq(sendMessages.campaign))
                                .join(campaignMessage).on(campaign.eq(campaignMessage.campaign))
                                .where(campaign.campaignId.eq(1L), campaignMessage.messageSection.eq("A"), sendMessages.campaignMessage.eq(campaignMessage)),
                        sendMessages.visitedTime.count(),
                        sendMessages.count()))
                .from(campaign)
                .join(sendMessages).on(campaign.eq(sendMessages.campaign))
                .join(campaignMessage).on(campaign.eq(campaignMessage.campaign))
                .where(campaign.campaignId.eq(1L), campaignMessage.messageSection.eq("A"), sendMessages.campaignMessage.eq(campaignMessage), sendMessages.sendState.eq("발송성공"))
                .fetchOne();

//        MessageBDto messageBDto = jpaQueryFactory.select(Projections.constructor(MessageBDto.class,
//                        sendMessages.count(),
////                        sendMessages.visitedTime.count(),
//                        JPAExpressions.select(sendMessages.visitedTime.count(), sendMessages.count())
//                                .from(campaign)
//                                .join(sendMessages).on(campaign.eq(sendMessages.campaign))
//                                .join(campaignMessage).on(campaign.eq(campaignMessage.campaign))
//                                .where(campaign.campaignId.eq(1L), campaignMessage.messageSection.eq("B"), sendMessages.campaignMessage.eq(campaignMessage), sendMessages.sendState.eq("발송성공"))
//                ))
//                .from(campaign)
//                .join(sendMessages).on(campaign.eq(sendMessages.campaign))
//                .join(campaignMessage).on(campaign.eq(campaignMessage.campaign))
//                .where(campaign.campaignId.eq(1L), campaignMessage.messageSection.eq("B"), sendMessages.campaignMessage.eq(campaignMessage))
//                .fetchOne();

        MessageBDto messageBDto = jpaQueryFactory.select(Projections.constructor(MessageBDto.class,
                        JPAExpressions.select(sendMessages.count())
                                .from(campaign)
                                .join(sendMessages).on(campaign.eq(sendMessages.campaign))
                                .join(campaignMessage).on(campaign.eq(campaignMessage.campaign))
                                .where(campaign.campaignId.eq(1L), campaignMessage.messageSection.eq("B"), sendMessages.campaignMessage.eq(campaignMessage)),
                        sendMessages.visitedTime.count(),
                        sendMessages.count()))
                .from(campaign)
                .join(sendMessages).on(campaign.eq(sendMessages.campaign))
                .join(campaignMessage).on(campaign.eq(campaignMessage.campaign))
                .where(campaign.campaignId.eq(1L), campaignMessage.messageSection.eq("B"), sendMessages.campaignMessage.eq(campaignMessage), sendMessages.sendState.eq("발송성공"))
                .fetchOne();
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeSeconds());

        response.setCustomerProperties(propertyDtos);
        response.setCopyWriteAB(copyWriteABDtos);
        response.setMessageA(messageADto);
        response.setMessageB(messageBDto);
        System.out.println(response);

    }

    @Data
    public static class CampaignDetailDto {
        public String campaignCreatedAt;
        public String sendingDateTime;
        public Long messageCount;
        public String messageType;
        public String groupName;
        public Long customerCount;
        public List<CustomerPropertyDto> customerProperties;
        public String excelOrgFileName;
        public String copyGroupName;
        public List<CopyWriteABDto> copyWriteAB;
        public MessageADto messageA;
        public MessageBDto messageB;
        public String comment;

        public CampaignDetailDto(LocalDateTime campaignCreatedAt, LocalDateTime sendingDateTime, Long messageCount, String messageType, String groupName, String excelOrgFileName, String copyGroupName, String comment) {
            this.campaignCreatedAt = campaignCreatedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            this.sendingDateTime = sendingDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
            this.messageCount = messageCount;
            this.messageType = messageType;
            this.groupName = groupName;
            this.customerCount = messageCount;
            this.excelOrgFileName = excelOrgFileName;
            this.copyGroupName = copyGroupName;
            this.comment = comment;
        }
    }

    @Data
    public static class CustomerPropertyDto {
        public String propertyValue;

        public CustomerPropertyDto(String propertyValue) {
            this.propertyValue = propertyValue;
        }
    }

    @Data
    public static class CopyWriteABDto {
        public String copyType;
        public String content;

        public CopyWriteABDto(String copyType, String content) {
            this.copyType = copyType;
            this.content = content;
        }
    }

    @Data
    public static class MessageADto {
        public Long messageACnt;
        public Long messageSuccessCnt;
        public Double uniqueCTR;

        public MessageADto(Long messageACnt, Long clickCnt, Long messageSuccessCnt) {
            this.messageACnt = messageACnt;
            this.messageSuccessCnt = messageSuccessCnt;
            double ctr = clickCnt / (double) messageSuccessCnt;
            this.uniqueCTR = Math.round(ctr * 10000) / 100.0;
        }
    }

    @Data
    public static class MessageBDto {
        public Long messageBCnt;
        public Long messageSuccessCnt;
        public Double uniqueCTR;

        public MessageBDto(Long messageBCnt, Long clickCnt, Long messageSuccessCnt) {
            this.messageBCnt = messageBCnt;
            this.messageSuccessCnt = messageSuccessCnt;
            double ctr = clickCnt / (double) messageSuccessCnt;
            this.uniqueCTR = Math.round(ctr * 10000) / 100.0;
        }
    }
}
