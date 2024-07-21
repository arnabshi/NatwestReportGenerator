package com.NatwestReportGen.reportGeneratorCsv;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
public class ReportGeneratorCsvApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportGeneratorCsvApplication.class, args);
	}

}
