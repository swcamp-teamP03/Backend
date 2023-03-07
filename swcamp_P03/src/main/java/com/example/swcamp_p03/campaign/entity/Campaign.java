package com.example.swcamp_p03.campaign.entity;

import com.example.swcamp_p03.copyGroup.entity.history.CopyGroupHistory;
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
    private String sendType;
    private String sendURL;
    private LocalDateTime sendingDate;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
    @ManyToOne
    @JoinColumn(name = "CUSTOMER_GROUP_HISTORY_ID")
    private CustomerGroupHistory customerGroupHistory;
    @ManyToOne
    @JoinColumn(name = "COPY_GROUP_HISTORY_ID")
    private CopyGroupHistory copyGroupHistory;
}
