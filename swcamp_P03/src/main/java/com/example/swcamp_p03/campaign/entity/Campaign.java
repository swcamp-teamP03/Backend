package com.example.swcamp_p03.campaign.entity;

import com.example.swcamp_p03.copyGroup.entity.CopyGroup;
import com.example.swcamp_p03.copyGroup.entity.history.CopyGroupHistory;
import com.example.swcamp_p03.customerGroup.entity.CustomerGroup;
import com.example.swcamp_p03.customerGroup.entity.history.CustomerGroupHistory;
import com.example.swcamp_p03.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_GROUP_ID")
    private CustomerGroup customerGroup;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COPY_GROUP_ID")
    private CopyGroup copyGroup;
}
