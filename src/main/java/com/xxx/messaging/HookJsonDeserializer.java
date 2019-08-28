package com.xxx.messaging;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HookJsonDeserializer implements JsonDeserializer<Hook> {
    private final Map<String, HookFactory> factories = new HashMap<>();

    @Autowired
    public HookJsonDeserializer(List<HookFactory> factories) {
        factories.forEach(factory -> this.factories.put(factory.hookClass().getSimpleName(), factory));
    }

    @Override
    public Hook deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String type = null;
        try {
            type = json.getAsJsonObject().get("type").getAsString();
        } catch (NullPointerException ignore) { }

        HookFactory factory = factoryOf(type);
        if (factory == null) {
            throw new JsonParseException(String.format("No hook factory found for %s", type));
        }

        return factory.create(json);
    }

    private HookFactory factoryOf(String name) {
        return name != null ? factories.get(name) : null;
    }
}