package com.xxx.messaging;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;

@Getter
public abstract class Hook {
    private static final Gson GSON = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
            .disableHtmlEscaping()
            .create();

    private Status onError;

    protected Hook(Status onError) {
        this.onError = (onError == null ? Status.ABORT : onError);
    }

    Status execute(Messaging messaging, Status status) {
        String message = GSON.toJson(ImmutableMap.of(
                "group", messaging.getGroup(),
                "id", messaging.getId(),
                "to", messaging.getTo(),
                "data", messaging.getData(),
                "status", status));

        try {
            return call(message);
        } catch (Exception e) {
            return onError;
        }
    }

    public abstract Status call(String message) throws HookFailedException;
}