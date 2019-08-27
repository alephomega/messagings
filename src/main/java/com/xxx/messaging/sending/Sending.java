package com.xxx.messaging.sending;

import com.xxx.messaging.Context;
import com.xxx.messaging.Notification;
import com.xxx.messaging.Phase;
import org.springframework.stereotype.Component;

@Component
public class Sending extends Phase {
    private final Sender sender;

    public Sending(Sender sender) {
        this.sender = sender;
    }

    @Override
    public Notification handle(Context context, Notification notification) {
        return sender.run(notification);
    }

    @Override
    public String name() {
        return "sending";
    }
}