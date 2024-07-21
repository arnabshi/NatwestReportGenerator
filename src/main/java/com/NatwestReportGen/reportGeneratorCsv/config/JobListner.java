package com.NatwestReportGen.reportGeneratorCsv.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobListner implements JobExecutionListener {
    private Logger logger= LoggerFactory.getLogger(JobListner.class);
    @Override
    public void beforeJob(JobExecution jobExecution) {
        if(jobExecution.getStatus()== BatchStatus.STARTED){
            logger.info("Job Started-------------------------------------");
        }
        else{
            logger.error("Job Starting fails-------------------------------------");
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus()== BatchStatus.COMPLETED){
            logger.info("Job Completed-------------------------------------");
        } else if (jobExecution.getStatus()== BatchStatus.FAILED) {
            logger.error("Job Execution failed-------------------------------------");
        }
        else{
            logger.error("Job Execution unexpected error-------------------------------------");
        }
    }
}
