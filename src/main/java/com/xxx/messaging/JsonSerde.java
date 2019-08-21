package com.xxx.messaging;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonSerde {
    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
            .disableHtmlEscaping().create();

    public static String messagingToJson(Messaging messaging) {
        return GSON.toJson(messaging);
    }

    public static String notificationToJson(Notification notification) {
        return GSON.toJson(notification);
    }

    public static Messaging jsonToMessaging(String json) {
        return GSON.fromJson(json, Messaging.class);
    }

    public static Notification jsonToNotification(String json) {
        return GSON.fromJson(json, Notification.class);
    }
}
