package com.xxx.messaging;

import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Map;

@Getter
public abstract class Phase {
    Notification process(Context context, Notification notification, Metadata metadata) {
        return after(context, run(context, before(context, notification, metadata), metadata), metadata);
    }

    private Notification run(Context context, Notification notification, Metadata metadata) {
        Step step = Step.Process;
        if (skip(context, step, metadata)) {
            return notification;
        }

        try {
            notification = handle(context, notification);
        } catch (Exception e) {
            context.again(step);
            return notification;
        }

        context.ok(step);
        return notification;
    }

    private Notification before(Context context, Notification notification, Metadata metadata) {
        return hook(context, notification, Step.Before, metadata);
    }

    private Notification after(Context context, Notification notification, Metadata metadata) {
        return hook(context, notification, Step.After, metadata);
    }

    private Notification hook(Context context, Notification notification, Step step, Metadata metadata) {
        if (skip(context, step, metadata)) {
            return notification;
        }

        Hook.Response response = context.getBefore().execute(notification, context.getStatus());
        Map<String, ?> message = response.getMessage();
        if (message != null) {
            notification.setMessage(message);
        }

        switch (response.getStatus()) {
            case OK:
                context.ok(step);
                break;
            case AGAIN:
                context.again(step);
                break;
            case ABORT:
            default:
                context.abort(step);
        }

        return notification;
    }

    private boolean skip(Context context, Step step, Metadata metadata) {
        Status status = context.getStatus();
        if (status != Status.UNKNOWN && status != Status.OK) {
            return true;
        }

        Metadata.Replay replay = metadata.getReplay();
        return replay != null && step.before(replay.getStep());
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
    public abstract Notification handle(Context context, Notification notification);
}