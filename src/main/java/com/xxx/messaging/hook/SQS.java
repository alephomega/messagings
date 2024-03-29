package com.xxx.messaging.hook;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.xxx.messaging.Hook;
import com.xxx.messaging.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SQS extends Hook {
    private AmazonSQS sqs;
    private String url;

    SQS(AmazonSQS sqs, String url, Status onError) {
        super(onError);
        this.sqs = sqs;
        this.url = url;
    }

    @Override
    public Hook.Response call(String to, String message) {
        sqs.sendMessage(new SendMessageRequest(url, message));
        return new Hook.Response(Status.OK);
    }
}