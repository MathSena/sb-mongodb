package com.mathsena.sbmongodb.config;

import com.mathsena.sbmongodb.model.Player;
import com.mathsena.sbmongodb.processor.PlayerProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class PlayerBatchConfig {

    @Autowired
    @Qualifier("transactionManager")
    private PlatformTransactionManager transactionManager;

    private final PlayerProcessor playerProcessor;

    private final MongoTemplate mongoTemplate;

    public PlayerBatchConfig(PlayerProcessor playerProcessor, MongoTemplate mongoTemplate) {
        this.playerProcessor = playerProcessor;
        this.mongoTemplate = mongoTemplate;
        log.debug("PlayerBatchConfig initialized with PlayerProcessor and MongoTemplate.");
    }

    @Bean
    public FlatFileItemReader<Player> playerReader() {
        log.debug("Configuring FlatFileItemReader for players.csv.");
        FlatFileItemReader<Player> reader = new FlatFileItemReader<>();
        // Set the resource to the CSV file in the classpath
        reader.setResource(new org.springframework.core.io.ClassPathResource("players.csv"));
        reader.setLineMapper(new DefaultLineMapper<>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                // Set names corresponding to the CSV columns
                setNames(new String[]{"id", "name", "position", "jerseyNumber", "teamName"});
                log.debug("DelimitedLineTokenizer configured with column names.");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                // Set the target type to your entity class
                setTargetType(Player.class);
                log.debug("BeanWrapperFieldSetMapper configured to map to Player class.");
            }});
        }});
        return reader;
    }

    public MongoItemWriter<Player> playerMongoWriter() {
        log.debug("Configuring MongoItemWriter for collection 'players'.");
        MongoItemWriter<Player> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("players");
        return writer;
    }

    @Bean
    public Job job(JobRepository jobRepository, Step step1) {
        log.info("Creating Job with step1.");
        return new JobBuilder("job", jobRepository)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository) {
        log.info("Configuring step1 with chunk size of 10.");
        return new StepBuilder("step1", jobRepository)
                .<Player, Player>chunk(10, transactionManager)
                .reader(playerReader())
                .processor(playerProcessor)
                .writer(playerMongoWriter())
                .build();
    }
}
