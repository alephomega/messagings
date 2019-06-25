package com.xxx.messaging.hook;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SQS extends AbstractHook {
    private AmazonSQS sqs;
    private String url;

    @Builder
    SQS(AmazonSQS sqs, String url, ReturnCode onSuccess, ReturnCode onFailure) {
        super(onSuccess, onFailure);
        this.sqs = sqs;
        this.url = url;
    }

    @Override
    public boolean call(String id, String message) {
        SendMessageRequest request = new SendMessageRequest(url, message);

        try {
            sqs.sendMessage(request);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}