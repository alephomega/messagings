package com.xxx.messaging;

import com.xxx.messaging.hook.OK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
class Processor {
    private final Phase phase;
    private final Forwarder forwarder;
    private final Helper helper;
    private final Iterable<ProcessListener> processListeners;

    @Autowired
    Processor(Phase phase, Forwarder forwarder, Helper helper, Iterable<ProcessListener> processListeners) {
        this.phase = phase;
        this.forwarder = forwarder;
        this.helper = helper;
        this.processListeners = processListeners;
    }

    void process(Messaging messaging) {
        Hooks hooks = callbacks(messaging);
        Context context = Context.builder().before(hooks.getBefore()).after(hooks.getAfter()).build();

        onContextInitialized(context);

        String name = phase.name();
        onBefore(name, messaging, context);

        Iterator<Notification> iterator = iterator(messaging);
        while(iterator.hasNext()) {
            Notification notification = iterator.next();
            onBefore(name, notification, context);
            notification = phase.run(context, notification);
            onAfter(name, notification, context);

            forwarder.forward(context, toMessaging(notification, messaging.getCallbacks(), null));
        }

        onAfter(name, messaging, context);
    }

    private Iterator<Notification> iterator(Messaging messaging) {
        Iterator<String> iterator = helper.recipients(messaging).iterator();

        return new Iterator<Notification>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Notification next() {
                String to = iterator.next();
                return Notification.builder()
                        .id(messaging.getId())
                        .topic(messaging.getTopic())
                        .collapseKey(messaging.getCollapseKey())
                        .to(to)
                        .message(messaging.getMessage())
                        .build();
            }
        };
    }

    private Hooks callbacks(Messaging messaging) {
        Hook before = OK.getInstance();
        Hook after = OK.getInstance();

        Hooks hooks = phase.callbacks(messaging);
        if (hooks != null) {
            if (hooks.getBefore() != null) {
                before = hooks.getBefore();
            }

            if (hooks.getAfter() != null) {
                after = hooks.getAfter();
            }
        }

        return new Hooks(before, after);
    }

    private Messaging toMessaging(Notification notification, Messaging.Callbacks callbacks, Messaging.Metadata metadata) {
        return Messaging.builder()
                .topic(notification.getTopic())
                .id(notification.getId())
                .to(notification.getTo())
                .collapseKey(notification.getCollapseKey())
                .message(notification.getMessage())
                .callbacks(callbacks)
                .metadata(metadata)
                .build();
    }

    private void onContextInitialized(Context context) {
        for (ProcessListener processListener : processListeners) {
            processListener.onContextInitiated(context);
        }
    }

    private void onBefore(String phase, Messaging messaging, Context context) {
        for (ProcessListener processListener : processListeners) {
            processListener.onBefore(phase, messaging, context);
        }
    }

    private void onAfter(String phase, Messaging messaging, Context context) {
        for (ProcessListener processListener : processListeners) {
            processListener.onAfter(phase, messaging, context);
        }
    }

    private void onBefore(String phase, Notification notification, Context context) {
        for (ProcessListener processListener : processListeners) {
            processListener.onBefore(phase, notification, context);
        }
    }

    private void onAfter(String phase, Notification notification, Context context) {
        for (ProcessListener processListener : processListeners) {
            processListener.onAfter(phase, notification, context);
        }
    }
}