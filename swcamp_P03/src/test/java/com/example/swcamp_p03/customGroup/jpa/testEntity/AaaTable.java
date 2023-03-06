package com.example.swcamp_p03.customGroup.jpa.testEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AaaTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aaaTableId;
    private String name;
    //    private String bbbTableId;
    @OneToMany(mappedBy = "aaaTable")
    private List<BbbTable> bbbTables = new ArrayList<>();

    public AaaTable(String name) {
        this.name = name;
//        this.bbbTableId = UUID.randomUUID().toString();
    }
}
