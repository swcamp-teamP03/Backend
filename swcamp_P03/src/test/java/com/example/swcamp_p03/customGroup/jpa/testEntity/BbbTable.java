package com.example.swcamp_p03.customGroup.jpa.testEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BbbTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bbbTableId;
    private String content;
    //    private String aaaTableId;
    @ManyToOne
    @JoinColumn(name = "AAA_TABLE_ID")
    private AaaTable aaaTable;

    public BbbTable(String content, AaaTable aaaTableId) {
        this.content = content;
        this.aaaTable = aaaTableId;
    }

    public void change(AaaTable aaaTable) {
        this.aaaTable = aaaTable;
        aaaTable.getBbbTables().add(this);
    }
}
