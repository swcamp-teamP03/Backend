package com.example.swcamp_p03.campaign.entity;

import com.example.swcamp_p03.copyGroup.entity.CopyGroup;
import com.example.swcamp_p03.copyGroup.entity.history.CopyGroupHistory;
import com.example.swcamp_p03.customerGroup.entity.CustomerGroup;
import com.example.swcamp_p03.customerGroup.entity.history.CustomerGroupHistory;
import com.example.swcamp_p03.user.entity.User;
import lombok.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campaignId;
    private String campaignName;
    private String messageType;
//    private String sendState;
    private String sendURL;
    private LocalDateTime sendingDate;
    private LocalDateTime createdAt;
    private Boolean favorite;
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_GROUP_ID")
    private CustomerGroup customerGroup;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COPY_GROUP_ID")
    private CopyGroup copyGroup;

    @Builder(builderClassName = "testInsert",builderMethodName = "testInsert")
    public Campaign(String campaignName, String messageType, String sendType, LocalDateTime createdAt, LocalDateTime sendingDate, boolean favorite, User user) {
        this.campaignName = campaignName;
        this.messageType = messageType;
        this.createdAt = createdAt;
        this.sendingDate = sendingDate;
        this.favorite = favorite;
        this.user = user;
    }

}
