package com.xxx.messaging;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Component
public class ProgressReporter implements ProcessListener {
    private final AmazonKinesis kinesis;
    private final String stream;

    @Autowired
    ProgressReporter(AmazonKinesis kinesis, String stream) {
        this.kinesis = kinesis;
        this.stream = stream;
    }

    @Override
    public void onBefore(String phase, Messaging messaging, Context context) {
        String topic = messaging.getTopic();
        String id = messaging.getId();
        if (topic == null) {
            send(id, String.format("{\"phase\":\"%s\",\"event\":\"START\",\"id\":\"%s\"}", phase, id));
        } else {
            send(id, String.format("{\"phase\":\"%s\",\"event\":\"START\",\"id\":\"%s\",\"topic\":\"%s\"}", phase, id, topic));
        }
    }

    @Override
    public void onAfter(String phase, Messaging messaging, Context context) {
        String topic = messaging.getTopic();
        String id = messaging.getId();
        if (topic == null) {
            send(id, String.format("{\"phase\":\"%s\",\"event\":\"COMPLETE\",\"id\":\"%s\"}", phase, id));
        } else {
            send(id, String.format("{\"phase\":\"%s\",\"event\":\"COMPLETE\",\"id\":\"%s\",\"topic\":\"%s\"}", phase, id, topic));
        }
    }

    @Override
    public void onAfter(String phase, Notification notification, Context context) {
        String topic = notification.getTopic();
        String id = notification.getId();
        if (topic == null) {
            send(id, String.format("{\"phase\":\"%s\",\"event\":\"PROCESS\",\"id\":\"%s\",\"to\":\"%s\",\"status\":\"%s\"}", phase, id, notification.getTo(), context.getStatus()));
        } else {
            send(id, String.format("{\"phase\":\"%s\",\"event\":\"PROCESS\",\"id\":\"%s\",\"topic\":\"%s\",\"to\":\"%s\",\"status\":\"%s\"}", phase, id, topic, notification.getTo(), context.getStatus()));
        }
    }

    @Override
    public void onError(String phase, Messaging messaging, Context context) {
        String topic = messaging.getTopic();
        String id = messaging.getId();
        if (topic == null) {
            send(id, String.format("{\"phase\":\"%s\",\"event\":\"ERROR\",\"id\":\"%s\"}", phase, id));
        } else {
            send(id, String.format("{\"phase\":\"%s\",\"event\":\"ERROR\",\"id\":\"%s\",\"topic\":\"%s\"}", phase, id, topic));
        }
    }

    private void send(String id, String data) {
        PutRecordRequest putRecordRequest = new PutRecordRequest()
                .withStreamName(stream)
                .withPartitionKey(id)
                .withData(ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8)));

        kinesis.putRecord(putRecordRequest);
    }

    @Override
    public void onContextInitiated(Context context) { }

    @Override
    public void onBefore(String phase, Notification notification, Context context) { }
}