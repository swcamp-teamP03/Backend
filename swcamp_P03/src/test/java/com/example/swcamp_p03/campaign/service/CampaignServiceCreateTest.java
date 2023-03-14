package com.example.swcamp_p03.campaign.service;

import com.example.swcamp_p03.campaign.dto.request.CampaignRequestDto;
import com.example.swcamp_p03.campaign.entity.Campaign;
import com.example.swcamp_p03.campaign.entity.CampaignMessage;
import com.example.swcamp_p03.campaign.entity.SendMessages;
import com.example.swcamp_p03.campaign.repository.CampaignMessageRepository;
import com.example.swcamp_p03.campaign.repository.CampaignRepository;
import com.example.swcamp_p03.campaign.repository.SendMessagesRepository;
import com.example.swcamp_p03.copyGroup.entity.CopyGroup;
import com.example.swcamp_p03.copyGroup.repository.CopyGroupRepository;
import com.example.swcamp_p03.customerGroup.dto.ExcelDataDto;
import com.example.swcamp_p03.customerGroup.entity.CustomerGroup;
import com.example.swcamp_p03.customerGroup.entity.ExcelData;
import com.example.swcamp_p03.customerGroup.entity.ExcelFile;
import com.example.swcamp_p03.customerGroup.repository.CustomerGroupRepository;
import com.example.swcamp_p03.customerGroup.repository.CustomerPropertyRepository;
import com.example.swcamp_p03.customerGroup.repository.ExcelDataRepository;
import com.example.swcamp_p03.customerGroup.repository.ExcelFileRepository;
import com.example.swcamp_p03.user.entity.User;
import com.example.swcamp_p03.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CampaignServiceCreateTest {

    @Autowired
    private CampaignService campaignService;
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private SendMessagesRepository sendMessagesRepository;
    @Autowired
    private CampaignMessageRepository campaignMessageRepository;
    @Autowired
    private CopyGroupRepository copyGroupRepository;
    @Autowired
    private CustomerGroupRepository customerGroupRepository;
    @Autowired
    private CustomerPropertyRepository customerPropertyRepository;
    @Autowired
    private ExcelDataRepository excelDataRepository;
    @Autowired
    private ExcelFileRepository excelFileRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;
    private CopyGroup copyGroup;
    private CustomerGroup customerGroup;
    private ExcelFile excelFile;
    private List<ExcelData> ExcelDataList;

    @BeforeEach
    void beforeEach(){
        //create user
        user = User.register()
                .email("testUser@testdata.com")
                .password("1234")
                .roles("ROLE_USER")
                .username("testUser")
                .phoneNumber("01012345678")
                .company("testCompany")
                .companyNumber("01012345678")
                .callingNumber("01012345678")
                .callRejectionNumber("01012345678")
                .build();
        userRepository.save(user);
        //create copyGroup
        copyGroup = CopyGroup.builder()
                .coupGroupName("testCopyGroupName")
                .brandName("testBrandName")
                .productName("testProductName")
                .keyword("testKeyword1,testKeyword2")
                .copyType("testType")
                .favorite(false)
                .sector("testSector")
                .createCount(3)
                .copyLength(100)
                .user(user)
                .unableEdit(false)
                .gptCopyList(new ArrayList<>())
                .build();
        copyGroupRepository.save(copyGroup);

        //create excelFile
        excelFile = ExcelFile.register()
                .excelFileOrgName("testOrgName")
                .excelFileSavedName("testSavedName")
                .excelFileSavedPath("testSavedPath")
                .excelFileSize("testFileSize")
                .build();
        excelFileRepository.save(excelFile);

        //create customerGroup
        customerGroup = CustomerGroup.register()
                .customerGroupName("testGroupName")
                .favorite(false)
                .user(user)
                .excelFile(excelFile)
                .build();
        customerGroupRepository.save(customerGroup);

        List<ExcelDataDto> dataList = new ArrayList<>();

        dataList.add(ExcelDataDto.excelDataRegister()
                .username("testUsername1")
                .phoneNumber("01011111111")
                .excelFileId(excelFile.getExcelFileId())
                .build());

        dataList.add(ExcelDataDto.excelDataRegister()
                .username("testUsername2")
                .phoneNumber("01022222222")
                .excelFileId(excelFile.getExcelFileId())
                .build());
        excelDataRepository.bulkInsert(dataList);
        ExcelDataList = excelDataRepository.findAllByExcelFile(excelFile);

    }

    @Test
    @DisplayName("AbTest 사용한 캠페인 생성")
    void CampaignCreateUseAbtestSuccessTest(){
        //given
        CampaignRequestDto requestDto = new CampaignRequestDto();
        requestDto.setCampaignName("testCampaignName");
        requestDto.setCustomerGroupId(customerGroup.getCustomerGroupId());
        requestDto.setCopyGroupId(copyGroup.getCopyGroupId());
        requestDto.setMessageType("LMS");
        requestDto.setSendType("ad");
        requestDto.setSendURL("https://www.youtube.com/");
        requestDto.setSendingDate(null);
        requestDto.setMessageA("테스트 내용 A");
        requestDto.setMessageB("테스트 내용 B");

        //when
        Long campaign;
        try {
            campaign = campaignService.createCampaign(user, requestDto, false);
        } catch (Exception e){
            e.printStackTrace();
            fail("테스트 실패 : 예외 발생");
            return;
        }

        //then
        Campaign findCampaign = campaignRepository.findById(campaign).orElseThrow();
        List<SendMessages> sendMessages = sendMessagesRepository.findAllByCampaign(findCampaign);
        List<CampaignMessage> campaignMessages = campaignMessageRepository.findAllByCampaign(findCampaign);

        assertEquals(sendMessages.size(), ExcelDataList.size(),"생성된 SendMessages 개수 확인");
        assertEquals(campaignMessages.size(), 2,"생성된 campaignMessages 개수 확인");

        if(campaignMessages.get(0).getMessage() == requestDto.getMessageA()){
            assertEquals(campaignMessages.get(0).getMessage(), requestDto.getMessageA(),"messageA 확인");
            assertEquals(campaignMessages.get(1).getMessage(), requestDto.getMessageB(),"messageA 확인");
        }else{
            assertEquals(campaignMessages.get(1).getMessage(), requestDto.getMessageA(),"messageA 확인");
            assertEquals(campaignMessages.get(0).getMessage(), requestDto.getMessageB(),"messageA 확인");
        }

        assertNotNull(sendMessages.get(0).getPhoneNumber(), "getPhoneNumber 는 null 일수 없습니다");
        assertNotNull(sendMessages.get(0).getCampaignMessage(), "getPhoneNumber 는 null 일수 없습니다");
        assertNotNull(sendMessages.get(0).getUniqueUrl(), "getPhoneNumber 는 null 일수 없습니다");
        assertNull(sendMessages.get(0).getErrorMessage(), "getErrorMessage 는 null 이어야 합니다.");
        assertNull(sendMessages.get(0).getVisitedDate(), "getVisitedDate 는 null 이어야 합니다.");
        assertNull(sendMessages.get(0).getVisitedTime(), "getVisitedTime 는 null 이어야 합니다.");

        assertEquals(sendMessages.get(0).getCampaign().getCampaignId(), campaign,"campaignID 확인");

        CampaignMessage message = sendMessages.get(0).getCampaignMessage();
        if(message.getCampaignMessageId() != campaignMessages.get(0).getCampaignMessageId()
                && message.getCampaignMessageId() != campaignMessages.get(1).getCampaignMessageId()){
            fail("CampaignMessage 저장이 잘못되었습니다.");
        }

        assertEquals(findCampaign.getCampaignName(), requestDto.getCampaignName(),"getCampaignName 확인");
        assertEquals(findCampaign.getCopyGroup().getCopyGroupId(), requestDto.getCopyGroupId(),"getCopyGroupId 확인");
        assertEquals(findCampaign.getCustomerGroup().getCustomerGroupId(), requestDto.getCustomerGroupId(),"getCustomerGroupId 확인");
        assertEquals(findCampaign.getSendURL(), requestDto.getSendURL(),"getSendURL 확인");
        assertEquals(findCampaign.getMessageType(), requestDto.getMessageType(),"getMessageType 확인");
        assertNotNull(findCampaign.getSendingDate(), "getSendingDate 는 null 일수 없습니다");
    }

    @Test
    @DisplayName("AbTest 없이 캠페인 생성")
    void CampaignCreateWithoutAbtestSuccessTest(){
        //given
        CampaignRequestDto requestDto = new CampaignRequestDto();
        requestDto.setCampaignName("testCampaignName");
        requestDto.setCustomerGroupId(customerGroup.getCustomerGroupId());
        requestDto.setCopyGroupId(copyGroup.getCopyGroupId());
        requestDto.setMessageType("LMS");
        requestDto.setSendType("ad");
        requestDto.setSendURL("https://www.youtube.com/");
        requestDto.setSendingDate(null);
        requestDto.setMessageA("테스트 내용 A");
        requestDto.setMessageB("");

        //when
        Long campaign;
        try {
            campaign = campaignService.createCampaign(user, requestDto, false);
        } catch (Exception e){
            e.printStackTrace();
            fail("테스트 실패 : 예외 발생");
            return;
        }

        //then
        Campaign findCampaign = campaignRepository.findById(campaign).orElseThrow();
        List<SendMessages> sendMessages = sendMessagesRepository.findAllByCampaign(findCampaign);
        List<CampaignMessage> campaignMessages = campaignMessageRepository.findAllByCampaign(findCampaign);

        assertEquals(sendMessages.size(), ExcelDataList.size(),"생성된 SendMessages 개수 확인");
        assertEquals(campaignMessages.size(), 1,"생성된 campaignMessages 개수 확인 (1)");

        assertEquals(campaignMessages.get(0).getMessage(), requestDto.getMessageA(),"messageA 확인");

        assertNotNull(sendMessages.get(0).getPhoneNumber(), "getPhoneNumber 는 null 일수 없습니다");
        assertNotNull(sendMessages.get(0).getCampaignMessage(), "getPhoneNumber 는 null 일수 없습니다");
        assertNotNull(sendMessages.get(0).getUniqueUrl(), "getPhoneNumber 는 null 일수 없습니다");
        assertNull(sendMessages.get(0).getErrorMessage(), "getErrorMessage 는 null 이어야 합니다.");
        assertNull(sendMessages.get(0).getVisitedDate(), "getVisitedDate 는 null 이어야 합니다.");
        assertNull(sendMessages.get(0).getVisitedTime(), "getVisitedTime 는 null 이어야 합니다.");

        assertEquals(sendMessages.get(0).getCampaign().getCampaignId(), campaign,"campaignID 확인");

        CampaignMessage message = sendMessages.get(0).getCampaignMessage();
        if(message.getCampaignMessageId() != campaignMessages.get(0).getCampaignMessageId()
                && message.getCampaignMessageId() != campaignMessages.get(1).getCampaignMessageId()){
            fail("CampaignMessage 저장이 잘못되었습니다.");
        }

        assertEquals(findCampaign.getCampaignName(), requestDto.getCampaignName(),"getCampaignName 확인");
        assertEquals(findCampaign.getCopyGroup().getCopyGroupId(), requestDto.getCopyGroupId(),"getCopyGroupId 확인");
        assertEquals(findCampaign.getCustomerGroup().getCustomerGroupId(), requestDto.getCustomerGroupId(),"getCustomerGroupId 확인");
        assertEquals(findCampaign.getSendURL(), requestDto.getSendURL(),"getSendURL 확인");
        assertEquals(findCampaign.getMessageType(), requestDto.getMessageType(),"getMessageType 확인");
        assertNotNull(findCampaign.getSendingDate(), "getSendingDate 는 null 일수 없습니다");

    }
}