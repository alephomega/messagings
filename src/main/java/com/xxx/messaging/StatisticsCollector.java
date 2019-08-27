package com.xxx.messaging;

import org.springframework.stereotype.Component;

@Component
public class StatisticsCollector implements ProcessListener {
    private static final String STATISTICS_ATTRIBUTE_KEY = "statistics";

    @Override
    public void onContextInitiated(Context context) {
        context.getAttributes().put(STATISTICS_ATTRIBUTE_KEY, new Stat());
    }

    @Override
    public void onError(String phase, Messaging messaging, Context context) {
        stat(context).error();
    }

    @Override
    public void onAfter(String phase, Notification notification, Context context) {
        Stat stat = (Stat) context.getAttributes().get(STATISTICS_ATTRIBUTE_KEY);
        switch (context.getStatus()) {
            case OK:
                stat.ok();
                break;
            case AGAIN:
                stat.again();
                break;
            case ABORT:
            default:
                stat.abort();
        }
    }

    static Stat stat(Context context) {
        return (Stat) context.getAttributes().get(STATISTICS_ATTRIBUTE_KEY);
    }

    @Override
    public void onBefore(String phase, Messaging messaging, Context context) { }

    @Override
    public void onAfter(String phase, Messaging messaging, Context context) { }

    @Override
    public void onBefore(String phase, Notification notification, Context context) { }
}