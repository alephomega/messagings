package com.xxx.messaging;

import com.amazonaws.services.kinesis.AmazonKinesis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProgressReporter implements ProcessListener {
    private final AmazonKinesis kinesis;
    private final Stat stat;

    @Autowired
    ProgressReporter(AmazonKinesis kinesis) {
        this.kinesis = kinesis;
        this.stat = new Stat();
    }

    @Override
    public void onBefore(String phase, Messaging messaging, Context context) {

    }

    @Override
    public void onAfter(String phase, Messaging messaging, Context context) {

    }

    @Override
    public void onContextInitiated(Context context) {

    }

    @Override
    public void onBefore(String phase, Notification notification, Context context) {

    }

    @Override
    public void onAfter(String phase, Notification notification, Context context) {

    }
}