package com.xxx.messaging.hook;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.xxx.messaging.Hook;
import com.xxx.messaging.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SQS extends Hook {
    private AmazonSQS sqs;
    private String url;

    @Builder
    SQS(AmazonSQS sqs, String url, Status onError) {
        super(onError);
        this.sqs = sqs;
        this.url = url;
    }

    @Override
    public Status call(String message) {
        sqs.sendMessage(new SendMessageRequest(url, message));
        return Status.OK;
    }
}