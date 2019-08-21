package com.xxx.messaging.partitioning;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public AmazonKinesis kinesis(@Value("${aws.region}") final String region) {
        ClientConfiguration config = new ClientConfiguration()
                .withConnectionTimeout(0)
                .withMaxConnections(0)
                .withConnectionTTL(0)
                .withClientExecutionTimeout(0)
                .withConnectionMaxIdleMillis(0)
                .withRetryPolicy(null)
                .withTcpKeepAlive(true)
                .withUserAgentPrefix("");

        return AmazonKinesisClientBuilder.standard()
                .withRegion(region)
                .withClientConfiguration(config)
                .build();
    }
}