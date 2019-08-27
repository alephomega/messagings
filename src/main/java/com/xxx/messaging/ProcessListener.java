package com.xxx.messaging;

public interface ProcessListener {
    void onContextInitiated(Context context);
    void onBefore(String phase, Messaging messaging, Context context);
    void onAfter(String phase, Messaging messaging, Context context);
    void onError(String phase, Messaging messaging, Context context);
    void onBefore(String phase, Notification notification, Context context);
    void onAfter(String phase, Notification notification, Context context);
}
