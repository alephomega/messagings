package com.xxx.messaging;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class MessagingJsonSerde {
    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
            .disableHtmlEscaping().create();

    static String toJson(Messaging messaging) {
        return GSON.toJson(messaging);
    }

    static Messaging fromJson(String json) {
        return GSON.fromJson(json, Messaging.class);
    }
}
