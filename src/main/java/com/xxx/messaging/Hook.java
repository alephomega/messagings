package com.xxx.messaging;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public abstract class Hook {
    private static final Gson GSON = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
            .disableHtmlEscaping()
            .create();

    private Status onError;

    protected Hook(Status onError) {
        this.onError = (onError == null ? Status.ABORT : onError);
    }

    Response execute(Notification notification, Status status) {
        String message = GSON.toJson(ImmutableMap.of(
                "topic", notification.getTopic(),
                "id", notification.getId(),
                "to", notification.getTo(),
                "message", notification.getMessage(),
                "status", status));

        try {
            return call(message);
        } catch (Exception e) {
            return new Response(onError, null);
        }
    }

    public abstract Response call(String message) throws HookFailedException;

    @Data
    public static class Response {
        private Status status;
        private Map<String, ?> message;

        public Response(Status status) {
            this(status, null);
        }

        public Response(Status status, Map<String, ?> message) {
            this.status = status;
            this.message = message;
        }
    }
}