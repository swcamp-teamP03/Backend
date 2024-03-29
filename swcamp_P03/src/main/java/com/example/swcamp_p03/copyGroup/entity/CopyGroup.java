package com.example.swcamp_p03.copyGroup.entity;

import com.example.swcamp_p03.copyGroup.dto.GptCopyDto;
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
public class CopyGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long copyGroupId;
    private String coupGroupName;
    private String brandName;
    private String productName;
    private String keyword;
    private String copyType;
    private String sector;
    private Boolean favorite;
    private Integer createCount;
    private Integer copyLength;
    private Boolean unableEdit;
    private String targetAge;
    private String targetGender;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "copyGroup", cascade = CascadeType.ALL)
    private List<GptCopy> gptCopyList = new ArrayList<>();

    public void addGptCopy(GptCopy gptCopy){
        gptCopyList.add(gptCopy);
    }

    public void clickFavorite(Boolean setValue) {
        favorite = setValue;
    }

    public void updateCopyList(List<GptCopy> copyList) {
        updateAt = LocalDateTime.now();
        gptCopyList.clear();
        for (GptCopy gptCopy : copyList) {
            gptCopyList.add(gptCopy);
        }
    }

    public void makeUnableEdit(){
        unableEdit = true;
    }
}
