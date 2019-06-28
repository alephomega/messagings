package com.xxx.messaging;

import com.amazonaws.services.sqs.AmazonSQS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Forwarder {
    private final AmazonSQS sqs;
    private final String done;
    private final String replay;

    @Autowired
    public Forwarder(AmazonSQS sqs,
                     @Value("${sqs.next-phase}") String done,
                     @Value("${sqs.replay}") String replay) {

        this.sqs = sqs;
        this.done = done;
        this.replay = replay;
    }

    void forward(PhaseContext context, Messaging messaging) {
        switch (context.getStatus()) {
            case OK:
                ok(messaging);
                break;
            case AGAIN:
                replay(messaging);
                break;
            case ABORT:
        }
    }

    private void ok(Messaging messaging) {
        messaging.setReplay(0);
        sqs.sendMessage(done, MessagingJsonSerde.toJson(messaging));
    }

    private void replay(Messaging messaging) {
        sqs.sendMessage(replay, MessagingJsonSerde.toJson(messaging));
    }
}