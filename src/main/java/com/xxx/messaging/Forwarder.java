package com.xxx.messaging;

import com.amazonaws.services.sqs.AmazonSQS;

public class Forwarder {
    private final AmazonSQS sqs;
    private final String done;
    private final String replay;

    public Forwarder(AmazonSQS sqs,
                     String done,
                     String replay) {
        this.sqs = sqs;
        this.done = done;
        this.replay = replay;
    }

    void forward(Context context, Messaging messaging) {
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
        messaging.reset();
        sqs.sendMessage(done, JsonSerde.messagingToJson(messaging));
    }

    private void replay(Messaging messaging) {
        Messaging.Metadata metadata = messaging.getMetadata();
        int r = metadata.getReplay() + 1;
        metadata.setReplay(r);

        sqs.sendMessage(replay, JsonSerde.messagingToJson(messaging));
    }
}