package com.NatwestReportGen.reportGeneratorCsv.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleRun {
    Logger logger= LoggerFactory.getLogger(ScheduleRun.class);
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;
    @Scheduled(cron = "0 08 23 * * ?")
    public void scheduledJobRun() throws Exception{
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(job, jobParameters);
            logger.info("Scheduled report generation start at " + java.time.LocalDate.now()+"-----------------------");
        } catch (Exception e) {
            e.getMessage();
            logger.error("Scheduled report generation failed -----------------------");
        }
    }
}
