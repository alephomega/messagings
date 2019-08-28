package com.xxx.messaging;

import com.amazonaws.services.sqs.AmazonSQS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class Forwarder {
    private final AmazonSQS sqs;
    private final String done;
    private final String replay;

    @Autowired
    Forwarder(AmazonSQS sqs, @Value("${filtering.next-phase}") String done, @Value("${filtering.replay}") String replay) {
        this.sqs = sqs;
        this.done = done;
        this.replay = replay;
    }

    void forward(Context context, Messaging messaging) {
        switch (context.getStatus()) {
            case OK:
                ok(context, messaging);
                break;
            case AGAIN:
                again(context, messaging);
                break;
            case UNKNOWN:
            case ABORT:
        }
    }

    private void ok(Context context, Messaging messaging) {
        messaging.setMetadata(context.getMetadata());
        sqs.sendMessage(done, JsonSerde.messagingToJson(messaging));
    }

    private void again(Context context, Messaging messaging) {
        messaging.setMetadata(context.getMetadata());
        sqs.sendMessage(replay, JsonSerde.messagingToJson(messaging));
    }
}