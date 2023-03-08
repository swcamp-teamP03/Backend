package com.example.swcamp_p03.campaign.dataInit;

import com.example.swcamp_p03.campaign.entity.Campaign;
import com.example.swcamp_p03.campaign.entity.CampaignMessage;
import com.example.swcamp_p03.campaign.entity.SendMessages;
import com.example.swcamp_p03.campaign.repository.CampaignMessageRepository;
import com.example.swcamp_p03.campaign.repository.CampaignRepository;
import com.example.swcamp_p03.campaign.repository.SendMessagesRepository;
import com.example.swcamp_p03.user.entity.User;
import com.example.swcamp_p03.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class CampaignDataInit {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private CampaignMessageRepository campaignMessageRepository;
    @Autowired
    private SendMessagesRepository sendMessagesRepository;

    @BeforeEach
    void before() {
        User user = User.builder()
                .username("name")
                .password("password")
                .roles("user")
                .build();
        userRepository.save(user);

        Campaign campaign1 = Campaign.testInsert()
                .favorite(false)
                .messageType("LMS")
                .campaignName("aaa")
                .createdAt(LocalDateTime.now())
                .sendingDate(LocalDateTime.of(2023,5, 12,12,0,0))
                .sendType("발송완료")
                .user(user)
                .build();
        Campaign campaign2 = Campaign.testInsert()
                .favorite(true)
                .messageType("SMS")
                .campaignName("bbb")
                .createdAt(LocalDateTime.of(2023,4, 24,12,0,0))
                .sendingDate(LocalDateTime.now())
                .sendType("발송완료")
                .user(user)
                .build();
        Campaign campaign3 = Campaign.testInsert()
                .favorite(false)
                .messageType("LMS")
                .campaignName("ccc")
                .createdAt(LocalDateTime.now())
                .sendingDate(LocalDateTime.of(2023,2, 12,12,0,0))
                .sendType("발송완료")
                .user(user)
                .build();
        campaignRepository.save(campaign1);
        campaignRepository.save(campaign2);
        campaignRepository.save(campaign3);

        CampaignMessage messageTypeA = CampaignMessage.testInsert()
                .message("copyA")
                .messageSection("A")
                .campaign(campaign1)
                .build();
        CampaignMessage messageTypeB = CampaignMessage.testInsert()
                .message("copyB")
                .messageSection("B")
                .campaign(campaign1)
                .build();
        campaignMessageRepository.save(messageTypeA);
        campaignMessageRepository.save(messageTypeB);

        SendMessages message1 = SendMessages.testInsert()
                .name("가나다1")
                .sendCheck(true)
                .sendDateTime(LocalDateTime.now())
                .campaignMessage(messageTypeA)
                .campaign(campaign1)
                .build();
        SendMessages message2 = SendMessages.testInsert()
                .name("가나다2")
                .sendCheck(true)
                .sendDateTime(LocalDateTime.now())
                .campaignMessage(messageTypeA)
                .campaign(campaign1)
                .build();
        SendMessages message3 = SendMessages.testInsert()
                .name("가나다3")
                .sendCheck(false)
                .sendDateTime(LocalDateTime.now())
                .campaignMessage(messageTypeA)
                .campaign(campaign1)
                .build();
        SendMessages message4 = SendMessages.testInsert()
                .name("라마바1")
                .sendCheck(true)
                .sendDateTime(LocalDateTime.now())
                .campaignMessage(messageTypeB)
                .campaign(campaign1)
                .build();
        SendMessages message5 = SendMessages.testInsert()
                .name("라마바2")
                .sendCheck(false)
                .sendDateTime(LocalDateTime.now())
                .campaignMessage(messageTypeB)
                .campaign(campaign1)
                .build();
        SendMessages message6 = SendMessages.testInsert()
                .name("라마바3")
                .sendCheck(true)
                .sendDateTime(LocalDateTime.now())
                .campaignMessage(messageTypeB)
                .campaign(campaign1)
                .build();
        sendMessagesRepository.save(message1);
        sendMessagesRepository.save(message2);
        sendMessagesRepository.save(message3);
        sendMessagesRepository.save(message4);
        sendMessagesRepository.save(message5);
        sendMessagesRepository.save(message6);


        SendMessages message11 = SendMessages.testInsert()
                .name("가나다1")
                .sendCheck(true)
                .sendDateTime(LocalDateTime.now())
//                .campaignMessage(messageTypeA)
                .campaign(campaign2)
                .build();
        SendMessages message22 = SendMessages.testInsert()
                .name("가나다2")
                .sendCheck(true)
                .sendDateTime(LocalDateTime.now())
//                .campaignMessage(messageTypeA)
                .campaign(campaign2)
                .build();
        SendMessages message33 = SendMessages.testInsert()
                .name("가나다3")
                .sendCheck(true)
                .sendDateTime(LocalDateTime.now())
//                .campaignMessage(messageTypeA)
                .campaign(campaign2)
                .build();
        SendMessages message44 = SendMessages.testInsert()
                .name("가나다4")
                .sendCheck(true)
                .sendDateTime(LocalDateTime.now())
//                .campaignMessage(messageTypeB)
                .campaign(campaign2)
                .build();
        SendMessages message55 = SendMessages.testInsert()
                .name("라마바1")
                .sendCheck(false)
                .sendDateTime(LocalDateTime.now())
//                .campaignMessage(messageTypeB)
                .campaign(campaign2)
                .build();
        SendMessages message66 = SendMessages.testInsert()
                .name("라마바2")
                .sendCheck(false)
                .sendDateTime(LocalDateTime.now())
//                .campaignMessage(messageTypeB)
                .campaign(campaign2)
                .build();

        SendMessages message77 = SendMessages.testInsert()
                .name("라마바3")
                .sendCheck(true)
                .sendDateTime(LocalDateTime.now())
//                .campaignMessage(messageTypeB)
                .campaign(campaign2)
                .build();
        SendMessages message88 = SendMessages.testInsert()
                .name("라마바4")
                .sendCheck(true)
                .sendDateTime(LocalDateTime.now())
//                .campaignMessage(messageTypeB)
                .campaign(campaign2)
                .build();
        sendMessagesRepository.save(message11);
        sendMessagesRepository.save(message22);
        sendMessagesRepository.save(message33);
        sendMessagesRepository.save(message44);
        sendMessagesRepository.save(message55);
        sendMessagesRepository.save(message66);
        sendMessagesRepository.save(message77);
        sendMessagesRepository.save(message88);

        SendMessages message111 = SendMessages.testInsert()
                .name("가나다1")
                .sendCheck(true)
                .sendDateTime(LocalDateTime.now())
                .campaignMessage(messageTypeA)
                .campaign(campaign3)
                .build();
        SendMessages message222 = SendMessages.testInsert()
                .name("가나다2")
                .sendCheck(false)
                .sendDateTime(LocalDateTime.now())
                .campaignMessage(messageTypeA)
                .campaign(campaign3)
                .build();
        SendMessages message333 = SendMessages.testInsert()
                .name("가나다3")
                .sendCheck(false)
                .sendDateTime(LocalDateTime.now())
                .campaignMessage(messageTypeA)
                .campaign(campaign3)
                .build();
        SendMessages message444 = SendMessages.testInsert()
                .name("라마바1")
                .sendCheck(false)
                .sendDateTime(LocalDateTime.now())
                .campaignMessage(messageTypeB)
                .campaign(campaign3)
                .build();
        sendMessagesRepository.save(message111);
        sendMessagesRepository.save(message222);
        sendMessagesRepository.save(message333);
        sendMessagesRepository.save(message444);
    }
}
