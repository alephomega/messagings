package com.xxx.messaging.hook;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xxx.messaging.Hook;
import com.xxx.messaging.Message;
import lombok.Getter;

import java.util.Map;

@Getter
abstract class AbstractHook implements Hook {
    private static final Gson GSON = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
            .disableHtmlEscaping()
            .create();

    private ReturnCode onSuccess;
    private ReturnCode onFailure;

    AbstractHook(ReturnCode onSuccess, ReturnCode onFailure) {
        this.onSuccess = onSuccess;
        this.onFailure = onFailure;
    }

    @Override
    public ReturnCode execute(Message message, Map<String, ?> args) {

        try {
            String jsonMessage = GSON.toJson(ImmutableMap.builder().putAll(args).put("message", message).build());
            return call(message.getId(), jsonMessage) ? onSuccess : onFailure;
        } catch (Exception e) {
            return onFailure;
        }
    }

    abstract boolean call(String id, String message);
}