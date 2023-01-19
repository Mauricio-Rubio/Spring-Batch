package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
/**
 * This configuration class configures the Spring Batch job that
 * is used to demonstrate that our item reader reads the correct
 * information from the CSV file.
 */
@Configuration
public class SpringBatchExampleJobConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringBatchExampleJobLauncher.class);

    @Bean
    public ItemReader<StudentDTO> itemReader() {
        LineMapper<StudentDTO> studentLineMapper = createStudentLineMapper();

        return new FlatFileItemReaderBuilder<StudentDTO>()
                .name("studentReader")
                .resource(new ClassPathResource("data/students.csv"))
                .lineMapper(studentLineMapper)
                .build();
    }

    // Da formato a cada linea y las convierte en objetos
    private LineMapper<StudentDTO> createStudentLineMapper() {
        DefaultLineMapper<StudentDTO> studentLineMapper = new DefaultLineMapper<>();

        LineTokenizer studentLineTokenizer = createStudentLineTokenizer();
        studentLineMapper.setLineTokenizer(studentLineTokenizer);

        FieldSetMapper<StudentDTO> studentInformationMapper = createStudentInformationMapper();
        studentLineMapper.setFieldSetMapper(studentInformationMapper);

        return studentLineMapper;
    }

    private LineTokenizer createStudentLineTokenizer() {
        DelimitedLineTokenizer studentLineTokenizer = new DelimitedLineTokenizer();
        studentLineTokenizer.setDelimiter(";");
        studentLineTokenizer.setNames(new String[]{"name", "emailAddress", "purchasedPackage"});
        return studentLineTokenizer;
    }

    private FieldSetMapper<StudentDTO> createStudentInformationMapper() {
        BeanWrapperFieldSetMapper<StudentDTO> studentInformationMapper = new BeanWrapperFieldSetMapper<>();
        studentInformationMapper.setTargetType(StudentDTO.class);
        return studentInformationMapper;
    }

    @Bean
    public ItemWriter<StudentDTO> itemWriter(Environment environment) {
        String exportFilePath = environment
                .getRequiredProperty("batch.job.export.file.path");
        Resource exportFileResource = new FileSystemResource(exportFilePath);
 
        String exportFileHeader =
                environment.getRequiredProperty("batch.job.export.file.header");
        StringHeaderWriter headerWriter = new StringHeaderWriter(exportFileHeader);
 
        LineAggregator<StudentDTO> lineAggregator = createStudentLineAggregator();
 
        LoggingItemWriter ob = new LoggingItemWriter();
        LOGGER.info(ob.toString());
        return new FlatFileItemWriterBuilder<StudentDTO>()
                .name("studentWriter")
                .headerCallback(headerWriter)
                .lineAggregator(lineAggregator)
                .resource(exportFileResource)
                .build();
    }
    
    
    private LineAggregator<StudentDTO> createStudentLineAggregator() {
        DelimitedLineAggregator<StudentDTO> lineAggregator = 
                new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(";");
 
        FieldExtractor<StudentDTO> fieldExtractor = createStudentFieldExtractor();
        lineAggregator.setFieldExtractor(fieldExtractor);
 
        return lineAggregator;
    }
 
    private FieldExtractor<StudentDTO> createStudentFieldExtractor() {
        BeanWrapperFieldExtractor<StudentDTO> extractor 
                = new BeanWrapperFieldExtractor<>();
        extractor.setNames(new String[] {
                "name", 
                "emailAddress", 
                "purchasedPackage"
        });
        return extractor;
    }
    
    

    /**
     * Creates a bean that represents the only step of our batch job.
     * @param reader
     * @param writer
     * @param stepBuilderFactory
     * @return
     */
    @Bean
    public Step exampleJobStep(ItemReader<StudentDTO> reader,
                               ItemWriter<StudentDTO> writer,
                               StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("exampleJobStep")
                .<StudentDTO, StudentDTO>chunk(10)
                .reader(reader)
                .writer(writer)
                .build();
    }

    /**
     * Creates a bean that represents our example batch job.
     * @param exampleJobStep
     * @param jobBuilderFactory
     * @return
     */
    @Bean
    public Job exampleJob(Step exampleJobStep,
                          JobBuilderFactory jobBuilderFactory) {
        return jobBuilderFactory.get("exampleJob")
                .incrementer(new RunIdIncrementer())
                .flow(exampleJobStep)
                .end()
                .build();
    }
}