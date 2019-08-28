package com.xxx.messaging;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public abstract class HookFactory<T extends Hook> {
    private static Gson GSON = new GsonBuilder().create();

    T create(JsonElement json) {
        T hook = GSON.fromJson(json, hookClass());
        return initialize(hook);
    }

    public abstract Class<T> hookClass();
    public abstract T initialize(T hook);
}
