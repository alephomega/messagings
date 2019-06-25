package com.xxx.messaging;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Forwarder {
    private static final Gson GSON = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
            .disableHtmlEscaping()
            .create();

    private final AmazonSQS sqs;
    private final String queueUrl;

    @Autowired
    public Forwarder(AmazonSQS sqs, String queueUrl) {
        this.sqs = sqs;
        this.queueUrl = queueUrl;
    }

    void forward(Message message) {
        sqs.sendMessage(new SendMessageRequest(queueUrl, GSON.toJson(message)));
    }
}