package com.NatwestReportGen.reportGeneratorCsv.config;

import com.NatwestReportGen.reportGeneratorCsv.Entity.Reference;
import org.springframework.batch.item.ItemProcessor;

public class referenceProcessor implements ItemProcessor<Reference,Reference> {
    @Override
    public Reference process(Reference item) throws Exception {
        return item;
    }
}
