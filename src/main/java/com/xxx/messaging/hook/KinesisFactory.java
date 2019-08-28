package com.xxx.messaging.hook;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.xxx.messaging.HookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KinesisFactory extends HookFactory<Kinesis> {
    private final AmazonKinesis kinesis;

    @Autowired
    public KinesisFactory(AmazonKinesis kinesis) {
        this.kinesis = kinesis;
    }

    @Override
    public Class<Kinesis> hookClass() {
        return Kinesis.class;
    }

    @Override
    public Kinesis initialize(Kinesis hook) {
        hook.setKinesis(kinesis);
        return hook;
    }
}
