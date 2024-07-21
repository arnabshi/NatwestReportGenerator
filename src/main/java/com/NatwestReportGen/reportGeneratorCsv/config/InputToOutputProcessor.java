package com.NatwestReportGen.reportGeneratorCsv.config;

import com.NatwestReportGen.reportGeneratorCsv.Entity.Input;
import com.NatwestReportGen.reportGeneratorCsv.Entity.Output;
import com.NatwestReportGen.reportGeneratorCsv.Entity.Reference;
import com.NatwestReportGen.reportGeneratorCsv.Repository.ReferenceRepo;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class InputToOutputProcessor implements ItemProcessor<Input, Output> {
    @Autowired
    ReferenceRepo referenceRepo;
    @Override
    public Output process(Input item) throws Exception {
        Reference referenceDataByRefkey1=referenceRepo.findByRefkey1(item.getRefkey1());
        Reference referenceDataByRefkey2=referenceRepo.findByRefkey2(item.getRefkey2());
        Output output;
        try{
            output= Output.builder()
                    .outfield1(item.getField1()+item.getField2())
                    .outfield2(referenceDataByRefkey1.getRefdata1())
                    .outfield3(referenceDataByRefkey2.getRefdata2()+referenceDataByRefkey2.getRefdata3())
                    .outfield4(BigDecimal.valueOf(item.getField5()*Math.max(item.getField5(), referenceDataByRefkey2.getRefdata4())))
                    .outfield5(BigDecimal.valueOf(Math.max(item.getField5(), referenceDataByRefkey2.getRefdata4())))
                    .build();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return output;
    }
}
