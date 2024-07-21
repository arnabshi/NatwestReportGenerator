package com.NatwestReportGen.reportGeneratorCsv.config;

import com.NatwestReportGen.reportGeneratorCsv.Entity.Input;
import com.NatwestReportGen.reportGeneratorCsv.Entity.Output;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;

public class ReferenceListner implements SkipListener<Output,Output> {
    Logger logger = LoggerFactory.getLogger(InputToOutputListner.class);

    @Override // item reader
    public void onSkipInRead(Throwable throwable) {
        logger.info("A failure on read {----------------------------------------------------------} ", throwable.getMessage());
    }

    @Override // item writter
    public void onSkipInWrite(Output item, Throwable throwable) {
        logger.info("A failure on write {----------------------------------------------------------}}", throwable.getMessage(), item);
    }

    @SneakyThrows
    @Override // item processor
    public void onSkipInProcess(Output output, Throwable throwable) {
        logger.info("Item {}  was skipped due to the exception  {----------------------------------------------------------}", new ObjectMapper().writeValueAsString(output),
                throwable.getMessage());
    }
}
