package com.NatwestReportGen.reportGeneratorCsv.config;


import com.NatwestReportGen.reportGeneratorCsv.Controller.ScheduleRun;
import com.NatwestReportGen.reportGeneratorCsv.Entity.Input;
import com.NatwestReportGen.reportGeneratorCsv.Entity.Output;
import com.NatwestReportGen.reportGeneratorCsv.Entity.Reference;
import com.NatwestReportGen.reportGeneratorCsv.Repository.ReferenceRepo;
import lombok.AllArgsConstructor;
import org.aspectj.apache.bcel.util.ClassPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@AllArgsConstructor
public class BatchConfig {
    Logger logger;
    String upstreamInputPath;
    String outputPath;

    @Autowired
    ReferenceRepo referenceRepo;

    BatchConfig(){
        this.logger= LoggerFactory.getLogger(BatchConfig.class);
        this.upstreamInputPath="G:\\NatwestExcelProject\\input.csv";
        this.outputPath="GeneratedReport\\output.csv";
    }

    @Bean
    public Job runJob(JobRepository jobRepository, PlatformTransactionManager transactionManager,JobListner listner){
        logger.info("Into the runjob function that returns Job-----------");
        return new JobBuilder("InputToOutput",jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listner)
                .start(step1(jobRepository,transactionManager))
                .next(step2(jobRepository,transactionManager))
                .build();
    }

    // step to read data from input.csv and generate output.csv start
    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        logger.info("In step2( Input.csv -> Output.csv ----------------------");
        InputToOutputListner listner=new InputToOutputListner();
        return new StepBuilder("inputStep",jobRepository)
                .<Input, Output>chunk(100,transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .faultTolerant()
                .skipLimit(250)
                .skip(Exception.class)
                .listener(listner)
                .taskExecutor(taskExecutor())
                .build();
    }

    //reader
    @Bean
    public FlatFileItemReader<Input> reader(){
        logger.info("In input.csv reader ----------------------");

        FlatFileItemReader<Input> itemReader=new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource(upstreamInputPath));
        itemReader.setName("InputReading");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }
    private LineMapper<Input> lineMapper() {
        logger.info("linMapping for input.csv----------------------");
        DefaultLineMapper<Input> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("field1", "field2", "field3", "field4", "field5", "refKey1", "refKey2");

        BeanWrapperFieldSetMapper<Input> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Input.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }
    //processor
    @Bean
    public InputToOutputProcessor processor() {
        logger.info("In Processor Input.csv -> Output.csv ----------------------");
        return new InputToOutputProcessor();
    }
    //writer
    @Bean
    public FlatFileItemWriter<Output> writer() {
        logger.info("In writer Input.csv -> Output.csv ----------------------");
        String exportFileHeader = "outfield1,outfield2,outfield3,outfield4,outfield5";
        StringHeaderWriter headerWriter = new StringHeaderWriter(exportFileHeader);
        return new FlatFileItemWriterBuilder<Output>()
                .name("outputItemWriter")
                .headerCallback(headerWriter)
                .resource(new FileSystemResource(outputPath))
                .delimited()
                .names(new String[]{"outfield1", "outfield2", "outfield3", "outfield4", "outfield5"})
                .build();
    }

    // step to read data from input.csv and generate output.csv ends here

    // step to save reference in database so that we can get values by refkey1 and refkey2
    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        logger.info("In step1( reference.csv )----------------------");
        ReferenceListner listner=new ReferenceListner();
        return new StepBuilder("saveReference",jobRepository)
                .<Reference, Reference>chunk(100,transactionManager)
                .reader(referenceReader())
                .processor(refProcessor())
                .writer(refWriter())
                .taskExecutor(taskExecutor())
                .faultTolerant()
                .skipLimit(250)
                .skip(Exception.class)
                .listener(listner)
                .build();
    }

    //reader
    @Bean
    public FlatFileItemReader<Reference> referenceReader(){
        logger.info("In reader( reference.csv )----------------------");
        FlatFileItemReader<Reference> itemReader=new FlatFileItemReader<>();
        itemReader.setResource(new ClassPathResource("reference.csv"));
        itemReader.setName("ReferenceReading");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(referenceLineMapper());
        return itemReader;
    }
    private LineMapper<Reference> referenceLineMapper() {
        logger.info("In linemapping( reference.csv )----------------------");
        DefaultLineMapper<Reference> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("refkey1", "refdata1", "refkey2", "refdata2", "refdata3", "refdata4");

        BeanWrapperFieldSetMapper<Reference> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Reference.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }
    //processor
    @Bean
    public referenceProcessor refProcessor() {
        logger.info("In processor( reference.csv )----------------------");
        return new referenceProcessor();
    }

    //writer
    @Bean
    public RepositoryItemWriter<Reference> refWriter() {
        logger.info("In writer( reference.csv )----------------------");
        RepositoryItemWriter<Reference> writer = new RepositoryItemWriter<>();
        writer.setRepository(referenceRepo);
        writer.setMethodName("save");
        return writer;
    }
    //reference file read write ends here

    //taskexecutor for multithreads
    @Bean
    public TaskExecutor taskExecutor() {
        logger.info("In thread----------------------");
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(125);
        return asyncTaskExecutor;
    }
}
