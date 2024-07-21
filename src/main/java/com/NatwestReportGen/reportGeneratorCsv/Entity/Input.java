package com.NatwestReportGen.reportGeneratorCsv.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Input {

    private String field1;
    private String field2;
    private String field3;

    private String field4;
    private double field5;
    private String refkey1;
    private String refkey2;
}
