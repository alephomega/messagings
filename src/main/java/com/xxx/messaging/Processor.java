package com.xxx.messaging;

import com.xxx.messaging.hook.OK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Slf4j
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

    Stat process(Messaging messaging) {
        Hooks hooks = callbacks(messaging);
        Context context = Context.builder()
                .before(hooks.getBefore())
                .after(hooks.getAfter())
                .metadata(messaging.getMetadata())
                .build();

        onContextInitialized(context);

        String name = phase.name();
        onBefore(name, messaging, context);

        try {
            Iterator<Notification> iterator = iterator(messaging);
            while (iterator.hasNext()) {
                Notification notification = iterator.next();
                onBefore(name, notification, context);
                notification = phase.process(context, notification, messaging.getMetadata());
                onAfter(name, notification, context);

                forward(context, toMessaging(notification, messaging.getCallbacks()));
                context.next();
            }

            onAfter(name, messaging, context);
        } catch (Exception e) {
            forward(context.error(), messaging);
            onError(name, messaging, context);
        }

        return StatisticsCollector.stat(context);
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

    private void forward(Context context, Messaging messaging) {
        try {
            forwarder.forward(context, messaging);
        } catch (Exception e) {

        }
    }

    private void onContextInitialized(Context context) {
        for (ProcessListener processListener : processListeners) {
            try {
                processListener.onContextInitiated(context);
            } catch (Exception e) {

            }
        }
    }

    private void onBefore(String phase, Messaging messaging, Context context) {
        for (ProcessListener processListener : processListeners) {
            try {
                processListener.onBefore(phase, messaging, context);
            } catch (Exception e) {

            }
        }
    }

    private void onAfter(String phase, Messaging messaging, Context context) {
        for (ProcessListener processListener : processListeners) {
            try {
                processListener.onAfter(phase, messaging, context);
            } catch (Exception e) {

            }
        }
    }

    private void onBefore(String phase, Notification notification, Context context) {
        for (ProcessListener processListener : processListeners) {
            try {
                processListener.onBefore(phase, notification, context);
            } catch (Exception e) {

            }
        }
    }

    private void onAfter(String phase, Notification notification, Context context) {
        for (ProcessListener processListener : processListeners) {
            try {
                processListener.onAfter(phase, notification, context);
            } catch (Exception e) {

            }
        }
    }

    private void onError(String phase, Messaging messaging, Context context) {
        for (ProcessListener processListener : processListeners) {
            try {
                processListener.onError(phase, messaging, context);
            } catch (Exception e) {

            }
        }
    }

    private Messaging toMessaging(Notification notification, Messaging.Callbacks callbacks) {
        return Messaging.builder()
                .topic(notification.getTopic())
                .id(notification.getId())
                .to(notification.getTo())
                .collapseKey(notification.getCollapseKey())
                .message(notification.getMessage())
                .callbacks(callbacks)
                .build();
    }
}