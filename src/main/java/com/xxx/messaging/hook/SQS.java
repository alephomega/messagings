package com.xxx.messaging.hook;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.xxx.messaging.Hook;
import com.xxx.messaging.Status;
import com.xxx.messaging.Symbol;
import lombok.Builder;
import lombok.Getter;

@Getter
@Symbol("SQS")
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
    public Hook.Response call(String message) {
        sqs.sendMessage(new SendMessageRequest(url, message));
        return new Hook.Response(Status.OK);
    }
}