package com.xxx.messaging;

import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Map;

@Getter
public abstract class Phase {
    Notification run(Context context, Notification notification) {
        return after(context, process(context, before(context, notification)));
    }

    private Notification process(Context context, Notification notification) {
        if (context.getStatus() != Status.OK) {
            return notification;
        }

        return execute(context, notification);
    }


    private Notification before(Context context, Notification notification) {
        Hook.Response response = context.getBefore().execute(notification, context.getStatus());
        context.setStatus(response.getStatus());

        Map<String, ?> message = response.getMessage();
        if (message != null) {
            notification.setMessage(message);
        }

        return notification;
    }

    private Notification after(Context context, Notification notification) {
        if (context.getStatus() != Status.OK) {
            return notification;
        }

        Hook.Response response = context.getAfter().execute(notification, context.getStatus());
        context.setStatus(response.getStatus());

        Map<String, ?> message = response.getMessage();
        if (message != null) {
            notification.setMessage(message);
        }

        return notification;
    }

    Hooks callbacks(Messaging messaging) {
        Messaging.Callbacks callbacks = messaging.getCallbacks();
        if (callbacks != null) {
            try {
                Field field = callbacks.getClass().getField(name());
                Object value = field.get(callbacks);
                if (value != null) {
                    return (Hooks) value;
                }
            } catch (Exception ignore) { }
        }

        return null;
    }

    public abstract String name();
    public abstract Notification execute(Context context, Notification notification);
}
