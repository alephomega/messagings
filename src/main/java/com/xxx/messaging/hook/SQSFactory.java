package com.xxx.messaging.hook;

import com.amazonaws.services.sqs.AmazonSQS;
import com.xxx.messaging.HookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SQSFactory extends HookFactory<SQS> {
    private final AmazonSQS sqs;

    @Autowired
    public SQSFactory(AmazonSQS sqs) {
        this.sqs = sqs;
    }


    @Override
    public Class<SQS> hookClass() {
        return SQS.class;
    }

    @Override
    public SQS initialize(SQS hook) {
        hook.setSqs(sqs);
        return hook;
    }
}
