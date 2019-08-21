package com.xxx.messaging.filtering;


import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
    public RestTemplate restTemplate(@Value("${hook.http.max-connections}") int maxConnections,
                                     @Value("${hook.http.max-connections-per-route}") int maxConnectionsPerRoute,
                                     @Value("${hook.http.connection-request-timeout}") int connectionRequestTimeout,
                                     @Value("${hook.http.connect-timeout}") int connectTimeout,
                                     @Value("${hook.http.read-timeout}") int readTimeout) {

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder
                        .create()
                        .setMaxConnPerRoute(maxConnectionsPerRoute)
                        .setMaxConnTotal(maxConnections)
                        .build());

        factory.setConnectionRequestTimeout(connectionRequestTimeout);
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);

        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        return restTemplate;
    }

    @Bean
    public AmazonSQS sqs(@Value("${aws.region}") final String region) {
        return AmazonSQSClientBuilder.standard()
                .withRegion(region)
                .build();
    }
}