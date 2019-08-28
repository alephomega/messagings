package com.xxx.messaging.hook;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.xxx.messaging.Hook;
import com.xxx.messaging.HookFailedException;
import com.xxx.messaging.Status;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Setter
@Getter
public class Kinesis extends Hook {
    private AmazonKinesis kinesis;
    private String stream;

    public Kinesis(AmazonKinesis kinesis, String stream, Status onError) {
        super(onError);
        this.kinesis = kinesis;
        this.stream = stream;
    }

    @Override
    public Response call(String to, String message) throws HookFailedException {
        PutRecordRequest request = new PutRecordRequest()
                .withStreamName(stream)
                .withPartitionKey(to)
                .withData(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));

        try {
            kinesis.putRecord(request);
        } catch (Exception e) {
            return new Response(getOnError());
        }

        return new Response(Status.OK);
    }
s}