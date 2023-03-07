package com.example.swcamp_p03.copyGroup.entity.history;

import com.example.swcamp_p03.copyGroup.entity.CopyGroup;
import com.example.swcamp_p03.copyGroup.entity.GptCopy;
import com.example.swcamp_p03.user.entity.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class CopyGroupHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long copyGroupHistoryId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COPY_GROUP_ID")
    private CopyGroup copyGroup;
    private String coupGroupName;
    private String brandName;
    private String productName;
    private String keyword;
    private String copyType;
    private Boolean favorite;
    private Integer createCount;
    private Integer copyLength;
    private LocalDateTime updateAt;

    private LocalDateTime createdAt;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "USER_ID")
//    private User user;

    @OneToMany(mappedBy = "copyGroupHistory", cascade = CascadeType.ALL)
    private List<GptCopyHistory> gptCopyList = new ArrayList<>();

    public CopyGroupHistory(CopyGroup copyGroup) {
        this.copyGroup = copyGroup;
        this.coupGroupName = copyGroup.getCoupGroupName();
        this.brandName = copyGroup.getBrandName();
        this.productName = copyGroup.getProductName();
        this.keyword = copyGroup.getKeyword();
        this.copyType = copyGroup.getCopyType();
        this.favorite = copyGroup.getFavorite();
        this.createCount = copyGroup.getCreateCount();
        this.copyLength = copyGroup.getCopyLength();
        this.updateAt = copyGroup.getUpdateAt();
        this.createdAt = copyGroup.getCreatedAt();
        List<GptCopy> gptCopyList1 = copyGroup.getGptCopyList();
        for (GptCopy gptCopy : gptCopyList1) {
            gptCopyList.add(new GptCopyHistory(gptCopy.getContent(),this,gptCopy.getPin(),gptCopy.getUpdateAt()));
        }
    }
}
