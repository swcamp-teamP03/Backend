package com.example.swcamp_p03.customerGroup.entity;

import com.example.swcamp_p03.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerGroupId;
    private String customerGroupName;
    private Boolean favorite;
    private Boolean unableEdit;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Embedded
    private ExcelFile excelFile;

    @ElementCollection
    @CollectionTable(name = "PROPERTY",
            joinColumns = @JoinColumn(name = "PROPERTY_ID"))
    private List<String> properties = new ArrayList<>();

    @Builder(builderClassName = "register",builderMethodName = "register")
    public CustomerGroup(String customerGroupName, Boolean favorite, User user, ExcelFile excelFile) {
        this.customerGroupName = customerGroupName;
        this.favorite = favorite;
        this.createdAt = LocalDateTime.now();
        this.user = user;
        this.excelFile = excelFile;
        this.unableEdit = false;
    }

    public void update(String customerGroupName) {
        this.customerGroupName = customerGroupName;
    }

    public void makeUnableEdit(){
        unableEdit = true;
    }

}
