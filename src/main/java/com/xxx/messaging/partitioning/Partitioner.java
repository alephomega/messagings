package com.xxx.messaging.partitioning;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.xxx.messaging.JsonSerde;
import com.xxx.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Component
public class Partitioner {
    private final AmazonKinesis kinesis;
    private final String stream;

    @Autowired
    public Partitioner(AmazonKinesis kinesis, @Value("${partitioning.stream}") String stream) {
        this.kinesis = kinesis;
        this.stream = stream;
    }

    Notification run(Notification notification) {
        PutRecordRequest request = new PutRecordRequest()
                .withStreamName(stream)
                .withPartitionKey(notification.getTo())
                .withData(ByteBuffer.wrap(JsonSerde.notificationToJson(notification).getBytes(StandardCharsets.UTF_8)));

        kinesis.putRecord(request);
        return notification;
    }
}