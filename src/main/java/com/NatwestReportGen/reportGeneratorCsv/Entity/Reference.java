package com.NatwestReportGen.reportGeneratorCsv.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Reference {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(unique = true)
    private String refkey1;
    private String refdata1;
    @Column(unique = true)
    private String refkey2;
    private String refdata2;
    private String refdata3;
    private double refdata4;
}
